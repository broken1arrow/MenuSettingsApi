package org.brokenarrow.library.menusettings.utillity;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;

public final class GuiTypeUtillity {
	private InventoryType inventoryType;
	
	public GuiTypeUtillity(final String inventoryType) {
		this.convertString(inventoryType);

	}

	@Nullable
	public InventoryType getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(InventoryType inventoryType) {
		this.inventoryType = inventoryType;
	}

	public void convertString(String string) {
		if (string == null) return;
		setInventoryType(getInventoryType(string));
	}

	public InventoryType getInventoryType(String string) {
		InventoryType[] types = InventoryType.values();
		if (string == null) return null;

		string = string.toUpperCase();
		for (InventoryType type : types) {
			if (type.name().equals(string))
				return type;
		}
		return types[0];
	}
}
