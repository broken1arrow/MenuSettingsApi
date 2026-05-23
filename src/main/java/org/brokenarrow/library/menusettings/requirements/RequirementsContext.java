package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.command.MenuPlaceholderContext;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.brokenarrow.library.menusettings.MenuSettingsAddon.getPLUGIN;


public class RequirementsContext {

    private List<Requirement> requirements;
    private int minimumRequirements;
    private boolean stopAtSuccess;
    private List<ClickActionTask> denyCommands;

    public RequirementsContext(List<Requirement> requirements) {
        this.setRequirements(requirements);
    }

    /**
     * Performs a synchronous evaluation of all requirements.
     *
     * <p>This method immediately returns whether the requirements are met
     * based on the current state at the time of evaluation.</p>
     *
     * <p>Any associated success or deny commands are executed asynchronously
     * (fire-and-forget). These commands may complete after this method returns
     * and may update the underlying state at a later time.</p>
     *
     * <p><strong>Note:</strong> This method does not wait for any command execution to complete.</p>
     *
     * @param viewer                 the player viewing the menu
     * @param menuPlaceholderContext the execution context used for placeholder resolution
     * @return {@code true} if the requirements are met at evaluation time, otherwise {@code false}
     */
    public boolean estimate(@NotNull final Player viewer, @Nullable final MenuPlaceholderContext menuPlaceholderContext) {
        int success = 0;

        for (Requirement requirement : getRequirements()) {
            boolean passed = requirement.estimate(viewer, menuPlaceholderContext);
            List<ClickActionTask> commands = passed ? requirement.getSuccessCommands() : requirement.getDenyCommands();
            if (!passed && !requirement.isOptional()) {
                return false;
            }
            if (passed) success++;

            if (commands != null) {
                runClickActionTasks(commands, viewer, menuPlaceholderContext)
                        .exceptionally(ex -> {
                            MenuSettingsAddon.getLogger(ex, "Could not run the command set.");
                            return null;
                        });
            }
            if (isStopAtSuccess() && success >= getMinimumRequirements()) break;
        }
        return success >= getMinimumRequirements();
    }

    /**
     * Performs a requirement evaluation and invokes the callback after all
     * triggered command tasks have completed.
     *
     * <p>This method evaluates requirements synchronously, but collects any
     * associated command tasks and waits for their completion before invoking
     * the callback.</p>
     *
     * <p><strong>Important:</strong> This only guarantees completion of commands that are
     * tracked via {@link CompletableFuture} (i.e., commands executed through
     * {@link #runClickActionTasks(List, Player, MenuPlaceholderContext)}).
     * </p>
     *
     * <p>Commands that perform additional asynchronous work outside of this system
     * (for example, plugins like LuckPerms that apply changes asynchronously without
     * immediate caching) may still complete <i>after</i> the callback is invoked.</p>
     *
     * <p>As a result, the callback reflects the evaluation result at the time of execution,
     * but does not guarantee that all external side effects are fully applied.</p>
     *
     * @param viewer                 the player viewing the menu
     * @param menuPlaceholderContext the execution context used for placeholder resolution
     * @param callback               callback invoked after tracked command tasks complete,
     *                               receiving the evaluation result
     */
    public void estimateLater(@NotNull final Player viewer, @Nullable final MenuPlaceholderContext menuPlaceholderContext, @NotNull final Consumer<Boolean> callback) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int success = 0;

        for (Requirement requirement : this.getRequirements()) {
            List<ClickActionTask> commands;
            if (requirement.estimate(viewer, menuPlaceholderContext)) {
                ++success;
                commands = requirement.getSuccessCommands();
                if (this.isStopAtSuccess() && success >= this.getMinimumRequirements())
                    break;

            } else {
                commands = requirement.getDenyCommands();
                if (!requirement.isOptional()) {
                    callback.accept(false);
                    return;
                }
            }
            if (commands != null) {
                futures.add(runClickActionTasks(commands, viewer, menuPlaceholderContext));
            }
        }
        final boolean result = success >= this.getMinimumRequirements();
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> callback.accept(result));
    }

    public CompletableFuture<Void> runClickActionTasks(@Nullable final List<ClickActionTask> list, @NotNull final Player wiver, @Nullable final MenuPlaceholderContext menuPlaceholderContext) {
        if (list == null || list.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (ClickActionTask action : list) {
            if (action == null) continue;
            futures.add(runTask(action, wiver, menuPlaceholderContext));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    public int getMinimumRequirements() {
        return minimumRequirements;
    }

    public void setMinimumRequirements(int minimumRequirements) {
        this.minimumRequirements = minimumRequirements;
    }

    public boolean isStopAtSuccess() {
        return stopAtSuccess;
    }

    public List<ClickActionTask> getDenyCommands() {
        return denyCommands;
    }

    public void setDenyCommands(List<ClickActionTask> denyCommands) {
        this.denyCommands = denyCommands;
    }

    public void setStopAtSuccess(boolean stopAtSuccess) {
        this.stopAtSuccess = stopAtSuccess;

    }

    private CompletableFuture<Void> runTask(@NotNull final ClickActionTask clickAction, @NotNull final Player wiver, @Nullable final MenuPlaceholderContext menuPlaceholderContext) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!clickAction.checkChance(wiver, menuPlaceholderContext)) {
            future.complete(null);
            return future;
        }

        long delay = clickAction.formatDelay(wiver, menuPlaceholderContext);

        Runnable task = () -> {
            try {
                clickAction.task(wiver, menuPlaceholderContext);
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        };

        if (delay > 0)
            Bukkit.getScheduler().runTaskLater(getPLUGIN(), task, delay);
        else
            Bukkit.getScheduler().runTask(getPLUGIN(), task);

        return future;
    }
}
