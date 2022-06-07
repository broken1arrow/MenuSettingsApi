package org.brokenarrow.library.menusettings.hooks.economy;

import org.brokenarrow.library.menusettings.hooks.vault.VaultEconomy;
import org.bukkit.Bukkit;

public class RegisterEconomyHook {

	private PriceProvider provider;

	public RegisterEconomyHook() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			provider = new VaultEconomy();
		}
	}
	
	public PriceProvider getProvider() {
		return provider;
	}
}
