package org.brokenarrow.library.menusettings.utillity;

import org.bukkit.entity.Player;

import java.util.Locale;

public class ExperiensUtillity {


	public static void setExp(Player player, String amount) {
		amount = amount.toLowerCase(Locale.ROOT);
		long exp = 0;
		boolean containsLevel = amount.contains("l");
		try {
			if (containsLevel)
				exp = Long.parseLong(amount.replace("l", ""));
			else
				exp = Long.parseLong(amount);
		} catch (NumberFormatException exception) {
			exception.printStackTrace();
		}
		if (exp <= 0) return;

		if (containsLevel)
			changePlayerLevel(player, (int) exp);
		else
			changePlayerExp(player, (int) exp);


	}

	// Calculate amount of EXP needed to level up
	public static int getExpToLevelUp(int level) {
		if (level <= 15) {
			return 2 * level + 7;
		} else if (level <= 30) {
			return 5 * level - 38;
		} else {
			return 9 * level - 158;
		}
	}

	// Calculate total experience up to a level
	public static int getExpAtLevel(int level) {
		if (level <= 16) {
			return (int) (Math.pow(level, 2) + 6 * level);
		} else if (level <= 31) {
			return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360.0);
		} else {
			return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220.0);
		}
	}

	// Calculate player's current EXP amount
	public static int getPlayerExp(Player player) {
		int exp = 0;
		int level = player.getLevel();

		// Get the amount of XP in past levels
		exp += getExpAtLevel(level);

		// Get amount of XP towards next level
		exp += Math.round(getExpToLevelUp(level) * player.getExp());

		return exp;
	}

	// Give or take EXP
	public static int changePlayerExp(Player player, int exp) {
		// Get player's current exp
		int currentExp = getPlayerExp(player);

		// Reset player's current exp to 0
		player.setExp(0);
		player.setLevel(0);

		// Give the player their exp back, with the difference
		int newExp = currentExp + exp;
		player.giveExp(newExp);

		// Return the player's new exp amount
		return newExp;
	}

	// Give or take Level
	public static int changePlayerLevel(Player player, int level) {
		// Get player's current exp
		int currentLevel = player.getLevel();

		// Reset player's current level to 0
		player.setLevel(0);

		// Give the player their exp back, with the difference
		int newExp = currentLevel + level;
		player.giveExp(newExp);

		// Return the player's new exp amount
		return newExp;
	}
}
