package org.brokenarrow.library.menusettings.hooks.economy;

import java.util.UUID;

public interface PriceProvider {

	default boolean withdrawAmont(UUID uuid, double amount) {
		return false;
	}

	default boolean depositAmont(UUID uuid, double amount) {
		return false;
	}

	default boolean setAmont(UUID uuid, double amount) {
		return false;
	}

	default boolean hasAmount(UUID uuid, double amount) {
		return false;
	}

	default double getBalance(UUID uuid) {
		return 0;
	}

	default <T extends Class<?>> Object getEcon() {
		return null;
	}
}
