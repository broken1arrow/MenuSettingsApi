package org.brokenarrow.library.menusettings.hooks.vault;

import net.milkbowl.vault.permission.Permission;
import org.brokenarrow.library.menusettings.hooks.permission.PermissionProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.brokenarrow.library.menusettings.exceptions.Valid.checkBoolean;
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
		RegisteredServiceProvider<Permission> rspPermission = getServer().getServicesManager().getRegistration(Permission.class);
		if (rspPermission == null) {
			return false;
		}
		this.perms = rspPermission.getProvider();
		return true;
	}

	@Override
	public boolean setPermission(Player player, String permission) {
		checkBoolean(this.perms == null, "vault is null, so you can't setPermission");

		return perms.playerAdd(null, player, permission);
	}

	@Override
	public boolean hasPermission(Player player, String permission) {
		checkBoolean(this.perms == null, "vault is null, so you can't check if player has permission");

		return permission != null && perms.playerHas(null, player, permission);
	}

	@Override
	public boolean removePermission(Player player, String permission) {
		checkBoolean(this.perms == null, "vault is null, so you can't remove permission from player");

		return perms.playerRemove(null, player, permission);
	}

	@Override
	public Permission getPerms() {
		return perms;
	}
}
