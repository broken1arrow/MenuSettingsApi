package org.brokenarrow.library.menusettings.tasks;

import com.google.common.base.Enums;
import me.clip.placeholderapi.PlaceholderAPI;
import org.brokenarrow.library.menusettings.clickactions.CommandActionType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

import static org.broken.lib.rbg.TextTranslator.toSpigotFormat;
import static org.brokenarrow.library.menusettings.RegisterMenuAddon.formatDubbleDecimal;
import static org.brokenarrow.library.menusettings.RegisterMenuAddon.setPlaceholders;


public class ClickActionTask {


	private final CommandActionType actionType;
	private final String execute;
	private String delay;
	private String chance;


	public ClickActionTask(CommandActionType actionType, String execute) {
		this.actionType = actionType;
		this.execute = execute;
	}

	public void task(Player player) {
		if (player == null) return;
		String executable = PlaceholderAPI.setPlaceholders(player, this.execute);

		switch (this.actionType) {
			case CONSOLE:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executable);
				break;
			case PLAYER:
				player.chat("/" + executable);
				break;
			case PLAYER_COMMAND_EVENT:
				break;
			case MESSAGE:
				player.sendMessage(toSpigotFormat(executable));
				break;
			case BROADCAST:
				break;
			case CHAT:
				player.chat(executable);
				break;
			case CLOSE:
				player.closeInventory();
				break;
			case REFRESH:
				player.updateInventory();
				break;
			case BROADCAST_SOUND:
			case BROADCAST_WORLD_SOUND:
			case PLAY_SOUND:
				Sound sound = null;
				float volume = 1.0F;
				float pitch = 1.0F;

				sound = Enums.getIfPresent(Sound.class, executable.toUpperCase()).orNull();

				if (sound == null) return;

				if (this.actionType == CommandActionType.BROADCAST_SOUND) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), sound, volume, pitch);
					}
				} else if (this.actionType == CommandActionType.BROADCAST_WORLD_SOUND) {
					player.getWorld().playSound(player.getLocation(), sound, volume, pitch);
				} else {
					player.playSound(player.getLocation(), sound, volume, pitch);
				}

				break;
			case TAKE_MONEY:
				break;
			case GIVE_MONEY:
				break;
			case TAKE_EXP:
				break;
			case GIVE_EXP:
				break;
			case TAKE_PERM:
				break;
			case GIVE_PERM:
				break;
		}
	}

	public long formatDelay(Player wiver) {
		if (this.delay == null || this.delay.isEmpty()) return -1;
		String delayTranslated = setPlaceholders(wiver, this.delay);

		try {
			return Long.parseLong(delayTranslated);
		} catch (NumberFormatException ignored) {
			return -1;
		}
	}

	public boolean checkChance(Player wiver) {
		if (this.chance == null) {
			return true;
		} else {
			String chanceTranslated = setPlaceholders(wiver, this.chance);
			double chance = 0.0D;
			try {
				chance = Double.parseDouble(chanceTranslated);
			} catch (NumberFormatException ignored) {
			}
			if (chance == -1) {
				return true;
			}
			if (chance >= 100.0D) {
				return true;
			} else {
				double random = ThreadLocalRandom.current().nextDouble() * 100.0D;
				random = Double.parseDouble(formatDubbleDecimal().format(random));
				return random <= chance;
			}
		}
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getChance() {
		return chance;
	}

	public void setChance(String chance) {
		this.chance = chance;
	}
}
