package org.brokenarrow.library.menusettings.utillity;

import com.google.common.base.Enums;
import de.tr7zw.changeme.nbtapi.metodes.RegisterNbtAPI;
import org.broken.lib.rbg.TextTranslator;
import org.brokenarrow.library.menusettings.builders.ItemWrapper;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.*;

/**
 * Create items and also count number of items of
 * specific type.
 */

public class CreateItemStack {

	private final ItemStack itemStack;
	private final Material matrial;
	private final String stringItem;
	private String rgb;
	private final Iterable<?> itemArray;
	private final String displayName;
	private final List<String> lore;
	private final Map<Enchantment, Tuple<Integer, Boolean>> enchantments = new HashMap<>();
	private final List<ItemFlag> visibleItemFlags = new ArrayList<>();
	private final List<Pattern> pattern = new ArrayList<>();
	private final List<PotionEffect> portionEffects = new ArrayList<>();
	private String itemMetaKey;
	private Object itemMetaValue;
	private Map<String, Object> itemMetaMap;
	private int amoutOfItems;
	private int red = -1;
	private int green = -1;
	private int blue = -1;
	private short data = -1;
	private int customModeldata = -1;
	private boolean glow;
	private boolean showEnchantments;
	private boolean waterBottle;
	private boolean unbreakable;
	private static ConvertToItemStack convertItems;

	private CreateItemStack(final Bulider bulider) {
		if (convertItems == null)
			convertItems = new ConvertToItemStack();
		this.itemStack = bulider.itemStack;
		this.matrial = bulider.matrial;
		this.stringItem = bulider.stringItem;
		this.itemArray = bulider.itemArray;
		this.displayName = bulider.displayName;
		this.lore = bulider.lore;

	}

