package org.brokenarrow.library.menusettings.hooks.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.brokenarrow.library.menusettings.hooks.economy.PriceProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

import static org.brokenarrow.library.menusettings.exceptions.Valid.checkBoolean;
import static org.bukkit.Bukkit.getServer;

public class VaultEconomy implements PriceProvider {
	private Economy econ;
	private Permission perms;

	public VaultEconomy() {
		setupEconomy();

	}

	public boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rspEconomy = getServer().getServicesManager().getRegistration(Economy.class);

		if (rspEconomy == null) {
			return false;
		}
		econ = rspEconomy.getProvider();
		return true;
	}

	@Override
	public boolean withdrawAmont(UUID uuid, double amount) {
		checkBoolean(econ == null, "vault is null, so you can't withdraw money");

		if (amount > getBalance(uuid))
			return false;
		EconomyResponse economyResponse = null;
		if (amount > 0)
			economyResponse = econ.withdrawPlayer(getOfflinePlayer(uuid), amount);

		return economyResponse != null && economyResponse.transactionSuccess();
	}

	@Override
	public boolean setAmont(UUID uuid, double amount) {
		checkBoolean(econ == null, "vault is null, so you can't set amont of money");

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
		checkBoolean(econ == null, "vault is null, so you can't check players balance");

		return econ.has(getOfflinePlayer(uuid), amount);
	}

	@Override
	public double getBalance(UUID uuid) {
		checkBoolean(econ == null, "vault is null, so you can't check players balance");

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
