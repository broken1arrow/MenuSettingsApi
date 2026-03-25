package org.brokenarrow.library.menusettings.utillity;

import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MenuActionHandler {
    void handle(@NotNull final MenuAction action,@NotNull final String menuName, @NotNull final MenuSettings context);
}