package org.brokenarrow.library.menusettings.settings;

import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.brokenarrow.library.menusettings.settings.GetCollections.parseSlotsFromString;
import static org.brokenarrow.library.menusettings.settings.GetCollections.sortList;

public class MenuCache extends AllYamlFilesInFolder {

	Map<String, MenuSettings> menuCache = new HashMap<>();

	public MenuCache(String folder, String filename, boolean shallGenerateDefultFiles) {
		super(folder, filename, shallGenerateDefultFiles);
	}

	/**
	 * Cache for all menu data.
	 *
	 * @return map with menu name and menuSettings.
	 */
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

		for (String key : configurationSection.getKeys(false)) {
			if (key == null) continue;
			Map<List<Integer>, List<ButtonSettings>> buttonsCache = new HashMap<>();
			String path = "Menus.";
			if (getCustomConfig().contains("Menus." + key + ".Menu_Items"))
				path = "Menus." + key;
			if (getFolderName() != null && !getFolderName().isEmpty())
				menuCache.put(getNameOfFile(file.getName()), chacheYamlData(buttonsCache, path));
			else
				menuCache.put(key, chacheYamlData(buttonsCache, path));
		}
		System.out.println("menuCache menuCache " + menuCache.keySet());
	}

	public MenuSettings chacheYamlData(Map<List<Integer>, List<ButtonSettings>> buttonsCache, String key) {

		FileConfiguration config = getCustomConfig();
		GetYamlSettings yamlSettings = new GetYamlSettings(config);
		String menuType = config.getString(key + ".Menu_Type");
		int menuSize = config.getInt(key + ".Menu_Size");
		String menuTitle = config.getString(key + ".Menu_Title");
		String fillSpace = config.getString(key + ".FillSpace");
		String sound = config.getString(key + ".Sound");
		boolean updateButtons = config.getBoolean(key + ".Update_Buttons");
		int delay = config.getInt(key + ".Update_buttons_delay");

		String path = key + ".Menu_Items";
		if (!config.contains(path))
			path = "Menu_Items";

		ConfigurationSection sectionOfButtons = config.getConfigurationSection(path);
		if (sectionOfButtons != null)
			for (String button : sectionOfButtons.getKeys(false)) {
				List<ButtonSettings> buttonSettings = new ArrayList<>();
				String buttonPath = path + button;

				int priority = config.getInt(buttonPath + ".Priority", 1);
				boolean refreshButtons = config.getBoolean(buttonPath + "Refresh_buttons");
				boolean updateButton = config.getBoolean(buttonPath + ".Update_Button");
				long updateButtondelay = config.getLong(buttonPath + ".Update_Button_delay");

				String slot = config.getString(buttonPath + ".Slot");

				String itemfromarmorslot = config.getString(buttonPath + ".Item_from_armor_slot");
				String itemfromhand = config.getString(buttonPath + ".Item_from_hand");
				String menuToOpen = config.getString(buttonPath + ".Menu_to_open");


				ButtonSettings.Builder builder = new ButtonSettings.Builder()
						.setButtonItem(yamlSettings.getItem(buttonPath, false))
						.setUpdateButton(updateButton)
						.setRefreshAllButtons(refreshButtons)
						.setRefreshTimeWhenUpdateButton(updateButtondelay)
						.setPriority(priority)
						.setButtonName(button)
						.setOpenNewMenu(menuToOpen)
						.setCheckArmor(itemfromarmorslot)
						.setCheckHand(itemfromhand)
						.setClickrequirement(yamlSettings.checkRequirements(buttonPath, "Click_requirement"))
						.setLeftClickRequirement(yamlSettings.checkRequirements(buttonPath, "Left_click_requirement"))
						.setRightClickRequirement(yamlSettings.checkRequirements(buttonPath, "Right_click_requirement"))
						.setMiddleClickRequirement(yamlSettings.checkRequirements(buttonPath, "Middle_click_requirement"))
						.setShiftLeftClickRequirement(yamlSettings.checkRequirements(buttonPath, "Shift_left_click_requirement"))
						.setShiftRightClickRequirement(yamlSettings.checkRequirements(buttonPath, "Shift_right_click_requirement"))
						.setViewRequirement(yamlSettings.checkRequirements(buttonPath, "View_requirement"))
						.setClickActionHandler(yamlSettings.checkCommands(buttonPath, "Click_commands"))
						.setLeftClickActionHandler(yamlSettings.checkCommands(buttonPath, "Left_click_commands"))
						.setRightClickActionHandler(yamlSettings.checkCommands(buttonPath, "Right_click_commands"))
						.setMiddleClickActionHandler(yamlSettings.checkCommands(buttonPath, "Middle_click_commands"))
						.setShiftLeftClickActionHandler(yamlSettings.checkCommands(buttonPath, "Left_shift_click_commands"))
						.setShiftRightClickActionHandler(yamlSettings.checkCommands(buttonPath, "Right_shift_click_commands"));
				List<Integer> slotsList = parseSlotsFromString(slot);

				List<ButtonSettings> oldbuttonsCache = buttonsCache.get(slotsList);
				if (oldbuttonsCache != null && !oldbuttonsCache.isEmpty()) {
					oldbuttonsCache.add(builder.build());
					buttonsCache.put(slotsList, oldbuttonsCache);
				} else {
					buttonSettings.add(builder.build());
					buttonsCache.put(slotsList, buttonSettings);
				}
			}

		MenuSettings.Builder builder = new MenuSettings.Builder()
				.setMenuType(menuType)
				.setFillSpace(fillSpace)
				.setItemSettings(sortList(buttonsCache))
				.setMenuSize(menuSize)
				.setGlobalDelay(delay)
				.setMenuTitle(menuTitle)
				.setSound(sound)
				.setOpenRequirement(yamlSettings.checkRequirements("Menus." + key, "Open_requirement"))
				.setUpdateButtons(updateButtons);

		return builder.build();
	}


}
