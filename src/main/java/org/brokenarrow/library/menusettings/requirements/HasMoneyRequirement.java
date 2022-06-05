package org.brokenarrow.library.menusettings.requirements;

import org.bukkit.entity.Player;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.getEconomyProvider;
import static org.brokenarrow.library.menusettings.RegisterMenuAddon.setPlaceholders;

public class HasMoneyRequirement extends Requirement {

	private final boolean invert;
	private final String amount;

	public HasMoneyRequirement(boolean invert, String amount) {
		this.invert = invert;
		this.amount = amount;
	}

	@Override
	boolean estimate(Player wiver) {
		try {
			return !invert == getEconomyProvider().hasAmount(wiver.getUniqueId(), Long.parseLong(setPlaceholders(wiver, this.amount)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}
}
