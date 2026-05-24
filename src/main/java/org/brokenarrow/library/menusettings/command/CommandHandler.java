package org.brokenarrow.library.menusettings.command;

import org.broken.arrow.library.command.CommandRegister;
import org.broken.arrow.library.command.builers.CommandBuilder;
import org.broken.arrow.library.command.command.CommandHolder;
import org.brokenarrow.library.menusettings.MenuSession;
import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
import org.brokenarrow.library.menusettings.utillity.RequirementResultHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommandHandler {
    private final Plugin plugin;
    private final String menuId;
    private final CommandRegister commandRegister;
    private final Map<String, CommandData> commandsData;
    private final ClickActionHandler openCommandsAction;
    private final RequirementsContext openRequirement;
    private MenuCommandExecutor onMenuCommandExecutor;

    public CommandHandler(@NotNull final Plugin plugin, @NotNull final String menuId, @NotNull final Consumer<CommandHandlerSettings> callback) {
        this.plugin = plugin;
        this.menuId = menuId;
        CommandHandlerSettings settings = new CommandHandlerSettings();
        callback.accept(settings);
        this.openCommandsAction = settings.getOpenCommandsAction();
        this.openRequirement = settings.getOpenRequirement();

        final List<String> openCommands = settings.getOpenCommands();
        final List<?> openArguments = settings.getOpenArguments();
        RequirementsContext openArgsRequirement = settings.getOpenArgsRequirement();

        this.commandRegister = new CommandRegister();
        this.commandsData = this.registerCommands(openCommands, openArguments, openArgsRequirement);
        System.out.println("this.commandsData keySet " + this.commandsData.keySet());
        System.out.println("this.commandsData " + this.commandsData);
    }


    public MenuCommandExecutor getMenuCommandExecutor() {
        return onMenuCommandExecutor;
    }

    public void setRunCommand(@Nonnull final MenuCommandExecutor onMenuCommandExecutor) {
        this.onMenuCommandExecutor = onMenuCommandExecutor;
    }

    public ClickActionHandler getOpenCommandsAction() {
        return openCommandsAction;
    }

    public class SubCommand extends CommandHolder {

        public SubCommand(String... commandLabel) {
            super(commandLabel);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
            this.checkConsole();
            final int index = commandLabel.indexOf(":");
            final CommandData commandData = commandsData.get(index > 0 ? commandLabel.substring(index + 1) : commandLabel);
            if (commandData != null) {
                if (openRequirement != null) {

                }
                Player player = (Player) sender;
                final MenuPlaceholderContext menuPlaceholderContext = new MenuPlaceholderContext(player, commandData.getArgumentsList(), cmdArg);
                final MenuSession session = new MenuSession(plugin, menuPlaceholderContext, menuId, player);
                boolean success = false;
                openRequirements(session, () -> true);
                return onMenuCommandExecutor.execute(session, menuPlaceholderContext);
            }
            return false;
        }

        private boolean openRequirements(MenuSession session, Supplier<Boolean> success) {
            session.checkOpenRequirements("", resultHandler -> {
                resultHandler.onSuccess(() -> {

                });

            });
            return success.get();
        }

        /**
         * Checks whether the player meets the requirements to open this menu.
         *
         * @param bypassPermission a permission node to bypass the requirements, if applicable
         * @param resultCallback   configures actions to execute on success or failure
         */
        public void checkOpenRequirements(@Nonnull final Player viewer, @Nonnull final MenuPlaceholderContext menuPlaceholderContext, @Nullable final String bypassPermission, @Nonnull final Consumer<RequirementResultHandler> resultCallback) {
            final RequirementResultHandler handler = new RequirementResultHandler();
            resultCallback.accept(handler);
            if (viewer != null && bypassPermission != null && !bypassPermission.isEmpty() && viewer.hasPermission(bypassPermission)) {
                handler.executeSuccess();
                return;
            }

            if (openRequirement != null) {
                openRequirement.estimateLater(viewer, menuPlaceholderContext, hasRequirements -> {
                    if (hasRequirements) {
                        handler.executeSuccess();
                    } else {
                        if (openRequirement.getDenyCommands() != null)
                            openRequirement.runClickActionTasks(openRequirement.getDenyCommands(), viewer, menuPlaceholderContext).thenRun(handler::executeFailure);
                    }
                });
            } else {
                handler.executeSuccess();
            }
        }
    }

    private Map<String, CommandData> registerCommands(@NotNull final List<String> openCommands, @Nullable final List<?> arguments, @Nullable final RequirementsContext openArgsRequirement) {
        Map<String, CommandData> map = new LinkedHashMap<>();
        openCommands.forEach(command -> {
            final CommandData commandData = new CommandData();
            final List<String> argumentsList = new ArrayList<>();

            if (arguments != null && !arguments.isEmpty()) {
                arguments.forEach(object -> {
                    if (!(object instanceof Map)) return;
                    Map<String, Object> mapOfArguments = (Map<String, Object>) object;
                    commandData.addArgument(argument -> {
                        Object arg = mapOfArguments.get("arg");
                        if (arg != null) {
                            final Object optional = mapOfArguments.get("optional");
                            argument.setArgument(arg + "");
                            argumentsList.add(arg + "");
                            if (optional != null)
                                argument.setOptional(Boolean.parseBoolean(optional + ""));
                        }
                    });
                });
            }

            this.registerMainCommand(command, argumentsList);
            commandData.setArgsRequirement(openArgsRequirement);
            map.put(command, commandData);
        });
        return map;
    }

    private void registerMainCommand(@Nonnull final String mainCommandLabel, @Nonnull final List<String> subCommand) {
        final CommandBuilder mainCommand = commandRegister.registerCommand(plugin, mainCommandLabel);
        if (!subCommand.isEmpty()) {
            mainCommand.registerSubCommands(new SubCommand(subCommand.toArray(new String[0])));
        } else {
            mainCommand.setMainCommand(new SubCommand("main"));
        }
    }

}