	/**
	 * It will use data from ItemWrapper to make the item.
	 * Finish creation with {@link #makeItemStack()}
	 *
	 * @param itemWrapper will use this class to build the item.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final ItemWrapper itemWrapper, Player player) {
		CreateItemStack createItemStack;
		String icon = setPlaceholders(player, itemWrapper.getIcon());
		String displayName = setPlaceholders(player, itemWrapper.getDisplayname());
		List<String> lore = setPlaceholders(player, itemWrapper.getLore());
		if (itemWrapper.getMatrialColor() == null)
			createItemStack = of(icon, displayName, lore);
		else
			createItemStack = of(icon, itemWrapper.getMatrialColor(), displayName, lore);

		createItemStack.addEnchantments(itemWrapper.getEnchantments(), false);
		createItemStack.addPattern(itemWrapper.getBannerPatterns());
		createItemStack.setItemFlags(itemWrapper.getHideFlags());
		createItemStack.setRgb(itemWrapper.getRbg());
		createItemStack.addPortionEffects(itemWrapper.getPortionEffects());
		createItemStack.setGlow(itemWrapper.isGlow());
		createItemStack.setUnbreakable(itemWrapper.isUnbreakable());
		itemWrapper.getData();
		itemWrapper.getCustomModeldata();


		if (itemWrapper.getDynamicAmount() != null)
			try {
				createItemStack.setAmoutOfItems(Integer.parseInt(setPlaceholders(player, itemWrapper.getDynamicAmount())));
			} catch (NumberFormatException exception) {
				exception.printStackTrace();
			}
		return createItemStack;
	}

	/**
	 * Start to create simple item. Some have no displayname or
	 * lore. Finish creation with {@link #makeItemStack()}
	 *
	 * @param item String name,Matrial or Itemstack.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final Object item) {
		return of(item, null, (List<String>) null);
	}

	/**
	 * Start to create simple item. Some have no displayname or
	 * lore, but have metadata. Finish creation with {@link #makeItemStack()}
	 *
	 * @param item          String name,Matrial or Itemstack.
	 * @param itemMetaKey   set metadata key
	 * @param itemMetaValue set metadata value
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final Object item, final String itemMetaKey, final String itemMetaValue) {
		return of(item).setItemMetaData(itemMetaKey, itemMetaValue);
	}

	/**
	 * Start to create simple item. Some have no displayname or
	 * lore. Finish creation with {@link #makeItemStack()}
	 *
	 * @param item  String name,Matrial or Itemstack.
	 * @param color if you use minecraft version before 1.13.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final Object item, String color) {
		return of(item, color, null, (List<String>) null);
	}

	/**
	 * Start to create simple item. Some have no displayname or
	 * lore, but have metadata. Finish creation with {@link #makeItemStack()}
	 *
	 * @param item          String name,Matrial or Itemstack.
	 * @param color         if you use minecraft version before 1.13.
	 * @param itemMetaKey   set metadata key
	 * @param itemMetaValue set metadata value
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final Object item, String color, final String itemMetaKey, final String itemMetaValue) {
		return of(item, color).setItemMetaData(itemMetaKey, itemMetaValue);
	}

	/**
	 * Start to create an item. Finish creation with {@link #makeItemStack()}
	 * <p>
	 * This method uses varargs and add it to list, Like this "a","b","c".
	 * You can also skip adding any value to lore too.
	 *
	 * @param item        String name,Matrial or Itemstack.
	 * @param displayName name on the item.
	 * @param lore        as varargs.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final Object item, final String displayName, final String... lore) {
		return of(item, displayName, Arrays.asList(lore));
	}


	/**
	 * Start to create an item. Finish creation with {@link #makeItemStack()}
	 *
	 * @param item        String name,Matrial or Itemstack.
	 * @param displayName name on the item.
	 * @param lore        on the item.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final Object item, final String displayName, final List<String> lore) {
		return new Bulider(getConvertItems().checkItem(item), displayName, lore).build();
	}

	/**
	 * Start to create an item. Finish creation with {@link #makeItemStack()}
	 *
	 * @param item        String name,Matrial or Itemstack.
	 * @param color       if you use minecraft version before 1.13.
	 * @param displayName name on the item.
	 * @param lore        on the item.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static CreateItemStack of(final Object item, String color, final String displayName, final List<String> lore) {
		return new Bulider(getConvertItems().checkItem(item, color), displayName, lore).build();
	}

	/**
	 * Start to create an item. Finish creation with {@link #makeItemStackArray()}
	 * <p>
	 * This method uses varargs and add it to list, Like this "a","b","c".
	 * You can also skip adding any value to lore too.
	 *
	 * @param itemArray   string name.
	 * @param displayName item name.
	 * @param lore        as varargs.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static <T> CreateItemStack of(final Iterable<T> itemArray, final String displayName, final String... lore) {
		return of(itemArray, displayName, Arrays.asList(lore));
	}

	/**
	 * Start to create an item. Finish creation with {@link #makeItemStackArray()}
	 *
	 * @param itemArray   string name.
	 * @param displayName item name.
	 * @param lore        on the item.
	 * @return CreateItemUtily class or class with air item (if item are null).
	 */
	public static <T> CreateItemStack of(final Iterable<T> itemArray, final String displayName, final List<String> lore) {
		return new Bulider(itemArray, displayName, lore).build();
	}

	/**
	 * Amount of items you want to create.
	 *
	 * @param amoutOfItems item amount.
	 * @return this class.
	 */
	public CreateItemStack setAmoutOfItems(final int amoutOfItems) {
		this.amoutOfItems = amoutOfItems;
		return this;
	}

	public boolean isGlow() {
		return glow;
	}

	/**
	 * Set glow on item and will not show the enchantments.
	 * Use {@link #addEnchantments(Object, boolean, int)} or {@link #addEnchantments(String...)}, for set custom
	 * enchants.
	 *
	 * @param glow set it true and the item will glow.
	 * @return this class.
	 */
	public CreateItemStack setGlow(final boolean glow) {
		this.glow = glow;
		return this;
	}

	public List<Pattern> getPattern() {
		return pattern;
	}

