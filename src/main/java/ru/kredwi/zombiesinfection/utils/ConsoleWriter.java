package ru.kredwi.zombiesinfection.utils;

import org.bukkit.Bukkit;

import ru.kredwi.zombiesinfection.files.ZIConfig;

public final class ConsoleWriter {
	
	public void writeInfoDebug(String text, Object...objects) {
		if (ZIConfig.DEBUG_MODE.asBoolean()) {
			String writedText = "";
			if (objects.length > 0) {
				writedText = String.format(text, objects);
			} else {
				writedText = text;
			}
			Bukkit.getLogger().info("[ZombiesInfection] " + writedText);	
		}
	}
	
}
