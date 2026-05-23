package org.brokenarrow.library.menusettings.command;

import org.brokenarrow.library.menusettings.MenuSession;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface FunctionCommand {

    boolean apply(MenuSession session, @Nonnull final MenuPlaceholderContext menuPlaceholderContext);

}
