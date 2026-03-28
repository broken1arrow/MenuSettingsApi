package org.brokenarrow.library.menusettings.clickactions;

import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.brokenarrow.library.menusettings.utillity.RunTimedTask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class ClickActionHandler {

    private final List<ClickActionTask> clickActionTaskList;

    public ClickActionHandler(List<ClickActionTask> clickActionTaskList) {
        this.clickActionTaskList = clickActionTaskList;
    }

    public CompletableFuture<Void> runClickActionTasks(@NotNull final Player wiver) {
        if (clickActionTaskList == null || clickActionTaskList.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (ClickActionTask action : this.clickActionTaskList) {
            if (action == null) continue;
            futures.add(runTask(action, wiver));
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private CompletableFuture<Void> runTask(ClickActionTask action, Player wiver) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        long delay = action.formatDelay(wiver);

        Runnable taskWrapper = () -> {
            try {
                action.task(wiver);
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
}
