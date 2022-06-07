package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;

import java.util.List;
import java.util.Map;

public final class MenuSettings {
	private final int menuSize;
	private final int globalDelay;
	private final boolean updateButtons;
	private final String menuTitle;
	private final String fillSpace;
	private final String sound;
	private final Map<List<Integer>, List<ButtonSettings>> itemSettings;
	private final RequirementsLogic openRequirement;
	private final Builder builder;

	private MenuSettings(Builder builder) {
		this.menuSize = builder.menuSize;
		this.globalDelay = builder.globalDelay;
		this.updateButtons = builder.updateButtons;
		this.menuTitle = builder.menuTitle;
		this.fillSpace = builder.fillSpace;
		this.sound = builder.sound;
		this.itemSettings = builder.itemSettings;
		this.openRequirement = builder.openRequirement;
		this.builder = builder;
	}

	public int getMenuSize() {
		return menuSize;
	}

	public int getGlobalDelay() {
		return globalDelay;
	}

	public boolean isUpdateButtons() {
		return updateButtons;
	}

	public String getSound() {
		return sound;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public String getFillSpace() {
		return fillSpace;
	}

	public Map<List<Integer>, List<ButtonSettings>> getItemSettings() {
		return itemSettings;
	}

	public RequirementsLogic getOpenRequirement() {
		return openRequirement;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {

		private int menuSize;
		private int globalDelay;
		private boolean updateButtons;
		private String menuTitle;
		private String fillSpace;
		private String sound;
		private Map<List<Integer>, List<ButtonSettings>> itemSettings;
		private RequirementsLogic openRequirement;

		public Builder setMenuSize(int menuSize) {
			this.menuSize = menuSize;
			return this;
		}

		public Builder setGlobalDelay(int globalDelay) {
			this.globalDelay = globalDelay;
			return this;
		}

		public Builder setUpdateButtons(boolean updateButtons) {
			this.updateButtons = updateButtons;
			return this;
		}

		public Builder setMenuTitle(String menuTitle) {
			this.menuTitle = menuTitle;
			return this;
		}

		public Builder setFillSpace(String fillSpace) {
			this.fillSpace = fillSpace;
			return this;
		}

		public Builder setSound(String sound) {
			this.sound = sound;
			return this;
		}

		public Builder setItemSettings(Map<List<Integer>, List<ButtonSettings>> itemSettings) {
			this.itemSettings = itemSettings;
			return this;
		}

		public Builder setOpenRequirement(RequirementsLogic openRequirement) {
			this.openRequirement = openRequirement;
			return this;
		}

		public MenuSettings build() {
			return new MenuSettings(this);
		}
	}

	@Override
	public String toString() {
		return "MenuSettings{" +
				"menuSize=" + menuSize +
				", globalDelay=" + globalDelay +
				", updateButtons=" + updateButtons +
				", menuTitle='" + menuTitle + '\'' +
				", fillSpace='" + fillSpace + '\'' +
				", sound='" + sound + '\'' +
				", itemSettings=" + itemSettings +
				", openRequirement=" + openRequirement +
				", builder=" + builder +
				'}';
	}
}
