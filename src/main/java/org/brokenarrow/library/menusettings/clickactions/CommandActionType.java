package org.brokenarrow.library.menusettings.clickactions;

public enum CommandActionType {
	CONSOLE("[console]"),
	PLAYER("[player]"),
	PLAYER_COMMAND_EVENT("[commandevent]"),
	MESSAGE("[message]"),
	BROADCAST("[broadcast]"),
	CHAT("[chat]"),
	CLOSE("[close]"),
	REFRESH("[refresh]"),//Refresh items in the current menu view
	BROADCAST_SOUND("[broadcastsound]"),
	BROADCAST_WORLD_SOUND("[broadcastsoundworld]"),
	PLAY_SOUND("[sound]"),
	TAKE_MONEY("[takemoney]"),
	GIVE_MONEY("[givemoney]"),
	TAKE_EXP("[takeexp]"),
	GIVE_EXP("[giveexp]"),
	TAKE_PERM("[takepermission]"),
	GIVE_PERM("[givepermission]");
	private final String identifier;

	CommandActionType(String identifier) {
		this.identifier = identifier;

	}

	public static CommandActionType getType(String string) {
		CommandActionType[] requirementTypes = values();

		for (CommandActionType type : requirementTypes) {
			if (string.startsWith(type.getIdentifier()))
				return type;
		}
		return null;
	}

	public String getIdentifier() {
		return identifier;
	}
}
