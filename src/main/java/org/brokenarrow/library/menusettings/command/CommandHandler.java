package org.brokenarrow.library.menusettings.command;

import org.broken.arrow.library.command.CommandRegister;
import org.broken.arrow.library.command.builers.CommandBuilder;
import org.broken.arrow.library.command.command.CommandHolder;
import org.broken.arrow.library.logging.Logging;
import org.brokenarrow.library.menusettings.MenuSession;
import org.brokenarrow.library.menusettings.command.modal.Argument;
import org.brokenarrow.library.menusettings.command.modal.CommandData;
import org.brokenarrow.library.menusettings.command.modal.CommandHandlerSettings;
import org.brokenarrow.library.menusettings.command.modal.MenuCommandExecutor;
import org.brokenarrow.library.menusettings.command.modal.MenuPlaceholderContext;
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
import java.util.stream.Collectors;

public class CommandHandler {
    private final Logging logging = new Logging(CommandHandler.class);
    private final Map<String, CommandData> commandsData = new LinkedHashMap<>();
    private final Plugin plugin;
    private final String menuId;
    private final CommandRegister commandRegister;
    private List<String> messages;
    private MenuCommandExecutor onMenuCommandExecutor;

    public CommandHandler(@NotNull final Plugin plugin, @NotNull final CommandRegister commandRegister, @NotNull final String menuId, @NotNull final Consumer<CommandHandlerSettings> callback) {
        final CommandHandlerSettings settings = new CommandHandlerSettings();

        this.plugin = plugin;
        this.menuId = menuId;
        callback.accept(settings);
        this.commandRegister = commandRegister;
        this.registerCommand(settings);
    }

    public MenuCommandExecutor getMenuCommandExecutor() {
        return onMenuCommandExecutor;
    }

    public void setRunCommand(@Nonnull final MenuCommandExecutor onMenuCommandExecutor) {
        this.onMenuCommandExecutor = onMenuCommandExecutor;
    }

    public void registerCommand(@NotNull final CommandHandlerSettings settings) {
        this.messages = settings.getMessage();
        final List<String> openCommands = settings.getOpenCommands();
        final List<?> openArguments = settings.getOpenArguments();
        RequirementsContext openArgsRequirement = settings.getOpenArgsRequirement();
        String overridePermission = settings.getOverridePermission();

        openCommands.forEach(command -> {
            final CommandData commandData = new CommandData();
            final List<String> argumentsList = new ArrayList<>();

            if (openArguments != null && !openArguments.isEmpty()) {
                openArguments.forEach(object -> {
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
            commandsData.put(command, commandData);
        });
    }

    public class MainCommand extends CommandHolder {

        public MainCommand(String... commandLabel) {
            super(commandLabel);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
            this.checkConsole();

            if (onMenuCommandExecutor == null) {
                logging.log(() -> "Not implemented the option to set commands in config");
                return true;
            }

            final int index = commandLabel.indexOf(":");
            final CommandData commandData = commandsData.get(index > 0 ? commandLabel.substring(index + 1) : commandLabel);
            if (commandData != null) {
                List<Argument> argumentsList = commandData.getArgumentsList();
                if (!checkCorrectArgumentsSet(sender, cmdArg, argumentsList)) return true;

                Player player = (Player) sender;
                final MenuPlaceholderContext menuPlaceholderContext = new MenuPlaceholderContext(player, argumentsList, cmdArg);
                final MenuSession session = new MenuSession(plugin, menuPlaceholderContext, menuId, player);
                session.checkOpenRequirements(commandData.getOverridePermission(), resultHandler -> {
                    resultHandler.onSuccess(() -> {
                        onMenuCommandExecutor.execute(session, menuPlaceholderContext);
                    });
                    resultHandler.onFailure(() -> {
                        sender.sendMessage(CreateItemStack.translateColors("[" + plugin.getName() + "] Could not open the menu, do you not have the set conditions?"));

                    });
                });
                return true;
            }
            return true;
        }

        @Override
        @Nullable
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArgs) {
            final int index = commandLabel.indexOf(":");
            final CommandData commandData = commandsData.get(index > 0 ? commandLabel.substring(index + 1) : commandLabel);
            if (commandData != null) {
                final int provided = cmdArgs.length;
                final List<Argument> argumentsList = commandData.getArgumentsList();
                if (provided - 1 < argumentsList.size()) {
                    Argument argument = argumentsList.get(provided == 0 ? 0 : provided - 1);
                    if (argument != null && !argument.isOptional()) {
                        return completeLastWord(argument.getArgument());
                    }
                }
            }
            return new ArrayList<>();
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
                    sender.sendMessage(CreateItemStack.translateColors(message.replace("{arguments}", argumentsList.stream()
                            .filter(argument -> !argument.isOptional())
                            .map(Argument::getArgument)
                            .collect(Collectors.joining(", "))))
                    );
                }
            }
            return valid;
        }
    }


    private void registerMainCommand(@Nonnull final String mainCommandLabel, @Nonnull final List<String> subCommand) {
        if (commandRegister.getCommand(mainCommandLabel) != null) return;

        final CommandBuilder mainCommand = commandRegister.registerCommand(plugin, mainCommandLabel);
        mainCommand.setMainCommand(new MainCommand("main"));
    }

}
