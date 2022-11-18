package org.brokenarrow.library.menusettings.requirements;

import org.bukkit.entity.Player;

import static org.brokenarrow.library.menusettings.MenuSettingsAddon.setPlaceholders;

public class InputOutputRequirement extends Requirement {

	private final RequirementType type;
	private final String input;
	private final String output;

	public InputOutputRequirement(RequirementType type, String input, String output) {
		this.type = type;
		this.input = input;
		this.output = output;

	}


	@Override
	boolean estimate(Player wiver) {
		String input = setPlaceholders(wiver, this.input);
		String output = setPlaceholders(wiver, this.output);
		switch (this.type) {
			case STRING_EQUALS:
				return input.equals(output);
			case STRING_CONTAINS:
				return input.contains(output);
			case STRING_EQUALS_IGNORE_CASE:
				return input.equalsIgnoreCase(output);
			case STRING_NOT_CONTAINS:
				return !input.contains(output);
			case STRING_IS_NOT_EQUALS:
				return !input.equals(output);
			case STRING_IS_NOT_EQUAL_IGNORE_CASE:
				return !input.equalsIgnoreCase(output);
			default:
				double inputDouble;
				double outputDouble;
				try {
					inputDouble = Double.parseDouble(input);
					outputDouble = Double.parseDouble(output);
				} catch (NumberFormatException exception) {
					return false;
				}
				switch (this.type) {
					case INPUT_NOT_EQUALS_OUTPUT:
						return inputDouble != outputDouble;
					case INPUT_EQUALS_OUTPUT:
						return inputDouble == outputDouble;
					case INPUT_GREATER_THAN_OUTPUT:
						return inputDouble > outputDouble;
					case INPUT_GREATER_THAN_OR_EQUALS_OUTPUT:
						return inputDouble >= outputDouble;
					case INPUT_LESS_THAN_OR_EQUALS_OUTPUT:
						return inputDouble <= outputDouble;
					case INPUT_LESS_THAN_OUTPUT:
						return inputDouble < outputDouble;
					default:
						return false;
				}
		}
	}
}
