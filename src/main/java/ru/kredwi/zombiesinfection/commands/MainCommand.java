package ru.kredwi.zombiesinfection.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import ru.kredwi.zombiesinfection.PlayerInfectionHandler;

public class MainCommand implements CommandExecutor, TabCompleter {

	private Plugin plugin = null;
	private PlayerInfectionHandler infectionHandler = null;
	
	public MainCommand(Plugin plugin, PlayerInfectionHandler infectionHandler) {
		this.plugin = plugin;
		this.infectionHandler = infectionHandler;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		ConfigurationSection messages = plugin.getConfig().getConfigurationSection("messages");
		if (!sender.hasPermission("zombiesinfection.reload")) {
			
			sender.sendMessage(messages.getString("no_permission"));
			
		} else if (1 > args.length) {
			
			sender.sendMessage(messages.getString("no_command_args"));
			
		} else if (args[0].equals("reload")) {
			
			plugin.reloadConfig();
			infectionHandler.updateVariable();
			
			sender.sendMessage(messages.getString("plugin_reload"));
			
		} else {
			sender.sendMessage(messages.getString("no_found_command"));
		}
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		List<String> list = new ArrayList<String>();
		if (args.length == 1) {
			list.add("reload");
		}
		return list.stream()
				.filter(s -> s.startsWith(args[args.length - 1]))
				.collect(Collectors.toList());
	}
	
}
