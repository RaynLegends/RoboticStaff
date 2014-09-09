package com.gmail.raynlegends.RoboticStaff;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// Command: /roboticstaff

		if (label.equalsIgnoreCase("roboticstaff")) {
			if (args.length == 0) {
				Functions.sendSenderMessage(sender, Messages.roboticstaffBase);
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {

					// Command: /roboticstaff reload

					if (sender.hasPermission("roboticstaff.reload")) {
						Functions.reloadPlugin(sender);
					}
				} else if (args[0].equalsIgnoreCase("tags")) {

					// Command: /roboticstaff tags

					if (sender.hasPermission("roboticstaff.autoanswer.list")) {
						Functions.sendSenderMessage(sender, Messages.tagsList);
						for (String tag : Main.getPlugin().getConfig().getStringList("autoanswer-tags")) {
							sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + tag);
						}
					}
				} else if (args[0].equalsIgnoreCase("words")) {

					// Command: /roboticstaff words

					if (sender.hasPermission("roboticstaff.antiswearing.list")) {
						Functions.sendSenderMessage(sender, Messages.wordsList);
						for (String word : Main.getPlugin().getConfig().getStringList("antiswearing-words")) {
							sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + word);
						}
					}
				} else if (args[0].equalsIgnoreCase("blocked")) {

					// Command: /roboticstaff blocked

					if (sender.hasPermission("roboticstaff.blockedcommands.list")) {
						Functions.sendSenderMessage(sender, Messages.blockedList);
						for (String blocked : Main.getPlugin().getConfig().getStringList("blockedcommands-general")) {
							sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + blocked);
						}
						Functions.sendSenderMessage(sender, Messages.blockedPerWorldTip);
					}
				} else {

					// Command: /roboticstaff anythingElse

					Functions.sendSenderMessage(sender, Messages.strangeRoboticstaffArguments.replace("%s", args[0]));
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("blocked")) {

					// Command: /roboticstaff blocked <world>

					if (sender.hasPermission("roboticstaff.blockedcommands.list")) {
						try {
							Functions.sendSenderMessage(sender, Messages.blockedPerWorldList.replace("%s", args[1]));
							for (String blocked : Main.getPlugin().getConfig().getStringList("blockedcommands-perworld." + args[1])) {
								sender.sendMessage(ChatColor.RED + ">> " + ChatColor.YELLOW + blocked);
							}
						} catch (Exception e) {
							Functions.sendSenderMessage(sender, Messages.blockedWorldNotFound.replace("%s", args[1]));
						}
					}
				} else {
					Functions.sendSenderMessage(sender, Messages.strangeRoboticstaffArguments.replace("%s", args[0]));
				}
			} else {
				Functions.sendSenderMessage(sender, Messages.strangeRoboticstaffArguments.replace("%s", args[0]));
			}
			return true;
		}

		// Command: /helpme

		if (label.equalsIgnoreCase("helpme")) {
			if (Main.getPlugin().getConfig().getBoolean("helpme-enabled")) {
				if (args.length == 0) {
					Functions.sendSenderMessage(sender, Main.getPlugin().getConfig().getString("helpme-help"));
					int i = 0;
					for (String ask : Main.getPlugin().getConfig().getStringList("helpme-asks")) {
						sender.sendMessage(ChatColor.DARK_RED + " " + i + ChatColor.DARK_GRAY + " > " + ChatColor.DARK_GREEN + ChatColor.translateAlternateColorCodes('&', ask));
						i++;
					}
				} else if (args.length == 1) {
					try {
						int int_arg = Integer.parseInt(args[0]);
						if (int_arg <= Main.getPlugin().getConfig().getStringList("helpme-asks").size() - 1) {
							sender.sendMessage("\n" + ChatColor.DARK_GREEN + ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getStringList("helpme-asks").get(int_arg)) + ChatColor.DARK_GRAY + "\n>> " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getStringList("helpme-answers").get(int_arg)));
						} else {
							sender.sendMessage(ChatColor.RED + Main.getPlugin().getConfig().getString("helpme-error") + ChatColor.YELLOW + " " + Main.getPlugin().getConfig().getString("helpme-error-1"));
						}
					} catch (Exception e) {
						sender.sendMessage(ChatColor.RED + Main.getPlugin().getConfig().getString("helpme-error") + ChatColor.YELLOW + " " + Main.getPlugin().getConfig().getString("helpme-error-2"));
					}
				} else {
					sender.sendMessage(ChatColor.RED + Main.getPlugin().getConfig().getString("helpme-error") + ChatColor.YELLOW + " " + Main.getPlugin().getConfig().getString("helpme-error-3"));
				}
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("helpme-disabled-message")));
			}
			return true;
		}
		return false;
	}
}
