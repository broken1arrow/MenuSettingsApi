package org.brokenarrow.library.menusettings.settings;

import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.ItemChecks;
import org.brokenarrow.library.menusettings.builders.ItemWrapper;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.clickactions.ClickRequiermentType;
import org.brokenarrow.library.menusettings.clickactions.CommandActionType;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.requirements.*;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.brokenarrow.library.menusettings.utillity.Tuple;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.brokenarrow.library.menusettings.requirements.RequirementType.*;

public class MenuCache extends AllYamlFilesInFolder {

	Map<String, MenuSettings> menuCache = new HashMap<>();

	public MenuCache(String folder, String filename, boolean shallGenerateDefultFiles) {
		super(folder, filename, shallGenerateDefultFiles);
	}

	public Map<String, MenuSettings> getMenuCache() {
		return menuCache;
	}

	@Override
	public void saveDataToFile(File file) {

	}

	@Override
	protected void loadSettingsFromYaml(File file) {
		ConfigurationSection configurationSection = getCustomConfig().getConfigurationSection("Menus");

		if (configurationSection == null) return;

		Map<List<Integer>, List<ButtonSettings>> buttonsCache = new HashMap<>();
		for (String key : configurationSection.getKeys(false)) {
			if (key == null) continue;

			if (getFolderName() != null && !getFolderName().isEmpty())
				menuCache.put(getFileName(file.getName()), chacheYamlData(buttonsCache, key));
			else
				menuCache.put(key, chacheYamlData(buttonsCache, key));
		}
		System.out.println("menuCache menuCache " + menuCache.keySet());
	}

	public MenuSettings chacheYamlData(Map<List<Integer>, List<ButtonSettings>> buttonsCache, String key) {
		FileConfiguration config = getCustomConfig();
		int menuSize = config.getInt("Menus." + key + ".Menu_Size");
		String menuTitle = config.getString("Menus." + key + ".Menu_Title");
		String fillSpace = config.getString("Menus." + key + ".FillSpace");
		boolean updateButtons = config.getBoolean("Menus." + key + ".Update_Buttons");
		int delay = config.getInt("Menus." + key + ".Global_Buttons_Delay");

		ConfigurationSection sectionOfButtons = config.getConfigurationSection("Menus." + key + ".Menu_Items");
		if (sectionOfButtons != null)
			for (String buttons : sectionOfButtons.getKeys(false)) {
				List<ButtonSettings> buttonSettings = new ArrayList<>();
				String path = "Menus." + key + ".Menu_Items." + buttons;

				int priority = config.getInt(path + ".Priority");
				boolean refreshButtons = config.getBoolean(path + "Refresh_buttons");
				boolean updateButton = config.getBoolean(path + ".Update_Button");
				int updateButtondelay = config.getInt(path + ".Update_Button_delay");
				String slot = config.getString(path + ".Slot");


				ButtonSettings.Builder builder = new ButtonSettings.Builder()
						.setButtonItem(addItem(config, path, false))
						.setUpdateButton(updateButton)
						.setPriority(priority)
						.setClickrequirement(checkRequirements(config, path, "Click_requirement"))
						.setLeftClickRequirement(checkRequirements(config, path, "Left_click_requirement"))
						.setRightClickRequirement(checkRequirements(config, path, "Right_click_requirement"))
						.setShiftLeftClickRequirement(checkRequirements(config, path, "Shift_left_click_requirement"))
						.setShiftRightClickRequirement(checkRequirements(config, path, "Shift_right_click_requirement"))
						.setViewRequirement(checkRequirements(config, path, "View_requirement"))
						.setClickActionHandler(checkCommands(config, path, "Click_commands"))
						.setLeftClickActionHandler(checkCommands(config, path, "Left_click_commands"))
						.setRightClickActionHandler(checkCommands(config, path, "Right_click_commands"))
						.setMiddleClickActionHandler(checkCommands(config, path, "Middle_click_commands"))
						.setShiftLeftClickActionHandler(checkCommands(config, path, "Left_shift_click_commands"))
						.setShiftRightClickActionHandler(checkCommands(config, path, "Right_shift_click_commands"));
				List<Integer> slotsList = getSlot(slot);

				List<ButtonSettings> oldbuttonsCache = buttonsCache.get(slotsList);
				if (oldbuttonsCache != null && !oldbuttonsCache.isEmpty()) {
					oldbuttonsCache.add(builder.build());
					buttonsCache.put(slotsList, oldbuttonsCache);
				} else {
					buttonSettings.add(builder.build());
					buttonsCache.put(slotsList, buttonSettings);
				}
			}

		MenuSettings.Builder builder = new MenuSettings.Builder().setFillSpace(fillSpace)
				.setItemSetting(sortList(buttonsCache))
				.setMenuSize(menuSize)
				.setGlobalDelay(delay)
				.setMenuTitle(menuTitle)
				.setUpdateButtons(updateButtons);

		return builder.build();
	}

