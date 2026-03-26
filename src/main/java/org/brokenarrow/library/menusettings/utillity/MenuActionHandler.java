package org.brokenarrow.library.menusettings.utillity;

import org.brokenarrow.library.menusettings.MenuSession;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MenuActionHandler {
    void handle(@NotNull final MenuAction action,@NotNull final String menuName, @NotNull final MenuSession menuSession);
}