package org.brokenarrow.library.menusettings.clickactions;

public enum ClickRequiermentType {
	OPEN_REQUIREMENT("Open_requirement"),
	VIEW_REQUIREMENT("View_requirement"),
	LEFT_CLICK_REQUIREMENT("Left_click_requirement"),
	RIGHT_CLICK_REQUIREMENT("Right_click_requirement"),
	SHIFT_LEFT_CLICK_REQUIREMENT("Shift_left_click_requirement"),
	SHIFT_RIGHT_CLICK_REQUIREMENT("Shift_right_click_requirement"),
	CLICK_REQUIREMENT("Click_requirement"),
	MIDDLE_CLICK_REQUIREMENT("Middle_click_requirement");

	private final String requirementType;

	ClickRequiermentType(String requirementType) {
		this.requirementType = requirementType;
	}

	public static ClickRequiermentType getType(String string) {
		ClickRequiermentType[] requirementTypes = values();

		for (ClickRequiermentType type : requirementTypes) {
			if (type.getRequirementType().equals(string))
				return type;
		}
		return null;
	}

	public String getRequirementType() {
		return requirementType;
	}
}
