package ru.kredwi.zombiesinfection.handler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import ru.kredwi.zombiesinfection.entity.InfectedPlayer;
import ru.kredwi.zombiesinfection.files.ZIConfig;
import ru.kredwi.zombiesinfection.task.InfectionTimer;
import ru.kredwi.zombiesinfection.utils.ConsoleWriter;
import ru.kredwi.zombiesinfection.utils.InfectionUtils;

/**
 * Overly complex class
 */
public class PlayerInfectionHandler {
	
	private Map<UUID, InfectedPlayer> infectedPlayers = new ConcurrentHashMap<>();
	
	private final ConsoleWriter console = new ConsoleWriter();
	private InfectionUtils infectionUtils = new InfectionUtils();
	
	private PotionHandler potionHandler;
	private BukkitRunnable InfectedTimer = null;
	private Plugin plugin;
	
	public PlayerInfectionHandler(Plugin plugin) {
		this.plugin = plugin;
		this.potionHandler = new PotionHandler(plugin, console);
	}

	/**
	 * Creating timer for search and handle infection players
	 */
	private void startInfectionTimer() {
		InfectedTimer = new InfectionTimer(plugin, infectedPlayers, this,
				potionHandler, infectionUtils, console);
		
		InfectedTimer.runTaskTimerAsynchronously(plugin, ZIConfig.DELAY.asInt(), ZIConfig.PERIOD.asInt());
	}
	
	public void addPlayer(UUID uuid, Long unixTime) {
		
		int time = infectionUtils.getInfectedRandomTime();
		
		infectedPlayers.put(uuid, new InfectedPlayer(unixTime, time));
		
		if (InfectedTimer == null || InfectedTimer.isCancelled()) {
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
	
	public void removeInfectionTimer() {
		console.writeInfoDebug("Canceling tasks with a timer");
		if (InfectedTimer != null || !InfectedTimer.isCancelled()) {
			InfectedTimer.cancel();
			InfectedTimer = null;
			console.writeInfoDebug("Timer task cancel!");
		} else {
			console.writeInfoDebug("Canceling timer task is not runned. Timer task cancel is aborted.");
		}
	}
}