	public CreateItemStack addPattern(Pattern... patterns) {
		if (patterns == null || patterns.length < 1) return this;

		this.pattern.addAll(Arrays.asList(patterns));
		return this;
	}

	public CreateItemStack addPattern(List<Pattern> pattern) {

		this.pattern.addAll(pattern);
		return this;
	}

	public Map<Enchantment, Tuple<Integer, Boolean>> getEnchantments() {
		return enchantments;
	}

	public boolean isWaterBottle() {
		return waterBottle;
	}

	public CreateItemStack setWaterBottle(boolean waterBottle) {
		this.waterBottle = waterBottle;
		return this;
	}

	/**
	 * Add own enchantments. Set {@link #setShowEnchantments(boolean)} to true
	 * if you whant to hide all enchants (defult so will it not hide enchants).
	 * <p>
	 * This method uses varargs and add it to list, like this enchantment;level;levelRestriction or
	 * enchantment;level and it will sett last one to false.
	 * <p>
	 * <p>
	 * Example usage here:
	 * "PROTECTION_FIRE;1;false","PROTECTION_EXPLOSIONS;15;true","WATER_WORKER;1;false".
	 *
	 * @param enchantments list of enchantments you want to add.
	 * @return this class.
	 */

	public CreateItemStack addEnchantments(final String... enchantments) {
		for (String enchant : enchantments) {
			int middle = enchant.indexOf(";");
			int last = enchant.lastIndexOf(";");
			addEnchantments(enchant.substring(0, middle), last > 0 && Boolean.getBoolean(enchant.substring(last + 1)), Integer.parseInt(enchant.substring(middle + 1, Math.max(last, enchant.length()))));
		}
		return this;
	}

	/**
	 * Add own enchantments. Set {@link #setShowEnchantments(boolean)} to true
	 * if you whant to hide all enchants (defult so will it not hide enchants).
	 *
	 * @param enchantmentMap add direcly a map with enchants and level and levelRestrictions.
	 * @param override       the old value in the map if you set it to true.
	 * @return this class.
	 */
	public CreateItemStack addEnchantments(Map<Enchantment, Tuple<Integer, Boolean>> enchantmentMap, boolean override) {
		Valid.checkNotNull(enchantmentMap, "this map is null");
		if (enchantmentMap.isEmpty())
			getPLUGIN().getLogger().log(Level.INFO, "This map is empty so no values vill be added");

		enchantmentMap.forEach((key, value) -> {
			if (!override)
				this.enchantments.putIfAbsent(key, value);
			else
				this.enchantments.put(key, value);
		});
		return this;
	}

	/**
	 * Add own enchantments. Set {@link #setShowEnchantments(boolean)} to true
	 * if you whant to hide all enchants (defult so will it not hide enchants).
	 *
	 * @param enchant          enchantments you want to add, suport string and Enchantment class.
	 * @param levelRestriction bypass the level limit.
	 * @param enchantmentLevel set level for this enchantment.
	 * @return this class.
	 */

	public CreateItemStack addEnchantments(final Object enchant, boolean levelRestriction, int enchantmentLevel) {
		Enchantment enchantment = null;
		if (enchant instanceof String)
			enchantment = Enchantment.getByKey(NamespacedKey.minecraft((String) enchant));
		else if (enchant instanceof Enchantment)
			enchantment = (Enchantment) enchant;

		if (enchantment != null)
			this.enchantments.put(enchantment, new Tuple<>(enchantmentLevel, levelRestriction));
		else
			getPLUGIN().getLogger().log(Level.INFO, "your enchantment: " + enchant + " ,are not valid.");

		return this;
	}


	/**
	 * When use {@link #addEnchantments(Object, boolean, int)}   or {@link #addEnchantments(String...)} and
	 * want to not show enchants set it to true. When use {@link #setGlow(boolean)} it will defult hide
	 * enchants, if you set #setGlow to true and set this to true it will show the enchantments.
	 *
	 * @param showEnchantments true and will show enchants.
	 * @return this class.
	 */
	public CreateItemStack setShowEnchantments(final boolean showEnchantments) {
		this.showEnchantments = showEnchantments;
		return this;
	}

