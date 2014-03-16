package com.gmail.raynlegends.RoboticStaff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	private Main plugin;
	private Functions functions;

	public Commands(Main instance, Functions functionsInstance) {
		plugin = instance;
		functions = functionsInstance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// Command: /roboticstaff

		if (label.equalsIgnoreCase("roboticstaff")) {
			if (args.length == 0) {
				functions.sendSenderMessage(sender, Messages.roboticstaffBase);
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {

					// Command: /roboticstaff reload

					if (sender.hasPermission("roboticstaff.reload")) {
						functions.reloadPlugin(sender);
					}
				} else if (args[0].equalsIgnoreCase("tags")) {

					// Command: /roboticstaff tags

					if (sender.hasPermission("roboticstaff.autoanswer.list")) {
						functions.sendSenderMessage(sender, Messages.tagsList);
						for (String tag : plugin.tags) {
							sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + tag);
						}
					}
				} else if (args[0].equalsIgnoreCase("words")) {

					// Command: /roboticstaff words

					if (sender.hasPermission("roboticstaff.antiswearing.list")) {
						functions.sendSenderMessage(sender, Messages.wordsList);
						for (String word : plugin.words) {
							sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + word);
						}
					}
				} else if (args[0].equalsIgnoreCase("blocked")) {

					// Command: /roboticstaff blocked

					if (sender.hasPermission("roboticstaff.blockedcommands.list")) {
						functions.sendSenderMessage(sender, Messages.blockedList);
						for (String blocked : plugin.getConfig().getStringList("blockedcommands-general")) {
							sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + blocked);
						}
						functions.sendSenderMessage(sender, Messages.blockedPerWorldTip);
					}
				} else {

					// Command: /roboticstaff anythingElse

					functions.sendSenderMessage(sender, Messages.strangeRoboticstaffArguments.replace("%s", args[0]));
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("blocked")) {

					// Command: /roboticstaff blocked <world>

					if (sender.hasPermission("roboticstaff.blockedcommands.list")) {
						try {
							functions.sendSenderMessage(sender, Messages.blockedPerWorldList);
							for (String blocked : plugin.getConfig().getStringList("blockedcommands-perworld." + args[1])) {
								sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + blocked);
							}
						} catch (Exception e) {
							functions.sendSenderMessage(sender, Messages.blockedWorldNotFound.replace("%s", args[1]));
						}
					}
				} else {
					functions.sendSenderMessage(sender, Messages.strangeRoboticstaffArguments.replace("%s", args[0]));
				}
			} else {
				functions.sendSenderMessage(sender, Messages.strangeRoboticstaffArguments.replace("%s", args[0]));
			}
			return true;
		}

		// Command: /helpme

		if (label.equalsIgnoreCase("helpme")) {
			if (plugin.getConfig().getBoolean("helpme-enabled")) {
				if (args.length == 0) {
					functions.sendSenderMessage(sender, plugin.helpme_help);
					int i = 0;
					for (String ask : plugin.asks) {
						sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + i + " - " + ask);
						i++;
					}
				} else if (args.length == 1) {
					try {
						int int_arg = Integer.parseInt(args[0]);
						if (int_arg <= plugin.asks.size() - 1) {
							sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + int_arg + " - " + plugin.asks.get(int_arg));
						} else {
							sender.sendMessage(ChatColor.RED + plugin.helpme_error + ChatColor.YELLOW + " " + plugin.helpme_error_1);
						}
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED + plugin.helpme_error + ChatColor.YELLOW + " " + plugin.helpme_error_2);
					}
				} else {
					sender.sendMessage(ChatColor.RED + plugin.helpme_error + ChatColor.YELLOW + " " + plugin.helpme_error_3);
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("helpme-disabled-message")));
			}
			return true;
		}
		return false;
	}
}
