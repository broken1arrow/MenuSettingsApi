package org.brokenarrow.library.menusettings;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.broken.arrow.library.nbt.RegisterNbtAPI;
import org.brokenarrow.library.menusettings.builders.MenuRegistrationConfig;
import org.brokenarrow.library.menusettings.hooks.economy.RegisterEconomyHook;
import org.brokenarrow.library.menusettings.hooks.permission.RegisterPermissionHook;
import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.brokenarrow.library.menusettings.settings.TemplatesCache;
import org.brokenarrow.library.menusettings.utillity.RandomUntility;
import org.brokenarrow.library.menusettings.utillity.menu.fallback.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    }

    @Override
    public void onEnable() {
        MenuSettingsListener menuSettingsListener = new MenuSettingsListener(this);
        menuDataRegister = new MenuDataRegister.Builder()
                .setDecimalFormat(formatDoubleDecimal())
                .setRandomUntility(new RandomUntility())
                .setServiceRegisterEvent(menuSettingsListener)
                .setRegisterEconomyHook(new RegisterEconomyHook())
                .setRegisterPermissionHook(new RegisterPermissionHook())
                .setNbtApi(new RegisterNbtAPI(this, false))
                .build();
        setServerVersion(PLUGIN);
        this.getLogger().log(Level.INFO, "Has set plugin and created needed classes.");
        isPlaceholderAPIRegisted = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (!isPlaceholderAPIRegisted)
            PLUGIN.getLogger().log(Level.WARNING, "You has not added PlaceholderAPI in plugins folder. The api will not work as it should (no placeholders will be translated)");
        this.getLogger().log(Level.INFO, "Has check if PlaceholderAPI is enable and exist.");
        this.getLogger().log(Level.INFO, "Has finish loading. To use the api, dont forget register in onEnable method in your plugin.");
        menuDataRegister.setAudiences((net.kyori.adventure.platform.bukkit.BukkitAudiences.create(this)));
        Bukkit.getPluginManager().registerEvents(menuSettingsListener, this);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    /**
     * Registers this API and loads YML files, while registering plugin hooks.
     *
     * <p>Automatically sets {@code shallGenerateDefaultFiles} to {@code false}.</p>
     *
     * @param plugin      your main plugin class
     * @param name        the file name or folder name (if you set up one menu per file)
     * @param makeOneFile {@code true} if only a single file is used
     * @return the {@link MenuDataRegister}, which contains all necessary methods for further usage
     */
    public MenuDataRegister registerPlugin(@NotNull final Plugin plugin, @NotNull final String name, boolean makeOneFile) {
        return registerPlugin(plugin, null, name, makeOneFile, false);
    }

    /**
     * Registers this API and loads YML files, while registering plugin hooks.
     *
     * @param plugin                   your main plugin class
     * @param name                     the file name or folder name (if you set up one menu per file)
     * @param makeOneFile              {@code true} if only a single file is used
     * @param shallGenerateDefaultFiles {@code true} to generate default files from resources if they do not exist
     * @return the {@link MenuDataRegister}, which contains all necessary methods for further usage
     */
    public MenuDataRegister registerPlugin(@NotNull final Plugin plugin, @NotNull final String name, boolean makeOneFile, boolean shallGenerateDefaultFiles) {
        return registerPlugin(plugin, null, name, makeOneFile, shallGenerateDefaultFiles);
    }

     /**
     * Registers this API and loads YML files, while registering plugin hooks.
     *
     * @param plugin                   your main plugin class
     * @param audiences                your instance of {@link BukkitAudiences}, if you do not want this API to override an existing one
     * @param name                     the file name or folder name (if you set up one menu per file)
     * @param makeOneFile              {@code true} if only a single file is used
     * @param shallGenerateDefaultFiles {@code true} to generate default files from resources if they do not exist
     * @return the {@link MenuDataRegister}, which contains all necessary methods for further usage
     */
    public MenuDataRegister registerPlugin(@NotNull final Plugin plugin, @Nullable final BukkitAudiences audiences, @NotNull final String name, boolean makeOneFile, boolean shallGenerateDefaultFiles) {
        final MenuRegistrationConfig config = new MenuRegistrationConfig();
        config.setOneFile(makeOneFile);
        config.setGenerateDefaultFiles(shallGenerateDefaultFiles);
        MenuCache menuCache = new MenuCache(plugin, name, config);
        TemplatesCache templatesCache = new TemplatesCache(plugin);
        menuDataRegister.addMenuCache(plugin, menuCache, templatesCache);
        if (audiences != null)
            menuDataRegister.setAudiences(audiences);
        return menuDataRegister;
    }

    /**
     * Registers this API and loads YML files, while registering plugin hooks.
     *
     * @param plugin         your main plugin class
     * @param name           the file name or folder name (if you set up one menu per file)
     * @param configCallBack a callback to configure {@link MenuRegistrationConfig}, including optional {@link BukkitAudiences}
     * @return the {@link MenuDataRegister}, which contains all necessary methods for further usage
     */
    public MenuDataRegister registerPlugin(@NotNull final Plugin plugin, @NotNull final String name, @NotNull final Consumer<MenuRegistrationConfig> configCallBack) {
        final MenuRegistrationConfig config = new MenuRegistrationConfig();
        configCallBack.accept(config);

        MenuCache menuCache = new MenuCache(plugin, name, config);
        TemplatesCache templatesCache = new TemplatesCache(plugin);
        menuDataRegister.addMenuCache(plugin, menuCache, templatesCache);
        if (config.getAudiences() != null)
            menuDataRegister.setAudiences(config.getAudiences());
        return menuDataRegister;
    }

    /**
     * Reloads the menu configuration files for the given plugin.
     *
     * @param plugin your main plugin class
     * @return the {@link MenuDataRegister}, which contains all necessary methods for further usage
     */
    public MenuDataRegister reloadPluginConfigs(@NotNull final Plugin plugin) {
        menuDataRegister.reloadConfigs(plugin);
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

    public static void getLogger(Level level, String message) {
        PLUGIN.getLogger().log(level, message);
    }

    public static void getLogger(Throwable ex, String message) {
        PLUGIN.getLogger().log(Level.WARNING, ex, () -> message);
    }

    private static DecimalFormat formatDoubleDecimal() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));

        return decimalFormat;
    }

}
