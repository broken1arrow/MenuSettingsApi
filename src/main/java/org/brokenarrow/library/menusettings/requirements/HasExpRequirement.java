package org.brokenarrow.library.menusettings.requirements;

import org.bukkit.entity.Player;

import static org.brokenarrow.library.menusettings.MenuSettingsAddon.setPlaceholders;
import static org.brokenarrow.library.menusettings.utillity.ExperienceUtillity.getPlayerExp;

public class HasExpRequirement extends Requirement {

	private final String experiencesAmount;
	private final boolean inverted;
	private final boolean checkLevel;

	public HasExpRequirement(String experiencesAmount, boolean checkLevel, boolean inverted) {
		this.experiencesAmount = experiencesAmount;
		this.inverted = inverted;
		this.checkLevel = checkLevel;
	}

	@Override
	boolean estimate(Player wiver) {
		int playerExperiens = this.checkLevel ? wiver.getLevel() : getPlayerExp(wiver);
		int exp = 0;
		try {
			exp = Integer.parseInt(setPlaceholders(wiver, this.experiencesAmount));
		} catch (NumberFormatException exception) {
			exception.printStackTrace();
		}
		return inverted == (playerExperiens < exp);
	}
}
