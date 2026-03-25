package org.brokenarrow.library.menusettings.utillity.menu.fallback;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof FallBackGUI) {
            e.setCancelled(true);
            ((FallBackGUI) holder).onClick(e.getRawSlot(), e.getClick());
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof FallBackGUI) {
            e.setCancelled(true);
        }
    }
}