	public Map<List<Integer>, List<ButtonSettings>> sortList(Map<List<Integer>, List<ButtonSettings>> buttonsCache) {
		Map<List<Integer>, List<ButtonSettings>> sortedButtons = new HashMap<>();
		for (Map.Entry<List<Integer>, List<ButtonSettings>> entry : buttonsCache.entrySet()) {
			List<ButtonSettings> value = entry.getValue();
			value = value.stream().sorted(Comparator.comparingInt(ButtonSettings::getPriority)).collect(Collectors.toList());
			sortedButtons.put(entry.getKey(), value);
		}
		return sortedButtons;
	}

	public RequirementsLogic checkRequirements(FileConfiguration config, String path, String clickType) {

		if (!clickType.contains("_requirement")) return null;
		String innerPath = path + "." + clickType + ".Requirement";

		ConfigurationSection innerRequirementSection = config.getConfigurationSection(innerPath);
		if (innerRequirementSection == null) return null;
		List<Requirement> requirementsList = new ArrayList<>();
		for (String requirement : innerRequirementSection.getKeys(false)) {
			String requirementsPath = path + "." + clickType + ".Requirement" + "." + requirement;
			List<Requirement> requirementList = checkRequirement(config, requirementsPath, clickType);
			if (requirementList != null)
				requirementsList.addAll(requirementList);
		}
		List<String> denyCommands = config.getStringList(path + "." + clickType + ".deny_commands");
		boolean stopAtSuccess = config.getBoolean(path + "." + clickType + ".stop_at_success");
		int minimumRequirement = config.getInt(path + "." + clickType + ".minimum_requirement");

		RequirementsLogic requirementsLogic = new RequirementsLogic(requirementsList);
		requirementsLogic.setDenyCommands(formatCommands(denyCommands));
		requirementsLogic.setStopAtSuccess(stopAtSuccess);


		if (minimumRequirement <= 0) {
			int required = 0;
			Iterator<Requirement> checkAmountOfRequirements = requirementsList.iterator();
			while (checkAmountOfRequirements.hasNext()) {
				Requirement rec = checkAmountOfRequirements.next();
				if (!rec.isOptional()) {
					required++;
				}
			}
			requirementsLogic.setMinimumRequirements(required);
		} else {
			if (minimumRequirement > requirementsList.size())
				minimumRequirement = requirementsList.size();
			requirementsLogic.setMinimumRequirements(minimumRequirement);
		}


		return requirementsLogic;
	}

