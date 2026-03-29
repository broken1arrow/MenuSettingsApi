package org.brokenarrow.library.menusettings.tasks;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.brokenarrow.library.menusettings.MenuDataRegister;
import org.brokenarrow.library.menusettings.MenuSession;
import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.builders.MenuContext;
import org.brokenarrow.library.menusettings.builders.MenuSettings;
import org.brokenarrow.library.menusettings.builders.Template;
import org.brokenarrow.library.menusettings.clickactions.CommandActionType;
import org.brokenarrow.library.menusettings.settings.MenuCache;
import org.brokenarrow.library.menusettings.settings.TemplatesCache;
import org.brokenarrow.library.menusettings.utillity.RequirementCheck;
import org.brokenarrow.library.menusettings.utillity.menu.fallback.FallBackGUI;
import org.brokenarrow.library.menusettings.utillity.MenuAction;
import org.brokenarrow.library.menusettings.utillity.MenuActionHandler;
import org.brokenarrow.library.menusettings.utillity.SkullCreator;
import org.brokenarrow.library.menusettings.utillity.SoundUtillity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.broken.lib.rbg.TextTranslator.toSpigotFormat;
import static org.brokenarrow.library.menusettings.MenuSettingsAddon.getLogger;
import static org.brokenarrow.library.menusettings.MenuSettingsAddon.setPlaceholders;
import static org.brokenarrow.library.menusettings.clickactions.CommandActionType.*;
import static org.brokenarrow.library.menusettings.utillity.CreateItemStack.formatColors;
import static org.brokenarrow.library.menusettings.utillity.ExperienceUtillity.setExp;
import static org.brokenarrow.library.menusettings.utillity.RunTimedTask.runTaskLater;


public class ClickActionTask {
    Logger logger = MenuSettingsAddon.getPLUGIN().getLogger();
    private final Plugin plugin;
    private final CommandActionType actionType;
    private final MenuContext menuContext;
    private final MenuActionHandler openCloseAction;
    private String executable;
    private String delay;
    private String chance;
    private final MenuDataRegister menuDataRegister = MenuDataRegister.getInstance();

    public ClickActionTask(@NotNull final Plugin plugin, @NotNull final CommandActionType actionType, @Nullable final MenuActionHandler openCloseAction) {
        this.plugin = plugin;
        this.actionType = actionType;
        this.menuContext = menuDataRegister.getMenuContext(plugin);
        this.openCloseAction = openCloseAction;
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
            case MINI_MESSAGE:
                menuDataRegister.getAudiences().player(player).sendMessage(MiniMessage.miniMessage().deserialize(executable));
                break;
            case MINI_BROADCAST:
                menuDataRegister.getAudiences().all().sendMessage(MiniMessage.miniMessage().deserialize(executable));
                break;
            case MINI_BOSSBAR:
                BossBar bossBar = BossBar.bossBar(MiniMessage.miniMessage().deserialize(executable), 0, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
                menuDataRegister.getAudiences().player(player).showBossBar(bossBar);
                runTaskLater(20 * 5, false, () -> menuDataRegister.getAudiences().player(player).hideBossBar(bossBar));
                break;
            case MINI_ACTIONBAR:
                menuDataRegister.getAudiences().player(player).sendActionBar(MiniMessage.miniMessage().deserialize(executable));
                break;
            case MINI_TITLE:
                String[] splited = null;
                if (executable.contains("|"))
                    splited = executable.split("\\|");
                String message = splited != null ? splited[0] : executable;
                String submessage = splited != null && splited.length == 2 ? splited[1] : "";
                final Component mainTitle = MiniMessage.miniMessage().deserialize(message);
                final Component subtitle = MiniMessage.miniMessage().deserialize(submessage);
                final Title title = Title.title(mainTitle, subtitle);
                menuDataRegister.getAudiences().player(player).showTitle(title);
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
            case OPEN:
                this.menuAction(player, executable);
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
                menuDataRegister.getEconomyProvider().withdrawAmont(player.getUniqueId(), Double.parseDouble(executable));
                break;
            case GIVE_MONEY:
                menuDataRegister.getEconomyProvider().depositAmont(player.getUniqueId(), Double.parseDouble(executable));
                break;
            case TAKE_EXP:
            case GIVE_EXP:
                if (this.actionType == TAKE_EXP)
                    executable = "-" + executable;

                setExp(player, executable);
                break;
            case TAKE_PERM:
                menuDataRegister.getPermissionProvider().setPermission(player, executable);
                break;
            case GIVE_PERM:
                menuDataRegister.getPermissionProvider().removePermission(player, executable);
                break;
            case GIVE_SKULL:
                giveSkullItem(player, executable);
                break;
            case GIVE_ITEM:
                giveItem(player, executable);
                break;
        }
    }

