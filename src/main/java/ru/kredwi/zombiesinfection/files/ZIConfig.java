package ru.kredwi.zombiesinfection.files;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import ru.kredwi.zombiesinfection.ZombiesInfection;

public enum ZIConfig {
	
	 // Basic settings
	DEBUG_MODE("debug"),
	EFFECT_WILL_APPLIED_AFTER("effect-will-applied-after"),
	DELAY("delay"),
	PERIOD("period"),

	// If player is infected settings
	IF_PLAYER_INFECTED_MESSAGE("if-player-infected.message"),
	IF_PLAYER_INFECTED_PLAY_SOUND("if-player-infected.play-sound"),
	IF_PLAYER_INFECTED_UPDATE_TIMER("if-player-infected.update-timer"),
	
	// Random time range settings
	RANDOM_TIME_FROM_RANGE_ENABLE("random_time_from_range.enable"),
	RANDOM_TIME_FROM_RANGE_FROM("random_time_from_range.range_from"),
	RANDOM_TIME_FROM_RANGE_TO("random_time_from_range.range_to"),
	 
	// Effect Gived
	POTION_GIVED_SECTION("potion-gived"),
	
	// Sound played settings
	SOUND_IN_ATTACK_ENABLE("sound-played.sound-in-attack.enable"),
	SOUND_IN_ATTACK_SOUND("sound-played.sound-in-attack.sound"),
	SOUND_IN_ATTACK_VOLUME("sound-played.sound-in-attack.volume"),
	SOUND_IN_ATTACK_PITCH("sound-played.sound-in-attack.pitch"),
	
	SOUND_IN_EFFECT_GIVED_ENABLE("sound-played.sound-in-effect-gived.enable"),
	SOUND_IN_EFFECT_GIVED_SOUND("sound-played.sound-in-effect-gived.sound"),
	SOUND_IN_EFFECT_GIVED_VOLUME("sound-played.sound-in-effect-gived.volume"),
	SOUND_IN_EFFECT_GIVED_PITCH("sound-played.sound-in-effect-gived.pitch"),

	// Messages
	PLUGIN_RELOAD("messages.plugin_reload"),
	NO_PERMISSION("messages.no_permission"),
	NO_COMMAND_ARGS("messages.no_command_args"),
	NO_FOUND_COMMAND("messages.no_found_command"),
	YOU_INFECTED("messages.you_infected"),
	EFFECT_GIVED("messages.effect-gived");
	
	private final String path;
	private final JavaPlugin plugin = JavaPlugin.getPlugin(ZombiesInfection.class);
	
	ZIConfig(String path) {
		this.path = path;
	}
	
	public boolean asBoolean() { return plugin.getConfig().getBoolean(this.path); }
	
	public int asInt() {
		int number = plugin.getConfig().getInt(this.path);
		
		switch (valueOf(this.name())) {
		
			case DELAY:
				case PERIOD:
					return number * 20; // 1 * 20 == 1 second
			default:
				return number;
		}
	}
	
	public double asDouble() { return plugin.getConfig().getDouble(this.path); }
	
	public String asString() { return plugin.getConfig().getString(this.path); }
	
	public ConfigurationSection asSection() { return plugin.getConfig().getConfigurationSection(this.path); }
}
