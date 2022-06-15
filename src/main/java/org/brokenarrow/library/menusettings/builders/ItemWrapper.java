package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.utillity.Tuple;
import org.bukkit.FireworkEffect;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class ItemWrapper {

	private final String displayname;
	private final String icon;
	private final String matrialColor;
	private final String rbg;
	private final String dynamicAmount;
	private final List<String> lore;
	private final List<PotionEffect> portionEffects;
	private final List<Pattern> bannerPatterns;
	private final Map<Enchantment, Tuple<Integer, Boolean>> enchantments;
	private final List<ItemFlag> hideFlags;
	//todo Potion Effects and RGB on some items, shall it be in own class or several classes?
	//See if this even is needed private final Map<String,Object> customNbt;
	private final int amount;
	private final int customModeldata;
	private final int data;
	private final boolean glow;
	private final boolean unbreakable;
	private final ItemChecks itemChecks;
	private final FireworkEffect fireworkEffects;
	private final Builder builder;

	private ItemWrapper(Builder builder) {
		this.displayname = builder.displayname;
		this.icon = builder.icon;
		this.matrialColor = builder.matrialColor;
		this.rbg = builder.rbg;
		this.dynamicAmount = builder.dynamicAmount;
		this.lore = builder.lore;
		this.portionEffects = builder.portionEffects;
		this.fireworkEffects = builder.fireworkEffects;
		this.bannerPatterns = builder.bannerPatterns;
		this.enchantments = builder.enchantments;
		this.hideFlags = builder.hideFlags;
		this.amount = builder.amount;
		this.customModeldata = builder.customModeldata;
		this.data = builder.data;
		this.glow = builder.glow;
		this.unbreakable = builder.unbreakable;
		this.itemChecks = builder.itemChecks;

		this.builder = builder;

	}

	@Nullable
	public String getDisplayname() {
		return displayname;
	}

	@Nullable
	public String getIcon() {
		return icon;
	}

	@Nullable
	public String getMatrialColor() {
		return matrialColor;
	}

	@Nullable
	public String getRbg() {
		return rbg;
	}

	@Nullable
	public String getDynamicAmount() {
		return dynamicAmount;
	}

	@Nullable
	public List<String> getLore() {
		return lore;
	}

	@Nullable
	public List<PotionEffect> getPortionEffects() {
		return portionEffects;
	}

	@Nullable
	public FireworkEffect getFireworkEffects() {
		return fireworkEffects;
	}

	@Nullable
	public Map<Enchantment, Tuple<Integer, Boolean>> getEnchantments() {
		return enchantments;
	}

	@Nullable
	public List<Pattern> getBannerPatterns() {
		return bannerPatterns;
	}

	@Nullable
	public List<ItemFlag> getHideFlags() {
		return hideFlags;
	}

	@NotNull
	public int getAmount() {
		return amount;
	}

	@NotNull
	public int getCustomModeldata() {
		return customModeldata;
	}

	@NotNull
	public int getData() {
		return data;
	}

	@NotNull
	public boolean isGlow() {
		return glow;
	}

	@NotNull
	public boolean isUnbreakable() {
		return unbreakable;
	}

	@Nullable
	public ItemChecks getItemChecks() {
		return itemChecks;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {
		private String displayname;
		private String icon;
		private String matrialColor;
		private String rbg;
		private String dynamicAmount;
		private List<String> lore;
		private List<PotionEffect> portionEffects;
		private List<Pattern> bannerPatterns;
		private List<ItemFlag> hideFlags;
		private Map<Enchantment, Tuple<Integer, Boolean>> enchantments;
		private int amount = 1;
		private int customModeldata = -1;
		private int data = -1;
		private boolean glow;
		private boolean unbreakable;
		private FireworkEffect fireworkEffects;
		private ItemChecks itemChecks;

		public Builder setDisplayname(String displayname) {
			this.displayname = displayname;
			return this;
		}

		public Builder setIcon(String icon) {
			this.icon = icon;
			return this;
		}

		public Builder setMatrialColor(String matrialColor) {
			this.matrialColor = matrialColor;
			return this;
		}

		public Builder setRbg(String rbg) {
			this.rbg = rbg;
			return this;
		}

		public Builder setDynamicAmount(String dynamicAmount) {
			this.dynamicAmount = dynamicAmount;
			return this;
		}

		public Builder setLore(List<String> lore) {
			this.lore = lore;
			return this;
		}

		public Builder setPortionEffects(List<PotionEffect> portionEffects) {
			this.portionEffects = portionEffects;
			return this;
		}

		public Builder setFireworkEffects(FireworkEffect fireworkEffects) {
			this.fireworkEffects = fireworkEffects;
			return this;
		}

		public Builder setBannerPatterns(List<Pattern> bannerPatterns) {
			this.bannerPatterns = bannerPatterns;
			return this;
		}

		public Builder setHideFlags(List<ItemFlag> hideFlags) {
			this.hideFlags = hideFlags;
			return this;
		}

		public Builder setEnchantments(Map<Enchantment, Tuple<Integer, Boolean>> enchantments) {
			this.enchantments = enchantments;
			return this;
		}

		public Builder setAmount(int amount) {
			this.amount = amount;
			return this;
		}

		public Builder setCustomModeldata(int customModeldata) {
			this.customModeldata = customModeldata;
			return this;
		}

		public Builder setData(int data) {
			this.data = data;
			return this;
		}

		public Builder setGlow(boolean glow) {
			this.glow = glow;
			return this;
		}

		public Builder setUnbreakable(boolean unbreakable) {
			this.unbreakable = unbreakable;
			return this;
		}

		public Builder setItemChecks(ItemChecks itemChecks) {
			this.itemChecks = itemChecks;
			return this;
		}

		public ItemWrapper build() {
			return new ItemWrapper(this);
		}
	}
}
