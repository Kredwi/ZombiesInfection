package ru.kredwi.zombiesinfection.handler;

import java.util.Map;
import java.util.Iterator;
import java.util.UUID;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import ru.kredwi.zombiesinfection.entity.InfectedPlayer;
import ru.kredwi.zombiesinfection.files.ZIConfig;
import ru.kredwi.zombiesinfection.utils.ConsoleWriter;
import ru.kredwi.zombiesinfection.utils.InfectionUtils;

/**
 * Overly complex class
 */
public class PlayerInfectionHandler {
	
	private Map<UUID, InfectedPlayer> infectedPlayers = new ConcurrentHashMap<>();
	
	private final ConsoleWriter console = new ConsoleWriter();
	private PotionHandler potionHandler = new PotionHandler(console);
	private InfectionUtils infectionUtils = new InfectionUtils();
	
	private BukkitRunnable InfectedTimer = null;
	private Plugin plugin;
	
	public PlayerInfectionHandler(Plugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Creating timer for search and handle infection players
	 */
	private void startInfectionTimer() {
		InfectedTimer = new BukkitRunnable() {
			
			@Override
			public void run() {
				int i = 0;
				int countUUIDs = infectedPlayers.size();
				
				console.writeInfoDebug("Running a map check for infected search (%s UUID)", countUUIDs);
				
				if (countUUIDs < 1) {
					console.writeInfoDebug("Players in infectedPlayers is 0");
					removeInfectionTimer();
					return;
				}
				
				Iterator<Map.Entry<UUID, InfectedPlayer>> iterator = infectedPlayers.entrySet().iterator();
				
				while (iterator.hasNext()) {
					
					Map.Entry<UUID, InfectedPlayer> entry = iterator.next();
					UUID k = entry.getKey();
					
					if (infectionUtils.timeIsOver(infectedPlayers.get(k))) {
						Player player = Bukkit.getPlayer(k);
						
						if (player == null || !player.isOnline()) {
							iterator.remove();
							removePlayer(k);
							continue;
						};
						
						console.writeInfoDebug("%s. Player: %s found, I give out a potion and remove it from the map", i++, player.getName());
						
						if (ZIConfig.SOUND_IN_EFFECT_GIVED_ENABLE.asBoolean()) {
							
							String SoundGivedInEffect = ZIConfig.SOUND_IN_EFFECT_GIVED_SOUND.asString();
							
							try {
								
								player.playSound(
										player.getLocation(),
										Sound.valueOf(SoundGivedInEffect),
										(float) ZIConfig.SOUND_IN_EFFECT_GIVED_VOLUME.asDouble(),
										(float) ZIConfig.SOUND_IN_EFFECT_GIVED_PITCH.asDouble()
									);	
							} catch (IllegalArgumentException e) {
								Bukkit.getLogger().severe("[ZombiesInfection] " + "SOUND " + SoundGivedInEffect + " IS NOT FOUND");
							}
						}
						
						potionHandler.getPotionsForPlayer(player);
						
						iterator.remove();
						removePlayer(k);
					}
				}
			}
		};
		// potion not work in async
		InfectedTimer.runTaskTimer(plugin, ZIConfig.DELAY.asInt(), ZIConfig.PERIOD.asInt());
	}
	
	public void addPlayer(UUID uuid, Long unixTime) {
		
		int time = infectionUtils.getInfectedRandomTime();
		
		infectedPlayers.put(uuid, new InfectedPlayer(unixTime, time));
		
		if (InfectedTimer == null) {
			this.startInfectionTimer();	// start infection timer
		}
		console.writeInfoDebug("UUID: %s added to the map. Unix time: %s. Effect applied in %s minutes.", uuid, unixTime, time);
	}

	public void removePlayer(UUID uuid) {
		console.writeInfoDebug("UUID: %s deleted from Map", uuid);
		infectedPlayers.remove(uuid);
	}
	
	public InfectedPlayer getPlayer(UUID uuid) {
		return infectedPlayers.get(uuid);
	}
	
	private void removeInfectionTimer() {
		console.writeInfoDebug("Canceling tasks with a timer");
		InfectedTimer.cancel();
		InfectedTimer = null;
	}
}
