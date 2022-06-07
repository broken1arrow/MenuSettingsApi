package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;
import org.brokenarrow.library.menusettings.utillity.CreateItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import static org.brokenarrow.library.menusettings.utillity.ArmorSlots.getArmorPiece;

public final class ButtonSettings {

	private final int priority;
	private final long refreshTimeWhenUpdateButton;
	private final boolean updateButton;
	private final boolean refreshAllButtons;
	private final String buttonName;
	private final String checkArmor;
	private final String checkHand;
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
		this.refreshAllButtons = builder.refreshAllButtons;
		this.buttonName = builder.buttonName;
		this.checkArmor = builder.checkArmor;
		this.checkHand = builder.checkHand;
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

	public long getRefreshTimeWhenUpdateButton() {
		return refreshTimeWhenUpdateButton;
	}

	public boolean isUpdateButton() {
		return updateButton;
	}

	public boolean isRefreshAllButtons() {
		return refreshAllButtons;
	}

	/**
	 * GetCollections name in this button.
	 *
	 * @return name on the button.
	 */
	public String getButtonName() {
		return buttonName;
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

	@Nullable
	public ItemStack getItemStack(Player viewer) {
		ItemWrapper itemWrapper = getButtonItem();
		String icon = itemWrapper.getIcon();
		Valid.checkBoolean(icon != null, "Your material is null, so can´t add this item to the menu " + ButtonSettings.class);

		ItemStack itemStack = null;
		if (this.getCheckHand() != null) {
			if (this.getCheckHand().equalsIgnoreCase("main_hand"))
				if (viewer.getInventory().getItemInMainHand() != null) {
					itemStack = viewer.getInventory().getItemInMainHand().clone();
				}
			if (this.getCheckHand().equalsIgnoreCase("off_hand"))
				if (viewer.getInventory().getItemInOffHand() != null) {
					itemStack = viewer.getInventory().getItemInOffHand().clone();
				}
		}
		if (this.getCheckArmor() != null) {
			itemStack = getArmorPiece(viewer, this.getCheckArmor());
		}
		return CreateItemStack.of(itemStack, itemWrapper, viewer).makeItemStack();
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
		private long refreshTimeWhenUpdateButton;
		private boolean updateButton;
		private boolean refreshAllButtons;
		private String buttonName;
		public String checkArmor;
		public String checkHand;
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

		public Builder setRefreshTimeWhenUpdateButton(long refreshTimeWhenUpdateButton) {
			this.refreshTimeWhenUpdateButton = refreshTimeWhenUpdateButton;
			return this;
		}

		public Builder setUpdateButton(boolean updateButton) {
			this.updateButton = updateButton;
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
