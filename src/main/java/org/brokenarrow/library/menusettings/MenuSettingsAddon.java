package org.brokenarrow.library.menusettings;

import de.tr7zw.changeme.nbtapi.metodes.RegisterNbtAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.brokenarrow.library.menusettings.hooks.economy.RegisterEconomyHook;
import org.brokenarrow.library.menusettings.hooks.permission.RegisterPermissionHook;
import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.brokenarrow.library.menusettings.utillity.RandomUntility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.utillity.ServerVersion.setServerVersion;

public final class MenuSettingsAddon extends JavaPlugin {

	private static Plugin PLUGIN;
	private static boolean isPlaceholderAPIRegisted;
	private static MenuDataRegister menuDataRegister;

	@Override
	public void onLoad() {
		PLUGIN = this;
		menuDataRegister = new MenuDataRegister.Builder().setDecimalFormat(formatDubbleDecimal())
				.setRandomUntility(new RandomUntility())
				.setRegisterEconomyHook(new RegisterEconomyHook())
				.setRegisterPermissionHook(new RegisterPermissionHook())
				.setNbtApi(new RegisterNbtAPI(this, false)).build();
		setServerVersion(PLUGIN);
		this.getLogger().log(Level.INFO, "Has set plugin and created needed classes.");
	}

	@Override
	public void onEnable() {
		isPlaceholderAPIRegisted = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
		if (!isPlaceholderAPIRegisted)
			PLUGIN.getLogger().log(Level.WARNING, "You has not added PlaceholderAPI in plugins folder. The api will not work as it should (no placeholders will be translated)");
		this.getLogger().log(Level.INFO, "Has check if PlaceholderAPI is enable and exist.");
		this.getLogger().log(Level.INFO, "Has finish loading. To use the api, dont forget register in onEnable method in your plugin.");
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}

	/**
	 * Register this api, this will load yml files and register plugin hooks.
	 * Also set shallGenerateDefultFiles to false.
	 *
	 * @param plugin      your main class.
	 * @param name        file name or folder name (if you set up 1 menu for every file).
	 * @param makeOneFile true if you use only one file.
	 * @return the MenuDataRegister some contains all methods needed.
	 */
	public MenuDataRegister registerPlugin(Plugin plugin, String name, boolean makeOneFile) {
		return registerPlugin(plugin, null, name, makeOneFile, false);
	}

	/**
	 * Register this api, this will load yml files and register plugin hooks.
	 *
	 * @param plugin                   your main class.
	 * @param name                     file name or folder name (if you set up 1 menu for every file).
	 * @param makeOneFile              true if you use only one file.
	 * @param shallGenerateDefultFiles if it shall also add files from your resources if they not exist.
	 * @return the MenuDataRegister some contains all methods needed.
	 */
	public MenuDataRegister registerPlugin(Plugin plugin, String name, boolean makeOneFile, boolean shallGenerateDefultFiles) {
		return registerPlugin(plugin, null, name, makeOneFile, shallGenerateDefultFiles);
	}

	/**
	 * Register this api, this will load yml files and register plugin hooks.
	 *
	 * @param plugin                   your main class.
	 * @param audiences                paste your own instance of BukkitAudiences (if you not want this api override your own registerd instance of BukkitAudiences).
	 * @param name                     file name or folder name (if you set up 1 menu for every file).
	 * @param makeOneFile              true if you use only one file.
	 * @param shallGenerateDefultFiles if it shall also add files from your resources if they not exist.
	 * @return the MenuDataRegister some contains all methods needed.
	 */
	public MenuDataRegister registerPlugin(Plugin plugin, BukkitAudiences audiences, String name, boolean makeOneFile, boolean shallGenerateDefultFiles) {
		MenuCache menuCache = new MenuCache(plugin, makeOneFile ? "" : name, makeOneFile, shallGenerateDefultFiles);
		menuDataRegister.addMenuCache(plugin, menuCache);
		menuDataRegister.setAudiences((audiences != null ? audiences : net.kyori.adventure.platform.bukkit.BukkitAudiences.create(this)));

		return menuDataRegister;
	}

	public static String setPlaceholders(Player player, String string) {
		if (isPlaceholderAPIRegisted)
			return PlaceholderAPI.setPlaceholders(player, string);
		return string;
	}

	public static List<String> setPlaceholders(Player player, List<String> list) {
		if (isPlaceholderAPIRegisted)
			return PlaceholderAPI.setPlaceholders(player, list);
		return list;
	}

	@NotNull
	public static MenuSettingsAddon getPLUGIN() {
		return (MenuSettingsAddon) PLUGIN;
	}

	public static void getLogger(Level level, String messsage) {
		PLUGIN.getLogger().log(level, messsage);
	}

	private static DecimalFormat formatDubbleDecimal() {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

		return decimalFormat;
	}

}
