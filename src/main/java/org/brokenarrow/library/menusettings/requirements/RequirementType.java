package org.brokenarrow.library.menusettings.requirements;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum RequirementType {

	HAS_PERMISSION(set("has permission")),
	DO_NOT_HAVE_PERMISSION(set("!has permission")),
	HAS_MONEY(set("has money")),
	DO_NOT_HAVE_MONEY(set("!has money")),
	STRING_EQUALS(set("string equals")),
	STRING_CONTAINS(set("string contains")),
	STRING_EQUALS_IGNORE_CASE(set("string equals ignorecase")),
	STRING_NOT_CONTAINS(set("!string contains")),
	STRING_IS_NOT_EQUALS(set("!string equals")),
	STRING_IS_NOT_EQUAL_IGNORE_CASE(set("!string equals ignorecase")),
	JAVASCRIPT(set("javascript", "js")),
	INT_EQUALS_OUTPUT(set("==")),
	INT_GREATER_THAN_OUTPUT(set(">")),
	INT_GREATER_THAN_OR_EQUALS_OUTPUT(set(">=")),
	INT_LESS_THAN_OR_EQUALS_OUTPUT(set("<=")),
	INT_LESS_THAN_OUTPUT(set("<")),

	CUSTOM(set("custom"));

	private final Set<String> identifier;

	RequirementType(Set<String> identifier) {
		this.identifier = identifier;
	}

	public static RequirementType getType(String string) {
		RequirementType[] requirementTypes = values();

		for (RequirementType type : requirementTypes) {
			for (String identifier : type.getIdentifier()) {
				if (identifier.equals(string))
					return type;
			}
		}
		return null;
	}

	public Set<String> getIdentifier() {
		return identifier;
	}

	private static Set<String> set(String... strings) {
		return new HashSet<>(Arrays.asList(strings));
	}

}