	public ItemWrapper addItem(FileConfiguration config, String path, boolean addItemChecks) {
		ItemWrapper.Builder builder = new ItemWrapper.Builder();
		boolean unbreakable = config.getBoolean(path + ".Unbreakable");
		boolean glow = config.getBoolean(path + ".Glow");
		String icon = config.getString(path + ".Icon");
		String displayName = config.getString(path + ".Display_name");
		String materialColor = config.getString(path + ".Material_color");
		String rpg = config.getString(path + ".Rpg");
		String dynamicAmount = config.getString(path + ".Dynamic_Amount");
		int amountOfItems = config.getInt(path + ".Amount");
		int data = config.getInt(path + ".Data", -1);
		int modeldata = config.getInt(path + ".Modeldata", -1);
		List<String> lore = config.getStringList(path + ".Lore");
		List<String> hideFlags = config.getStringList(path + ".Hide_item_flags");
		Map<String, Map<String, String>> portionsEffects = getMaps(config, path + ".Potion_effects");
		Map<String, Map<String, String>> enchantments = getMaps(config, path + ".Enchantments");
		Map<String, String> bannerPattern = getMap(config, path + ".Banner_pattern");

		System.out.println("bannerPatter " + bannerPattern);
		System.out.println("portionsefect " + portionsEffects);

		builder.setDisplayname(displayName)
				.setLore(lore)
				.setAmount(amountOfItems)
				.setDynamicAmount(dynamicAmount)
				.setData(data)
				.setIcon(icon)
				.setGlow(glow)
				.setMatrialColor(materialColor)
				.setRbg(rpg)
				.setCustomModeldata(modeldata)
				.setHideFlags(getItemFlags(hideFlags))
				.setUnbreakable(unbreakable)
				.setPortionEffects(getPotionEffects(portionsEffects))
				.setEnchantments(getEnchantments(enchantments))
				.setBannerPatterns(getPattern(bannerPattern));
		if (addItemChecks)
			builder.setItemChecks(getItemChecks(config, path));
		return builder.build();
	}

	public ItemChecks getItemChecks(FileConfiguration config, String path) {
		ItemChecks.Builder builder = new ItemChecks.Builder();
		boolean offHand = config.getBoolean(path + ".Off_hand");
		boolean item = config.getBoolean(path + ".Armor_slots");
		boolean strict = config.getBoolean(path + ".Strict");
		boolean nameContains = config.getBoolean(path + ".Name_contains");
		boolean nameEquals = config.getBoolean(path + ".Name_equals");
		boolean nameIgnorecase = config.getBoolean(path + ".Name_ignorecase");
		boolean loreContains = config.getBoolean(path + ".Lore_contains");
		boolean loreEquals = config.getBoolean(path + ".Lore_equals");
		boolean loreIgnorecase = config.getBoolean(path + ".Lore_ignorecase");

		builder.setCheckArmorSlots(item)
				.setCheckOffHand(offHand)
				// check only items some not contains lore or displayname.
				.setStrict(strict)
				.setCheckNameContains(nameContains)
				.setCheckNameEquals(nameEquals)
				.setCheckNameIgnorecase(nameIgnorecase)
				.setCheckLoreContains(loreContains)
				.setCheckLoreEquals(loreEquals)
				.setCheckLoreIgnorecase(loreIgnorecase);

		return builder.build();
	}

	public Map<String, Map<String, String>> getMaps(FileConfiguration config, String path) {
		ConfigurationSection configurationSection = config.getConfigurationSection(path);
		if (configurationSection == null) return null;
		Map<String, Map<String, String>> map = new HashMap<>();
		for (String innerPath : configurationSection.getKeys(false)) {
			Map<String, String> innerkeys = new HashMap<>();
			ConfigurationSection innerconfigurationSection = config.getConfigurationSection(path + "." + innerPath);
			if (innerconfigurationSection == null) continue;

			for (String cildrenKeys : innerconfigurationSection.getKeys(false))
				innerkeys.put(cildrenKeys.toLowerCase(Locale.ROOT), config.getString(path + "." + innerPath + "." + cildrenKeys));
			map.put(innerPath.toUpperCase(Locale.ROOT), innerkeys);
		}
		return map;
	}

	public Map<String, String> getMap(FileConfiguration config, String path) {
		ConfigurationSection configurationSection = config.getConfigurationSection(path);
		if (configurationSection == null) return null;
		Map<String, String> map = new HashMap<>();
		for (String innerPath : configurationSection.getKeys(false)) {
			map.put(innerPath.toUpperCase(Locale.ROOT), config.getString(path + "." + innerPath));
		}
		return map;
	}

