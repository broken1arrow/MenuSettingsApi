package org.brokenarrow.library.menusettings.command;

import org.broken.arrow.library.command.CommandRegister;
import org.broken.arrow.library.command.builers.CommandBuilder;
import org.broken.arrow.library.command.command.CommandHolder;
import org.broken.arrow.library.logging.Logging;
import org.brokenarrow.library.menusettings.MenuSession;
import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
import org.brokenarrow.library.menusettings.utillity.CreateItemStack;
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

public class CommandHandler {
    private Logging logging = new Logging(CommandHandler.class);
    private final Plugin plugin;
    private final String menuId;
    private final CommandRegister commandRegister;
    private final Map<String, CommandData> commandsData;
    private final ClickActionHandler openCommandsAction;
    private final List<String> messages;
    private MenuCommandExecutor onMenuCommandExecutor;

    public CommandHandler(@NotNull final Plugin plugin, @NotNull final String menuId, @NotNull final Consumer<CommandHandlerSettings> callback) {
        this.plugin = plugin;
        this.menuId = menuId;
        CommandHandlerSettings settings = new CommandHandlerSettings();
        callback.accept(settings);
        this.openCommandsAction = settings.getOpenCommandsAction();
        this.messages = settings.getMessage();

        final List<String> openCommands = settings.getOpenCommands();
        final List<?> openArguments = settings.getOpenArguments();
        RequirementsContext openArgsRequirement = settings.getOpenArgsRequirement();
        String overridePermission = settings.getOverridePermission();

        this.commandRegister = new CommandRegister();
        this.commandsData = this.registerCommands(overridePermission, openCommands, openArguments, openArgsRequirement);
    }

    public MenuCommandExecutor getMenuCommandExecutor() {
        return onMenuCommandExecutor;
    }

    public void setRunCommand(@Nonnull final MenuCommandExecutor onMenuCommandExecutor) {
        this.onMenuCommandExecutor = onMenuCommandExecutor;
    }

    public class SubCommand extends CommandHolder {

        public SubCommand(String... commandLabel) {
            super(commandLabel);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
            this.checkConsole();

            if (onMenuCommandExecutor == null) {
                logging.log(() -> "Not implemented the option to set commands in config");
                return false;
            }

            final int index = commandLabel.indexOf(":");
            final CommandData commandData = commandsData.get(index > 0 ? commandLabel.substring(index + 1) : commandLabel);
            if (commandData != null) {
                List<Argument> argumentsList = commandData.getArgumentsList();
                if (checkCorrectArgumentsSet(sender, cmdArg, argumentsList)) return false;

                Player player = (Player) sender;
                final MenuPlaceholderContext menuPlaceholderContext = new MenuPlaceholderContext(player, argumentsList, cmdArg);
                final MenuSession session = new MenuSession(plugin, menuPlaceholderContext, menuId, player);
                session.checkOpenRequirements(commandData.getOverridePermission(), resultHandler -> {
                    resultHandler.onSuccess(() -> {
                        onMenuCommandExecutor.execute(session, menuPlaceholderContext);
                    });
                });
                return true;
            }
            return false;
        }

        private boolean checkCorrectArgumentsSet(@NotNull final CommandSender sender, @NotNull final String[] cmdArg, @NotNull final List<Argument> argumentsList) {
            final long required = argumentsList.stream()
                    .filter(arg -> !arg.isOptional())
                    .count();
            final int provided = cmdArg.length;
            final int max = argumentsList.size();
            final boolean valid = provided >= required && provided <= max;

            if (!valid) {
                for (String message : messages) {
                    sender.sendMessage(CreateItemStack.translateColors(message));
                }
            }
            return valid;
        }
    }

    private Map<String, CommandData> registerCommands(final String overridePermission, @NotNull final List<String> openCommands, @Nullable final List<?> arguments, @Nullable final RequirementsContext openArgsRequirement) {
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
            commandData.setOverridePermission(overridePermission);
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
