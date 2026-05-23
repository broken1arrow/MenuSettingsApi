package org.brokenarrow.library.menusettings.requirements;

import org.brokenarrow.library.menusettings.clickactions.ClickRequirementType;
import org.brokenarrow.library.menusettings.command.MenuPlaceholderContext;
import org.brokenarrow.library.menusettings.tasks.ClickActionTask;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

	abstract boolean estimate(@NotNull final Player wiver, @Nullable final MenuPlaceholderContext menuPlaceholderContext);

	public RequirementType getType() {
		return null;
	}

	public boolean isOptional() {
		return optional;
	}

	public List<ClickActionTask> getDenyCommands() {
		return denyCommands;
	}

	public List<ClickActionTask> getSuccessCommands() {
		return successCommands;
	}

	public ClickRequirementType getClickRequirementType() {
		return clickRequirementType;
	}

	public void setClickRequirementType(ClickRequirementType clickRequirementType) {
		this.clickRequirementType = clickRequirementType;

	}

	public void setSuccessCommands(List<ClickActionTask> successCommands) {
		this.successCommands = successCommands;

	}

	public void setOptional(boolean optional) {
		this.optional = optional;

	}

	public void setDenyCommands(List<ClickActionTask> denyCommands) {
		this.denyCommands = denyCommands;

	}

	public ClickActionTask getClickActionTask() {
		return clickActionTask;
	}

	public void setClickActionTask(ClickActionTask clickActionTask) {
		this.clickActionTask = clickActionTask;
	}
}