	/**
	 * Set custom metadata on item.
	 *
	 * @param itemMetaKey   key for get value.
	 * @param itemMetaValue value you want to set.
	 * @return this class.
	 */
	public CreateItemStack setItemMetaData(final String itemMetaKey, final Object itemMetaValue) {
		this.itemMetaKey = itemMetaKey;
		this.itemMetaValue = itemMetaValue;
		return this;
	}

	/**
	 * Map list of metadata you want to set on a item.
	 * It use map key and value form the map.
	 *
	 * @param itemMetaMap map of values.
	 * @return this class.
	 */
	public CreateItemStack setItemMetaDataList(final Map<String, Object> itemMetaMap) {
		this.itemMetaMap = itemMetaMap;
		return this;
	}

	public boolean isUnbreakable() {
		return unbreakable;
	}

	public CreateItemStack setUnbreakable(boolean unbreakable) {
		this.unbreakable = unbreakable;
		return this;
	}

	public short getData() {
		return data;
	}

	public CreateItemStack setData(short data) {
		this.data = data;
		return this;
	}

	public int getCustomModeldata() {
		return customModeldata;
	}

	public CreateItemStack setCustomModeldata(int customModeldata) {
		this.customModeldata = customModeldata;
		return this;
	}

	public List<PotionEffect> getPortionEffects() {
		return portionEffects;
	}

	public void addPortionEffects(PotionEffect... potionEffects) {
		portionEffects.addAll(Arrays.asList(potionEffects));
	}

	public void addPortionEffects(List<PotionEffect> potionEffects) {
		portionEffects.addAll(potionEffects);
	}

	public String getRgb() {
		return rgb;
	}

