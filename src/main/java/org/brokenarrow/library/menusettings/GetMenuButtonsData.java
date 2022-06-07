package org.brokenarrow.library.menusettings;

import org.brokenarrow.library.menusettings.builders.ButtonSettings;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Map;

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

	public Player getWiver() {
		return wiver;
	}

	public boolean checkOpenRequirements(String permission) {
		if (this.wiver != null && permission != null && !permission.isEmpty() && this.wiver.hasPermission(permission))
			return true;
		
		if (menucache.getOpenRequirement() != null) {
			if (!menucache.getOpenRequirement().estimate(this.wiver)) {
				if (menucache.getOpenRequirement().getDenyCommands() != null)
					menucache.getOpenRequirement().runClickActionTask(menucache.getOpenRequirement().getDenyCommands(), this.wiver);
				return false;
			}
		}
		return true;
	}

	public boolean checkClickRequirements(ButtonSettings requirements, ClickType clickType) {
		if (requirements == null)
			return true;

		if (requirements.getClickActionHandler() != null) {
			if (requirements.getClickrequirement() != null && !requirements.getClickrequirement().estimate(this.wiver)) {
				requirements.getClickrequirement().runClickActionTask(requirements.getClickrequirement().getDenyCommands(), this.wiver);
				return false;
			}
			requirements.getClickActionHandler().runClickActionTask(this.wiver);
			return true;
		} else {
			if (clickType.isShiftClick() && clickType.isLeftClick() && requirements.getShiftLeftClickActionHandler() != null) {
				if (requirements.getShiftLeftClickRequirement() != null && !requirements.getShiftLeftClickRequirement().estimate(this.wiver)) {
					requirements.getShiftLeftClickRequirement().runClickActionTask(requirements.getLeftClickRequirement().getDenyCommands(), this.wiver);
					return false;
				}
				requirements.getShiftLeftClickActionHandler().runClickActionTask(this.wiver);
				return true;
			} else if (clickType.isShiftClick() && clickType.isRightClick() && requirements.getShiftRightClickActionHandler() != null) {
				if (requirements.getShiftRightClickRequirement() != null && !requirements.getShiftRightClickRequirement().estimate(this.wiver)) {
					requirements.getShiftRightClickRequirement().runClickActionTask(requirements.getShiftRightClickRequirement().getDenyCommands(), this.wiver);
					return false;
				}
				requirements.getShiftRightClickActionHandler().runClickActionTask(this.wiver);
				return true;
			} else if (clickType.isRightClick() && requirements.getRightClickActionHandler() != null) {
				if (requirements.getRightClickRequirement() != null && !requirements.getRightClickRequirement().estimate(this.wiver)) {
					requirements.getShiftRightClickRequirement().runClickActionTask(requirements.getShiftRightClickRequirement().getDenyCommands(), this.wiver);
					return false;
				}
				requirements.getRightClickActionHandler().runClickActionTask(this.wiver);
				return true;

			} else if (clickType.isLeftClick() && requirements.getLeftClickActionHandler() != null) {
				if (requirements.getLeftClickRequirement() != null && !requirements.getLeftClickRequirement().estimate(this.wiver)) {
					requirements.getLeftClickRequirement().runClickActionTask(requirements.getLeftClickRequirement().getDenyCommands(), this.wiver);
					return false;
				}
				requirements.getLeftClickActionHandler().runClickActionTask(this.wiver);
				return true;
			} else if (clickType == ClickType.MIDDLE && requirements.getMiddleClickActionHandler() != null) {
				if (requirements.getMiddleClickRequirement() != null && !requirements.getMiddleClickRequirement().estimate(this.wiver)) {
					requirements.getShiftRightClickRequirement().runClickActionTask(requirements.getShiftRightClickRequirement().getDenyCommands(), this.wiver);
					return false;
				}
				requirements.getMiddleClickActionHandler().runClickActionTask(this.wiver);
				return true;
			}

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
