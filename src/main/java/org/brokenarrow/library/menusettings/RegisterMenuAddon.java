package org.brokenarrow.library.menusettings;

import de.tr7zw.changeme.nbtapi.metodes.RegisterNbtAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.brokenarrow.library.menusettings.exceptions.Valid;
import org.brokenarrow.library.menusettings.hooks.economy.PriceProvider;
import org.brokenarrow.library.menusettings.hooks.economy.RegisterEconomyHook;
import org.brokenarrow.library.menusettings.hooks.permission.PermissionProvider;
import org.brokenarrow.library.menusettings.hooks.permission.RegisterPermissionHook;
import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.nashornplus.NashornPlusPlugin;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.exceptions.Valid.checkBoolean;
import static org.brokenarrow.library.menusettings.utillity.ServerVersion.setServerVersion;

public class RegisterMenuAddon {

	private static Plugin PLUGIN;
	private static MenuCache menuCache;
	private static DecimalFormat decimalFormat;
	private static RegisterMenuAddon instance;

	private static ScriptEngineFactory engineFactory;
	private static ScriptEngineManager engineManager;
	private static ScriptEngine scriptEngine;
	private final ServicesManager manager = Bukkit.getServer().getServicesManager();
	private static RegisterEconomyHook registerEconomyHook;
	private static RegisterPermissionHook registerPermissionHook;
	private static RegisterNbtAPI nbtApi;

	/**
	 * Register this api, this will load yml files and register plugin hooks.
	 * Also set shallGenerateDefultFiles to false.
	 *
	 * @param plugin      your main class.
	 * @param name        file name or folder name (if you set up 1 menu for every file).
	 * @param makeOneFile true if you use only one file.
	 */
	public RegisterMenuAddon(Plugin plugin, String name, boolean makeOneFile) {
		this(plugin, name, makeOneFile, false);
	}

	/**
	 * Register this api, this will load yml files and register plugin hooks.
	 *
	 * @param plugin                   your main class.
	 * @param name                     file name or folder name (if you set up 1 menu for every file).
	 * @param makeOneFile              true if you use only one file.
	 * @param shallGenerateDefultFiles if it shall also add files from your resources if they not exist.
	 */
	public RegisterMenuAddon(Plugin plugin, String name, boolean makeOneFile, boolean shallGenerateDefultFiles) {
		instance = this;
		PLUGIN = plugin;
		menuCache = new MenuCache(makeOneFile ? "" : name, !makeOneFile ? "" : name, shallGenerateDefultFiles);
		setServerVersion(plugin);
		nbtApi = new RegisterNbtAPI(plugin, false);
		registerNashorn();
		registerEconomyHook = new RegisterEconomyHook();
		registerPermissionHook = new RegisterPermissionHook();
	}

	public static String setPlaceholders(Player player, String string) {
		return PlaceholderAPI.setPlaceholders(player, string);
	}

	public static List<String> setPlaceholders(Player player, List<String> list) {
		return PlaceholderAPI.setPlaceholders(player, list);
	}

	public static RegisterNbtAPI getNbtApi() {
		return nbtApi;
	}

	private void registerNashorn() {
		if (Bukkit.getPluginManager().getPlugin("NashornPlus") != null) {
			engineManager = NashornPlusPlugin.getInstance().getEngineManager();
			engineFactory = NashornPlusPlugin.getInstance().getEngineFactory();
			if (engineManager != null) {
				ScriptEngine scriptEng = engineManager.getEngineByName("Nashorn");
				if (scriptEng == null) {
					System.out.println("is null scriptEng ");
					engineManager = new ScriptEngineManager(null);
					scriptEng = engineManager.getEngineByName("Nashorn");
				}
				scriptEngine = scriptEng;
			}

		} else {
			if (engineManager == null) {
				if (this.manager.isProvidedFor(ScriptEngineManager.class)) {
					RegisteredServiceProvider<ScriptEngineManager> provider = this.manager.getRegistration(ScriptEngineManager.class);
					if (provider == null) {
						getPLUGIN().getLogger().log(Level.WARNING, "ScriptEngineManager exist but registered service provider is null");
						return;
					}
					engineManager = provider.getProvider();
				} else {
					engineManager = new ScriptEngineManager();
					this.manager.register(ScriptEngineManager.class, engineManager, getPLUGIN(), ServicePriority.Highest);
				}
				ScriptEngine scriptEng = engineManager.getEngineByName("Nashorn");
				if (scriptEng == null) {
					engineManager = new ScriptEngineManager(null);
					scriptEng = engineManager.getEngineByName("Nashorn");
				}
				if (scriptEng == null) {
					System.out.println("is still null");
					try {
						Class<?> nashorn = Class.forName("org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory");
						Constructor<?> constructor = nashorn.getDeclaredConstructor();
						constructor.setAccessible(true);
						Object scriptEngineFactory = constructor.newInstance();
						if (scriptEngineFactory instanceof ScriptEngineFactory) {
							ScriptEngineFactory engineFactory = (ScriptEngineFactory) scriptEngineFactory;
							engineManager.registerEngineName("Nashorn", engineFactory);
							scriptEng = engineManager.getEngineByName("Nashorn");
						}
					} catch (ClassNotFoundException exception) {
						getLogger(Level.WARNING, "can´t find Nashorn engine, javascript will not work.");
						return;
					} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				scriptEngine = scriptEng;
			}
		}
	}

	@NotNull
	public RegisterEconomyHook getRegisterEconomyHook() {
		return registerEconomyHook;
	}

	public static PriceProvider getEconomyProvider() {
		checkBoolean(registerEconomyHook.getProvider() == null, "economyProvider is null, so you can't add or remove money from players");
		return registerEconomyHook.getProvider();
	}

	@NotNull
	public RegisterPermissionHook getRegisterPermissionHook() {
		return registerPermissionHook;
	}

	public static PermissionProvider getPermissionProvider() {
		checkBoolean(registerEconomyHook.getProvider() == null, "permissionProvider is null, so you can't set or remove permissions from players");
		return registerPermissionHook.getProvider();
	}

	@Nullable
	public static ScriptEngineFactory getEngineFactory() {
		return engineFactory;
	}

	@Nullable
	public static ScriptEngineManager getEngineManager() {
		return engineManager;
	}

	@Nullable
	public static ScriptEngine getScriptEngine() {
		return scriptEngine;
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

	public static void getLogger(Level level, String messsage) {
		PLUGIN.getLogger().log(level, messsage);
	}

	protected static RegisterMenuAddon getInstance() {
		Valid.checkNotNull(instance + "You have not instantiate this class, you need to do new RegisterMenuAddon()");
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
