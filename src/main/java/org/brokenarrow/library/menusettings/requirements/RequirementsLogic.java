package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.getPLUGIN;


public class RequirementsLogic {

	private List<Requirement> requirements;
	private int minimumRequirements;
	private boolean stopAtSuccess;
	private List<ClickActionTask> denyCommands;

	public RequirementsLogic(List<Requirement> requirements) {
		this.setRequirements(requirements);
	}

	public boolean estimate(Player wiver) {
		int success = 0;
		for (Requirement requirement : this.getRequirements()) {
			if (requirement.estimate(wiver)) {
				++success;
				if (requirement.getSuccessCommands() != null) {
					runClickActionTask(requirement.getSuccessCommands(), wiver);
				}
				if (this.isStopAtSuccess() && success >= this.getMinimumRequirements())
					break;
			} else {
				if (requirement.getDenyCommands() != null) {
					runClickActionTask(requirement.getDenyCommands(), wiver);
				}
				if (!requirement.isOptional()) {
					return false;
				}
			}
		}
		return success >= this.getMinimumRequirements();
	}

	public void runClickActionTask(List<ClickActionTask> clickActionTaskList, Player wiver) {
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
