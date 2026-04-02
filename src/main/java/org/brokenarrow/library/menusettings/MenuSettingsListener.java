package org.brokenarrow.library.menusettings;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.brokenarrow.library.menusettings.hooks.economy.RegisterEconomyHook;
import org.brokenarrow.library.menusettings.hooks.permission.RegisterPermissionHook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;
import org.jetbrains.annotations.NotNull;

public class MenuSettingsListener implements Listener {

    @NotNull
    private final MenuSettingsAddon plugin;
    private RegisterEconomyHook economyHook;
    private RegisterPermissionHook permissionHook;

    public MenuSettingsListener(@NotNull final MenuSettingsAddon menuSettingsAddon) {
        this.plugin = menuSettingsAddon;
    }

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        if (event.getProvider().getService() == Economy.class) {
            this.economyHook = new RegisterEconomyHook();
            plugin.getLogger().info("Hooked into economy via event!");
        }
        if (event.getProvider().getService() == Permission.class) {
            this.permissionHook = new RegisterPermissionHook() ;
            plugin.getLogger().info("Hooked into Permission via event!");
        }
        plugin.getLogger().info("ServiceRegisterEven getProvider trihgger " + event.getProvider());
        plugin.getLogger().info("ServiceRegisterEven trihgger " + event.getProvider().getService());
    }

    public RegisterEconomyHook getEconomyHook() {
        return economyHook;
    }

    public RegisterPermissionHook getPermissionHook() {
        return permissionHook;
    }
}