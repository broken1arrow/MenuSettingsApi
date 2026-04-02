package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
import org.brokenarrow.library.menusettings.utillity.GuiTypeUtillity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static org.brokenarrow.library.menusettings.MenuSettingsAddon.setPlaceholders;

/**
 * Represents the configuration for a menu, including layout, behavior,
 * button mappings, and requirements.
 *
 * <p>This class is immutable and should be constructed using the {@link Builder}.</p>
 */
public final class MenuSettings {
	private final int menuSize;
	private final int globalDelay;
	private final boolean updateButtons;
	private final boolean refreshAll;
	private final String menuType;
	private final String menuTitle;
	private final String fillSpace;
	private final String sound;
	private final Map<List<Integer>, List<ButtonSettings>> itemSettings;
	private final RequirementsContext openRequirement;
	private final Builder builder;

	private MenuSettings(Builder builder) {
		this.menuType = builder.menuType;
		this.menuSize = builder.menuSize;
		this.globalDelay = builder.globalDelay;
		this.updateButtons = builder.updateButtons;
		this.menuTitle = builder.menuTitle;
		this.fillSpace = builder.fillSpace;
		this.sound = builder.sound;
		this.itemSettings = builder.itemSettings;
		this.openRequirement = builder.openRequirement;
		this.refreshAll = builder.refreshAll;

		this.builder = builder;
	}

	/**
	 * Returns the raw menu type string.
	 *
	 * <p>This value is not processed and may contain placeholders.</p>
	 *
	 * @return the configured menu type string, or {@code null} if not set
	 */
	@Nullable
	public String getMenuType() {
		return menuType;
	}


	/**
	 * Resolves and returns the {@link InventoryType} for this menu.
	 *
	 * <p>This method applies placeholder replacement using the provided player
	 * before resolving the inventory type.</p>
	 *
	 * @param player the player viewing or opening the menu
	 * @return the resolved {@link InventoryType}, or {@code null} if invalid or not set
	 */
	@Nullable
	public InventoryType getMenuType(Player player) {
		GuiTypeUtillity type = new GuiTypeUtillity(setPlaceholders(player, this.getMenuType()));
		return type.getInventoryType();
	}

	/**
	 * Returns the configured menu size.
	 *
	 * @return the menu size
	 */
	public int getMenuSize() {
		return menuSize;
	}

	/**
	 * Returns the global delay applied to button interactions.
	 *
	 * <p>The meaning and unit of this value are defined by the implementation
	 * using this setting (for example, ticks, milliseconds, or custom logic).</p>
	 *
	 * @return the configured global delay
	 */
	public int getGlobalDelay() {
		return globalDelay;
	}

	/**
	 * Indicates whether buttons in the menu should update automatically.
	 *
	 * <p>The exact update behavior is determined by the implementation
	 * using this setting.</p>
	 *
	 * @return {@code true} if buttons should update automatically, otherwise {@code false}
	 */
	public boolean isUpdateButtons() {
		return updateButtons;
	}

	/**
	 * Returns the sound to be played when interacting with the menu.
	 *
	 * @return the configured sound name, or {@code null} if not set
	 */
	public String getSound() {
		return sound;
	}

	/**
	 * Returns the menu title.
	 *
	 * <p>This value may contain placeholders and formatting codes.</p>
	 *
	 * @return the menu title
	 */
	public String getMenuTitle() {
		return menuTitle;
	}

	/**
	 * Returns the filler item definition used for empty slots, could be formated like 0-35 and
	 * fill all slots between this numbers.
	 *
	 * @return the filler item configuration string, or {@code null} if not set
	 */
	public String getFillSpace() {
		return fillSpace;
	}

	/**
	 * Indicates whether all buttons should be refreshed when an update occurs.
	 *
	 * <p>The definition of a "refresh" and when it is triggered depends on the
	 * implementation using this setting.</p>
	 *
	 * @return {@code true} if all buttons should be refreshed, otherwise {@code false}
	 */
	public boolean isRefreshAll() {
		return refreshAll;
	}

	/**
	 * Returns the mapping of slot groups to their corresponding button settings.
	 *
	 * <p>Each key represents a list of slot indices, and the value represents
	 * one or more {@link ButtonSettings} that may be evaluated for those slots.</p>
	 *
	 * @return a map of slot lists to button settings
	 */
	public Map<List<Integer>, List<ButtonSettings>> getItemSettings() {
		return itemSettings;
	}

	/**
	 * Returns the requirement context that must be satisfied in order to open the menu.
	 *
	 * @return the open requirement context, or {@code null} if no requirement is set
	 */
	public RequirementsContext getOpenRequirement() {
		return openRequirement;
	}

	public Builder getBuilder() {
		return builder;
	}

	public static class Builder {

		private int menuSize;
		private int globalDelay;
		private boolean updateButtons;
		private boolean refreshAll;
		private String menuType;
		private String menuTitle;
		private String fillSpace;
		private String sound;
		private Map<List<Integer>, List<ButtonSettings>> itemSettings;
		private RequirementsContext openRequirement;

		public Builder setMenuType(String menuType) {
			this.menuType = menuType;
			return this;
		}

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

		public Builder setOpenRequirement(RequirementsContext openRequirement) {
			this.openRequirement = openRequirement;
			return this;
		}

		public Builder setRefreshAllButtons(boolean refreshAll) {
			this.refreshAll = refreshAll;
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
				", menuTyppe='" + menuType + '\'' +
				", menuTitle='" + menuTitle + '\'' +
				", fillSpace='" + fillSpace + '\'' +
				", sound='" + sound + '\'' +
				", itemSettings=" + itemSettings +
				", openRequirement=" + openRequirement +
				", builder=" + builder +
				'}';
	}
}
