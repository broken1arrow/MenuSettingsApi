package org.brokenarrow.library.menusettings.settings;

import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.MenuRegistrationConfig;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.command.CommandHandler;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
import org.brokenarrow.library.menusettings.utillity.MenuActionHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.brokenarrow.library.menusettings.settings.ConfigParsingUtils.parseSlotsFromString;
import static org.brokenarrow.library.menusettings.settings.ConfigParsingUtils.sortList;

public class MenuCache extends SimpleYamlHelper {
    private final MenuActionHandler openCloseAction;
    private Map<String, MenuSettings> menuCache = new HashMap<>();

    public MenuCache(@NotNull final Plugin plugin, final String name, final MenuRegistrationConfig config) {
        super(plugin, name, config.isOneFile(), config.isGenerateDefaultFiles());
        openCloseAction = config.getActionHandler();
    }

    /**
     * Cache for all menu data.
     *
     * @return map with menu name and menuSettings.
     */
    public Map<String, MenuSettings> getMenuCache() {
        return menuCache;
    }

    /**
     * Get the menu settings.
     *
     * @return Returns the menu settings.
     */
    @Nullable
    public MenuSettings getMenuSettings(final String menuName) {
        return menuCache.get(menuName);
    }


    public void clearMenuCache() {
        menuCache = new HashMap<>();
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

            if (!isSingelFile()) {
                final String menuName = getNameOfFile(file.getName());
                menuCache.put(menuName, parseMenuSettings(menuName, buttonsCache, path));
            } else {
                menuCache.put(key, parseMenuSettings(key, buttonsCache, path));
            }
        }
    }

    public MenuSettings parseMenuSettings(@NotNull final String menuName, Map<List<Integer>, List<ButtonSettings>> buttonsCache, String key) {

        final FileConfiguration config = getCustomConfig();
        final YamlConfigMapper yamlConfigMapper = new YamlConfigMapper(plugin, menuName, config, openCloseAction);
        final String menuType = config.getString(key + ".Menu_Type");
        final int menuSize = config.getInt(key + ".Menu_Size");
        final String menuTitle = config.getString(key + ".Menu_Title");
        final String fillSpace = config.getString(key + ".FillSpace");

        final String sound = config.getString(key + ".Sound");
        final boolean refreshAll = config.getBoolean(key + ".Refresh_all_buttons");
        int interval = config.getInt(key + ".Update_all_buttons_interval");
        if (interval < 1)
            interval = config.getInt(key + ".Update_all_buttons_delay");
        final boolean updateButtons = config.getBoolean(key + ".Shall_update_on_interval");

        final RequirementsContext openRequirement = yamlConfigMapper.checkRequirements(key, "Open_requirement");
        final List<String> openCommands = config.getStringList(key + ".Open_commands");
        openCommands.removeIf(s -> s.startsWith("["));
        ClickActionHandler openCommandsAction = yamlConfigMapper.checkCommands(key, "Open_commands");
        if (openCommandsAction != null && openCommandsAction.isActionTaskListEmpty()) {
            openCommandsAction = null;
        }
        final ClickActionHandler finalOpenCommandsAction = openCommandsAction;
        final String overridePermission = config.getString(key + ".Override_permission");

        String args = key + ".Open_args";
        if (!config.contains(args))
            args = key + "Open_args";

        final List<?> openArguments = config.getList(args + ".Args");
        List<String> message = config.getStringList(args + ".Message");
        if (message.isEmpty())
            message = Collections.singletonList(config.getString(args + ".Message"));
        final List<String> finalMessage = message;
        final RequirementsContext openArgsRequirement = yamlConfigMapper.checkRequirements(args, "Args_requirement");

        String path = key + ".Menu_Items";
        if (!config.contains(path))
            path = "Menu_Items";

        ConfigurationSection sectionOfButtons = config.getConfigurationSection(path);
        if (sectionOfButtons != null)
            for (String button : sectionOfButtons.getKeys(false)) {
                List<ButtonSettings> buttonSettings = new ArrayList<>();
                String buttonPath = path + "." + button;

                int priority = config.getInt(buttonPath + ".Priority", 1);
                boolean refreshClickedButton = config.getBoolean(buttonPath + ".Refresh");
                boolean updateButton = config.getBoolean(buttonPath + ".Update_on_interval");
                long updateButtonInterval = config.getLong(buttonPath + ".Update_interval");
                if (updateButtonInterval < 1)
                    updateButtonInterval = config.getLong(buttonPath + ".Update_delay");

                String slot = config.getString(buttonPath + ".Slot");

                String itemFromArmorSlot = config.getString(buttonPath + ".Item_from_armor_slot");
                String itemFromHand = config.getString(buttonPath + ".Item_from_hand");
                String menuToOpen = config.getString(buttonPath + ".Menu_to_open");


                ButtonSettings.Builder builder = new ButtonSettings.Builder()
                        .setButtonItem(yamlConfigMapper.getItem(buttonPath, false))
                        .setUpdateButton(updateButton)
                        .setRefreshClickedButton(refreshClickedButton)
                        .setRefreshTimeWhenUpdateButton(updateButtonInterval)
                        .setPriority(priority)
                        .setButtonName(button)
                        .setOpenNewMenu(menuToOpen)
                        .setCheckArmor(itemFromArmorSlot)
                        .setCheckHand(itemFromHand)
                        .setClickrequirement(yamlConfigMapper.checkRequirements(buttonPath, "Click_requirement"))
                        .setLeftClickRequirement(yamlConfigMapper.checkRequirements(buttonPath, "Left_click_requirement"))
                        .setRightClickRequirement(yamlConfigMapper.checkRequirements(buttonPath, "Right_click_requirement"))
                        .setMiddleClickRequirement(yamlConfigMapper.checkRequirements(buttonPath, "Middle_click_requirement"))
                        .setShiftLeftClickRequirement(yamlConfigMapper.checkRequirements(buttonPath, "Shift_left_click_requirement"))
                        .setShiftRightClickRequirement(yamlConfigMapper.checkRequirements(buttonPath, "Shift_right_click_requirement"))
                        .setViewRequirement(yamlConfigMapper.checkRequirements(buttonPath, "View_requirement"))
                        .setClickActionHandler(yamlConfigMapper.checkCommands(buttonPath, "Click_commands"))
                        .setLeftClickActionHandler(yamlConfigMapper.checkCommands(buttonPath, "Left_click_commands"))
                        .setRightClickActionHandler(yamlConfigMapper.checkCommands(buttonPath, "Right_click_commands"))
                        .setMiddleClickActionHandler(yamlConfigMapper.checkCommands(buttonPath, "Middle_click_commands"))
                        .setShiftLeftClickActionHandler(yamlConfigMapper.checkCommands(buttonPath, "Left_shift_click_commands"))
                        .setShiftRightClickActionHandler(yamlConfigMapper.checkCommands(buttonPath, "Right_shift_click_commands"));
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


        CommandHandler commandHandler = new CommandHandler(plugin, menuName, commandHandlerSettings -> {
            commandHandlerSettings.setOpenRequirement(openRequirement);
            commandHandlerSettings.setOpenCommands(openCommands);
            commandHandlerSettings.setOverridePermission(overridePermission );
            commandHandlerSettings.setOpenAction(finalOpenCommandsAction);
            commandHandlerSettings.setOpenArguments(openArguments);
            commandHandlerSettings.setOpenArgsRequirement(openArgsRequirement);
            commandHandlerSettings.setArgsMissingMessage(finalMessage);
        });


        MenuSettings.Builder builder = new MenuSettings.Builder()
                .setMenuType(menuType)
                .setFillSpace(fillSpace)
                .setItemSettings(sortList(buttonsCache))
                .setMenuSize(menuSize)
                .setGlobalDelay(interval)
                .setMenuTitle(menuTitle)
                .setSound(sound)
                .setOpenRequirement(openRequirement)
                .setUpdateButtonsInterval(updateButtons)
                .setRefreshAllButtons(refreshAll)
                .setCommandHandler(commandHandler);

        return builder.build();
    }


}
