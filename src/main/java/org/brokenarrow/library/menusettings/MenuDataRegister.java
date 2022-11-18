package org.brokenarrow.library.menusettings;

import de.tr7zw.changeme.nbtapi.metodes.RegisterNbtAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.brokenarrow.library.menusettings.hooks.economy.PriceProvider;
import org.brokenarrow.library.menusettings.hooks.economy.RegisterEconomyHook;
import org.brokenarrow.library.menusettings.hooks.permission.PermissionProvider;
import org.brokenarrow.library.menusettings.hooks.permission.RegisterPermissionHook;
import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.brokenarrow.library.menusettings.utillity.RandomUntility;
import org.bukkit.Bukkit;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.MenuSettingsAddon.getLogger;
import static org.brokenarrow.library.menusettings.MenuSettingsAddon.getPLUGIN;
import static org.brokenarrow.library.menusettings.exceptions.Valid.checkNotNull;

public final class MenuDataRegister {

	private final Map<Plugin, MenuCache> menuCache = new HashMap<>();
	private final DecimalFormat decimalFormat;
	private final RegisterEconomyHook registerEconomyHook;
	private final RegisterPermissionHook registerPermissionHook;
	private final RegisterNbtAPI nbtApi;
	private final RandomUntility randomUntility;
	private net.kyori.adventure.platform.bukkit.BukkitAudiences audiences;
	private static MenuDataRegister instance;
	private ScriptEngineFactory engineFactory;
	private ScriptEngineManager engineManager;
	private ScriptEngine scriptEngine;
	private final ServicesManager manager = Bukkit.getServer().getServicesManager();

	private MenuDataRegister(Builder builder) {
		this.decimalFormat = builder.decimalFormat;
		this.engineManager = builder.engineManager;
		this.scriptEngine = builder.scriptEngine;
		this.registerEconomyHook = builder.registerEconomyHook;
		this.registerPermissionHook = builder.registerPermissionHook;
		this.nbtApi = builder.nbtApi;
		this.randomUntility = builder.randomUntility;
		registerNashorn();
		instance = this;
	}

	private Map<Plugin, MenuCache> getMenuCache() {
		return menuCache;
	}

	public void addMenuCache(final Plugin plugin, MenuCache menuCache) {
		getMenuCache().put(plugin, menuCache);
	}

	public void removeMenuCache(final Plugin plugin) {
		getMenuCache().remove(plugin);
	}

	public void clearMenuCache(final BukkitAudiences audiences) {
		getMenuCache().clear();
	}

	public void setAudiences(final BukkitAudiences audiences) {
		this.audiences = audiences;
	}

	public static MenuDataRegister getInstance() {
		return instance;
	}

	/**
	 * Get the menu cache.
	 *
	 * @return the menu cache instance or null if the plugin are not registed.
	 */
	@Nullable
	public MenuCache getMenuCache(@NotNull Plugin plugin) {
		return menuCache.get(plugin);
	}

	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}

	public ScriptEngineManager getEngineManager() {
		return engineManager;
	}

	public ScriptEngine getScriptEngine() {
		return scriptEngine;
	}

	@NotNull
	public PriceProvider getEconomyProvider() {
		PriceProvider economyHook = getRegisterEconomyHook().getProvider();
		checkNotNull(economyHook, "economyProvider is null, so you can't add or remove money from players");
		return economyHook;
	}

	@NotNull
	public PermissionProvider getPermissionProvider() {
		PermissionProvider permissionHook = getRegisterPermissionHook().getProvider();
		checkNotNull(permissionHook, "permissionProvider is null, so you can't set or remove permissions from players");
		return permissionHook;
	}

	public RegisterEconomyHook getRegisterEconomyHook() {
		return registerEconomyHook;
	}

	public RegisterPermissionHook getRegisterPermissionHook() {
		return registerPermissionHook;
	}

	public RegisterNbtAPI getNbtApi() {
		return nbtApi;
	}

	public RandomUntility getRandomUntility() {
		return randomUntility;
	}

	public BukkitAudiences getAudiences() {
		return audiences;
	}

	public static class Builder {
		private Map<Plugin, MenuCache> menuCache = new HashMap<>();
		private DecimalFormat decimalFormat;
		private ScriptEngineManager engineManager;
		private ScriptEngine scriptEngine;
		private RegisterEconomyHook registerEconomyHook;
		private RegisterPermissionHook registerPermissionHook;
		private RegisterNbtAPI nbtApi;
		private RandomUntility randomUntility;
		private net.kyori.adventure.platform.bukkit.BukkitAudiences audiences;

		public Builder setMenuCache(Plugin plugin, MenuCache menuCache) {
			this.menuCache.put(plugin, menuCache);
			return this;
		}

		public Builder setMenuCache(Map<Plugin, MenuCache> menuCache) {
			this.menuCache = menuCache;
			return this;
		}

		public Builder setDecimalFormat(DecimalFormat decimalFormat) {
			this.decimalFormat = decimalFormat;
			return this;
		}

		public Builder setEngineManager(ScriptEngineManager engineManager) {
			this.engineManager = engineManager;
			return this;
		}

		public Builder setScriptEngine(ScriptEngine scriptEngine) {
			this.scriptEngine = scriptEngine;
			return this;
		}

		public Builder setRegisterEconomyHook(RegisterEconomyHook registerEconomyHook) {
			this.registerEconomyHook = registerEconomyHook;
			return this;
		}

		public Builder setRegisterPermissionHook(RegisterPermissionHook registerPermissionHook) {
			this.registerPermissionHook = registerPermissionHook;
			return this;
		}

		public Builder setNbtApi(RegisterNbtAPI nbtApi) {
			this.nbtApi = nbtApi;
			return this;
		}

		public Builder setRandomUntility(RandomUntility randomUntility) {
			this.randomUntility = randomUntility;
			return this;
		}

		public Builder setAudiences(BukkitAudiences audiences) {
			this.audiences = audiences;
			return this;
		}

		public MenuDataRegister build() {
			return new MenuDataRegister(this);
		}
	}

	private void registerNashorn() {
		if (Bukkit.getPluginManager().getPlugin("NashornPlus") != null) {
			engineManager = NashornPlusPlugin.getInstance().getEngineManager();
			engineFactory = NashornPlusPlugin.getInstance().getEngineFactory();
			if (engineManager != null) {
				ScriptEngine scriptEng = engineManager.getEngineByName("Nashorn");
				if (scriptEng == null) {
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
}