	public CreateItemStack setRgb(String rgb) {
		this.rgb = rgb;

		String[] colors = this.getRgb().split(",");
		Valid.checkBoolean(colors.length < 4, "rgb is not format correcly. Should be formated like this 'r,b,g'. Example '20,15,47'.");
		try {
			red = Integer.parseInt(colors[0]);
			green = Integer.parseInt(colors[2]);
			blue = Integer.parseInt(colors[1]);
		} catch (NumberFormatException exception) {
			getPLUGIN().getLogger().log(Level.WARNING, "you don´t use numbers inside this " + rgb);
			exception.printStackTrace();
		}

		return this;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	/**
	 * Hide one or several metadata values on a itemstack.
	 *
	 * @param itemFlags add one or several flags you not want to hide.
	 * @return this class.
	 */
	public CreateItemStack setItemFlags(ItemFlag... itemFlags) {
		this.setItemFlags(Arrays.asList(itemFlags));
		return this;
	}

	/**
	 * Hide one or several metadata values on a itemstack.
	 *
	 * @param itemFlags add one or several flags you not want to hide.
	 * @return this class.
	 */
	public CreateItemStack setItemFlags(List<ItemFlag> itemFlags) {
		this.visibleItemFlags.addAll(itemFlags);
		return this;
	}

	/**
	 * Create itemstack, call it after you added all data you want
	 * on the item.
	 *
	 * @return new itemstack with amount of 1 if you not set it.
	 */
	public ItemStack makeItemStack() {
		ItemStack itemstack = checkTypeOfItem();
		RegisterNbtAPI nbtApi = getNbtApi();

		if (itemstack != null && itemstack.getType() != Material.AIR) {
			if (nbtApi != null)
				if (this.itemMetaMap != null) {
					for (final Map.Entry<String, Object> entitys : this.itemMetaMap.entrySet()) {
						itemstack = nbtApi.getCompMetadata().setMetadata(itemstack, entitys.getKey(), entitys.getValue());
					}
				} else if (this.itemMetaKey != null && this.itemMetaValue != null)
					itemstack = nbtApi.getCompMetadata().setMetadata(itemstack, this.itemMetaKey, this.itemMetaValue);

			final ItemMeta itemMeta = itemstack.getItemMeta();

			if (itemMeta != null) {
				if (this.displayName != null) {
					itemMeta.setDisplayName(translateColors(this.displayName));
				}
				if (this.lore != null && !this.lore.isEmpty()) {
					itemMeta.setLore(translateColors(this.lore));
				}
				addItemMeta(itemMeta);
				
				if (this.getData() > 0)
					itemstack.setDurability(this.getData());
			}
			itemstack.setItemMeta(itemMeta);
			itemstack.setAmount(this.amoutOfItems == 0 ? 1 : this.amoutOfItems);
		}
		return itemstack != null ? itemstack : new ItemStack(Material.AIR);
	}

	/**
	 * Create itemstack array, call it after you added all data you want
	 * on the item.
	 *
	 * @return new itemstack array with amount of 1 if you not set it.
	 */
	public ItemStack[] makeItemStackArray() {
		ItemStack itemstack = null;
		RegisterNbtAPI nbtApi = getNbtApi();
		final List<ItemStack> list = new ArrayList<>();

		if (this.itemArray != null)
			for (final Object itemStringName : this.itemArray) {
				itemstack = checkTypeOfItem(itemStringName);
				if (itemstack == null) continue;

				if (!(itemstack.getType() == Material.AIR)) {
					if (nbtApi != null)
						if (itemMetaMap != null) {
							for (final Map.Entry<String, Object> entitys : this.itemMetaMap.entrySet()) {
								itemstack = nbtApi.getCompMetadata().setMetadata(itemstack, entitys.getKey(), entitys.getValue());
							}
						} else if (this.itemMetaKey != null && this.itemMetaValue != null)
							itemstack = nbtApi.getCompMetadata().setMetadata(itemstack, this.itemMetaKey, this.itemMetaValue);

					final ItemMeta itemMeta = itemstack.getItemMeta();

					if (itemMeta != null) {
						if (this.displayName != null) {
							itemMeta.setDisplayName(translateColors(this.displayName));
						}
						if (this.lore != null && !this.lore.isEmpty()) {
							itemMeta.setLore(translateColors(this.lore));
						}
						addItemMeta(itemMeta);

					}
					itemstack.setItemMeta(itemMeta);
					itemstack.setAmount(this.amoutOfItems == 0 ? 1 : this.amoutOfItems);
					list.add(itemstack);
				}
			}
		return itemstack != null ? list.toArray(new ItemStack[0]) : new ItemStack[]{new ItemStack(Material.AIR)};
	}


	private ItemStack checkTypeOfItem() {
		if (this.itemStack != null)
			return itemStack;
		else if (this.matrial != null)
			return new ItemStack(matrial);
		else if (this.stringItem != null)
			return new ItemStack(Enums.getIfPresent(Material.class, this.stringItem).orNull() == null ? Material.AIR : Material.valueOf(this.stringItem));

		return null;
	}

	private ItemStack checkTypeOfItem(Object object) {
		return getConvertItems().checkItem(object);
	/*	if (object instanceof ItemStack)
			return (ItemStack) object;
		else if (object instanceof Material)
			return new ItemStack((Material) object);
		else if (object instanceof String)
			return new ItemStack(Enums.getIfPresent(Material.class, (String) object).orNull() == null ? Material.AIR : Material.valueOf((String) object));

		return null;*/
	}

	private void addItemMeta(final ItemMeta itemMeta) {
		addBannerPatterns(itemMeta);
		addLeatherArmorColors(itemMeta);
		addFireworkEffect(itemMeta);
		addEnchantments(itemMeta);
		addBottleEffects(itemMeta);
		addUnbreakableMeta(itemMeta);
		addCustomModelData(itemMeta);
	}

	private void hideEnchantments(final ItemMeta itemMeta) {
		itemMeta.addItemFlags(Arrays.stream(ItemFlag.values()).filter(itemFlag -> !visibleItemFlags.contains(itemFlag)).toArray(ItemFlag[]::new));
	}

	public boolean addEnchantments(final ItemMeta itemMeta) {
		if (!this.getEnchantments().isEmpty()) {
			boolean haveEnchant = false;
			for (final Map.Entry<Enchantment, Tuple<Integer, Boolean>> enchant : this.getEnchantments().entrySet()) {
				if (enchant == null) {
					getPLUGIN().getLogger().log(Level.INFO, "Your enchantment are null.");
					continue;
				}
				Tuple<Integer, Boolean> level = enchant.getValue();
				haveEnchant = itemMeta.addEnchant(enchant.getKey(), level.getFirst() <= 0 ? 1 : level.getFirst(), level.getSecond());
			}
			if (isShowEnchantments())
				hideEnchantments(itemMeta);
			return haveEnchant;
		} else if (this.isGlow()) {
			if (!isShowEnchantments() || !visibleItemFlags.isEmpty())
				hideEnchantments(itemMeta);
			return itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, false);
		}
		return false;
	}

