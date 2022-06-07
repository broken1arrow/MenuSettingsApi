package org.brokenarrow.library.menusettings.utillity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum ArmorSlots {

	ARMOR_HELMET("armor_helmet"),
	ARMOR_CHESTPLATE("armor_chestplate"),
	ARMOR_LEGGINGS("armor_leggings"),
	ARMOR_BOOTS("armor_boots");

	private final String identifier;

	ArmorSlots(String armorPiece) {
		identifier = armorPiece;
	}

	public static ArmorSlots getType(String string) {
		ArmorSlots[] requirementTypes = values();

		for (ArmorSlots type : requirementTypes) {
			if (type.getIdentifier().equals(string))
				return type;
		}
		return null;
	}

	public static ItemStack getArmorPiece(Player viewer, String type) {
		ArmorSlots armorSlots = ArmorSlots.getType(type);
		if (armorSlots == null) return null;

		switch (armorSlots) {
			case ARMOR_HELMET:
				if (viewer.getInventory().getHelmet() != null)
					return viewer.getInventory().getHelmet().clone();
			case ARMOR_CHESTPLATE:
				if (viewer.getInventory().getChestplate() != null)
					return viewer.getInventory().getChestplate().clone();
			case ARMOR_LEGGINGS:
				if (viewer.getInventory().getLeggings() != null)
					return viewer.getInventory().getLeggings().clone();
			case ARMOR_BOOTS:
				if (viewer.getInventory().getBoots() != null)
					return viewer.getInventory().getBoots().clone();
			default:
				return null;
		}
	}

	public String getIdentifier() {
		return identifier;
	}

}
