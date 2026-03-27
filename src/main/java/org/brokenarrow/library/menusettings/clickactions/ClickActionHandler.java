package org.brokenarrow.library.menusettings.clickactions;

import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.brokenarrow.library.menusettings.utillity.Action;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.brokenarrow.library.menusettings.utillity.RunTimedTask.runTask;
import static org.brokenarrow.library.menusettings.utillity.RunTimedTask.runTaskLater;


public class ClickActionHandler {

    private final List<ClickActionTask> clickActionTaskList;

    public ClickActionHandler(List<ClickActionTask> clickActionTaskList) {
        this.clickActionTaskList = clickActionTaskList;
    }

    public void runClickActionTask(Player wiver, @NotNull Action action) {
        if (clickActionTaskList == null)
            return;
        for (ClickActionTask clickAction : this.clickActionTaskList) {
            if (clickAction == null) continue;

            if (clickAction.checkChance(wiver)) {
                long delay = clickAction.formatDelay(wiver);
                if (delay > 0)
                    runTaskLater(delay, false, () -> {
                        clickAction.task(wiver);
                        action.perform();
                    });
                else
                    runTask(false, () -> {
                        clickAction.task(wiver);
                        action.perform();
                    });

            }
        }
    }
}
