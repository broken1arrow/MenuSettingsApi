package org.brokenarrow.library.menusettings.utillity;


import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

import static org.broken.arrow.library.itemcreator.SkullCreator.getSkullUrl;

/**
 * A library for the Bukkit API to create player skulls
 * from names, base64 strings, and texture URLs.
 * <p>
 * Does not use any NMS code, and should work across all versions.
 *
 * @author Dean B on 12/28/2016.
 */
public class SkullCreator {

	private SkullCreator() {
	}
	/**
	 * Creates a player skull item with the skin based on a player's name.
	 *
	 * @param name The Player's name.
	 * @return The head of the Player.
	 * @deprecated names don't make for good identifiers.
	 */
	public static ItemStack itemFromName(String name) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemFromName(name);
	}

	/**
	 * Creates a player skull item with the skin based on a player's UUID.
	 *
	 * @param id The Player's UUID.
	 * @return The head of the Player.
	 */
	public static ItemStack itemFromUuid(UUID id) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemFromUuid(id);
	}

	/**
	 * Creates a player skull item with the skin at a Mojang URL.
	 *
	 * @param url The Mojang URL.
	 * @return The head of the Player.
	 */
	public static ItemStack itemFromUrl(String url) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemFromUrl(url);
	}

	/**
	 * Creates a player skull item with the skin based on a base64 string.
	 *
	 * @param base64 The Mojang URL.
	 * @return The head of the Player.
	 */
	public static ItemStack itemFromBase64(String base64) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemFromBase64(base64);
	}

	/**
	 * Modifies a skull to use the skin of the player with a given name.
	 *
	 * @param item The item to apply the name to. Must be a player skull.
	 * @param name The Player's name.
	 * @return The head of the Player.
	 * @deprecated names don't make for good identifiers.
	 */
	@Deprecated
	public static ItemStack itemWithName(ItemStack item, String name) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemWithName(item, name);
	}

	/**
	 * Modifies a skull to use the skin of the player with a given UUID.
	 *
	 * @param item The item to apply the name to. Must be a player skull.
	 * @param id   The Player's UUID.
	 * @return The head of the Player.
	 */
	public static ItemStack itemWithUuid(ItemStack item, UUID id) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemWithUuid(item, id);
	}

	/**
	 * Modifies a skull to use the skin at the given Mojang URL.
	 *
	 * @param item The item to apply the skin to. Must be a player skull.
	 * @param url  The URL of the Mojang skin.
	 * @return The head associated with the URL.
	 */
	public static ItemStack itemWithUrl(ItemStack item, String url) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemWithUrl(item, url);
	}

	/**
	 * Modifies a skull to use the skin based on the given base64 string.
	 *
	 * @param item   The ItemStack to put the base64 onto. Must be a player skull.
	 * @param base64 The base64 string containing the texture.
	 * @return The head with a custom texture.
	 */
	public static ItemStack itemWithBase64(ItemStack item, String base64) {
		return org.broken.arrow.library.itemcreator.SkullCreator.itemWithBase64(item, base64);
	}

	/**
	 * Sets the block to a skull with the given name.
	 *
	 * @param block The block to set.
	 * @param name  The player to set it to.
	 * @deprecated names don't make for good identifiers.
	 */
	@Deprecated
	public static void blockWithName(Block block, String name) {
		org.broken.arrow.library.itemcreator.SkullCreator.blockWithName(block, name);
	}

	/**
	 * Sets the block to a skull with the given UUID.
	 *
	 * @param block The block to set.
	 * @param id    The player to set it to.
	 */
	public static void blockWithUuid(Block block, UUID id) {
		org.broken.arrow.library.itemcreator.SkullCreator.blockWithUuid(block, id);
	}

	/**
	 * Sets the block to a skull with the skin found at the provided mojang URL.
	 *
	 * @param block The block to set.
	 * @param url   The mojang URL to set it to use.
	 */
	public static void blockWithUrl(Block block, String url) {
		org.broken.arrow.library.itemcreator.SkullCreator.blockWithUrl(block, url);
	}

	/**
	 * Sets the block to a skull with the skin for the base64 string.
	 *
	 * @param block  The block to set.
	 * @param base64 The base64 to set it to use.
	 */
	public static void blockWithBase64(Block block, String base64) {
		org.broken.arrow.library.itemcreator.SkullCreator.blockWithBase64(block, base64);
	}


	public static boolean applySkullFromItem(Block block, ItemStack item) {
		ItemMeta itemMeta = item.getItemMeta();
		if (!(itemMeta instanceof SkullMeta)) return false;

		SkullMeta meta = (SkullMeta) itemMeta;
		String url = getSkullUrl(meta);
		if (url != null) {
			blockWithUrl(block, url);
			return true;
		}

		if (meta.hasOwner()) {
			OfflinePlayer player = meta.getOwningPlayer();
			if (player != null) {
				blockWithUuid(block, player.getUniqueId());
			}
		}
		return true;
	}
}