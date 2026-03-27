package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.requirements.RequirementsLogic;
import org.brokenarrow.library.menusettings.utillity.Action;
import org.brokenarrow.library.menusettings.utillity.CreateItemStack;
import org.brokenarrow.library.menusettings.utillity.SkullCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static org.brokenarrow.library.menusettings.utillity.ArmorSlots.getArmorPiece;

/**
 * Represents the runtime context of a menu button.
 * <p>
 * Combines the {@link ButtonSettings} configuration with the current viewer,
 * and provides logic for rendering and handling interactions for that button.
 */
public class ButtonContext {

    private final Player viewer;
    private final ButtonSettings buttonSettings;

    public ButtonContext(@NotNull final ButtonSettings buttonSettings, @NotNull final Player viewer) {
        this.viewer = viewer;
        this.buttonSettings = buttonSettings;
    }

    /**
     * Returns the configuration for this button.
     *
     * @return the button settings
     */
    @NotNull
    public ButtonSettings getButtonSettings() {
        return buttonSettings;
    }

    /**
     * Creates the {@link ItemStack} representation of this button.
     * <p>
     * The item is built using the {@link ButtonSettings} and supports dynamic sources
     * such as player-held items, armor slots, or custom skull textures.
     *
     * @return the generated item stack, or null if no valid item could be created
     */
    public ItemStack getItemStack() {
        return getItemStack(this.viewer);
    }

    /**
     * Creates the {@link ItemStack} representation of this button.
     * <p>
     * The item is built using the {@link ButtonSettings} and supports dynamic sources
     * such as player-held items, armor slots, or custom skull textures.
     *
     * @param viewer Overrides the default viewer for this operation.
     * @return the generated item stack, or null if no valid item could be created
     */
    @Nullable
    public ItemStack getItemStack(@NotNull final Player viewer) {
        final ButtonSettings settings = this.buttonSettings;
        final ItemWrapper itemWrapper = settings.getButtonItem();
        final String icon = itemWrapper.getIcon();
        Valid.checkBoolean(icon != null, "Your material is null, so can´t add this item to the menu " + icon);

        ItemStack itemStack = null;
        String checkHand = settings.getCheckHand();
        if (checkHand != null) {
            final String itemhand = checkHand.toLowerCase();
            if (itemhand.equals("main_hand"))
                if (viewer.getInventory().getItemInMainHand() != null) {
                    itemStack = viewer.getInventory().getItemInMainHand().clone();
                }
            if (itemhand.equals("off_hand"))
                if (viewer.getInventory().getItemInOffHand() != null) {
                    itemStack = viewer.getInventory().getItemInOffHand().clone();
                }
        }
        String checkArmor = settings.getCheckArmor();
        if (checkArmor != null) {
            itemStack = getArmorPiece(viewer, checkArmor.toLowerCase());
        }
        if (icon.startsWith("uuid="))
            itemStack = SkullCreator.itemFromUuid(UUID.fromString(icon.replaceFirst("uuid=", "")));
        else if (icon.startsWith("base64="))
            itemStack = SkullCreator.itemFromBase64(icon.replaceFirst("base64=", ""));
        else if (icon.startsWith("url="))
            itemStack = SkullCreator.itemFromUrl(icon.replaceFirst("url=", ""));
        else if (icon.startsWith("Player_Skull=") && viewer != null) {
            itemStack = SkullCreator.itemFromUuid(viewer.getUniqueId());
        }

        return CreateItemStack.of(itemStack, itemWrapper, viewer).makeItemStack();
    }

    /**
     * Handles a click interaction for this button.
     * <p>
     * Validates any configured requirements for the given click type and executes
     * the corresponding actions if all requirements are met.
     *
     * @param clickType the type of click performed
     * @param action Called after it run the command.
     * @return true if the click action was executed, false otherwise
     */
    public boolean handleClick(@NotNull final ClickType clickType, @NotNull final Action action) {
        return handleClick(this.viewer, clickType, action);
    }

