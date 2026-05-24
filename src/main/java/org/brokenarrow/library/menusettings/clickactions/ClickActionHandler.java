package org.brokenarrow.library.menusettings.clickactions;

import org.brokenarrow.library.menusettings.command.MenuPlaceholderContext;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.brokenarrow.library.menusettings.utillity.RunTimedTask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class ClickActionHandler {

    private final List<ClickActionTask> clickActionTaskList;

    public ClickActionHandler(@NotNull final List<ClickActionTask> clickActionTaskList) {
        this.clickActionTaskList = clickActionTaskList;
    }

    public CompletableFuture<Void> runClickActionTasks(@NotNull final Player wiver, @Nullable final MenuPlaceholderContext menuPlaceholderContext) {
        if (clickActionTaskList.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (ClickActionTask action : this.clickActionTaskList) {
            if (action == null) continue;
            futures.add(runTask(menuPlaceholderContext, action, wiver));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    public List<ClickActionTask> getClickActionTaskList() {
        return clickActionTaskList;
    }

    public boolean isActionTaskListEmpty() {
        return clickActionTaskList.isEmpty();
    }

    private CompletableFuture<Void> runTask(@Nullable final MenuPlaceholderContext menuPlaceholderContext, @NotNull final ClickActionTask action, @NotNull final Player wiver) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        long delay = action.formatDelay(wiver, menuPlaceholderContext);

        Runnable taskWrapper = () -> {
            try {
                action.task(wiver, menuPlaceholderContext);
            } finally {
                future.complete(null);
            }
        };
        if (delay > 0) {
            RunTimedTask.runTaskLater(delay, false, taskWrapper);
        } else {
            RunTimedTask.runTask(false, taskWrapper);
        }
        return future;
    }

    @Override
    public String toString() {
        return "ClickActionHandler{" +
                "clickActionTaskList=" + clickActionTaskList +
                '}';
    }
}
