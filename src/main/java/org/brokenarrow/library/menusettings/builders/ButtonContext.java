package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
import org.brokenarrow.library.menusettings.utillity.Action;
import org.brokenarrow.library.menusettings.utillity.CreateItemStack;
import org.brokenarrow.library.menusettings.utillity.RequirementCheck;
import org.brokenarrow.library.menusettings.utillity.RequirementResult;
import org.brokenarrow.library.menusettings.utillity.SkullCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;

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
     * @param resultCallback    Called after it run the command.
     */
    public void handleClick(@NotNull final ClickType clickType, @NotNull final  RequirementResult resultCallback) {
        handleClick(this.viewer, clickType, resultCallback);
    }

    /**
     * Handles a click interaction for this button.
     * <p>
     * Validates any configured requirements for the given click type and executes
     * the corresponding actions if all requirements are met.
     *
     * @param viewer    Overrides the default viewer for this operation.
     * @param clickType the type of click performed
     * @param resultCallback    Called after it run the command.
     */
    public void handleClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final RequirementResult resultCallback) {

        handleGlobalClick(viewer, resultCallback);
        handleShiftClick(viewer, clickType, resultCallback);
        handlePrimaryClick(viewer, clickType, resultCallback);
        handleMiddleClick(viewer, clickType, resultCallback);
    }

    /**
     * Handles the global click action for this button.
     * <p>
     * If a click requirement exists, the action will only be run if the requirement passes.
     * Otherwise, deny commands will run. The actual execution is asynchronous
     * and the method returns immediately; do not rely on return value for success/failure.
     *
     * @param viewer the player clicking
     * @param resultCallback  the action to execute if allowed
     */
    private void handleGlobalClick(@NotNull Player viewer, @NotNull final  RequirementResult resultCallback) {
        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler globalClickHandler = settings.getClickActionHandler();
        final RequirementsContext clickrequirement = settings.getClickRequirement();
        this.handleClickRequirements(viewer, globalClickHandler, clickrequirement, resultCallback);
    }

    private void handleMiddleClick(@NotNull Player viewer, @NotNull final ClickType clickType, @NotNull final  RequirementResult resultCallback) {
        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler middleClickAction = settings.getMiddleClickActionHandler();
        final RequirementsContext middleClickRequirement = settings.getMiddleClickRequirement();
        if (clickType == ClickType.MIDDLE) {
            this.handleClickRequirements(viewer, middleClickAction, middleClickRequirement, resultCallback);
        }
    }

    /**
     * Handles left and right click interactions for this button.
     *
     * @param clickType the type of click performed
     * @param action    Called after it run the command.
     */
    public void handlePrimaryClick(@NotNull ClickType clickType, @NotNull final Action action) {
        this.handlePrimaryClick(this.viewer, clickType, action);
    }

    /**
     * Handles left and right click interactions for this button.
     *
     * @param viewer    Overrides the default viewer for this operation.
     * @param clickType the type of click performed
     * @param action    Called after it run the command.
     */
    public void handlePrimaryClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Action action) {
        if (clickType.isShiftClick()) return;

        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler rightClickAction = settings.getRightClickActionHandler();
        final RequirementsContext rightClickRequirement = settings.getRightClickRequirement();
        if (clickType.isRightClick()) {
            this.handleClickRequirements(viewer, rightClickAction, rightClickRequirement, action);
            return;
        }
        final ClickActionHandler leftClickAction = settings.getLeftClickActionHandler();
        final RequirementsContext leftClickRequirement = settings.getLeftClickRequirement();
        if (clickType.isLeftClick()) {
            this.handleClickRequirements(viewer, leftClickAction, leftClickRequirement, action);
        }

    }

    /**
     * Handles shift-click interactions for this button.
     *
     * @param clickType the type of click performed
     * @param action    Called after it run the command.
     */
    public void handleShiftClick(@NotNull final ClickType clickType, @NotNull final Action action) {
        handleShiftClick(this.viewer, clickType, action);
    }

    /**
     * Handles shift-click interactions for this button.
     *
     * @param viewer    Overrides the default viewer for this operation.
     * @param clickType the type of click performed
     * @param action    Called after it run the command.
     */
    public void handleShiftClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Action action) {
        if (!clickType.isShiftClick()) return;

        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler shiftLeftClickAction = settings.getShiftLeftClickActionHandler();
        final RequirementsContext leftClickRequirement = settings.getShiftLeftClickRequirement();
        if (clickType.isLeftClick()) {
            this.handleClickRequirements(viewer, shiftLeftClickAction, leftClickRequirement, action);
        }

        final ClickActionHandler shiftRightClickAction = settings.getShiftRightClickActionHandler();
        final RequirementsContext rightClickRequirement = settings.getShiftRightClickRequirement();
        if (clickType.isRightClick()) {
            this.handleClickRequirements(viewer, shiftRightClickAction, rightClickRequirement, action);
        }
    }

    private void handleClickRequirements(@NotNull Player viewer, @Nullable ClickActionHandler clickActionHandler, @Nullable RequirementsContext requirementsContext, @NotNull RequirementResult resultCallback) {
        if (clickActionHandler == null) {
            return;
        }

        if (requirementsContext == null) {
            clickActionHandler.runClickActionTasks(viewer)
                    .thenRun(resultCallback::onSuccess);
            return;
        }
        viewer.set;
        requirementsContext.estimate(viewer, hasRequirement -> {
            if (hasRequirement) {
                clickActionHandler.runClickActionTasks(viewer)
                        .thenRun(resultCallback::onSuccess);
            } else {
                requirementsContext
                        .runClickActionTasks(requirementsContext.getDenyCommands(), viewer)
                        .thenRun(() -> resultCallback::onFailure);
            }
        });
    }

}
