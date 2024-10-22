package ru.kredwi.zombiesinfection.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import ru.kredwi.zombiesinfection.PlayerInfectionHandler;

public class EntityDamage implements Listener {
	
	private PlayerInfectionHandler infectionHandler = null;
	private Plugin plugin = null;
	
	public EntityDamage(Plugin plugin, PlayerInfectionHandler infectionHandler) {
		this.plugin = plugin;
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
			
			ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
			String you_infected = messages.getString("you_infected");
			
			if (!entity.hasPermission("zombiesinfection.effect")) return;
			
			infectionHandler.addPlayer(entity.getUniqueId(), Long.valueOf(System.currentTimeMillis() / 1000L));
			entity.sendMessage(you_infected);
		}
	}
}
