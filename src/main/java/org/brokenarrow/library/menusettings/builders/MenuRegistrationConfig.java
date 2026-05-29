package org.brokenarrow.library.menusettings.builders;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.broken.arrow.library.command.CommandRegister;
import org.brokenarrow.library.menusettings.command.modal.MenuCommandExecutor;
import org.brokenarrow.library.menusettings.utillity.MenuActionHandler;

import javax.annotation.Nonnull;

/**
 * Configuration object used during menu registration with the API.
 *
 * <p>This class provides options for how menus are loaded, whether
 * default files are generated, custom action handling, and integration
 * with {@link BukkitAudiences}.</p>
 *
 * <p>It is intended to replace older registration methods with a cleaner,
 * configurable approach.</p>
 */
public class MenuRegistrationConfig {
    private  BukkitAudiences audiences;
    private  boolean oneFile;
    private  boolean generateDefaultFiles;
    private  MenuActionHandler actionHandler;
    private MenuCommandExecutor menuCommandExecutor;
    private final CommandRegister commandRegister;

    public MenuRegistrationConfig() {
        this.commandRegister = new CommandRegister();
    }

    /**
     * Returns whether a single file is used for all menus.
     *
     * @return {@code true} if only one file is used, otherwise {@code false}
     */
    public boolean isOneFile() {
        return oneFile;
    }

    /**
     * Sets whether all menus should be loaded from a single file.
     *
     * @param oneFile {@code true} to use one file, {@code false} for separate files
     */
    public void setOneFile(final boolean oneFile) {
        this.oneFile = oneFile;
    }

    /**
     * Returns whether default menu files should be generated if missing.
     *
     * @return {@code true} if default files will be generated, otherwise {@code false}
     */
    public boolean isGenerateDefaultFiles() {
        return generateDefaultFiles;
    }

    /**
     * Retries the command register for this menu instance.
     *
     * @return the command register instance.
     */
    public CommandRegister getCommandRegister() {
        return commandRegister;
    }

    /**
     * Sets whether default menu files should be generated from resources.
     *
     * @param generateDefaultFiles {@code true} to generate missing files, otherwise {@code false}
     */
    public void setGenerateDefaultFiles(final boolean generateDefaultFiles) {
        this.generateDefaultFiles = generateDefaultFiles;
    }

    /**
     * Returns the custom {@link MenuActionHandler} used for handling menu actions
     * such as opening or closing a menu.
     *
     * <p>If no handler is set, menus will fall back to a default implementation
     * with limited functionality. For example, custom actions will not be executed
     * when opening or closing menus.</p>
     *
     * @return the configured action handler, or {@code null} if none is set
     */
    public MenuActionHandler getActionHandler() {
        return actionHandler;
    }

    /**
     * Sets a custom {@link MenuActionHandler} to handle menu actions such as
     * opening or closing a menu.
     *
     * <p>If {@code null} is provided, the API will use a fallback menu
     * implementation with limited functionality (e.g., custom actions
     * will not run).</p>
     *
     * @param actionHandler the handler to use, or {@code null} to use the default/fallback behavior
     */
    public void setActionHandler(final MenuActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    /**
     * Returns the currently configured {@link MenuCommandExecutor}.
     *
     * @return the command executor, or {@code null} if none has been set
     */
    public MenuCommandExecutor getMenuCommandExecutor() {
        return menuCommandExecutor;
    }

    /**
     * Sets the {@link MenuCommandExecutor} used to handle execution of menu open commands.
     *
     * <p>This executor is invoked after:
     * <ul>
     *     <li>A configured open command is executed by a player</li>
     *     <li>All command arguments are validated</li>
     *     <li>Any defined requirements or permissions are successfully passed</li>
     * </ul>
     *
     * <p>The executor provides access to:
     * <ul>
     *     <li>The active {@link org.brokenarrow.library.menusettings.MenuSession}</li>
     *     <li>The {@link org.brokenarrow.library.menusettings.command.modal.MenuPlaceholderContext} containing parsed command arguments</li>
     * </ul>
     *
     * <p>This allows developers to inject custom logic before or during menu opening,
     * such as modifying placeholders, cancelling execution, or performing additional checks.</p>
     *
     * <p>If no executor is set, commands defined in menu configurations will not perform
     * anything like open a menu.</p>
     *
     * @param menuCommandExecutor the executor responsible for handling command execution
     */
    public void setMenuCommandExecutor(@Nonnull final MenuCommandExecutor menuCommandExecutor) {
        this.menuCommandExecutor = menuCommandExecutor;
    }

    /**
     * Returns the {@link BukkitAudiences} instance used for sending messages/components.
     *
     * @return the configured audiences, or {@code null} if none is set
     */
    public BukkitAudiences getAudiences() {
        return audiences;
    }

    /**
     * Sets the {@link BukkitAudiences} instance to use for sending messages/components.
     *
     * @param audiences the audiences instance, or {@code null} to let the API create one
     */
    public void setAudiences(BukkitAudiences audiences) {
        this.audiences = audiences;
    }
}