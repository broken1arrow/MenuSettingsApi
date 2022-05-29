package org.brokenarrow.library.menusettings.hooks.permission;

import org.brokenarrow.library.menusettings.hooks.vault.VaultPermission;
import org.bukkit.Bukkit;

public class RegisterPermissionHook {
	private PermissionProvider provider;

	public RegisterPermissionHook() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			provider = new VaultPermission();
		}
	}

	public void checkPermHock() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			provider = new VaultPermission();
		}

	}

	public PermissionProvider getProvider() {
		return provider;
	}
}
