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

import static org.brokenarrow.library.menusettings.settings.GetCollections.getSlot;
import static org.brokenarrow.library.menusettings.settings.GetCollections.sortList;

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
		GetYamlSettings yamlSettings = new GetYamlSettings(config);
		int menuSize = config.getInt("Menus." + key + ".Menu_Size");
		String menuTitle = config.getString("Menus." + key + ".Menu_Title");
		String fillSpace = config.getString("Menus." + key + ".FillSpace");
		boolean updateButtons = config.getBoolean("Menus." + key + ".Update_Buttons");
		int delay = config.getInt("Menus." + key + ".Global_Buttons_Delay");

		ConfigurationSection sectionOfButtons = config.getConfigurationSection("Menus." + key + ".Menu_Items");
		if (sectionOfButtons != null)
			for (String button : sectionOfButtons.getKeys(false)) {
				List<ButtonSettings> buttonSettings = new ArrayList<>();
				String path = "Menus." + key + ".Menu_Items." + button;

				int priority = config.getInt(path + ".Priority");
				boolean refreshButtons = config.getBoolean(path + "Refresh_buttons");
				boolean updateButton = config.getBoolean(path + ".Update_Button");
				long updateButtondelay = config.getLong(path + ".Update_Button_delay");
				String slot = config.getString(path + ".Slot");


				ButtonSettings.Builder builder = new ButtonSettings.Builder()
						.setButtonItem(yamlSettings.addItem(path, false))
						.setUpdateButton(updateButton)
						.setRefreshAllButtons(refreshButtons)
						.setRefreshTimeWhenUpdateButton(updateButtondelay)
						.setPriority(priority)
						.setButtonName(button)
						.setClickrequirement(yamlSettings.checkRequirements(path, "Click_requirement"))
						.setLeftClickRequirement(yamlSettings.checkRequirements(path, "Left_click_requirement"))
						.setRightClickRequirement(yamlSettings.checkRequirements(path, "Right_click_requirement"))
						.setMiddleClickRequirement(yamlSettings.checkRequirements(path, "Middle_click_requirement"))
						.setShiftLeftClickRequirement(yamlSettings.checkRequirements(path, "Shift_left_click_requirement"))
						.setShiftRightClickRequirement(yamlSettings.checkRequirements(path, "Shift_right_click_requirement"))
						.setViewRequirement(yamlSettings.checkRequirements(path, "View_requirement"))
						.setClickActionHandler(yamlSettings.checkCommands(path, "Click_commands"))
						.setLeftClickActionHandler(yamlSettings.checkCommands(path, "Left_click_commands"))
						.setRightClickActionHandler(yamlSettings.checkCommands(path, "Right_click_commands"))
						.setMiddleClickActionHandler(yamlSettings.checkCommands(path, "Middle_click_commands"))
						.setShiftLeftClickActionHandler(yamlSettings.checkCommands(path, "Left_shift_click_commands"))
						.setShiftRightClickActionHandler(yamlSettings.checkCommands(path, "Right_shift_click_commands"));
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

		MenuSettings.Builder builder = new MenuSettings.Builder()
				.setFillSpace(fillSpace)
				.setItemSettings(sortList(buttonsCache))
				.setMenuSize(menuSize)
				.setGlobalDelay(delay)
				.setMenuTitle(menuTitle)
				.setOpenRequirement(yamlSettings.checkRequirements("Menus." + key, "Open_requirement"))
				.setUpdateButtons(updateButtons);

		return builder.build();
	}


}
