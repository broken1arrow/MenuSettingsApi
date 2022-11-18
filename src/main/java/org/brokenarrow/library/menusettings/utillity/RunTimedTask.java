package org.brokenarrow.library.menusettings.utillity;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import static org.brokenarrow.library.menusettings.MenuSettingsAddon.getPLUGIN;

public class RunTimedTask {


	public static BukkitTask runTask(boolean async, Runnable task) {
		if (async)
			return Bukkit.getScheduler().runTaskAsynchronously(getPLUGIN(), task);
		else
			return Bukkit.getScheduler().runTask(getPLUGIN(), task);
	}

	public static BukkitTask runTaskLater(long tick, boolean async, Runnable task) {
		if (async)
			return Bukkit.getScheduler().runTaskLaterAsynchronously(getPLUGIN(), task, tick);
		else
			return Bukkit.getScheduler().runTaskLater(getPLUGIN(), task, tick);
	}
}
