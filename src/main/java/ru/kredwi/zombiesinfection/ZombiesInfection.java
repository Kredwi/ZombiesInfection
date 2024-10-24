package ru.kredwi.zombiesinfection;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import ru.kredwi.zombiesinfection.commands.ZICommand;
import ru.kredwi.zombiesinfection.events.EntityDamage;
import ru.kredwi.zombiesinfection.handler.PlayerInfectionHandler;
import ru.kredwi.zombiesinfection.utils.ConsoleWriter;

/**
 * Hello world!
 * First JavaDocs !!
 * 2024
 */
public class ZombiesInfection extends JavaPlugin
{
	
	private final ConsoleWriter console = new ConsoleWriter();
	
	@Override
	public void onEnable() {
		
		console.writeInfoDebug("Loading Configuration");
		saveDefaultConfig();
		console.writeInfoDebug("Configuration is succesfully loaded!");
		
		int configVersion = getConfig().getInt("config_version", 0);
		
		if (configVersion != 0) {
			if (getConfig().getInt("config_version", 0)  < 2) {
				for (int i = 0; i < 5; i++) {
					Bukkit.getLogger().warning("[ZombiesInfection] " + "config.yml is outdated, please delete it and restart your server to avoid errors and problems.");
				}
			}
		}
		
		PlayerInfectionHandler infectionDamage = new PlayerInfectionHandler(this);
		
		PluginCommand mainCommand = getCommand("zombiesinfection");
		ZICommand reloadCommandExecutor = new ZICommand(this, console);
		
		mainCommand.setExecutor(reloadCommandExecutor);
		mainCommand.setTabCompleter(reloadCommandExecutor);
		
		getServer().getPluginManager().registerEvents(new EntityDamage(infectionDamage), this);
	}
}
