package org.brokenarrow.library.menusettings;

import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.setPlaceholders;

/**
 * This help class, help you get both buttons and requirements needed for open menu or click on a item.
 */
public final class GetMenuButtonsData {
	private final MenuSettings menuSettings;
	private final Player wiver;
	private final String menuName;
	private final RegisterMenuAddon registerMenuAddon = RegisterMenuAddon.getInstance();

	/**
	 * Create new instance of this class and use the methods inside.
	 * It will load settings for the menu you typed.
	 *
	 * @param menuName the name of the menu (if you use several files, is it the name).
	 * @param player   the player some have the menu open.
	 */

	public GetMenuButtonsData(@NotNull final String menuName, @NotNull final Player player) {
		Valid.checkNotNull(this.registerMenuAddon, "RegisterMenuAddon class is not registed.");
		Valid.checkNotNull(this.registerMenuAddon.getMenuCache(), "menu chache is null, so can't load the settings");
		this.menuName = menuName;
		Map<String, MenuSettings> menu = this.registerMenuAddon.getMenuCache().getMenuCache();
		if (menu != null) {
			this.menuSettings = menu.get(menuName);
		} else
			this.menuSettings = null;

		this.wiver = player;
	}

	/**
	 * Get the MenuSettings. From this you get all methods
	 * you need to add to your menu. From the title to buttons
	 * can you access from this.
	 *
	 * @return menusettings.
	 */

	public MenuSettings getMenuSettings() {
		Valid.checkNotNull(menuSettings, "menu Settings cache is null, so can't load the menu settings. Check if this menu name is valid: " + menuName);
		return menuSettings;
	}

	/**
	 * Get all settings for menu buttons. Return a map with slots as keys and buttons as values.
	 * Every button can have one or more slots it can be placed inside the menu and can also have several
	 * buttons to the same slots.
	 *
	 * @return map with all slots and item settings.
	 */
	public Map<List<Integer>, List<ButtonSettings>> getItemSettings() {
		Valid.checkNotNull(menuSettings, "menu Settings cache is null, so can't load the menu settings. Check if this menu name is valid: " + menuName);
		Map<List<Integer>, List<ButtonSettings>> itemSettings = menuSettings.getItemSettings();
		return itemSettings != null ? itemSettings : new HashMap<>();
	}

	/**
	 * Get list of all slots some are set.
	 *
	 * @return list of slots buttons is placed.
	 */
	public Set<Integer> getSlots() {
		return this.getItemSettings().keySet().stream().flatMap(List::stream).collect(Collectors.toSet());
	}

	public Player getWiver() {
		return wiver;
	}

	/**
	 * Check the open menu requirements.
	 *
	 * @param bypassPermission set this if you want to set bypass permission.
	 * @return true if player meet the requirements.
	 */
	public boolean checkOpenRequirements(String bypassPermission) {
		if (this.wiver != null && bypassPermission != null && !bypassPermission.isEmpty() && this.wiver.hasPermission(bypassPermission))
			return true;
		RequirementsLogic openRequirement = this.getMenuSettings().getOpenRequirement();
		if (openRequirement != null) {
			if (openRequirement.estimate(this.wiver)) return true;

			if (openRequirement.getDenyCommands() != null)
				openRequirement.runClickActionTask(openRequirement.getDenyCommands(), this.wiver);
			return false;
		}
		return true;
	}

	/**
	 * Checks click from the player. If you set requirements for a click type in the file
	 * It will check that before executing any command and also send a message to the player if
	 * The player does not meet your set requirements.
	 *
	 * @param buttonSettings the settings.
	 * @param clickType      player click with.
	 * @return true if player meet requirements.
	 */
	public boolean checkClickRequirements(ButtonSettings buttonSettings, ClickType clickType) {
		if (buttonSettings == null)
			return true;

		if (buttonSettings.getClickActionHandler() != null) {
			RequirementsLogic clickrequirement = buttonSettings.getClickRequirement();
			if (clickrequirement != null && !clickrequirement.estimate(this.wiver)) {
				clickrequirement.runClickActionTask(clickrequirement.getDenyCommands(), this.wiver);
				return false;
			}
			buttonSettings.getClickActionHandler().runClickActionTask(this.wiver);
			return true;
		}
		if (checkShiftClickRequirements(buttonSettings, clickType)) return true;
		if (checkRightAndLeftClickRequirements(buttonSettings, clickType)) return true;

		if (clickType == ClickType.MIDDLE && buttonSettings.getMiddleClickActionHandler() != null) {
			if (buttonSettings.getMiddleClickRequirement() != null && !buttonSettings.getMiddleClickRequirement().estimate(this.wiver)) {
				buttonSettings.getShiftRightClickRequirement().runClickActionTask(buttonSettings.getShiftRightClickRequirement().getDenyCommands(), this.wiver);
				return false;
			}
			buttonSettings.getMiddleClickActionHandler().runClickActionTask(this.wiver);
			return true;
		}
		return false;
	}

