package org.brokenarrow.library.menusettings.settings;

import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.builders.Template;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.brokenarrow.library.menusettings.utillity.CreateItemStack.formatColors;

public class TemplatesCache extends SimpleYamlHelper {

    Map<String, Template> cache = new HashMap<>();

    public TemplatesCache(Plugin plugin) {
        super(plugin, "templates.yml", true);
        File file = new File(plugin.getDataFolder(), "templates.yml");
        if (!file.exists()) {
            createMissingFile(file);
        }
    }

    public Map<String, Template> getCache() {
        return cache;
    }

    @Nullable
    public Template getTemplet(final String key) {
        return cache.get(key);
    }

    @Override
    protected void saveDataToFile(File file) {

    }

    @Override
    protected void loadSettingsFromYaml(File file) {
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection configuration = yamlConfiguration.getConfigurationSection("items");
        if (configuration == null) return;

        for (String key : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(key);
            if (section == null) continue;

            Template template = new Template();
            template.setMaterial(section.getString("material"));
            template.setAmount(section.getInt("amount", 1));
            template.setDisplayName(formatColors(section.getString("display_name", "")));
            template.setLore(formatColors(section.getStringList("lore")));
            template.setTexture(section.getString("texture"));

            cache.put(key.toLowerCase(), template);
        }
    }

    private void createMissingFile(File file) {
        try {
            InputStream inputStream = MenuSettingsAddon.getPLUGIN().getResource("templates.yml");
            if (inputStream == null) return;

            OutputStream out = Files.newOutputStream(file.toPath());
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
