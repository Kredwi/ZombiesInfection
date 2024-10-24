package ru.kredwi.zombiesinfection.events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import ru.kredwi.zombiesinfection.files.ZIConfig;
import ru.kredwi.zombiesinfection.handler.PlayerInfectionHandler;

public class EntityDamage implements Listener {
	
	private PlayerInfectionHandler infectionHandler = null;
	
	public EntityDamage(PlayerInfectionHandler infectionHandler) {
		this.infectionHandler = infectionHandler;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		
		Entity damager = event.getDamager();
		
		if (damager instanceof Zombie) {
			
			addNewTime(event.getEntity());
			
		} else if (damager instanceof ZombieVillager) {
			
			addNewTime(event.getEntity());
			
		}
	}
	private void addNewTime(Entity entity) {
		if (entity instanceof Player) {
			
			Player player = (Player) entity;
			
			if (!player.hasPermission("zombiesinfection.effect")) return;
			
			boolean isInfected = infectionHandler.getPlayer(player.getUniqueId()) != null;
			
			this.addPlayerOrUpdate(player, isInfected);
			this.playSound(player, isInfected);
			this.sendMessage(player, isInfected);
		}
	}
	
	private void sendMessage(Player player, boolean isInfected) {
		if (!ZIConfig.IF_PLAYER_INFECTED_MESSAGE.asBoolean() && isInfected) return;
		player.sendMessage(ZIConfig.YOU_INFECTED.asString());
	}
	
	private void playSound(Player player, boolean isInfected) {
		if (ZIConfig.SOUND_IN_ATTACK_ENABLE.asBoolean()) {
			if (!ZIConfig.IF_PLAYER_INFECTED_PLAY_SOUND.asBoolean() && isInfected) return;
			String SoundInAttack = ZIConfig.SOUND_IN_ATTACK_SOUND.asString();
			try {
				player.playSound(
						player.getLocation(),
						Sound.valueOf(SoundInAttack),
						(float) ZIConfig.SOUND_IN_ATTACK_VOLUME.asDouble(),
						(float) ZIConfig.SOUND_IN_ATTACK_PITCH.asDouble()
						);
			} catch (IllegalArgumentException e) {
				Bukkit.getLogger().severe("[ZombiesInfection] " + "SOUND " + SoundInAttack + " IS NOT FOUND");
			}
		}
	}
	
	private void addPlayerOrUpdate(Player player, boolean isInfected) {
		if (!ZIConfig.IF_PLAYER_INFECTED_UPDATE_TIMER.asBoolean() && isInfected) return;
		infectionHandler.addPlayer(player.getUniqueId(), Long.valueOf(System.currentTimeMillis() / 1000L));
	}
}