    /**
     * Handles a click interaction for this button.
     * <p>
     * Validates any configured requirements for the given click type and executes
     * the corresponding actions if all requirements are met.
     *
     * @param viewer    Overrides the default viewer for this operation.
     * @param clickType the type of click performed
     * @param action Called after it run the command.
     * @return true if the click action was executed, false otherwise
     */
    public boolean handleClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Action action) {
        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler globalClickHandler = settings.getClickActionHandler();
        if (globalClickHandler != null) {
            RequirementsLogic clickrequirement = settings.getClickRequirement();
            if (clickrequirement != null && !clickrequirement.estimate(viewer)) {
                clickrequirement.runClickActionTask(clickrequirement.getDenyCommands(), viewer);
                return false;
            }
            globalClickHandler.runClickActionTask(viewer, action);
            return true;
        }
        if (handleShiftClick(viewer, clickType, action)) return true;
        if (handlePrimaryClick(viewer, clickType, action)) return true;

        final ClickActionHandler middleClickAction = settings.getMiddleClickActionHandler();
        if (clickType == ClickType.MIDDLE && middleClickAction != null) {
            final RequirementsLogic middleClickRequirement = settings.getMiddleClickRequirement();
            if (middleClickRequirement != null && !middleClickRequirement.estimate(viewer)) {
                middleClickRequirement.runClickActionTask(middleClickRequirement.getDenyCommands(), viewer);
                return false;
            }
            middleClickAction.runClickActionTask(viewer, action);
            return true;
        }
        return false;
    }

    /**
     * Handles left and right click interactions for this button.
     *
     * @param clickType the type of click performed
     * @param action Called after it run the command.
     * @return true if the click action was executed, false otherwise
     */
    public boolean handlePrimaryClick(@NotNull ClickType clickType, @NotNull final Action action) {
        return handlePrimaryClick(this.viewer, clickType, action);
    }

    /**
     * Handles left and right click interactions for this button.
     *
     * @param viewer    Overrides the default viewer for this operation.
     * @param clickType the type of click performed
     * @param action Called after it run the command.
     * @return true if the click action was executed, false otherwise
     */
    public boolean handlePrimaryClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Action action) {
        if (clickType.isShiftClick()) return false;

        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler rightClickAction = settings.getRightClickActionHandler();
        if (clickType.isRightClick() && rightClickAction != null) {
            RequirementsLogic rightClickRequirement = settings.getRightClickRequirement();
            if (rightClickRequirement != null && !rightClickRequirement.estimate(viewer)) {
                rightClickRequirement.runClickActionTask(rightClickRequirement.getDenyCommands(), viewer);
                return false;
            }
            rightClickAction.runClickActionTask(viewer, action);
            return true;

        }
        final ClickActionHandler leftClickAction = settings.getLeftClickActionHandler();
        if (clickType.isLeftClick() && leftClickAction != null) {
            RequirementsLogic leftClickRequirement = settings.getLeftClickRequirement();
            if (leftClickRequirement != null && !leftClickRequirement.estimate(viewer)) {
                leftClickRequirement.runClickActionTask(leftClickRequirement.getDenyCommands(), viewer);
                return false;
            }
            leftClickAction.runClickActionTask(viewer, action);
            return true;
        }
        return false;
    }

    /**
     * Handles shift-click interactions for this button.
     *
     * @param clickType the type of click performed
     * @param action Called after it run the command.
     * @return true if the click action was executed, false otherwise
     */
    public boolean handleShiftClick(@NotNull final ClickType clickType, @NotNull final Action action) {
        return handleShiftClick(this.viewer, clickType, action);
    }

    /**
     * Handles shift-click interactions for this button.
     *
     *
     * @param viewer    Overrides the default viewer for this operation.
     * @param clickType the type of click performed
     * @param action Called after it run the command.
     * @return true if the click action was executed, false otherwise
     */
    public boolean handleShiftClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Action action) {
        if (!clickType.isShiftClick()) return false;

        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler shiftLeftClickAction = settings.getShiftLeftClickActionHandler();
        if (clickType.isLeftClick() && shiftLeftClickAction != null) {
            RequirementsLogic leftClickRequirement = settings.getShiftLeftClickRequirement();
            if (leftClickRequirement != null && !leftClickRequirement.estimate(viewer)) {
                leftClickRequirement.runClickActionTask(leftClickRequirement.getDenyCommands(), viewer);
                return false;
            }
            shiftLeftClickAction.runClickActionTask(viewer, action);
            return true;
        }

        final ClickActionHandler shiftRightClickAction = settings.getShiftRightClickActionHandler();
        if (clickType.isRightClick() && shiftRightClickAction != null) {
            RequirementsLogic rightClickRequirement = settings.getShiftRightClickRequirement();
            if (rightClickRequirement != null && !rightClickRequirement.estimate(viewer)) {
                rightClickRequirement.runClickActionTask(rightClickRequirement.getDenyCommands(), viewer);
                return false;
            }
            shiftRightClickAction.runClickActionTask(viewer, action);
            return true;
        }
        return false;
    }

}
