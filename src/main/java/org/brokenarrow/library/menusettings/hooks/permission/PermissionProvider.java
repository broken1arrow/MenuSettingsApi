package org.brokenarrow.library.menusettings.hooks.permission;

import org.bukkit.entity.Player;

public interface PermissionProvider {


	default boolean setPermission(Player player, String permission) {
		return false;
	}

	default boolean hasPermission(Player player, String permission) {
		return false;
	}

	default boolean removePermission(Player player, String permission) {
		return false;
	}

	<T extends Class<?>> Object getPerms();
}