	public List<ItemFlag> getItemFlags(List<String> itemFlags) {
		if (itemFlags == null || itemFlags.isEmpty()) return null;
		List<ItemFlag> itemFlagList = new ArrayList<>();
		for (String itemFlag : itemFlags) {
			if (itemFlag == null) continue;
			try {
				ItemFlag flag = ItemFlag.valueOf(itemFlag);
				itemFlagList.add(flag);
			} catch (IllegalArgumentException exception) {
				exception.printStackTrace();
			}
		}
		return itemFlagList;
	}

	public List<Pattern> getPattern(Map<String, String> enchantmentList) {
		if (enchantmentList == null || enchantmentList.isEmpty()) return null;
		List<Pattern> enchantmentsMap = new ArrayList<>();
		for (Map.Entry<String, String> petterns : enchantmentList.entrySet()) {
			String pattern = petterns.getKey();
			String color = petterns.getValue();

			if (pattern != null)
				if (color != null)
					enchantmentsMap.add(new Pattern(DyeColor.valueOf(color), PatternType.valueOf(pattern)));
				else
					enchantmentsMap.add(new Pattern(DyeColor.WHITE, PatternType.valueOf(pattern)));
		}
		return enchantmentsMap;
	}

	public Map<Enchantment, Tuple<Integer, Boolean>> getEnchantments(Map<String, Map<String, String>> enchantmentList) {
		if (enchantmentList == null || enchantmentList.isEmpty()) return null;
		Map<Enchantment, Tuple<Integer, Boolean>> enchantmentsMap = new HashMap<>();

		for (Map.Entry<String, Map<String, String>> stringMapEntry : enchantmentList.entrySet()) {
			Valid.checkNotNull(stringMapEntry.getKey(), "Enchantment is null. Should always return a value");
			Enchantment enchantment = Enchantment.getByKey(NamespacedKey.fromString(stringMapEntry.getKey()));
			Map<String, String> enchantOptions = stringMapEntry.getValue();
			String enchantmentLevel = enchantOptions.get("level");
			int level = enchantmentLevel == null ? 1 : Integer.parseInt(enchantmentLevel);
			boolean levelRestriction = Boolean.getBoolean(enchantOptions.get("level_restriction"));

			if (enchantment != null)
				enchantmentsMap.put(enchantment, new Tuple<>(level, !levelRestriction));
		}

		return enchantmentsMap;
	}

	public List<PotionEffect> getPotionEffects(Map<String, Map<String, String>> portionsEffects) {
		if (portionsEffects == null || portionsEffects.isEmpty()) return null;
		List<PotionEffect> potionEffectList = new ArrayList<>();

		for (Map.Entry<String, Map<String, String>> potionEffect : portionsEffects.entrySet()) {
			Valid.checkNotNull(potionEffect.getKey(), "Portion effects is null. Should always return a value");
			PotionEffectType potionEffectType = PotionEffectType.getByName(potionEffect.getKey());
			Map<String, String> potionEffectValue = potionEffect.getValue();
			System.out.println("potionEffectValue " + potionEffectValue);
			if (potionEffectType != null)
				potionEffectList.add(new PotionEffect(potionEffectType, Integer.parseInt(potionEffectValue.get("duration")), Integer.parseInt(potionEffectValue.get("amplifier"))));
		}
		return potionEffectList;
	}

