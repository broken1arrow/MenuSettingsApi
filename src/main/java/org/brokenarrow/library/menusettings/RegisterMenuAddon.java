package org.brokenarrow.library.menusettings;

import me.clip.placeholderapi.PlaceholderAPI;
import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.mineacademy.nashornplus.NashornPlusPlugin;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class RegisterMenuAddon {

	private static Plugin PLUGIN;
	private static MenuCache menuCache;
	private static DecimalFormat decimalFormat;
	private static final RegisterMenuAddon instance = new RegisterMenuAddon();
	private static int accesAmount;
	private static ScriptEngineFactory engineFactory;
	private static ScriptEngineManager engineManager;

	private RegisterMenuAddon() {
		accesAmount++;
		if (accesAmount > 0)
			System.out.println("acces amoiunt " + accesAmount);
		registerNashorn();
	}

	public RegisterMenuAddon(Plugin plugin, String name, boolean makeOneFile, boolean shallGenerateDefultFiles) {
		if (makeOneFile) {
			PLUGIN = plugin;
			menuCache = new MenuCache("", name, shallGenerateDefultFiles);
		} else {
			PLUGIN = plugin;
			menuCache = new MenuCache(name, "", shallGenerateDefultFiles);
		}
	}

	public RegisterMenuAddon(Plugin plugin, String name, boolean makeOneFile) {
		if (makeOneFile) {
			PLUGIN = plugin;
			menuCache = new MenuCache("", name, false);
		} else {
			PLUGIN = plugin;
			menuCache = new MenuCache(name, "", false);
		}
	}

	public RegisterMenuAddon(Plugin plugin, String folderName, String filename, boolean shallGenerateDefultFiles) {
		PLUGIN = plugin;
		menuCache = new MenuCache(folderName, filename, shallGenerateDefultFiles);
	}

	public static String setPlaceholders(Player player, String string) {
		return PlaceholderAPI.setPlaceholders(player, string);
	}

	private void registerNashorn() {
		if (Bukkit.getPluginManager().getPlugin("NashornPlus") != null) {
			engineManager = NashornPlusPlugin.getInstance().getEngineManager();
			engineFactory = NashornPlusPlugin.getInstance().getEngineFactory();
		}

	}

	public static ScriptEngineFactory getEngineFactory() {
		return engineFactory;
	}

	public static ScriptEngineManager getEngineManager() {
		return engineManager;
	}

	public static DecimalFormat formatDubbleDecimal() {
		if (decimalFormat == null) {
			decimalFormat = new DecimalFormat("#.##");
			decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
		}
		return decimalFormat;
	}

	public static Plugin getPLUGIN() {
		return PLUGIN;
	}

	static RegisterMenuAddon getInstance() {
		return instance;
	}

	/**
	 * Get the menu cache, but not forget to make an instance of this class first.
	 *
	 * @return the menu cache instance.
	 */
	public MenuCache getMenuCache() {
		return menuCache;
	}

}
