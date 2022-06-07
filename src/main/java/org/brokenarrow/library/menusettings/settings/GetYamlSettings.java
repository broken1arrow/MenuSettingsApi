package org.brokenarrow.library.menusettings.settings;

import org.brokenarrow.library.menusettings.builders.ItemChecks;
import org.brokenarrow.library.menusettings.builders.ItemWrapper;
import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.clickactions.ClickRequiermentType;
import org.brokenarrow.library.menusettings.requirements.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

import static org.brokenarrow.library.menusettings.requirements.RequirementType.*;
import static org.brokenarrow.library.menusettings.settings.GetCollections.*;

public final class GetYamlSettings {
	private final FileConfiguration config;

	public GetYamlSettings(FileConfiguration config) {
		this.config = config;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public ClickActionHandler checkCommands(String path, String commandType) {
		String requirementsPath = path + "." + commandType;
		List<String> commans = this.getConfig().getStringList(requirementsPath);
		if (!commans.isEmpty())
			return new ClickActionHandler(formatCommands(commans));
		return null;
	}

	public RequirementsLogic checkRequirements(String path, String clickType) {

		if (!clickType.contains("_requirement")) return null;
		String innerPath = path + "." + clickType + ".Requirement";

		ConfigurationSection innerRequirementSection = this.getConfig().getConfigurationSection(innerPath);
		if (innerRequirementSection == null) return null;
		List<Requirement> requirementsList = new ArrayList<>();
		for (String requirement : innerRequirementSection.getKeys(false)) {
			String requirementsPath = path + "." + clickType + ".Requirement" + "." + requirement;
			List<Requirement> requirementList = checkRequirement(requirementsPath, clickType);
			if (requirementList != null)
				requirementsList.addAll(requirementList);
		}
		
		List<String> denyCommands = this.getConfig().getStringList(path + "." + clickType + ".deny_commands");
		boolean stopAtSuccess = this.getConfig().getBoolean(path + "." + clickType + ".stop_at_success");
		int minimumRequirement = this.getConfig().getInt(path + "." + clickType + ".minimum_requirement");

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

	public ItemWrapper addItem(String path, boolean addItemChecks) {
		ItemWrapper.Builder builder = new ItemWrapper.Builder();
		boolean unbreakable = this.getConfig().getBoolean(path + ".Unbreakable");
		boolean glow = this.getConfig().getBoolean(path + ".Glow");
		String icon = this.getConfig().getString(path + ".Icon");
		String displayName = this.getConfig().getString(path + ".Display_name");
		String materialColor = this.getConfig().getString(path + ".Material_color");
		String rpg = this.getConfig().getString(path + ".Rpg");
		String dynamicAmount = this.getConfig().getString(path + ".Dynamic_Amount");
		int amountOfItems = this.getConfig().getInt(path + ".Amount", 1);
		int data = this.getConfig().getInt(path + ".Data", -1);
		int modeldata = this.getConfig().getInt(path + ".Modeldata", -1);
		List<String> lore = this.getConfig().getStringList(path + ".Lore");
		List<String> hideFlags = this.getConfig().getStringList(path + ".Hide_item_flags");
		Map<String, Map<String, String>> portionsEffects = getMaps(path + ".Potion_effects");
		Map<String, Map<String, String>> enchantments = getMaps(path + ".Enchantments");
		Map<String, String> bannerPattern = getMap(path + ".Banner_pattern");

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
			builder.setItemChecks(getItemChecks(path));
		return builder.build();
	}

	public ItemChecks getItemChecks(String path) {
		ItemChecks.Builder builder = new ItemChecks.Builder();
		boolean offHand = this.getConfig().getBoolean(path + ".Off_hand");
		boolean item = this.getConfig().getBoolean(path + ".Armor_slots");
		boolean strict = this.getConfig().getBoolean(path + ".Strict");
		boolean nameContains = this.getConfig().getBoolean(path + ".Name_contains");
		boolean nameEquals = this.getConfig().getBoolean(path + ".Name_equals");
		boolean nameIgnorecase = this.getConfig().getBoolean(path + ".Name_ignorecase");
		boolean loreContains = this.getConfig().getBoolean(path + ".Lore_contains");
		boolean loreEquals = this.getConfig().getBoolean(path + ".Lore_equals");
		boolean loreIgnorecase = this.getConfig().getBoolean(path + ".Lore_ignorecase");

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

	public Map<String, Map<String, String>> getMaps(String path) {
		ConfigurationSection configurationSection = this.getConfig().getConfigurationSection(path);
		if (configurationSection == null) return null;
		Map<String, Map<String, String>> map = new HashMap<>();
		for (String innerPath : configurationSection.getKeys(false)) {
			Map<String, String> innerkeys = new HashMap<>();
			ConfigurationSection innerconfigurationSection = this.getConfig().getConfigurationSection(path + "." + innerPath);
			if (innerconfigurationSection == null) continue;

			for (String cildrenKeys : innerconfigurationSection.getKeys(false))
				innerkeys.put(cildrenKeys.toLowerCase(Locale.ROOT), this.getConfig().getString(path + "." + innerPath + "." + cildrenKeys));
			map.put(innerPath.toUpperCase(Locale.ROOT), innerkeys);
		}
		return map;
	}

	public Map<String, String> getMap(String path) {
		ConfigurationSection configurationSection = this.getConfig().getConfigurationSection(path);
		if (configurationSection == null) return null;
		Map<String, String> map = new HashMap<>();
		for (String innerPath : configurationSection.getKeys(false)) {
			map.put(innerPath.toUpperCase(Locale.ROOT), this.getConfig().getString(path + "." + innerPath));
		}
		return map;
	}

	public List<Requirement> checkRequirement(String path, String clicktype) {
		List<Requirement> requirementsList = new ArrayList<>();
		Requirement rec = null;


		String type = this.getConfig().getString(path + ".type");
		String input = this.getConfig().getString(path + ".input");
		String output = this.getConfig().getString(path + ".output");
		String permission = this.getConfig().getString(path + ".permission");
		String expression = this.getConfig().getString(path + ".expression");
		String amount = this.getConfig().getString(path + ".amount");

		boolean useLevel = this.getConfig().getBoolean(path + ".level");
		ClickRequiermentType clickRequiermentType = ClickRequiermentType.getType(clicktype);
		RequirementType requirementType = RequirementType.getType(type);

		if (requirementType == null) return null;
		List<String> successCommands = null;
		List<String> denyCommands = null;
		if (clicktype != null) {
			successCommands = this.getConfig().getStringList(path + ".success_commands");
			denyCommands = this.getConfig().getStringList(path + ".deny_commands");
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
				rec = new HasItemRequirement(addItem(path + ".item", true), requirementType == DO_NOT_HAVE_ITEM);
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
			case INT_NOT_EQUALS_OUTPUT:
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
			boolean optionalRequirement = this.getConfig().getBoolean(path + ".optional_requirement");
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


}
