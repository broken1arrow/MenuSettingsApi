package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.clickactions.ClickRequirementType;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Requirement {


	private boolean optional;
	private List<ClickActionTask> denyCommands;
	private List<ClickActionTask> successCommands;
	private ClickRequirementType clickRequirementType;
	private ClickActionTask clickActionTask;

	public Requirement() {
		this.optional = false;
	}

	public Requirement(boolean optional) {
		this.setOptional(optional);
	}

	abstract boolean estimate(Player wiver);

	public RequirementType getType() {
		return null;
	}

	public boolean isOptional() {
		return optional;
	}

	public List<ClickActionTask> getDenyCommands() {
		return denyCommands;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;

	}

	public void setDenyCommands(List<ClickActionTask> denyCommands) {
		this.denyCommands = denyCommands;

	}

	public List<ClickActionTask> getSuccessCommands() {
		return successCommands;
	}

	public ClickRequirementType getClickRequiermentType() {
		return clickRequirementType;
	}

	public void setClickRequiermentType(ClickRequirementType clickRequirementType) {
		this.clickRequirementType = clickRequirementType;

	}

	public void setSuccessCommands(List<ClickActionTask> successCommands) {
		this.successCommands = successCommands;

	}

	public ClickActionTask getClickActionTask() {
		return clickActionTask;
	}

	public void setClickActionTask(ClickActionTask clickActionTask) {
		this.clickActionTask = clickActionTask;
	}
}
