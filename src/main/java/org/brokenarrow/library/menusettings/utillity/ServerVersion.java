package org.brokenarrow.library.menusettings.utillity;

import org.bukkit.plugin.Plugin;

public enum ServerVersion {
	v1_18(18),
	v1_17(17),
	v1_16(16),
	v1_15(15),
	v1_14(14),
	v1_13(13),
	v1_12(12),
	v1_11(11),
	v1_10(10),
	v1_9(9),
	v1_8(8),
	v1_7(7),
	v1_6(6),
	v1_5(5),
	v1_4(4),
	v1_3_AND_BELOW(3);

	private final int version;
	private static int currentServerVersion;

	public static boolean equals(ServerVersion version) {
		return serverVersion(version) == 0;
	}

	public static boolean newerThan(ServerVersion version) {
		return serverVersion(version) > 0;
	}

	public static boolean olderThan(ServerVersion version) {
		return serverVersion(version) < 0;
	}

	public static int serverVersion(ServerVersion version) {
		return currentServerVersion - version.getVersion();
	}

	public static void setServerVersion(Plugin plugin) {
		currentServerVersion = Integer.parseInt(plugin.getServer().getBukkitVersion().split("\\.")[1]);
	}

	ServerVersion(int version) {
		this.version = version;

	}

	public int getVersion() {
		return version;
	}

	public static int getCurrentServerVersion() {
		return currentServerVersion;
	}
}