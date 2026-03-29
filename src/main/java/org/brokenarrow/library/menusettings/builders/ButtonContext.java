package org.brokenarrow.library.menusettings.builders;

import org.brokenarrow.library.menusettings.clickactions.ClickActionHandler;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.requirements.RequirementsContext;
import org.brokenarrow.library.menusettings.utillity.CreateItemStack;
import org.brokenarrow.library.menusettings.utillity.RequirementResultHandler;
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
     * the corresponding actions. The provided callback is used to configure
     * success and failure behavior.
     *
     * @param clickType      the type of click performed
     * @param resultCallback configures actions to execute on success or failure
     */
    public void handleClick(@NotNull final ClickType clickType, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        handleClick(this.viewer, clickType, resultCallback);
    }

    /**
     * Handles a click interaction for this button.
     * <p>
     * Validates any configured requirements for the given click type and executes
     * the corresponding actions. The provided callback is used to configure
     * success and failure behavior.
     *
     * @param viewer         overrides the default viewer for this operation
     * @param clickType      the type of click performed
     * @param resultCallback configures actions to execute on success or failure
     *
     */
    public void handleClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Consumer<RequirementResultHandler> resultCallback) {

        if (handleGlobalClick(viewer, resultCallback)) return;
        if (handleShiftClick(viewer, clickType, resultCallback)) return;
        if (handlePrimaryClick(viewer, clickType, resultCallback)) return;
        handleMiddleClick(viewer, clickType, resultCallback);
    }

    /**
     * Handles the global click action for this button.
     * <p>
     * If a click requirement exists, the action will only be run if the requirement passes.
     * Otherwise, deny commands will run. The provided callback is used to configure
     * success and failure behavior.
     *
     * @param viewer         the player clicking
     * @param resultCallback configures actions to execute on success or failure
     * @return true if a matching click handler was found and its execution was initiated
     */
    public boolean handleGlobalClick(@NotNull Player viewer, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler globalClickHandler = settings.getClickActionHandler();
        final RequirementsContext clickRequirement = settings.getClickRequirement();
        return this.handleClickRequirements(viewer, globalClickHandler, clickRequirement, resultCallback);
    }

    /**
     * Handles the middle click action for this button.
     * <p>
     * If a click requirement exists, the action will only be run if the requirement passes.
     * Otherwise, deny commands will run. The provided callback is used to configure
     * success and failure behavior.
     *
     * @param viewer         the player clicking
     * @param resultCallback configures actions to execute on success or failure
     * @return true if a matching click handler was found and its execution was initiated
     */
    public boolean handleMiddleClick(@NotNull Player viewer, @NotNull final ClickType clickType, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        if (clickType != ClickType.MIDDLE) return false;

        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler middleClickAction = settings.getMiddleClickActionHandler();
        final RequirementsContext middleClickRequirement = settings.getMiddleClickRequirement();
        return this.handleClickRequirements(viewer, middleClickAction, middleClickRequirement, resultCallback);
    }

    /**
     * Handles left and right click interactions for this button.
     *
     * @param clickType      the type of click performed
     * @param resultCallback configures actions to execute on success or failure
     * @return true if a matching click handler was found and its execution was initiated
     */
    public boolean handlePrimaryClick(@NotNull ClickType clickType, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        return this.handlePrimaryClick(this.viewer, clickType, resultCallback);
    }

    /**
     * Handles left and right click interactions for this button.
     *
     * @param viewer         Overrides the default viewer for this operation
     * @param clickType      the type of click performed
     * @param resultCallback configures actions to execute on success or failure
     * @return true if a matching click handler was found and its execution was initiated
     */
    public boolean handlePrimaryClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        if (clickType.isShiftClick()) return false;

        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler rightClickAction = settings.getRightClickActionHandler();
        final RequirementsContext rightClickRequirement = settings.getRightClickRequirement();
        if (clickType.isRightClick()) {
            return this.handleClickRequirements(viewer, rightClickAction, rightClickRequirement, resultCallback);
        }
        final ClickActionHandler leftClickAction = settings.getLeftClickActionHandler();
        final RequirementsContext leftClickRequirement = settings.getLeftClickRequirement();
        if (clickType.isLeftClick()) {
            return this.handleClickRequirements(viewer, leftClickAction, leftClickRequirement, resultCallback);
        }
        return false;
    }

    /**
     * Handles shift-click interactions for this button.
     *
     * @param clickType      the type of click performed
     * @param resultCallback configures actions to execute on success or failure
     * @return true if a matching click handler was found and its execution was initiated
     */
    public boolean handleShiftClick(@NotNull final ClickType clickType, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        return handleShiftClick(this.viewer, clickType, resultCallback);
    }

    /**
     * Handles shift-click interactions for this button.
     *
     * @param viewer         Overrides the default viewer for this operation.
     * @param clickType      the type of click performed
     * @param resultCallback configures actions to execute on success or failure.
     * @return true if a matching click handler was found and its execution was initiated
     */
    public boolean handleShiftClick(@NotNull final Player viewer, @NotNull final ClickType clickType, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        if (!clickType.isShiftClick()) return false;

        final ButtonSettings settings = this.buttonSettings;
        final ClickActionHandler shiftLeftClickAction = settings.getShiftLeftClickActionHandler();
        final RequirementsContext leftClickRequirement = settings.getShiftLeftClickRequirement();
        if (clickType.isLeftClick()) {
            return this.handleClickRequirements(viewer, shiftLeftClickAction, leftClickRequirement, resultCallback);
        }

        final ClickActionHandler shiftRightClickAction = settings.getShiftRightClickActionHandler();
        final RequirementsContext rightClickRequirement = settings.getShiftRightClickRequirement();
        if (clickType.isRightClick()) {
            return this.handleClickRequirements(viewer, shiftRightClickAction, rightClickRequirement, resultCallback);
        }
        return false;
    }

    private boolean handleClickRequirements(@NotNull Player viewer, @Nullable ClickActionHandler clickActionHandler, @Nullable RequirementsContext requirementsContext, @NotNull final Consumer<RequirementResultHandler> resultCallback) {
        if (clickActionHandler == null) {
            return false;
        }
        final RequirementResultHandler handler = new RequirementResultHandler();
        resultCallback.accept(handler);

        if (requirementsContext == null) {
            clickActionHandler.runClickActionTasks(viewer)
                    .thenRun(handler::executeSuccess);
            return true;
        }
        requirementsContext.estimate(viewer, hasRequirement -> {
            if (hasRequirement) {
                clickActionHandler.runClickActionTasks(viewer)
                        .thenRun(handler::executeSuccess);
            } else {
                requirementsContext
                        .runClickActionTasks(requirementsContext.getDenyCommands(), viewer)
                        .thenRun(handler::executeFailure);
            }
        });
        return true;
    }

}
