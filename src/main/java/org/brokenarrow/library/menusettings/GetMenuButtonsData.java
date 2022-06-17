package org.brokenarrow.library.menusettings;

import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.setPlaceholders;

public class GetMenuButtonsData {

	private MenuSettings menucache;
	private final Player wiver;
	private final RegisterMenuAddon registerMenuAddon = RegisterMenuAddon.getInstance();

	public GetMenuButtonsData(String menuName, Player player) {
		if (this.registerMenuAddon == null)
			throw new RuntimeException("RegisterMenuAddon class is not registed.");
		if (this.registerMenuAddon.getMenuCache() == null)
			throw new RuntimeException("menu chache is null, so can't load the settings");
		Map<String, MenuSettings> menu = this.registerMenuAddon.getMenuCache().getMenuCache();
		if (menu != null) {
			this.menucache = menu.get(menuName);
		}
		this.wiver = player;
	}

	public MenuSettings getMenucache() {
		return menucache;
	}

	/**
	 * Get all settings for menu button´s. Return a map with slots every button shall be placed.
	 *
	 * @return map with all items settings.
	 */
	public Map<List<Integer>, List<ButtonSettings>> getItemSettings() {
		return menucache.getItemSettings();
	}

	/**
	 * Get list of all slots some are set.
	 *
	 * @return list of slots buttons is placed.
	 */
	public List<Integer> getSlots() {
		return menucache.getItemSettings().keySet().stream().flatMap(List::stream).collect(Collectors.toList());
	}

	public Player getWiver() {
		return wiver;
	}

	public boolean checkOpenRequirements(String bypassPermission) {
		if (this.wiver != null && bypassPermission != null && !bypassPermission.isEmpty() && this.wiver.hasPermission(bypassPermission))
			return true;
		RequirementsLogic openRequirement = menucache.getOpenRequirement();
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
	 * @param buttonSettings
	 * @param clickType
	 * @return
	 */
	public boolean checkClickRequirements(ButtonSettings buttonSettings, ClickType clickType) {
		if (buttonSettings == null)
			return true;

		if (buttonSettings.getClickActionHandler() != null) {
			if (buttonSettings.getClickrequirement() != null && !buttonSettings.getClickrequirement().estimate(this.wiver)) {
				buttonSettings.getClickrequirement().runClickActionTask(buttonSettings.getClickrequirement().getDenyCommands(), this.wiver);
				return false;
			}
			buttonSettings.getClickActionHandler().runClickActionTask(this.wiver);
			return true;
		} else {
			if (checkShiftClickRequirements(buttonSettings, clickType)) return true;
			if (checkRightAndLeftClickRequirements(buttonSettings, clickType)) return true;

			else if (clickType == ClickType.MIDDLE && buttonSettings.getMiddleClickActionHandler() != null) {
				if (buttonSettings.getMiddleClickRequirement() != null && !buttonSettings.getMiddleClickRequirement().estimate(this.wiver)) {
					buttonSettings.getShiftRightClickRequirement().runClickActionTask(buttonSettings.getShiftRightClickRequirement().getDenyCommands(), this.wiver);
					return false;
				}
				buttonSettings.getMiddleClickActionHandler().runClickActionTask(this.wiver);
				return true;
			}

		}
		return false;
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

	/**
	 * GetCollections button data on current slot. It return data from {@link ButtonSettings}
	 *
	 * @param slot get the data from.
	 * @return items settings or null.
	 */
	public ButtonSettings getButton(int slot) {
		List<ButtonSettings> buttons = getButtons(slot);
		if (buttons != null)
			for (ButtonSettings key : buttons) {
				if (checkRequirement(key.getViewRequirement())) {
					return key;
				}
			}
		return null;
	}

	public boolean checkRequirement(RequirementsLogic viewRequirement) {
		if (viewRequirement == null)
			return true;
		return viewRequirement.estimate(this.wiver);
	}

	public List<ButtonSettings> getButtons(int slot) {
		for (List<Integer> keys : menucache.getItemSettings().keySet()) {
			if (keys.contains(slot))
				return menucache.getItemSettings().get(keys);
		}
		return null;
	}
}
