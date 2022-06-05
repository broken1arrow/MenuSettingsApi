package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public final class ButtonSettings {

	private final int priority;
	private final int refreshTimeWhenUpdateButton;
	private final boolean updateButton;
	private final boolean glow;
	private final boolean refreshAllButtons;
	private final String displayName;
	private final String buttonName;
	private final String slot;
	private final String icon;
	private final List<String> lore;
	private final ItemWrapper buttonItem;
	private final RequirementsLogic viewRequirement;
	private final RequirementsLogic clickrequirement;
	private final RequirementsLogic shiftRightClickRequirement;
	private final RequirementsLogic shiftLeftClickRequirement;
	private final RequirementsLogic leftClickRequirement;
	private final RequirementsLogic rightClickRequirement;
	private final RequirementsLogic middleClickRequirement;
	private final ClickActionHandler clickActionHandler;
	private final ClickActionHandler leftClickActionHandler;
	private final ClickActionHandler rightClickActionHandler;
	private final ClickActionHandler middleClickActionHandler;
	private final ClickActionHandler shiftLeftClickActionHandler;
	private final ClickActionHandler shiftRightClickActionHandler;
	private final Builder builder;

	private ButtonSettings(Builder builder) {
		this.priority = builder.priority;
		this.refreshTimeWhenUpdateButton = builder.refreshTimeWhenUpdateButton;
		this.updateButton = builder.updateButton;
		this.glow = builder.glow;
		this.refreshAllButtons = builder.refreshAllButtons;
		this.displayName = builder.displayName;
		this.buttonName = builder.buttonName;
		this.slot = builder.slot;
		this.icon = builder.icon;
		this.lore = builder.lore;
		this.buttonItem = builder.buttonItem;
		this.viewRequirement = builder.viewRequirement;
		this.clickrequirement = builder.clickrequirement;
		this.shiftRightClickRequirement = builder.shiftRightClickRequirement;
		this.shiftLeftClickRequirement = builder.shiftLeftClickRequirement;
		this.leftClickRequirement = builder.leftClickRequirement;
		this.rightClickRequirement = builder.rightClickRequirement;
		this.middleClickRequirement = builder.middleClickRequirement;
		this.clickActionHandler = builder.clickActionHandler;
		this.leftClickActionHandler = builder.leftClickActionHandler;
		this.rightClickActionHandler = builder.rightClickActionHandler;
		this.middleClickActionHandler = builder.middleClickActionHandler;
		this.shiftLeftClickActionHandler = builder.shiftLeftClickActionHandler;
		this.shiftRightClickActionHandler = builder.shiftRightClickActionHandler;
		this.builder = builder;
	}

	public int getPriority() {
		return priority;
	}

	public int getRefreshTimeWhenUpdateButton() {
		return refreshTimeWhenUpdateButton;
	}

	public boolean isUpdateButton() {
		return updateButton;
	}

	public boolean isGlow() {
		return glow;
	}

	public boolean isRefreshAllButtons() {
		return refreshAllButtons;
	}

	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Get name in this button.
	 *
	 * @return name on the button.
	 */
	public String getButtonName() {
		return buttonName;
	}

	public String getSlot() {
		return slot;
	}

	public String getIcon() {
		return icon;
	}

	public List<String> getLore() {
		return lore;
	}

	public ItemWrapper getButtonItem() {
		return buttonItem;
	}


	public ItemStack getItemStack() {
		
		return buttonItem;
	}

	public RequirementsLogic getViewRequirement() {
		return viewRequirement;
	}

	public RequirementsLogic getClickrequirement() {
		return clickrequirement;
	}

	public RequirementsLogic getShiftRightClickRequirement() {
		return shiftRightClickRequirement;
	}

	public RequirementsLogic getShiftLeftClickRequirement() {
		return shiftLeftClickRequirement;
	}

	public RequirementsLogic getLeftClickRequirement() {
		return leftClickRequirement;
	}

	public RequirementsLogic getRightClickRequirement() {
		return rightClickRequirement;
	}

	public RequirementsLogic getMiddleClickRequirement() {
		return middleClickRequirement;
	}

	public ClickActionHandler getClickActionHandler() {
		return clickActionHandler;
	}

	public ClickActionHandler getLeftClickActionHandler() {
		return leftClickActionHandler;
	}

	public ClickActionHandler getRightClickActionHandler() {
		return rightClickActionHandler;
	}

	public ClickActionHandler getMiddleClickActionHandler() {
		return middleClickActionHandler;
	}

	public ClickActionHandler getShiftLeftClickActionHandler() {
		return shiftLeftClickActionHandler;
	}

	public ClickActionHandler getShiftRightClickActionHandler() {
		return shiftRightClickActionHandler;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {
		private int priority;
		private int refreshTimeWhenUpdateButton;
		private boolean updateButton;
		private boolean glow;
		private boolean refreshAllButtons;
		private String displayName = "";
		private String buttonName;
		private String slot;
		private String icon;
		private List<String> lore;
		private ItemWrapper buttonItem;
		private RequirementsLogic viewRequirement;
		private RequirementsLogic clickrequirement;
		private RequirementsLogic shiftRightClickRequirement;
		private RequirementsLogic shiftLeftClickRequirement;
		private RequirementsLogic leftClickRequirement;
		private RequirementsLogic rightClickRequirement;
		private RequirementsLogic middleClickRequirement;
		private ClickActionHandler clickActionHandler;
		private ClickActionHandler leftClickActionHandler;
		private ClickActionHandler rightClickActionHandler;
		private ClickActionHandler middleClickActionHandler;
		private ClickActionHandler shiftLeftClickActionHandler;
		private ClickActionHandler shiftRightClickActionHandler;

		public Builder setPriority(int priority) {
			this.priority = priority;
			return this;
		}

		public Builder setRefreshTimeWhenUpdateButton(int refreshTimeWhenUpdateButton) {
			this.refreshTimeWhenUpdateButton = refreshTimeWhenUpdateButton;
			return this;
		}

		public Builder setUpdateButton(boolean updateButton) {
			this.updateButton = updateButton;
			return this;
		}

		public Builder setGlow(boolean glow) {
			this.glow = glow;
			return this;
		}

		public Builder setRefreshAllButtons(boolean refreshAllButtons) {
			this.refreshAllButtons = refreshAllButtons;
			return this;
		}

		public Builder setButtonItem(ItemWrapper buttonItem) {
			this.buttonItem = buttonItem;
			return this;
		}

		public Builder setDisplayName(String displayName) {
			this.displayName = displayName;
			return this;
		}

		public Builder setButtonName(String buttonName) {

			this.buttonName = buttonName;
			return this;
		}

		public Builder setSlot(String slot) {
			this.slot = slot;
			return this;
		}

		public Builder setIcon(String icon) {
			this.icon = icon;
			return this;
		}

		public Builder setLore(List<String> lore) {
			this.lore = lore;
			return this;
		}

		public Builder setViewRequirement(RequirementsLogic requirementsList) {
			this.viewRequirement = requirementsList;
			return this;
		}

		public Builder setClickrequirement(RequirementsLogic clickrequirement) {
			this.clickrequirement = clickrequirement;
			return this;
		}

		public Builder setShiftRightClickRequirement(RequirementsLogic shiftRightClickRequirement) {
			this.shiftRightClickRequirement = shiftRightClickRequirement;
			return this;
		}

		public Builder setShiftLeftClickRequirement(RequirementsLogic shiftLeftClickRequirement) {
			this.shiftLeftClickRequirement = shiftLeftClickRequirement;
			return this;
		}

		public Builder setLeftClickRequirement(RequirementsLogic leftClickRequirement) {
			this.leftClickRequirement = leftClickRequirement;
			return this;
		}

		public Builder setRightClickRequirement(RequirementsLogic rightClickRequirement) {
			this.rightClickRequirement = rightClickRequirement;
			return this;
		}

		public Builder setMiddleClickRequirement(RequirementsLogic middleClickRequirement) {
			this.middleClickRequirement = middleClickRequirement;
			return this;
		}

		public Builder setClickActionHandler(ClickActionHandler clickActionHandler) {
			this.clickActionHandler = clickActionHandler;
			return this;
		}

		public Builder setLeftClickActionHandler(ClickActionHandler leftClickActionHandler) {
			this.leftClickActionHandler = leftClickActionHandler;
			return this;
		}

		public Builder setRightClickActionHandler(ClickActionHandler rightClickActionHandler) {
			this.rightClickActionHandler = rightClickActionHandler;
			return this;
		}

		public Builder setMiddleClickActionHandler(ClickActionHandler middleClickActionHandler) {
			this.middleClickActionHandler = middleClickActionHandler;
			return this;
		}

		public Builder setShiftLeftClickActionHandler(ClickActionHandler shiftLeftClickActionHandler) {
			this.shiftLeftClickActionHandler = shiftLeftClickActionHandler;
			return this;
		}

		public Builder setShiftRightClickActionHandler(ClickActionHandler shiftRightClickActionHandler) {
			this.shiftRightClickActionHandler = shiftRightClickActionHandler;
			return this;
		}

		public ButtonSettings build() {
			return new ButtonSettings(this);
		}
	}


	@Override
	public String toString() {
		return "ButtonSettings{" +
				"priority=" + priority +
				", updateButton=" + updateButton +
				", refreshButton=" + refreshTimeWhenUpdateButton +
				", glow=" + glow +
				", displayname='" + displayName + '\'' +
				", slot='" + slot + '\'' +
				", icon='" + icon + '\'' +
				", lore=" + lore +
				", viewRequirement=" + viewRequirement +
				", clickrequirement=" + clickrequirement +
				", shiftRightClickRequirement=" + shiftRightClickRequirement +
				", shiftLeftClickRequirement=" + shiftLeftClickRequirement +
				", leftClickRequirement=" + leftClickRequirement +
				", rightClickRequirement=" + rightClickRequirement +
				", middleClickRequirement=" + middleClickRequirement +
				", clickActionHandler=" + clickActionHandler +
				", leftClickActionHandler=" + leftClickActionHandler +
				", rightClickActionHandler=" + rightClickActionHandler +
				", middleClickActionHandler=" + middleClickActionHandler +
				", shiftLeftClickActionHandler=" + shiftLeftClickActionHandler +
				", shiftRightClickActionHandler=" + shiftRightClickActionHandler +
				", builder=" + builder +
				'}';
	}
}