	private void addBannerPatterns(final ItemMeta itemMeta) {
		if (getPattern() == null || getPattern().isEmpty())
			return;

		if (itemMeta instanceof BannerMeta) {
			BannerMeta bannerMeta = (BannerMeta) itemMeta;
			bannerMeta.setPatterns(getPattern());
		}
	}

	private void addLeatherArmorColors(final ItemMeta itemMeta) {
		if (getRgb() == null || getRed() < 0)
			return;

		if (itemMeta instanceof LeatherArmorMeta) {
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemMeta;
			leatherArmorMeta.setColor(Color.fromBGR(getBlue(), getGreen(), getRed()));
		}
	}

	private void addBottleEffects(final ItemMeta itemMeta) {

		if (itemMeta instanceof PotionMeta) {
			PotionMeta potionMeta = (PotionMeta) itemMeta;

			if (isWaterBottle()) {
				PotionData potionData = new PotionData(PotionType.WATER);
				potionMeta.setBasePotionData(potionData);
				return;
			}
			if (getPortionEffects() == null || getPortionEffects().isEmpty())
				return;
			if (getRgb() == null || (getRed() < 0 && getGreen() < 0 && getBlue() < 0))
				return;
			potionMeta.setColor(Color.fromBGR(getBlue(), getGreen(), getRed()));
			getPortionEffects().forEach((portionEffect) -> potionMeta.addCustomEffect(portionEffect, true));

		}
	}

	private void addFireworkEffect(final ItemMeta itemMeta) {
		if (getRgb() == null || (getRed() < 0 && getGreen() < 0 && getBlue() < 0))
			return;

		if (itemMeta instanceof FireworkEffectMeta) {
			FireworkEffectMeta fireworkEffectMeta = (FireworkEffectMeta) itemMeta;
			fireworkEffectMeta.setEffect(FireworkEffect.builder().withColor(Color.fromBGR(getBlue(), getGreen(), getRed())).build());
		}
	}

	private void addUnbreakableMeta(final ItemMeta itemMeta) {
		itemMeta.setUnbreakable(isUnbreakable());
	}

	private void addCustomModelData(final ItemMeta itemMeta) {
		if (this.getCustomModeldata() > 0)
			itemMeta.setCustomModelData(this.getCustomModeldata());
	}

	private boolean isShowEnchantments() {
		return showEnchantments;
	}


	private List<String> translateColors(final List<String> rawLore) {
		final List<String> lores = new ArrayList<>();
		for (final String lore : rawLore)
			if (lore != null)
				lores.add(TextTranslator.toSpigotFormat(lore));
		return lores;
	}

	private String translateColors(final String rawSingelLine) {
		return TextTranslator.toSpigotFormat(rawSingelLine);
	}


	public static ItemStack createItemStackAsOne(Material material) {
		ItemStack itemstack = null;
		if (material != null)
			itemstack = new ItemStack(material);

		return createItemStackAsOne(itemstack != null ? itemstack : new ItemStack(Material.AIR));
	}

