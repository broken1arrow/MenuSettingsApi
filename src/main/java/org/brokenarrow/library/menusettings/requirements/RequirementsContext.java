package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

    public boolean estimate(Player wiver) {
        int success = 0;
        for (Requirement requirement : this.getRequirements()) {
            if (requirement.estimate(wiver)) {
                ++success;
                final List<ClickActionTask> commands = requirement.getSuccessCommands();
                if (commands != null) {
                    runClickActionTask(commands, wiver);
                }
                if (this.isStopAtSuccess() && success >= this.getMinimumRequirements())
                    break;
            } else {
                final List<ClickActionTask> commands = requirement.getDenyCommands();
                if (commands != null) {
                    runClickActionTask(commands, wiver);
                }
                if (!requirement.isOptional()) {
                    return false;
                }
            }
        }
        return success >= this.getMinimumRequirements();
    }

    public void runClickActionTask(List<ClickActionTask> clickActionTaskList, Player wiver) {
        if (clickActionTaskList == null)
            return;

        for (ClickActionTask clickAction : clickActionTaskList) {
            if (clickAction == null) continue;

            if (clickAction.checkChance(wiver)) {
                long delay = clickAction.formatDelay(wiver);
                if (delay > 0)
                    Bukkit.getScheduler().runTaskLater(getPLUGIN(), () -> clickAction.task(wiver), delay);
                else
                    Bukkit.getScheduler().runTask(getPLUGIN(), () -> clickAction.task(wiver));
            }
        }
    }

    public void estimate(@NotNull final Player wiver,@NotNull final Consumer<Boolean> callback) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int success = 0;

        for (Requirement requirement : this.getRequirements()) {
            List<ClickActionTask> commands;
            if (requirement.estimate(wiver)) {
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
                futures.add(runClickActionTasks(commands, wiver));
            }
        }
        final boolean result = success >= this.getMinimumRequirements();
        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenRun(() -> callback.accept(result));
    }

    public CompletableFuture<Void> runClickActionTasks(List<ClickActionTask> list, Player wiver) {
        if (list == null || list.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        final List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (ClickActionTask action : list) {
            if (action == null) continue;
            futures.add(runTask(action, wiver));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<Void> runTask(ClickActionTask clickAction, Player wiver) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (!clickAction.checkChance(wiver)) {
            future.complete(null);
            return future;
        }

        long delay = clickAction.formatDelay(wiver);

        Runnable task = () -> {
            try {
                clickAction.task(wiver);
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

}
