package org.brokenarrow.library.menusettings.hooks.vault;

import net.milkbowl.vault.permission.Permission;
import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.hooks.permission.PermissionProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.exceptions.Valid.checkNotNull;
import static org.bukkit.Bukkit.getServer;

public class VaultPermission implements PermissionProvider {
    private Permission perms;

    public VaultPermission() {
        setupPermission();
    }

    public boolean setupPermission() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        getServer().getScheduler().runTaskLater(MenuSettingsAddon.getPLUGIN(), () -> {
            RegisteredServiceProvider<Permission> rspPermission = getServer().getServicesManager().getRegistration(Permission.class);
            getServer().getLogger().log(Level.WARNING, "Permission " + rspPermission);
            if (rspPermission == null) {
                return;
            }
            getServer().getLogger().log(Level.WARNING, "Permission Provider " + rspPermission.getProvider());
            this.perms = rspPermission.getProvider();
        }, 10 * 2);
        return true;
    }

    @Override
    public boolean setPermission(Player player, String permission) {
        checkNotNull(this.perms, "vault is null, so you can't setPermission");

        return perms.playerAdd(null, player, permission);
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        checkNotNull(this.perms, "vault is null, so you can't check if player has permission");

        return permission != null && perms.playerHas(null, player, permission);
    }

    @Override
    public boolean removePermission(Player player, String permission) {
        checkNotNull(this.perms, "vault is null, so you can't remove permission from player");

        return perms.playerRemove(null, player, permission);
    }

    @Override
    public Permission getPerms() {
        return perms;
    }
}