	public static ItemStack createItemStackAsOne(ItemStack itemstacks) {
		ItemStack itemstack = null;
		if (itemstacks != null && !itemstacks.getType().equals(Material.AIR)) {
			itemstack = itemstacks.clone();
			ItemMeta meta = itemstack.getItemMeta();
			itemstack.setItemMeta(meta);
			itemstack.setAmount(1);
		}
		return itemstack != null ? itemstack : new ItemStack(Material.AIR);
	}

	public static ItemStack[] createItemStackAsOne(ItemStack[] itemstacks) {
		ItemStack itemstack = null;
		if (itemstacks != null) {
			for (ItemStack item : itemstacks)
				if (!(item.getType() == Material.AIR)) {
					itemstack = item.clone();
					ItemMeta meta = itemstack.getItemMeta();
					itemstack.setItemMeta(meta);
					itemstack.setAmount(1);
					return new ItemStack[]{itemstack};
				}
		}
		return new ItemStack[]{new ItemStack(Material.AIR)};
	}

	public static ItemStack createItemStackWhitAmount(Material matrial, int amount) {
		ItemStack itemstack = null;
		if (matrial != null) {
			itemstack = new ItemStack(matrial);
			itemstack.setAmount(amount);
		}
		return itemstack != null ? itemstack : new ItemStack(Material.AIR);
	}

	protected static ConvertToItemStack getConvertItems() {
		if (convertItems == null)
			convertItems = new ConvertToItemStack();
		return convertItems;
	}


	public static List<String> formatColors(List<String> rawLore) {
		List<String> lores = new ArrayList<>();
		for (String lore : rawLore)
			lores.add(translateHexCodes(lore));
		return lores;
	}

	public static String formatColors(String rawSingelLine) {
		return translateHexCodes(rawSingelLine);
	}

	private static String translateHexCodes(String textTranslate) {

		return TextTranslator.toSpigotFormat(textTranslate);
	}

	private static class Bulider {

		private ItemStack itemStack;
		private Material matrial;
		private String stringItem;
		private Iterable<?> itemArray;
		private final String displayName;
		private final List<String> lore;

		/**
		 * Create one itemStack, with name and lore. You can also add more
		 * like enchants and metadata.
		 *
		 * @param itemStack   item you want to create.
		 * @param displayName name onb item.
		 * @param lore        lore on item.
		 */
		private Bulider(final ItemStack itemStack, final String displayName, final List<String> lore) {
			this.itemStack = itemStack;
			this.displayName = displayName;
			this.lore = lore;
		}

		/**
		 * Create one itemStack, with name and lore. You can also add more
		 * like enchants and metadata.
		 *
		 * @param matrial     you want to create.
		 * @param displayName name onb item.
		 * @param lore        lore on item.
		 */
		private Bulider(final Material matrial, final String displayName, final List<String> lore) {
			this.matrial = matrial;
			this.displayName = displayName;
			this.lore = lore;
		}

		/**
		 * Create one itemStack, with name and lore. You can also add more
		 * like enchants and metadata.
		 *
		 * @param stringItem  you want to create.
		 * @param displayName name onb item.
		 * @param lore        lore on item.
		 */
		private Bulider(final String stringItem, final String displayName, final List<String> lore) {
			this.stringItem = stringItem;
			this.displayName = displayName;
			this.lore = lore;
		}

		/**
		 * Create array of itemStack´s, with name and lore. You can also add more
		 * like enchants and metadata.
		 *
		 * @param itemArray   you want to create.
		 * @param displayName name onb item.
		 * @param lore        lore on item.
		 */
		private <T> Bulider(final Iterable<T> itemArray, final String displayName, final List<String> lore) {
			this.itemArray = itemArray;
			this.displayName = displayName;
			this.lore = lore;
		}

		/**
		 * Build your item. And call {@link #makeItemStack()} or {@link #makeItemStackArray()}
		 * depending on if you want to create array of items or ony 1 stack.
		 *
		 * @return CreateItemUtily class with your data you have set.
		 */
		public CreateItemStack build() {
			return new CreateItemStack(this);
		}
	}
}
