package ru.kredwi.zombiesinfection;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ru.kredwi.zombiesinfection.commands.MainCommand;
import ru.kredwi.zombiesinfection.events.EntityDamage;

/**
 * Hello world!
 * First JavaDocs !!
 * 2024
 */
public class ZombiesInfection extends JavaPlugin
{
	
	private static final String CONFIG_FILE = "config.yml";
	private YamlConfiguration config;
	
	@Override
	public void onLoad() {
		File file = new File(getDataFolder(), CONFIG_FILE);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			saveResource(CONFIG_FILE, false);
		}
		
		config = YamlConfiguration.loadConfiguration(file);
		
		try {
			config.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onEnable() {
		PlayerInfectionHandler infectionDamage = new PlayerInfectionHandler(this);
		
		PluginCommand mainCommand = getCommand("zombiesinfection");
		MainCommand reloadCommandExecutor = new MainCommand(this, infectionDamage);
		
		mainCommand.setExecutor(reloadCommandExecutor);
		mainCommand.setTabCompleter(reloadCommandExecutor);
		
		getServer().getPluginManager().registerEvents(new EntityDamage(this, infectionDamage), this);
	}
}