	public List<Requirement> checkRequirement(FileConfiguration config, String path, String clicktype) {
		List<Requirement> requirementsList = new ArrayList<>();
		Requirement rec = null;


		String type = config.getString(path + ".type");
		String input = config.getString(path + ".input");
		String output = config.getString(path + ".output");
		String permission = config.getString(path + ".permission");
		String expression = config.getString(path + ".expression");
		String amount = config.getString(path + ".amount");
		boolean useLevel = config.getBoolean(path + ".level");
		ClickRequiermentType clickRequiermentType = ClickRequiermentType.getType(clicktype);
		RequirementType requirementType = RequirementType.getType(type);

		if (requirementType == null) return null;
		List<String> successCommands = null;
		List<String> denyCommands = null;
		if (clicktype != null) {
			if (!clicktype.equals("View_requirement")) {
				successCommands = config.getStringList(path + ".success_commands");
				denyCommands = config.getStringList(path + ".deny_commands");
			}

		}

		switch (requirementType) {
			case HAS_PERMISSION:
			case DO_NOT_HAVE_PERMISSION:
				if (permission != null)
					rec = new HasPermissionRequirement(permission, requirementType == DO_NOT_HAVE_PERMISSION);
				else
					System.out.println("permission path is null");
				break;
			case HAS_MONEY:
			case DO_NOT_HAVE_MONEY:
				if (amount != null)
					rec = new HasMoneyRequirement(requirementType == DO_NOT_HAVE_MONEY, amount);
				break;
			case HAS_ITEM:
			case DO_NOT_HAVE_ITEM:
				break;
			case HAS_EXPERIENCE:
			case DO_NOT_HAVE_EXPERIENCE:
				if (amount != null)
					rec = new HasExpRequirement(amount, useLevel, requirementType == DO_NOT_HAVE_EXPERIENCE);
				else
					System.out.println("amount path is null");
				break;
			case JAVASCRIPT:
				if (expression != null)
					rec = new JavascriptRequirement(expression);
				else
					System.out.println("Javascript expression is null ");
				break;
			case STRING_EQUALS:
			case STRING_CONTAINS:
			case STRING_EQUALS_IGNORE_CASE:
			case STRING_NOT_CONTAINS:
			case STRING_IS_NOT_EQUALS:
			case STRING_IS_NOT_EQUAL_IGNORE_CASE:
			case INT_EQUALS_OUTPUT:
			case INT_GREATER_THAN_OUTPUT:
			case INT_GREATER_THAN_OR_EQUALS_OUTPUT:
			case INT_LESS_THAN_OR_EQUALS_OUTPUT:
			case INT_LESS_THAN_OUTPUT:
				rec = new InputOutputRequirement(requirementType, input, output);
				break;
			case CUSTOM:
				break;
		}
		if (rec != null) {
			boolean optionalRequirement = config.getBoolean(path + ".optional_requirement");
			rec.setOptional(optionalRequirement);
			if (clickRequiermentType != null)
				rec.setClickRequiermentType(clickRequiermentType);

			rec.setSuccessCommands(formatCommands(successCommands));
			rec.setDenyCommands(formatCommands(denyCommands));
			requirementsList.add(rec);
		}
		if (requirementsList.isEmpty())
			return null;
		return requirementsList;
	}

	public List<ClickActionTask> formatCommands(List<String> comands) {
		if (comands == null || comands.isEmpty()) return null;
		List<ClickActionTask> list = new ArrayList<>();
		for (String command : comands) {
			CommandActionType commandType = CommandActionType.getType(command);

			if (commandType != null) {
				command = command.replace(commandType.getIdentifier(), "");
				if (command.startsWith(" "))
					command = command.trim();
				ClickActionTask action = new ClickActionTask(commandType, command);
				action.setChance("-1");
				action.setDelay("-1");
				list.add(action);
			}
		}
		return list;
	}

	public ClickActionHandler checkCommands(FileConfiguration config, String path, String commandType) {
		String requirementsPath = path + "." + commandType;
		List<String> commans = config.getStringList(requirementsPath);
		if (!commans.isEmpty())
			return new ClickActionHandler(formatCommands(commans));
		return null;
	}

	public List<Integer> getSlot(String slots) {
		List<Integer> slotList = new ArrayList<>();

		if (slots == null || slots.equals(""))
			return new ArrayList<>();

		try {
			for (String slot : slots.split(",")) {
				if (slot.equals("")) {
					continue;
				}
				if (slot.contains("-")) {
					int firstSlot = Integer.parseInt(slot.split("-")[0]);
					int lastSlot = Integer.parseInt(slot.split("-")[1]);
					slotList.addAll(IntStream.rangeClosed(firstSlot, lastSlot).boxed().collect(Collectors.toList()));
				} else
					slotList.add(Integer.valueOf(slot));

			}
		} catch (NumberFormatException e) {
			throw new NumberFormatException("can not parse this " + slots + " as numbers.");
		}
		return slotList;
	}

	public static class getInnerValues {
		Object value1;
		Object value2;
		Object value3;
	}
}
