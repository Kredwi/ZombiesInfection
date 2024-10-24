package ru.kredwi.zombiesinfection.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ru.kredwi.zombiesinfection.files.ZIConfig;
import ru.kredwi.zombiesinfection.utils.ConsoleWriter;

public class PotionHandler {
	
	private ConsoleWriter console;
	
	public PotionHandler(ConsoleWriter console) {
		this.console = console;
	}
	
	public void getPotionsForPlayer(Player player) {
		
		for (String potionKey : ZIConfig.POTION_GIVED_SECTION.asSection().getKeys(false)) {
			this.givePotionForPlayer(player, potionKey);
		}
		
		player.sendMessage(ZIConfig.EFFECT_GIVED.asString());
	}
	
	
	private void givePotionForPlayer(Player player, String potionKey) {
		PotionEffectType potion = PotionEffectType.getByName(potionKey);
		if (potion == null) {
			Bukkit.getLogger().severe("[ZombiesInfection] " + potionKey + " is not found");
			return;
		}
		
		int potionDuration = ZIConfig.POTION_GIVED_SECTION.asSection().getConfigurationSection(potionKey).getInt("potion-duration") * 20;
		int potionLevel = 1;
		
		if (!player.hasPotionEffect(potion)) {
			player.addPotionEffect(new PotionEffect(potion, potionDuration, potionLevel));
		} else {
			PotionEffect currentPotion = player.getPotionEffect(potion);
			player.removePotionEffect(potion);
			player.addPotionEffect(new PotionEffect(potion, currentPotion.getDuration() + potionDuration, potionLevel));
		}
			
		console.writeInfoDebug("Potion %s is given to player %s", potion.getName(), player.getName());
	}
}
