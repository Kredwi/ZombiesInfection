package ru.kredwi.zombiesinfection;

import java.util.Map;
import java.util.Iterator;
import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Overly complex class
 */
public class PlayerInfectionHandler {
	
	private Map<UUID, Long> infectedPlayers = new ConcurrentHashMap<>();
	private BukkitRunnable InfectedTimer = null;
	private Plugin plugin;
	private FileConfiguration config;
	private boolean debugMode = false;
	private boolean randomTime = true;
	private int potionDuration = 0; // 60 seconds in 20 ticks
	private final int potionLevel = 1;
	private int fromEffect = 0;
	private int period = 0;
	private int delay = 0;
	private int rangeFrom = 0;
	private int rangeTo = 0;
	
	public PlayerInfectionHandler(Plugin plugin) {
		this.plugin = plugin;
		
		this.updateVariable(); // update local variable from config x1
	}
	
	public void updateVariable() {
		this.config = plugin.getConfig();
		
		writeInfoDebug("Updating variables in Playerinteractionhandler");
		
		this.debugMode = this.config.getBoolean("debug");
		
		this.potionDuration = this.config.getInt("potion_duration") * 20;
		this.delay = this.config.getInt("delay") * 20;
		this.period = this.config.getInt("period") * 20;
		
		// POTION LEVEL NOT WORK
//		this.potionLevel = this.config.getInt("potion_level");
		this.fromEffect = this.config.getInt("from_effect");
		
		
		ConfigurationSection configRange = this.config.getConfigurationSection("random_time_from_range");
		this.randomTime = configRange.getBoolean("enable");
		this.rangeFrom = configRange.getInt("range_from");
		this.rangeTo = configRange.getInt("range_to");
		
		if (InfectedTimer == null) {
			this.startInfectionTimer();	
		}
		
		writeInfoDebug("The update of variables in the player interaction handler has been completed successfully");
	}

	/**
	 * creating timer for search and handle infection players
	 */
	private void startInfectionTimer() {
		InfectedTimer = new BukkitRunnable() {
			
			@Override
			public void run() {
				int i = 0;
				int countUUIDs = infectedPlayers.size();
				
				writeInfoDebug("Running a map check for infected search (%s UUID)", countUUIDs);
				
				if (countUUIDs < 1) {
					writeInfoDebug("Players in infectedPlayers is 0");
					removeInfectionTimer();
					return;
				}
				
				Iterator<Map.Entry<UUID, Long>> iterator = infectedPlayers.entrySet().iterator();
				
				while (iterator.hasNext()) {
					
					Map.Entry<UUID, Long> entry = iterator.next();
					UUID k = entry.getKey();
					
					if (timeIsLeft(k)) {
						Player player = Bukkit.getPlayer(k);
						
						if (player == null || !player.isOnline()) {
							iterator.remove();
							removePlayer(k);
							continue;
						};
						
						writeInfoDebug("%s. Player: %s found, I give out a potion and remove it from the map", i++, player.getName());
						
						givePotionForPlayer(player, PotionEffectType.POISON);
						
						iterator.remove();
						removePlayer(k);
					}
				}
			}
		};
		// potion not work in async
		InfectedTimer.runTaskTimer(plugin, delay, period);
	}
	
	public void addPlayer(UUID uuid, Long unixTime) {
		infectedPlayers.put(uuid, unixTime);
		if (InfectedTimer == null) {
			this.startInfectionTimer();	// start infection timer
		}
		writeInfoDebug("UUID: %s added to the map. Unix time: %s", uuid, unixTime);
	}

	private void removePlayer(UUID uuid) {
		writeInfoDebug("UUID: %s deleted from Map", uuid);
		infectedPlayers.remove(uuid);
	}

	private void givePotionForPlayer(Player player, PotionEffectType potion) {		
		if (!player.hasPotionEffect(PotionEffectType.POISON)) {
			player.addPotionEffect(new PotionEffect(potion, potionDuration, potionLevel));
		} else {
			// NOT TESTED
			PotionEffect currentPotion = player.getPotionEffect(potion);
			player.removePotionEffect(potion);
			player.addPotionEffect(new PotionEffect(potion, currentPotion.getDuration() + potionDuration, potionLevel));
		}
		
		writeInfoDebug("Potion %s is given to player %s", potion.getName(), player.getName());
	}
	
	private boolean timeIsLeft(UUID uuid) {
		return (System.currentTimeMillis() / 1000L) - infectedPlayers.get(uuid) > TimeUnit.MINUTES.toSeconds(this.getInfectionPlayerTime());
	}

	// yea, time not set in map((
	private int getInfectionPlayerTime() {
		int currentRandom = (int) (Math.random() * (rangeTo - rangeFrom + 1)) + rangeFrom;
		return randomTime ? currentRandom : fromEffect;
	}
	
	private void removeInfectionTimer() {
		writeInfoDebug("Canceling tasks with a timer");
		InfectedTimer.cancel();
		InfectedTimer = null;
	}
	
	private void writeInfoDebug(String text, Object...objects) {
		if (debugMode) {
			String writedText = "";
			if (objects.length > 0) {
				writedText = String.format(text, objects);
			} else {
				writedText = text;
			}
			Bukkit.getLogger().info(writedText);	
		}
	}
}
