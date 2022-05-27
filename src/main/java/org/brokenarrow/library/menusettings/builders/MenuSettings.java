package org.brokenarrow.library.menusettings.builders;

import java.util.List;
import java.util.Map;

public final class MenuSettings {
	private final int menuSize;
	private final int globalDelay;
	private final boolean updateButtons;
	private final String menuTitle;
	private final String fillSpace;
	private final Map<List<Integer>, List<ItemSettings>> itemSettings;
	private final Builder builder;

	private MenuSettings(Builder builder) {
		this.menuSize = builder.menuSize;
		this.globalDelay = builder.globalDelay;
		this.updateButtons = builder.updateButtons;
		this.menuTitle = builder.menuTitle;
		this.fillSpace = builder.fillSpace;
		this.itemSettings = builder.itemSettings;
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

	public String getMenuTitle() {
		return menuTitle;
	}

	public String getFillSpace() {
		return fillSpace;
	}

	public Map<List<Integer>, List<ItemSettings>> getItemSettings() {
		return itemSettings;
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
		private Map<List<Integer>, List<ItemSettings>> itemSettings;

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

		public Builder setItemSetting(Map<List<Integer>, List<ItemSettings>> itemSettings) {
			this.itemSettings = itemSettings;
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
				", fillSpace=" + fillSpace +
				", itemSettings=" + itemSettings +
				", builder=" + builder +
				'}';
	}
}
