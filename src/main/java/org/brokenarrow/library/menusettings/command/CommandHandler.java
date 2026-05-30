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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Handles registration and execution of menu-related commands.
 *
 * <p>This class is responsible for:
 * <ul>
 *     <li>Registering commands defined via {@link CommandHandlerSettings}</li>
 *     <li>Mapping commands to their argument structure and requirements</li>
 *     <li>Validating arguments on execution</li>
 *     <li>Creating {@link MenuSession} instances for valid executions</li>
 *     <li>Delegating execution logic via {@link MenuCommandExecutor}</li>
 * </ul>
 *
 * <p>Usage pattern:
 * <pre>
 * new CommandHandler(plugin, commandRegister, menuId, settings -> {
 *     // configure settings
 * }).setRunCommand((session, context) -> {
 *     // handle execution
 * });
 * </pre>
 *
 * <p>Note: Commands will not perform any action unless a {@link MenuCommandExecutor}
 * is provided via {@link #setCommandExecutor(MenuCommandExecutor)}.
 */
public class CommandHandler {
    private final Logging logging = new Logging(CommandHandler.class);
    private final Map<String, CommandData> commandsData = new LinkedHashMap<>();
    private final Plugin plugin;
    private final String menuId;
    private final CommandRegister commandRegister;
    private List<String> messages;
    private MenuCommandExecutor onMenuCommandExecutor;

    /**
     * Creates and registers a new command handler.
     *
     * <p>The provided callback is used to configure command behavior via
     * {@link CommandHandlerSettings}.
     *
     * @param plugin          the owning plugin instance
     * @param commandRegister the command registration utility
     * @param menuId          the menu identifier associated with this handler
     * @param callback        configuration callback for command settings
     */
    public CommandHandler(@NotNull final Plugin plugin, @NotNull final CommandRegister commandRegister, @NotNull final String menuId, @NotNull final Consumer<CommandHandlerSettings> callback) {
        final CommandHandlerSettings settings = new CommandHandlerSettings();

        this.plugin = plugin;
        this.menuId = menuId;
        callback.accept(settings);
        this.commandRegister = commandRegister;
        this.registerCommand(settings);
    }

    /**
     * Returns the currently assigned command executor.
     *
     * @return the {@link MenuCommandExecutor}, or null if not set
     */
    public MenuCommandExecutor getMenuCommandExecutor() {
        return onMenuCommandExecutor;
    }

    /**
     * Clean up the old commands set from the config.
     */
    public void resetRegisteredCommands() {
        this.commandsData.clear();
    }

    /**
     * Internal method used to bind the {@link MenuCommandExecutor} to this handler.
     *
     * <p>This executor is invoked after:
     * <ul>
     *     <li>The command is matched</li>
     *     <li>Arguments are validated</li>
     *     <li>Requirements are successfully passed</li>
     * </ul>
     *
     * <p>The executor receives:
     * <ul>
     *     <li>The active {@link MenuSession}</li>
     *     <li>The {@link MenuPlaceholderContext} containing parsed arguments</li>
     * </ul>
     *
     * <p><b>Note:</b> This method is intended for internal API usage only.
     * Plugin developers should configure command execution via
     * {@link org.brokenarrow.library.menusettings.builders.MenuRegistrationConfig#setMenuCommandExecutor(MenuCommandExecutor)} instead.
     *
     * @param onMenuCommandExecutor the executor responsible for handling command execution logic
     */
    @ApiStatus.Internal
    public void setCommandExecutor(@Nonnull final MenuCommandExecutor onMenuCommandExecutor) {
        this.onMenuCommandExecutor = onMenuCommandExecutor;
    }

    /**
     * Internal method that parses {@link CommandHandlerSettings} and registers
     * the corresponding commands and argument structures.
     *
     * <p>This includes:
     * <ul>
     *     <li>Registering command labels with the {@link CommandRegister}</li>
     *     <li>Building argument definitions (including optional arguments)</li>
     *     <li>Applying permission overrides</li>
     *     <li>Assigning argument requirement contexts</li>
     * </ul>
     *
     * <p>The resulting data is stored internally and used during command execution
     * for validation and menu session creation.</p>
     *
     * <p><b>Note:</b> This method is intended for internal API usage only.</p>
     *
     * @param settings the configuration used to define commands
     */
    @ApiStatus.Internal
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

    /**
     * Internal command implementation responsible for handling execution and tab completion.
     *
     * <p>This class:
     * <ul>
     *     <li>Validates argument counts</li>
     *     <li>Resolves command data</li>
     *     <li>Creates menu sessions</li>
     *     <li>Delegates execution to {@link MenuCommandExecutor}</li>
     * </ul>
     */
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
                final MenuPlaceholderContext menuPlaceholderContext = new MenuPlaceholderContext( argumentsList, cmdArg);
                final MenuSession session = new MenuSession(plugin, menuPlaceholderContext, menuId, player);
                session.checkOpenRequirements(commandData.getOverridePermission(), resultHandler -> {
                    resultHandler.onSuccess(() -> {
                        onMenuCommandExecutor.execute(session, menuPlaceholderContext);
                    });
                    resultHandler.onFailure(() -> {
                        logging.log(() -> "Could not open the menu, do you not have the set conditions?");
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
                final List<Argument> argumentsList = commandData.getArgumentsList();
                return completeLastWord(argumentsList.stream()
                        .filter(argument -> !argument.isOptional())
                        .map(argument -> argument.getArgument() + ":")
                        .collect(Collectors.toList()));
            }
            return new ArrayList<>();
        }

        /**
         * Validates that the provided arguments match the expected argument definition.
         *
         * <p>Checks:
         * <ul>
         *     <li>Minimum required arguments</li>
         *     <li>Maximum allowed arguments</li>
         * </ul>
         *
         * <p>If validation fails, configured messages are sent to the sender.
         *
         * @param sender        the command sender
         * @param cmdArg        the provided arguments
         * @param argumentsList the expected argument definitions
         * @return true if arguments are valid, otherwise false
         */
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

    /**
     * Registers the root command if it has not already been registered.
     *
     * @param mainCommandLabel the command label
     * @param subCommand       the list of argument keys (used for structure, not subcommands)
     */
    private void registerMainCommand(@Nonnull final String mainCommandLabel, @Nonnull final List<String> subCommand) {
        if (commandRegister.getCommand(mainCommandLabel) != null) return;

        final CommandBuilder mainCommand = commandRegister.registerCommand(plugin, mainCommandLabel);
        mainCommand.setMainCommand(new MainCommand("main"));
    }

}
