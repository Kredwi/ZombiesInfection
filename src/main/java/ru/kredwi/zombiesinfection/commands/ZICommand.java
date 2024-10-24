package ru.kredwi.zombiesinfection.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import ru.kredwi.zombiesinfection.files.ZIConfig;
import ru.kredwi.zombiesinfection.utils.ConsoleWriter;

public class ZICommand implements CommandExecutor, TabCompleter {

	private final ConsoleWriter console;
	private Plugin plugin = null;
	
	public ZICommand(Plugin plugin, ConsoleWriter console) {
		this.plugin = plugin;
		this.console = console;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("zombiesinfection.reload")) {
			sender.sendMessage(ZIConfig.NO_PERMISSION.asString());
		} else if (1 > args.length) {
			sender.sendMessage(String.format(ZIConfig.NO_COMMAND_ARGS.asString(), label));
		} else if (args[0].equalsIgnoreCase("reload")) {
			plugin.reloadConfig();
			sender.sendMessage(ZIConfig.PLUGIN_RELOAD.asString());
			console.writeInfoDebug(ZIConfig.PLUGIN_RELOAD.asString());
		} else {
			sender.sendMessage(ZIConfig.NO_FOUND_COMMAND.asString());
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
