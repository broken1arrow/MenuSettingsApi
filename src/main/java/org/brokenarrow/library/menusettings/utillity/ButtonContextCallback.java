package org.brokenarrow.library.menusettings.utillity;

import org.brokenarrow.library.menusettings.builders.ButtonContext;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ButtonContextCallback<T>{

    T apply(@Nullable final ButtonContext buttonContext);
}