package org.brokenarrow.library.menusettings.hooks.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.brokenarrow.library.menusettings.MenuSettingsAddon;
import org.brokenarrow.library.menusettings.hooks.economy.PriceProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;
import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.exceptions.Valid.checkBoolean;
import static org.brokenarrow.library.menusettings.exceptions.Valid.checkNotNull;
import static org.bukkit.Bukkit.getServer;

public class VaultEconomy implements PriceProvider {
    private Economy econ;
    private Permission perms;

    public VaultEconomy() {
        setupEconomy();

    }

    public void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getServer().getLogger().log(Level.WARNING, "Could not find Vault, economy will not work");
            return;
        }
        getServer().getScheduler().runTaskLater(MenuSettingsAddon.getPLUGIN(), () -> {
            RegisteredServiceProvider<Economy> rspEconomy = getServer().getServicesManager().getRegistration(Economy.class);
            getServer().getLogger().log(Level.WARNING, "rspEconomy " + rspEconomy);
            if (rspEconomy == null) {
                return;
            }
            getServer().getLogger().log(Level.WARNING, "rspEconomy.getProvider() " + rspEconomy.getProvider());
            econ = rspEconomy.getProvider();
        }, 1);
    }

    @Override
    public boolean withdrawAmont(UUID uuid, double amount) {
        checkNotNull(econ, "vault is null, so you can't withdraw money");

        if (amount > getBalance(uuid))
            return false;
        EconomyResponse economyResponse = null;
        if (amount > 0)
            economyResponse = econ.withdrawPlayer(getOfflinePlayer(uuid), amount);

        return economyResponse != null && economyResponse.transactionSuccess();
    }

    @Override
    public boolean setAmont(UUID uuid, double amount) {
        checkNotNull(econ, "vault is null, so you can't set amont of money");

        EconomyResponse economyResponse = null;
        if (amount > 0) {
            economyResponse = econ.withdrawPlayer(getOfflinePlayer(uuid), getBalance(uuid));
            if (economyResponse.transactionSuccess())
                economyResponse = econ.depositPlayer(getOfflinePlayer(uuid), amount);
        }

        return economyResponse != null && economyResponse.transactionSuccess();
    }

    @Override
    public boolean depositAmont(UUID uuid, double amount) {
        checkBoolean(econ == null, "vault is null, so you can't deposit money");

        EconomyResponse economyResponse = null;
        if (amount > 0)
            economyResponse = econ.depositPlayer(getOfflinePlayer(uuid), amount);

        return economyResponse != null && economyResponse.transactionSuccess();
    }

    @Override
    public boolean hasAmount(UUID uuid, double amount) {
        checkNotNull(econ, "vault is null, so you can't check players balance");

        return econ.has(getOfflinePlayer(uuid), amount);
    }

    @Override
    public double getBalance(UUID uuid) {
        checkNotNull(econ, "vault is null, so you can't check players balance");

        return econ.getBalance(getOfflinePlayer(uuid));
    }

    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @Override
    public Economy getEcon() {
        return econ;
    }

}
