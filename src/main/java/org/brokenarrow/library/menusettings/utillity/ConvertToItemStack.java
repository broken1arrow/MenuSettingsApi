package org.brokenarrow.library.menusettings.utillity;

import com.google.common.base.Enums;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;

/**
 * This class convert object to itemstack (if the object can be translated), It also check if you
 * input string enum of a item. First check and translate it to right item depending
 * on minecraft version and then convert to matrial and last to itemstack.
 */
public class ConvertToItemStack {

	protected ConvertToItemStack() {
	}

	/**
	 * Check the objekt if it ether ItemStack,Material or String
	 * last one need the name be same as the Material name
	 * (upper case or not do not mater (this method convert it to upper case auto)).
	 *
	 * @param object of ether ItemStack,Material or String.
	 * @return itemstack.
	 */
	public ItemStack checkItem(final Object object) {
		if (object instanceof ItemStack)
			return (ItemStack) object;
		else if (object instanceof Material)
			return new ItemStack((Material) object);
		else if (object instanceof String) {
			String stringName = ((String) object).toUpperCase(Locale.ROOT);
			return checkString(stringName);
		}
		return null;
	}

	/**
	 * Check the objekt if it ether ItemStack,Material or String
	 * last one need the name be same as the Material name
	 * (upper case or not do not mater (this method convert it to upper case auto)).
	 * <p>
	 * <p>
	 * This is a help method for older minecraft versions some not have easy way to use colors.
	 * This option only work on items some can use colors.
	 *
	 * @param object of ether ItemStack,Material or String.
	 * @param color  of your item (if it like glass,wool or concrete as example).
	 * @return itemstack.
	 */
	public ItemStack checkItem(final Object object, String color) {
		color = color.toUpperCase(Locale.ROOT);
		if (object instanceof ItemStack)
			return checkItemStack((ItemStack) object, color);
		else if (object instanceof Material) {
			short colorNumber = checkColor(color);
			if (colorNumber > 0)
				return new ItemStack((Material) object, 1, colorNumber);
			return new ItemStack((Material) object, 1);
		} else if (object instanceof String) {
			String stringName = ((String) object).toUpperCase(Locale.ROOT);
			return checkString(stringName);
		}
		return null;
	}

	public ItemStack checkItemStack(final ItemStack itemStack, String color) {
		if (ServerVersion.olderThan(ServerVersion.v1_13) && itemStack != null) {
			short colorNumber = checkColor(color);
			ItemStack stack;
			if (colorNumber > 0)
				stack = new ItemStack(itemStack.getType(), itemStack.getAmount(), colorNumber);
			else
				stack = new ItemStack(itemStack.getType(), itemStack.getAmount());
			ItemMeta itemMeta = itemStack.getItemMeta();
			if (itemMeta != null)
				stack.setItemMeta(itemMeta);
			return stack;
		}
		return itemStack;
	}

	public ItemStack checkString(final String stringName) {
		if (ServerVersion.olderThan(ServerVersion.v1_13)) {
			ItemStack stack = createStack(stringName, 1);
			if (stack != null)
				return stack;
		}
		return new ItemStack(Enums.getIfPresent(Material.class, stringName).orNull() == null ? Material.AIR : Material.valueOf(stringName));
	}

	public ItemStack createStack(final String item, int amount) {
		if (amount <= 0)
			amount = 1;
		int color = checkColor(item);
		if (item.contains("GLASS")) {
			return new ItemStack(Material.valueOf("STAINED_GLASS"), amount, (short) color);
		}
		if (item.contains("GLASS_PANE")) {
			return new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), amount, (short) color);
		}
		if (item.endsWith("_WOOL")) {
			return new ItemStack(Material.valueOf("WOOL"), amount, (short) color);
		}
		if (item.endsWith("_CARPET")) {
			return new ItemStack(Material.valueOf("CARPET"), amount, (short) color);
		}
		if (ServerVersion.newerThan(ServerVersion.v1_8)) {
			if (item.contains("CONCRETE_POWDER")) {
				return new ItemStack(Material.valueOf("CONCRETE_POWDER"), amount, (short) color);
			}

			if (item.endsWith("_CONCRETE")) {
				return new ItemStack(Material.valueOf("CONCRETE"), amount, (short) color);
			}
		}
		if ((item.endsWith("_TERRACOTTA") || item.endsWith("_STAINED_CLAY")) && !item.endsWith("GLAZED_TERRACOTTA")) {
			return new ItemStack(Material.valueOf("STAINED_CLAY"), amount, (short) color);
		}
		if (item.equals("TERRACOTTA")) {
			return new ItemStack(Material.valueOf("HARD_CLAY"), amount, (short) 0);
		} else {
			Material material = Enums.getIfPresent(Material.class, item).orNull();
			if (material != null && color != -1)
				return new ItemStack(material, amount, (short) color);
			else if (material != null)
				return new ItemStack(material, amount);
			else
				return null;
		}
	}

	public short checkColor(String color) {
		int end;
		if (color.startsWith("LIGHT")) {
			end = color.indexOf("_S");
			if (end < 0)
				end = color.indexOf("_G");
			if (end < 0)
				end = color.indexOf("_P");
			if (end < 0)
				end = color.indexOf("_C");
			if (end < 0)
				end = color.indexOf("_W");
		} else
			end = color.indexOf('_');
		if (end < 0)
			end = color.length();
		color = color.substring(0, end);

		if (color.equals("WHITE"))
			return 0;
		if (color.equals("ORANGE"))
			return 1;
		if (color.equals("MAGENTA"))
			return 2;
		if (color.equals("LIGHT_BLUE"))
			return 3;
		if (color.equals("YELLOW"))
			return 4;
		if (color.equals("LIME"))
			return 5;
		if (color.equals("PINK"))
			return 6;
		if (color.equals("GRAY"))
			return 7;
		if (color.equals("LIGHT_GRAY"))
			return 8;
		if (color.equals("CYAN"))
			return 9;
		if (color.equals("PURPLE"))
			return 10;
		if (color.equals("BLUE"))
			return 11;
		if (color.equals("BROWN"))
			return 12;
		if (color.equals("GREEN"))
			return 13;
		if (color.equals("RED"))
			return 14;
		if (color.equals("BLACK"))
			return 15;
		return -1;
	}
}
