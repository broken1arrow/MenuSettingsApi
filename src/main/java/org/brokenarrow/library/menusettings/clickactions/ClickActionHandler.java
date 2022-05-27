package org.brokenarrow.library.menusettings.clickactions;

import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.getPLUGIN;


public class ClickActionHandler {

	private final List<ClickActionTask> clickActionTaskList;

	public ClickActionHandler(List<ClickActionTask> clickActionTaskList) {
		this.clickActionTaskList = clickActionTaskList;
	}

	public void runClickActionTask(Player wiver) {
		for (ClickActionTask clickAction : this.clickActionTaskList) {
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
}
