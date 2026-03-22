package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.brokenarrow.library.menusettings.settings.TemplatesCache;

public class MenuLogic {
    private final MenuCache menuCache;
  private final TemplatesCache cache;

    public MenuLogic(MenuCache menuCache, TemplatesCache cache) {
        this.menuCache = menuCache;
        this.cache = cache;
    }

    public MenuCache getMenuCache() {
        return menuCache;
    }

    public TemplatesCache getTemplates() {
        return cache;
    }
}
