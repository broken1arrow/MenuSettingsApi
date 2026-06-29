package org.brokenarrow.library.menusettings.utillity;

import org.broken.arrow.library.version.VersionUtil;
import org.bukkit.plugin.Plugin;

public enum ServerVersion {
	v1_19(19.0F),
	v1_18_0(18.0F),
	v1_17(17.0F),
	v1_16(16.0F),
	v1_15(15.0F),
	v1_14(14.0F),
	v1_13(13.0F),
	v1_12(12.0F),
	v1_11(11.0F),
	v1_10(10.0F),
	v1_9(9.0F),
	v1_8(8.0F),
	v1_7(7.0F),
	v1_6(6.0F),
	v1_5(5.0F),
	v1_4(4.0F),
	v1_3_AND_BELOW(3.0F);

	private static VersionUtil VERSION;
	private final float version;

	public static boolean newerThan(ServerVersion version) {
		return VERSION.versionNewer(version.version );
	}

	public static boolean olderThan(ServerVersion version) {
		return VERSION.versionOlder(version.version ) ;
	}


	public static void setServerVersion(Plugin plugin) {
		VERSION = new VersionUtil(plugin);
	}

	ServerVersion(float version) {
		this.version = version;

	}

}