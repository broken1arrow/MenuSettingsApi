package org.brokenarrow.library.menusettings.builders;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginAwareness;

import java.util.List;

public final class ItemWrapper {

	private final String displayname;
	private final String icon;
	private final String matrialColor;
	private final List<String> lore;
	private final List<Enchantment> enchantments;
	private final List<String> enchantments;
	private final List<PluginAwareness.Flags> enchantments;
	private final ItemMeta metadata;
	private final BannerMeta metadata;
	//todo Potion Effects and RGB on some items, shall it be in own class or several classes?
	//See if this even is needed private final Map<String,Object> customNbt;
	private final int amount;
	private final int modeldata;
	private final int data;
	private final boolean glow;
	private final boolean strict;


	private ItemWrapper() {

	}

	private ItemWrapper(Builder builder) {
	}

	public static class Builder {
		private String displayname;
		private String icon;
		private String matrialColor;
		private List<String> lore;
		private int amount;
		private int modeldata;
		private int data;
		private boolean glow;
		private boolean strict;

	}
}