    private void menuAction(final Player player, final String executable) {
        final CommandActionType action = this.actionType;
        final MenuCache menuCache = this.menuContext != null ? this.menuContext.getMenuCache() : null;

        if (action == CLOSE && this.openCloseAction == null) {
            player.closeInventory();
            return;
        }

        if (menuCache == null) {
            logger.warning("Could not find the menu contect for this plugin: " + plugin.getName() + ". Did you register your menu?");
            return;
        }

        final MenuSettings menuSettings = menuCache.getMenuCache().get(executable);
        if (menuSettings == null) {
            logger.warning("No menu found with this name: " + executable);
            return;
        }

        if (this.openCloseAction != null) {
            final MenuSession menuSession = new MenuSession(this.plugin, executable, player);
            openCloseAction.handle(action == OPEN ? MenuAction.OPEN : MenuAction.CLOSE, executable, menuSession);
            return;
        }

        logger.warning("Fallback GUI used for menu: " + executable);
        FallBackGUI fallBackGUI = new FallBackGUI(plugin, executable, player);
        fallBackGUI.beforeOpen((check) -> {
            if (check == RequirementCheck.SUCCESS)
                player.openInventory(fallBackGUI.getInventory());
        });
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
                double random = Double.parseDouble(menuDataRegister.getDecimalFormat().format(menuDataRegister.getRandomUntility().randomDouble()));
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

    private void giveSkullItem(Player player, String executable) {
        int start = executable.indexOf("{display_name=");
        int end = executable.indexOf("}");
        String displayname = "";
        if (start >= 0 && end > start) {
            String rawName = executable.substring(start + 14, end); // "&6Guld"
            executable = executable.replace("{display_name=" + rawName + "}", "").trim();
            displayname = formatColors(rawName);
        }
        ItemStack itemStack = getSkull(player, executable);
        if (itemStack != null) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (displayname.isEmpty()) {
                    meta.setDisplayName("§fSkull");
                } else {
                    meta.setDisplayName(displayname);
                }
            }
            itemStack.setItemMeta(meta);
            Map<Integer, ItemStack> leftOvers = player.getInventory().addItem(itemStack);
            if (!leftOvers.isEmpty()) {
                leftOvers.values().forEach(stack -> {
                    if (stack != null)
                        player.getWorld().dropItem(player.getLocation(), stack);
                });
            }
        }
    }

    private void giveItem(@NotNull final Player player, @NotNull final String executable) {
        final TemplatesCache templateCache = menuContext != null ? menuContext.getTemplatesCache() : null;
        if (templateCache != null) {
            int start = executable.indexOf("{");
            int end = executable.indexOf("}");
            if (start >= 0 && end > start) {
                final String key = executable.substring(start + 1, end);
                final String remaining = executable.replace("{" + key + "}", "").trim();
                final Template template = templateCache.getTemplet(key.toLowerCase());
                int amount = 1;
                if (!remaining.isEmpty()) {
                    try {
                        amount = Integer.parseInt(remaining.split(" ")[0]);
                    } catch (NumberFormatException e) {
                        amount = -1;
                    }
                }
                if (template != null) {
                    if (amount <= 0)
                        amount = template.getAmount();
                    String texture = template.getTexture();
                    ItemStack itemStack = getSkull(player, texture);
                    itemStack = getItemStack(itemStack, template);
                    createStack(player, itemStack, template, amount);
                }
            }
        }
    }

    private ItemStack getSkull(@NotNull final Player player, @Nullable final String texture) {
        if (texture == null || texture.isEmpty())
            return null;

        ItemStack itemStack = null;
        if (texture.startsWith("uuid="))
            itemStack = SkullCreator.itemFromUuid(UUID.fromString(texture.substring(5)));
        else if (texture.startsWith("base64="))
            itemStack = SkullCreator.itemFromBase64(texture.substring(7));
        else if (texture.startsWith("url="))
            itemStack = SkullCreator.itemFromUrl(texture.substring(4));
        else if (texture.startsWith("player_skull=")) {
            itemStack = SkullCreator.itemFromUuid(player.getUniqueId());
        }
        return itemStack;
    }

    @Nullable
    private ItemStack getItemStack(@Nullable ItemStack itemStack, @NotNull final Template template) {
        if (itemStack == null) {
            String templateMatrial = template.getMatrial();
            if (templateMatrial == null || templateMatrial.isEmpty())
                return null;
            Material material = Material.getMaterial(templateMatrial.toUpperCase());
            if (material == null)
                return null;
            itemStack = new ItemStack(material);
        }
        return itemStack;
    }

    private static void createStack(@NotNull final Player player, @Nullable final ItemStack itemStack, @NotNull final Template template, final int amount) {
        if (itemStack != null) {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(template.getDisplayName());
                meta.setLore(template.getLore());
            }
            itemStack.setItemMeta(meta);
            if (amount > 0)
                itemStack.setAmount(amount);
            Map<Integer, ItemStack> leftOvers = player.getInventory().addItem(itemStack);
            if (!leftOvers.isEmpty()) {
                leftOvers.values().forEach(stack -> {
                    if (stack != null)
                        player.getWorld().dropItem(player.getLocation(), stack);
                });
            }
        }
    }

}
