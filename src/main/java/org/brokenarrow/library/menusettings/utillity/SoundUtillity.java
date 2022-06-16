package org.brokenarrow.library.menusettings.utillity;

import org.bukkit.Sound;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

import static org.brokenarrow.library.menusettings.RegisterMenuAddon.getLogger;

public final class SoundUtillity {
	private Sound sound;
	private float volume = 1;
	private float pitch = 1;


	public SoundUtillity(final String soundString) {
		this.convertString(soundString);

	}

	@Nullable
	public Sound getSound() {
		return sound;
	}

	public float getVolume() {
		return volume;
	}

	public float getPitch() {
		return pitch;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public void convertString(String string) {
		if (string == null) return;

		if (string.contains(" "))
			setSoundValues(string.split(" "));
		if (string.contains(","))
			setSoundValues(string.split(","));
		else if (!string.matches(".*\\d.*"))
			setSound(getSoundType(string));
	}

	public void setSoundValues(String[] string) {
		if (string.length > 0)
			setSound(getSoundType(string[0]));
		if (string.length > 1)
			setVolume(getFloat(string[1]));
		if (string.length > 2)
			setPitch(getFloat(string[2]));
	}

	public float getFloat(String floatNum) {
		if (floatNum == null) return 1;
		float number;

		try {
			number = Float.parseFloat(floatNum);
		} catch (NumberFormatException e) {
			getLogger(Level.WARNING, "your input " + floatNum + " don´t contains valid number.");
			e.printStackTrace();
			return 1;
		}
		return number;
	}

	@Nullable
	public Sound getSoundType(String string) {
		Sound[] types = Sound.values();
		if (string == null) return null;

		string = string.toUpperCase();
		for (Sound type : types) {
			if (type.name().equals(string))
				return type;
		}
		return types[0];
	}
}
