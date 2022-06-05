package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.builders.ItemChecks;
import org.brokenarrow.library.menusettings.builders.ItemWrapper;
import org.brokenarrow.library.menusettings.utillity.CreateItemStack;
import org.brokenarrow.library.menusettings.utillity.ServerVersion;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.setPlaceholders;
import static org.brokenarrow.library.menusettings.utillity.CreateItemStack.formatColors;

public class HasItemRequirement extends Requirement {

	private final ItemWrapper wrapper;
	private final boolean invert;

	public HasItemRequirement(ItemWrapper wrapper, boolean invert) {
		this.wrapper = wrapper;
		this.invert = invert;

	}

	@Override
	boolean estimate(Player wiver) {
		String matrial = setPlaceholders(wiver, wrapper.getIcon()).toUpperCase(Locale.ROOT);
		ItemChecks itemChecks = this.wrapper.getItemChecks();
		if (itemChecks == null) return false;

		ItemStack itemStack = CreateItemStack.of(matrial).makeItemStack();
		if (itemStack == null || itemStack.getType() == Material.AIR)
			return this.invert;
		ItemStack[] armorSlots = itemChecks.isCheckArmorSlots() ? wiver.getInventory().getArmorContents() : null;
		ItemStack[] offHand = itemChecks.isCheckOffHand() ? wiver.getInventory().getExtraContents() : null;
		ItemStack[] playerInventory = wiver.getInventory().getStorageContents();
		int total = 0;
		for (ItemStack inventoryItem : playerInventory) {
			if (checkIfIsRequiredItem(inventoryItem, itemChecks, wiver, itemStack.getType()))
				total += inventoryItem.getAmount();
		}
		if (offHand != null)
			for (ItemStack inventoryItem : offHand) {
				if (checkIfIsRequiredItem(inventoryItem, itemChecks, wiver, itemStack.getType()))
					total += inventoryItem.getAmount();
			}
		if (armorSlots != null)
			for (ItemStack inventoryItem : armorSlots) {
				if (checkIfIsRequiredItem(inventoryItem, itemChecks, wiver, itemStack.getType()))
					total += inventoryItem.getAmount();
			}
		return this.invert == total < this.wrapper.getAmount();
	}

	private boolean checkIfIsRequiredItem(ItemStack itemToCheck, ItemChecks itemChecks, Player wiver, Material material) {
		if (itemToCheck == null || itemToCheck.getType() != material) return false;
		if (this.wrapper.getData() != 0 && itemToCheck.getDurability() != this.wrapper.getData()) return false;


		ItemMeta itemMetaOnInventoryItem = itemToCheck.getItemMeta();
		if (itemMetaOnInventoryItem != null) {
			if (itemChecks.isStrict()) {
				if (ServerVersion.newerThan(ServerVersion.v1_13))
					if (itemMetaOnInventoryItem.hasCustomModelData())
						return false;
				if (itemMetaOnInventoryItem.hasLore())
					return false;
				return !itemMetaOnInventoryItem.hasDisplayName();

			}
			if (this.wrapper.getCustomModeldata() != 0) {
				if (ServerVersion.newerThan(ServerVersion.v1_13)) {
					if (!itemMetaOnInventoryItem.hasCustomModelData())
						return false;
					if (itemMetaOnInventoryItem.getCustomModelData() != this.wrapper.getCustomModeldata())
						return false;
				}
			}

			if (this.wrapper.getDisplayname() != null) {
				if (itemMetaOnInventoryItem.hasDisplayName()) {
					String itemToMatch = formatColors(setPlaceholders(wiver, this.wrapper.getDisplayname()));
					String itemInInventory = formatColors(setPlaceholders(wiver, itemMetaOnInventoryItem.getDisplayName()));
					CheckTypes types = null;

					if (itemChecks.isCheckNameContains())
						types = CheckTypes.CONTAINS;
					if (itemChecks.isCheckNameIgnorecase())
						types = CheckTypes.EQUALS_IGNORE_CASE;
					if (itemChecks.isCheckNameEquals())
						types = CheckTypes.EQUALS;

					if (!checkLore(itemToMatch, itemInInventory, types))
						return false;

				} else {
					return false;
				}
			}
			if (this.wrapper.getLore() != null) {
				if (itemMetaOnInventoryItem.hasLore()) {
					String itemToMatch = getLore(this.wrapper.getLore(), wiver);
					String itemInInventory = getLore(itemMetaOnInventoryItem.getLore(), wiver);
					CheckTypes types = null;

					if (itemChecks.isCheckLoreContains())
						types = CheckTypes.CONTAINS;
					if (itemChecks.isCheckLoreIgnorecase())
						types = CheckTypes.EQUALS_IGNORE_CASE;
					if (itemChecks.isCheckLoreEquals())
						types = CheckTypes.EQUALS;

					if (!checkLore(itemToMatch, itemInInventory, types))
						return false;

				} else {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private boolean checkLore(String itemToMatch, String itemInInventory, CheckTypes types) {
		if (types == null) return false;

		if (types == CheckTypes.CONTAINS)
			if (!itemToMatch.contains(itemInInventory))
				return false;
		if (types == CheckTypes.EQUALS_IGNORE_CASE)
			if (!itemToMatch.equalsIgnoreCase(itemInInventory))
				return false;
		if (types == CheckTypes.EQUALS)
			return itemToMatch.equals(itemInInventory);

		return true;
	}

	private String getLore(List<String> loreList, Player player) {
		if (loreList == null) return "";

		return loreList.stream().map((lore) -> setPlaceholders(player, lore))
				.map(CreateItemStack::formatColors).collect(Collectors.joining("&&"));
	}

	public enum CheckTypes {
		CONTAINS,
		EQUALS_IGNORE_CASE,
		EQUALS
	}
}


