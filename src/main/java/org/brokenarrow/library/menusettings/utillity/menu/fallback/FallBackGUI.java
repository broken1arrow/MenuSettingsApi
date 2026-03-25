package org.brokenarrow.library.menusettings.utillity.menu.fallback;

import org.brokenarrow.library.menusettings.MenuSession;
import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.utillity.CreateItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class FallBackGUI implements InventoryHolder {
    private final Inventory inventory;
    private MenuSession menuSession;

    public FallBackGUI(@NotNull final Plugin plugin, @NotNull final String menuName, @NotNull final Player player) {
        menuSession = new MenuSession(plugin, menuName, player);
        MenuSettings menuSettings = menuSession.getMenuSettings();
        int size = menuSettings.getMenuSize();
        if (size <= 0 || size % 9 != 0)
            size = 5 * 9;

        this.inventory = Bukkit.createInventory(this, size, "§4[Fallback] §r" +  menuSettings.getMenuTitle());
        for (int slot = 0; slot < this.inventory.getSize(); slot++) {
            ButtonSettings buttonSettings = menuSession.getButton(slot);
            if (buttonSettings != null) {
                this.inventory.setItem(slot, buttonSettings.getItemStack(player));
            }
        }
        int slot = inventory.firstEmpty();
        if (slot != -1) {
            inventory.setItem(slot, createFallbackIndicator());
        }
    }

    private ItemStack createFallbackIndicator() {
        return CreateItemStack.of(Material.BARRIER,"§4Fallback Mode"," " ,"§7Some features may not work", "").makeItemStack();
    }

    public boolean beforeOpen() {
       return menuSession.checkOpenRequirements("");
    }

    public void onClick(int slot, ClickType click) {
        ButtonSettings buttonSettings = menuSession.getButton(slot);
        if (buttonSettings != null) {
            menuSession.checkClickRequirements(buttonSettings, click);
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
