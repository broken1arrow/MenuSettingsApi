package org.brokenarrow.library.menusettings.clickactions;

public enum ClickRequirementType {
	OPEN_REQUIREMENT("Open_requirement"),
	VIEW_REQUIREMENT("View_requirement"),
	LEFT_CLICK_REQUIREMENT("Left_click_requirement"),
	RIGHT_CLICK_REQUIREMENT("Right_click_requirement"),
	SHIFT_LEFT_CLICK_REQUIREMENT("Shift_left_click_requirement"),
	SHIFT_RIGHT_CLICK_REQUIREMENT("Shift_right_click_requirement"),
	CLICK_REQUIREMENT("Click_requirement"),
	MIDDLE_CLICK_REQUIREMENT("Middle_click_requirement");

	private final String requirementType;

	ClickRequirementType(String requirementType) {
		this.requirementType = requirementType;
	}

	public static ClickRequirementType getType(String string) {
		ClickRequirementType[] requirementTypes = values();

		for (ClickRequirementType type : requirementTypes) {
			if (type.getRequirementType().equals(string))
				return type;
		}
		return null;
	}

	public String getRequirementType() {
		return requirementType;
	}
}
