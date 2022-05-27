package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.clickactions.ClickRequiermentType;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Requirement {


	private boolean optional;
	private List<ClickActionTask> denyCommands;
	private List<ClickActionTask> successCommands;
	private ClickRequiermentType clickRequiermentType;
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

	public ClickRequiermentType getClickRequiermentType() {
		return clickRequiermentType;
	}

	public void setClickRequiermentType(ClickRequiermentType clickRequiermentType) {
		this.clickRequiermentType = clickRequiermentType;

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
