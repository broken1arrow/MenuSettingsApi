package org.brokenarrow.library.menusettings.command;

import org.brokenarrow.library.menusettings.MenuSession;

import javax.annotation.Nonnull;

/**
 * Represents a handler responsible for executing logic when a menu command is invoked.
 *
 * <p>The executor is provided with a {@link MenuSession}, representing the current
 * menu interaction, and a {@link MenuPlaceholderContext}, which contains resolved
 * placeholder values derived from command arguments.</p>
 *
 * <p>The returned boolean follows the standard command contract, where {@code true}
 * indicates successful handling and {@code false} indicates failure.</p>
 */
@FunctionalInterface
public interface MenuCommandExecutor {

    /**
     * Executes the command logic for a menu interaction.
     *
     * @param session the active menu session for the player
     * @param context the placeholder context used for resolving dynamic values
     * @return {@code true} if the command was handled successfully, otherwise {@code false}
     */
    boolean execute(@Nonnull final MenuSession session, @Nonnull final MenuPlaceholderContext context);

}
