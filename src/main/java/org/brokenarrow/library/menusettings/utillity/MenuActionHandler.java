package org.brokenarrow.library.menusettings.utillity;

import org.brokenarrow.library.menusettings.MenuSession;
import org.jetbrains.annotations.NotNull;

/**
 * Functional interface for handling menu-related actions such as open or close.
 *
 * <p>This can be set via {@link org.brokenarrow.library.menusettings.builders.MenuRegistrationConfig#setActionHandler(MenuActionHandler)}.
 * If no handler is provided, menus will use a fallback implementation with limited functionality
 * (for example, custom actions will not be executed).</p>
 */
@FunctionalInterface
public interface MenuActionHandler {

    /**
     * Called when a menu action occurs.
     *
     * @param action     the type of menu action that occurred (e.g, OPEN or CLOSE)
     * @param menuName   the name of the menu being interacted with
     * @param menuSession the current {@link MenuSession} associated with the player/menu
     */
    void handle(@NotNull final MenuAction action,@NotNull final String menuName, @NotNull final MenuSession menuSession);
}