	/**
	 * GetCollections button data on current slot. It return data from {@link ButtonSettings}
	 *
	 * @param slot get the data from.
	 * @return ButtonSettings or null.
	 */
	@Nullable
	public ButtonSettings getButton(int slot) {
		List<ButtonSettings> buttons = getButtons(slot);
		if (buttons == null) return null;

		for (ButtonSettings key : buttons) {
			if (checkRequirement(key.getViewRequirement())) {
				return key;
			}
		}
		return null;
	}

	public boolean checkRightAndLeftClickRequirements(ButtonSettings buttonSettings, ClickType clickType) {
		if (clickType.isShiftClick()) return false;

		if (clickType.isRightClick() && buttonSettings.getRightClickActionHandler() != null) {
			RequirementsLogic rightClickRequirement = buttonSettings.getRightClickRequirement();
			if (rightClickRequirement != null && !rightClickRequirement.estimate(this.wiver)) {
				rightClickRequirement.runClickActionTask(rightClickRequirement.getDenyCommands(), this.wiver);
				return false;
			}
			buttonSettings.getRightClickActionHandler().runClickActionTask(this.wiver);
			return true;

		}
		if (clickType.isLeftClick() && buttonSettings.getLeftClickActionHandler() != null) {
			RequirementsLogic leftClickRequirement = buttonSettings.getLeftClickRequirement();
			if (leftClickRequirement != null && !leftClickRequirement.estimate(this.wiver)) {
				leftClickRequirement.runClickActionTask(leftClickRequirement.getDenyCommands(), this.wiver);
				return false;
			}
			buttonSettings.getLeftClickActionHandler().runClickActionTask(this.wiver);
			return true;
		}
		return false;
	}

	public boolean checkShiftClickRequirements(ButtonSettings buttonSettings, ClickType clickType) {
		if (!clickType.isShiftClick()) return false;

		if (clickType.isLeftClick() && buttonSettings.getShiftLeftClickActionHandler() != null) {
			RequirementsLogic leftClickRequirement = buttonSettings.getShiftLeftClickRequirement();
			if (leftClickRequirement != null && !leftClickRequirement.estimate(this.wiver)) {
				leftClickRequirement.runClickActionTask(buttonSettings.getLeftClickRequirement().getDenyCommands(), this.wiver);
				return false;
			}
			buttonSettings.getShiftLeftClickActionHandler().runClickActionTask(this.wiver);
			return true;
		}
		if (clickType.isRightClick() && buttonSettings.getShiftRightClickActionHandler() != null) {
			RequirementsLogic rightClickRequirement = buttonSettings.getShiftRightClickRequirement();
			if (rightClickRequirement != null && !rightClickRequirement.estimate(this.wiver)) {
				rightClickRequirement.runClickActionTask(buttonSettings.getShiftRightClickRequirement().getDenyCommands(), this.wiver);
				return false;
			}
			buttonSettings.getShiftRightClickActionHandler().runClickActionTask(this.wiver);
			return true;
		}
		return false;
	}

	public String setPlaceholder(String string) {
		return setPlaceholders(getWiver(), string);
	}

	public boolean checkRequirement(RequirementsLogic viewRequirement) {
		if (viewRequirement == null)
			return true;
		return viewRequirement.estimate(this.wiver);
	}

	public List<ButtonSettings> getButtons(int slot) {
		for (List<Integer> keys : this.getMenuSettings().getItemSettings().keySet()) {
			if (keys.contains(slot))
				return this.getItemSettings().get(keys);
		}
		return null;
	}
}
