package org.brokenarrow.library.menusettings.tasks;

import org.brokenarrow.library.menusettings.clickactions.CommandActionType;
import org.brokenarrow.library.menusettings.utillity.SoundUtillity;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.logging.Level;

import static org.broken.lib.rbg.TextTranslator.toSpigotFormat;
import static org.brokenarrow.library.menusettings.RegisterMenuAddon.*;
import static org.brokenarrow.library.menusettings.clickactions.CommandActionType.TAKE_EXP;
import static org.brokenarrow.library.menusettings.utillity.ExperienceUtillity.setExp;


public class ClickActionTask {

	private final CommandActionType actionType;
	private String executable;
	private String delay;
	private String chance;


	public ClickActionTask(CommandActionType actionType) {
		this.actionType = actionType;
	}

	public void task(Player player) {
		if (player == null) return;
		String executable = setPlaceholders(player, this.getExecutable());

		switch (this.actionType) {
			case CONSOLE:
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executable);
				break;
			case PLAYER:
				player.chat("/" + executable);
				break;
			case PLACEHOLDER:
				setPlaceholders(player, executable);
			case PLAYER_COMMAND_EVENT:
				Bukkit.getPluginManager().callEvent(new PlayerCommandPreprocessEvent(player, "/" + executable));
				break;
			case MESSAGE:
				player.sendMessage(toSpigotFormat(executable));
				break;
			case BROADCAST:
				Bukkit.broadcastMessage(toSpigotFormat(executable));
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
				SoundUtillity soundU = new SoundUtillity(executable);
				Sound sound = soundU.getSound();
				float volume = soundU.getVolume();
				float pitch = soundU.getPitch();

				if (sound == null) return;

				if (this.actionType == CommandActionType.BROADCAST_SOUND) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), sound, volume, pitch);
					}
				} else if (this.actionType == CommandActionType.BROADCAST_WORLD_SOUND) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (player.getWorld().getName().equals(p.getWorld().getName()))
							player.getWorld().playSound(p.getLocation(), sound, volume, pitch);
					}
				} else {
					player.playSound(player.getLocation(), sound, volume, pitch);
				}

				break;
			case TAKE_MONEY:
				getEconomyProvider().withdrawAmont(player.getUniqueId(), Double.parseDouble(executable));
				break;
			case GIVE_MONEY:
				getEconomyProvider().depositAmont(player.getUniqueId(), Double.parseDouble(executable));
				break;
			case TAKE_EXP:
			case GIVE_EXP:
				if (this.actionType == TAKE_EXP)
					executable = "-" + executable;

				setExp(player, executable);
				break;
			case TAKE_PERM:
				getPermissionProvider().setPermission(player, executable);
				break;
			case GIVE_PERM:
				getPermissionProvider().removePermission(player, executable);
				break;
		}
	}

	public long formatDelay(Player wiver) {
		if (this.getDelay() == null || this.getDelay().isEmpty()) return -1;
		String delayTranslated = setPlaceholders(wiver, this.getDelay());

		try {
			return Long.parseLong(delayTranslated);
		} catch (NumberFormatException ignored) {
			return -1;
		}
	}

	public boolean checkChance(Player wiver) {
		if (this.getChance() == null) {
			return true;
		} else {
			String chanceTranslated = setPlaceholders(wiver, this.getChance());
			double chance;
			try {
				chance = Double.parseDouble(chanceTranslated);
			} catch (NumberFormatException ignored) {
				getLogger(Level.WARNING, "Your set chance, is not valid format, your input " + chanceTranslated + ". use numbers only. " +
						"This will now return false and the command/action will not be executed");
				return false;
			}
			if (chance == -1) {
				return true;
			}
			if (chance >= 100.0) {
				return true;
			} else {
				double random = Double.parseDouble(formatDubbleDecimal().format(getRandomUntility().randomDouble()));
				return random <= chance;
			}
		}
	}

	public String getExecutable() {
		return executable;
	}

	public void setExecutable(String executable) {
		this.executable = executable;
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
