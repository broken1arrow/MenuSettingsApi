package org.brokenarrow.library.menusettings.command;

import org.broken.arrow.library.command.CommandRegister;
import org.broken.arrow.library.command.builers.CommandBuilder;
import org.broken.arrow.library.command.command.CommandHolder;
import org.brokenarrow.library.menusettings.MenuSession;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
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
    private final Plugin plugin;
    private final String menuId;
    private final CommandRegister commandRegister;
    private final Map<String, CommandData> commandsData;
    private MenuPlaceholderContext menuPlaceholderContext;
    private FunctionCommand runCommand;

    public CommandHandler(@NotNull final Plugin plugin, @NotNull final String menuId, @NotNull final Consumer<CommandHandlerSettings> callback) {
        this.plugin = plugin;
        this.menuId = menuId;
        CommandHandlerSettings settings = new CommandHandlerSettings();
        callback.accept(settings);
        final List<String> openCommands = settings.getOpenCommands();
        final List<?> openArguments = settings.getOpenArguments();
        RequirementsContext openArgsRequirement = settings.getOpenArgsRequirement();

        this.commandsData = this.registerCommands(openCommands, openArguments, openArgsRequirement);
        this.commandRegister = new CommandRegister();
    }

    @Nullable
    public MenuPlaceholderContext getMenuExecutionContext() {
        return menuPlaceholderContext;
    }

    public FunctionCommand getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(@Nonnull final FunctionCommand runCommand) {
        this.runCommand = runCommand;
    }

    public class SubCommand extends CommandHolder {

        public SubCommand(String... commandLabel) {
            super(commandLabel);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] cmdArg) {
            this.checkConsole();

            CommandData commandData = commandsData.get(commandLabel);
            if (commandData != null) {
                final MenuPlaceholderContext menuPlaceholderContext = new MenuPlaceholderContext((Player) sender, commandData.getArgumentsList(), cmdArg);
                final MenuSession session = new MenuSession(plugin, menuPlaceholderContext, menuId, (Player) sender);
                return runCommand.apply(session, menuPlaceholderContext);
            }
            return false;
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
