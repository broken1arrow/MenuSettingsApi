package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
import org.jetbrains.annotations.Nullable;

public final class ButtonSettings {

	private final int priority;
	private final long refreshTimeWhenUpdateButton;
	private final boolean updateButton;
	private final boolean refreshClickedButton;
	private final String buttonName;
	private final String checkArmor;
	private final String checkHand;
	private final String openNewMenu;
	private final ItemWrapper buttonItem;
	private final RequirementsContext viewRequirement;
	private final RequirementsContext clickrequirement;
	private final RequirementsContext shiftRightClickRequirement;
	private final RequirementsContext shiftLeftClickRequirement;
	private final RequirementsContext leftClickRequirement;
	private final RequirementsContext rightClickRequirement;
	private final RequirementsContext middleClickRequirement;
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
		this.refreshClickedButton = builder.refreshClickedButton;
		this.buttonName = builder.buttonName;
		this.checkArmor = builder.checkArmor;
		this.checkHand = builder.checkHand;
		this.openNewMenu = builder.openNewMenu;
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

	/**
	 * This method you get the time between each update this button shall update. Is used with
	 * {@link #isUpdateButton()} if that are true it will updated the button.
	 *
	 * @return amount of seconds between every update.
	 */
	public long getRefreshButtonTime() {
		return refreshTimeWhenUpdateButton;
	}

	/**
	 * This method is used for check if the buttion shall update
	 * when menu is open time bewteen every update are used with {@link #getRefreshButtonTime()}.
	 *
	 * @return true if it shall update the button.
	 */
	public boolean isUpdateButton() {
		return updateButton;
	}

	/**
	 * Refrech only one button, insted of all buttons.
	 *
	 * @return true if it shall refresh only clicked button.
	 */
	public boolean isRefreshClickedButton() {
		return refreshClickedButton;
	}

	/**
	 * Get name on this button.
	 *
	 * @return name on the button.
	 */
	public String getButtonName() {
		return buttonName;
	}

	/**
	 * Get name on menu to open.
	 *
	 * @return name on the menu.
	 */
	@Nullable
	public String getMenuToOpen() {
		return openNewMenu;
	}

	public String getCheckArmor() {
		return checkArmor;
	}

	public String getCheckHand() {
		return checkHand;
	}

	public ItemWrapper getButtonItem() {
		return buttonItem;
	}

	public RequirementsContext getViewRequirement() {
		return viewRequirement;
	}

	public RequirementsContext getClickRequirement() {
		return clickrequirement;
	}

	public RequirementsContext getShiftRightClickRequirement() {
		return shiftRightClickRequirement;
	}

	public RequirementsContext getShiftLeftClickRequirement() {
		return shiftLeftClickRequirement;
	}

	public RequirementsContext getLeftClickRequirement() {
		return leftClickRequirement;
	}

	public RequirementsContext getRightClickRequirement() {
		return rightClickRequirement;
	}

	public RequirementsContext getMiddleClickRequirement() {
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
		private long refreshTimeWhenUpdateButton;
		private boolean updateButton;
		private boolean refreshClickedButton;
		private String buttonName;
		public String checkArmor;
		public String checkHand;
		private String openNewMenu;
		private ItemWrapper buttonItem;
		private RequirementsContext viewRequirement;
		private RequirementsContext clickrequirement;
		private RequirementsContext shiftRightClickRequirement;
		private RequirementsContext shiftLeftClickRequirement;
		private RequirementsContext leftClickRequirement;
		private RequirementsContext rightClickRequirement;
		private RequirementsContext middleClickRequirement;
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

		public Builder setRefreshTimeWhenUpdateButton(long refreshTimeWhenUpdateButton) {
			this.refreshTimeWhenUpdateButton = refreshTimeWhenUpdateButton;
			return this;
		}

		public Builder setUpdateButton(boolean updateButton) {
			this.updateButton = updateButton;
			return this;
		}

		public Builder setRefreshClickedButton(boolean refreshClickedButton) {
			this.refreshClickedButton = refreshClickedButton;
			return this;
		}

		public Builder setButtonItem(ItemWrapper buttonItem) {
			this.buttonItem = buttonItem;
			return this;
		}

		public Builder setButtonName(String buttonName) {

			this.buttonName = buttonName;
			return this;
		}

		public Builder setCheckArmor(String checkArmor) {
			this.checkArmor = checkArmor;
			return this;
		}

		public Builder setCheckHand(String checkHand) {
			this.checkHand = checkHand;
			return this;
		}

		public Builder setOpenNewMenu(String openNewMenu) {
			this.openNewMenu = openNewMenu;
			return this;
		}

		public Builder setViewRequirement(RequirementsContext requirementsList) {
			this.viewRequirement = requirementsList;
			return this;
		}

		public Builder setClickrequirement(RequirementsContext clickrequirement) {
			this.clickrequirement = clickrequirement;
			return this;
		}

		public Builder setShiftRightClickRequirement(RequirementsContext shiftRightClickRequirement) {
			this.shiftRightClickRequirement = shiftRightClickRequirement;
			return this;
		}

		public Builder setShiftLeftClickRequirement(RequirementsContext shiftLeftClickRequirement) {
			this.shiftLeftClickRequirement = shiftLeftClickRequirement;
			return this;
		}

		public Builder setLeftClickRequirement(RequirementsContext leftClickRequirement) {
			this.leftClickRequirement = leftClickRequirement;
			return this;
		}

		public Builder setRightClickRequirement(RequirementsContext rightClickRequirement) {
			this.rightClickRequirement = rightClickRequirement;
			return this;
		}

		public Builder setMiddleClickRequirement(RequirementsContext middleClickRequirement) {
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
				", refreshTimeWhenUpdateButton=" + refreshTimeWhenUpdateButton +
				", updateButton=" + updateButton +
				", buttonName='" + buttonName + '\'' +
				", checkArmor='" + checkArmor + '\'' +
				", checkHand='" + checkHand + '\'' +
				", openMenu='" + openNewMenu + '\'' +
				", buttonItem=" + buttonItem +
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
