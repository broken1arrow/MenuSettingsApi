package org.brokenarrow.library.menusettings.requirements;

import org.bukkit.entity.Player;

public class HasPermissionRequirement extends Requirement {

	private final String permission;
	private final boolean inverted;

	public HasPermissionRequirement(String permission, boolean inverted) {
		this.permission = permission;
		this.inverted = inverted;
	}

	@Override
	boolean estimate(Player wiver) {
		return this.inverted != wiver.hasPermission(permission);
	}
}
