package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.brokenarrow.library.menusettings.settings.TemplatesCache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MenuContext {
    private final MenuCache menuCache;
    private final TemplatesCache templatesCache;

    public MenuContext(@Nonnull final MenuCache menuCache,@Nullable final TemplatesCache templatesCache) {
        this.menuCache = menuCache;
        this.templatesCache = templatesCache;
    }

    public MenuCache getMenuCache() {
        return menuCache;
    }

    public TemplatesCache getTemplatesCache() {
        return templatesCache;
    }
}
