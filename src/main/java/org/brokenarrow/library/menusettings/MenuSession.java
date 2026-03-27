package org.brokenarrow.library.menusettings;

import org.brokenarrow.library.menusettings.builders.ButtonContext;
import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;
import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.brokenarrow.library.menusettings.MenuSettingsAddon.setPlaceholders;

/**
 * Helper class for accessing buttons and requirements for a menu.
 * <p>
 * This class allows you to retrieve the {@link MenuSettings} for a menu,
 * check open requirements, collect buttons, and handle placeholders for a specific player.
 */
public final class MenuSession {
	private final MenuSettings menuSettings;
	private final Player viewer;
	private final String menuName;
	private final MenuDataRegister menuDataRegister = MenuDataRegister.getInstance();

	/**
	 * Creates a new MenuSession instance for a specific menu and player.
	 * <p>
	 * This will load the settings for the specified menu from the plugin's menu cache.
	 *
	 * @param plugin   the plugin instance that registered the menu
	 * @param menuName the name of the menu to load
	 * @param player   the player who will view the menu
	 */
	public MenuSession(@NotNull Plugin plugin, @NotNull final String menuName, @NotNull final Player player) {
		Valid.checkNotNull(this.menuDataRegister, "MenuSettingsAddon class is not registed.");
		MenuCache menuCache = this.menuDataRegister.getMenuCache(plugin);
		Valid.checkNotNull(menuCache, "the plugin is not registed, so can't load the settings");
		this.menuName = menuName;
		Map<String, MenuSettings> menu = menuCache.getMenuCache();
		if (menu != null) {
			this.menuSettings = menu.get(menuName);
		} else
			this.menuSettings = null;

		this.viewer = player;
	}


	/**
	 * Returns the {@link MenuSettings} for this menu.
	 * <p>
	 * Through this object, you can access all menu-related settings, including title, size, buttons, and open requirements.
	 *
	 * @return the menu settings
	 * @throws IllegalStateException if the menu settings are not found
	 */
	public MenuSettings getMenuSettings() {
		Valid.checkNotNull(menuSettings, "menu Settings cache is null, so can't load the menu settings. Check if this menu name is valid: " + menuName);
		return menuSettings;
	}

	/**
	 * Returns a map of all button settings for this menu.
	 * <p>
	 * The map uses a list of slots as the key and a list of {@link ButtonSettings} as the value.
	 * Multiple buttons can share the same slot.
	 *
	 * @return a map of all button settings, never null
	 */
	public Map<List<Integer>, List<ButtonSettings>> getItemSettings() {
		Valid.checkNotNull(menuSettings, "menu Settings cache is null, so can't load the menu settings. Check if this menu name is valid: " + menuName);
		Map<List<Integer>, List<ButtonSettings>> itemSettings = menuSettings.getItemSettings();
		return itemSettings != null ? itemSettings : new HashMap<>();
	}


	/**
	 * Returns a set of all slots that contain buttons in this menu.
	 *
	 * @return a set of slot indices
	 */
	public Set<Integer> getSlots() {
		return this.getItemSettings().keySet().stream().flatMap(List::stream).collect(Collectors.toSet());
	}

	/**
	 * Returns the player viewing this menu session.
	 *
	 * @return the viewer player
	 */
	public Player getViewer() {
		return viewer;
	}

	/**
	 * Checks whether the player meets the requirements to open this menu.
	 *
	 * @param bypassPermission a permission node to bypass the requirements, if applicable
	 * @return true if the player meets the requirements, false otherwise
	 */
	public boolean checkOpenRequirements(String bypassPermission) {
		if (this.viewer != null && bypassPermission != null && !bypassPermission.isEmpty() && this.viewer.hasPermission(bypassPermission))
			return true;
		RequirementsLogic openRequirement = this.getMenuSettings().getOpenRequirement();
		if (openRequirement != null) {
			if (openRequirement.estimate(this.viewer)) return true;

			if (openRequirement.getDenyCommands() != null)
				openRequirement.runClickActionTask(openRequirement.getDenyCommands(), this.viewer);
			return false;
		}
		return true;
	}

	/**
	 * Returns the first button available at the specified slot that the player can view.
	 *
	 * @param slot the slot index
	 * @return a {@link ButtonContext} for the button, or null if no button is visible
	 */
	@Nullable
	public ButtonContext getButton(int slot) {
		List<ButtonSettings> buttonSettings = this.collectButtons(slot);
		if (buttonSettings == null) return null;

		for (ButtonSettings key : buttonSettings) {
			if (checkRequirement(key.getViewRequirement())) {
				return new ButtonContext(key, this.viewer);
			}
		}
		return null;
	}

	/**
	 * Returns all buttons available at the specified slot.
	 *
	 * @param slot the slot index
	 * @return a list of {@link ButtonContext} objects, or null if no buttons are set
	 */
	public List<ButtonContext> getButtons(int slot) {
		List<ButtonSettings> buttonSettingsList = collectButtons(slot);
		if(buttonSettingsList != null)
			return buttonSettingsList.stream().map(buttonSettings -> new ButtonContext(buttonSettings,this.viewer)).collect(Collectors.toList());
		return null;
	}

	/**
	 * Translates placeholders in the given string using PlaceholderAPI for the viewer.
	 *
	 * @param string the string containing placeholders
	 * @return the string with placeholders replaced, or the original string if PlaceholderAPI is unavailable
	 */
	public String setPlaceholder(String string) {
		return setPlaceholders(getViewer(), string);
	}

	/**
	 * Checks if the player meets a specific requirement.
	 *
	 * @param viewRequirement the requirement to check
	 * @return true if the requirement is met or null, false otherwise
	 */
	private boolean checkRequirement(RequirementsLogic viewRequirement) {
		if (viewRequirement == null)
			return true;
		return viewRequirement.estimate(this.viewer);
	}

	/**
	 * Collects all buttons for the given slot index.
	 *
	 * @param slot the slot index
	 * @return a list of {@link ButtonSettings} or null if none exist
	 */
	private List<ButtonSettings> collectButtons(int slot) {
		for (List<Integer> keys : this.getMenuSettings().getItemSettings().keySet()) {
			if (keys.contains(slot)) {
				List<ButtonSettings> buttonSettingsList = this.getItemSettings().get(keys);
				if(buttonSettingsList != null) {
					return buttonSettingsList;
				}
			}
		}
		return null;
	}
}
