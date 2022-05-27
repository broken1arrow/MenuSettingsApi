package org.brokenarrow.library.menusettings.settings;

import org.brokenarrow.library.menusettings.builders.ItemSettings;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.clickactions.ClickRequiermentType;
import org.brokenarrow.library.menusettings.clickactions.CommandActionType;
import org.brokenarrow.library.menusettings.requirements.*;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

		Map<List<Integer>, List<ItemSettings>> buttonsCache = new HashMap<>();
		for (String key : configurationSection.getKeys(false)) {
			if (key == null) continue;

			if (getFolderName() != null && !getFolderName().isEmpty())
				menuCache.put(getFileName(file.getName()), chacheYamlData(buttonsCache, key));
			else
				menuCache.put(key, chacheYamlData(buttonsCache, key));
		}
		System.out.println("menuCache menuCache " + menuCache.keySet());
	}

	public MenuSettings chacheYamlData(Map<List<Integer>, List<ItemSettings>> buttonsCache, String key) {
		FileConfiguration config = getCustomConfig();
		int menuSize = config.getInt("Menus." + key + ".Menu_Size");
		String menuTitle = config.getString("Menus." + key + ".Menu_Title");
		String fillSpace = config.getString("Menus." + key + ".FillSpace");
		boolean updateButtons = config.getBoolean("Menus." + key + ".Update_Buttons");
		int delay = config.getInt("Menus." + key + ".Global_Buttons_Delay");

		ConfigurationSection sectionOfButtons = config.getConfigurationSection("Menus." + key + ".Menu_Items");
		if (sectionOfButtons != null)
			for (String buttons : sectionOfButtons.getKeys(false)) {
				List<ItemSettings> itemSettings = new ArrayList<>();
				int priority = config.getInt("Menus." + key + ".Menu_Items." + buttons + ".Priority");
				boolean updateButton = config.getBoolean("Menus." + key + ".Menu_Items." + buttons + ".Update_Button");
				int updateButtondelay = config.getInt("Menus." + key + ".Menu_Items." + buttons + ".Update_delay");
				boolean glow = config.getBoolean("Menus." + key + ".Menu_Items." + buttons + ".Glow");
				String slot = config.getString("Menus." + key + ".Menu_Items." + buttons + ".Slot");
				String icon = config.getString("Menus." + key + ".Menu_Items." + buttons + ".Icon");
				String displayName = config.getString("Menus." + key + ".Menu_Items." + buttons + ".Display_name");
				List<String> lore = config.getStringList("Menus." + key + ".Menu_Items." + buttons + ".Lore");

				String path = "Menus." + key + ".Menu_Items." + buttons;

				ItemSettings.Builder builder = new ItemSettings.Builder()
						.setDisplayname(displayName)
						.setIcon(icon).setGlow(glow)
						.setLore(lore)
						.setSlot(slot)
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

				List<ItemSettings> oldbuttonsCache = buttonsCache.get(slotsList);
				if (oldbuttonsCache != null && !oldbuttonsCache.isEmpty()) {
					oldbuttonsCache.add(builder.build());
					buttonsCache.put(slotsList, oldbuttonsCache);
				} else {
					itemSettings.add(builder.build());
					buttonsCache.put(slotsList, itemSettings);
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

	public Map<List<Integer>, List<ItemSettings>> sortList(Map<List<Integer>, List<ItemSettings>> buttonsCache) {
		Map<List<Integer>, List<ItemSettings>> sortedButtons = new HashMap<>();
		for (Map.Entry<List<Integer>, List<ItemSettings>> entry : buttonsCache.entrySet()) {
			List<ItemSettings> value = entry.getValue();
			value = value.stream().sorted(Comparator.comparingInt(ItemSettings::getPriority)).collect(Collectors.toList());
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

	public List<Requirement> checkRequirement(FileConfiguration config, String path, String clicktype) {
		List<Requirement> requirementsList = new ArrayList<>();
		Requirement rec = null;


		String type = config.getString(path + ".type");
		String input = config.getString(path + ".input");
		String output = config.getString(path + ".output");
		String expression = config.getString(path + ".expression");
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
				break;
			case DO_NOT_HAVE_PERMISSION:
				break;
			case HAS_MONEY:
				break;
			case DO_NOT_HAVE_MONEY:
				break;
			case JAVASCRIPT:
				if (expression != null)
					rec = new JavascriptRequirement(expression);
				else
					System.out.println("Javascript  expression is null ");
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
		System.out.println("requirementsPath " + config.getStringList(requirementsPath));
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
}
