package ru.kredwi.zombiesinfection.task;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import ru.kredwi.zombiesinfection.entity.InfectedPlayer;
import ru.kredwi.zombiesinfection.files.ZIConfig;
import ru.kredwi.zombiesinfection.handler.PlayerInfectionHandler;
import ru.kredwi.zombiesinfection.handler.PotionHandler;
import ru.kredwi.zombiesinfection.utils.ConsoleWriter;
import ru.kredwi.zombiesinfection.utils.InfectionUtils;

public class InfectionTimer extends BukkitRunnable {
	
	private PotionHandler potionHandler;
	private ConsoleWriter console;
	private Map<UUID, InfectedPlayer> infectedPlayers;
	private PlayerInfectionHandler infectionHandler;
	private InfectionUtils infectionUtils;
	
	public InfectionTimer(Plugin plugin, Map<UUID, InfectedPlayer> infectedPlayers,
			PlayerInfectionHandler infectionHandler, PotionHandler potionHandler,
			InfectionUtils infectionUtils, ConsoleWriter console) {
		this.infectedPlayers = infectedPlayers;
		this.infectionHandler = infectionHandler;
		this.infectionUtils = infectionUtils;
		this.console = console;
		this.potionHandler = potionHandler;
	}
	
	@Override
	public void run() {
		int i = 0;
		int countUUIDs = infectedPlayers.size();
		
		console.writeInfoDebug("Running a map check for infected search (%s UUID)", countUUIDs);
		
		if (countUUIDs < 1) {
			console.writeInfoDebug("Players in infectedPlayers is 0");
			infectionHandler.removeInfectionTimer();
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
					infectionHandler.removePlayer(k);
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
				infectionHandler.removePlayer(k);
			}
		}
	}
}
