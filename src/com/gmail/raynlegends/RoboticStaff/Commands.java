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
				} else if (args[0].equalsIgnoreCase("toggle")) {

					// Command: /roboticstaff blocked

					if (sender.hasPermission("roboticstaff.toggle")) {
						Functions.sendSenderMessage(sender, "Modules: helpme, anticaps, autoanswer, antiswearing, blockedcommands, antispam-delay, antispam-ip, antispam-website; OnActions: playerjoin, playerquit, playerdeath, playerkick, playerchangedworld, playerlevelchange, playergamemodechange");
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
				} else if (args[0].equalsIgnoreCase("toggle")) {

					// Command: /roboticstaff blocked

					if (sender.hasPermission("roboticstaff.toggle")) {
						switch (args[1].toLowerCase()) {
						case "helpme": Main.getPlugin().getConfig().set("helpme-enabled", !Main.getPlugin().getConfig().getBoolean("helpme-enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("helpme-enabled") ? "Enabled!" : "Disabled!")); break;
						case "anticaps": Main.getPlugin().getConfig().set("anticaps-enabled", !Main.getPlugin().getConfig().getBoolean("anticaps-enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("anticaps-enabled") ? "Enabled!" : "Disabled!")); break; 
						case "autoanswer": Main.getPlugin().getConfig().set("autoanswer-enabled", !Main.getPlugin().getConfig().getBoolean("autoanswer-enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("autoanswer-enabled") ? "Enabled!" : "Disabled!")); break; 
						case "antiswearing": Main.getPlugin().getConfig().set("antiswearing-enabled", !Main.getPlugin().getConfig().getBoolean("antiswearing-enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("antiswearing-enabled") ? "Enabled!" : "Disabled!")); break;
						case "blockedcommands": Main.getPlugin().getConfig().set("blockedcommands-enabled", !Main.getPlugin().getConfig().getBoolean("blockedcommands-enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("blockedcommands-enabled") ? "Enabled!" : "Disabled!")); break;
						case "antispam-delay": Main.getPlugin().getConfig().set("antispam-delay.enabled", !Main.getPlugin().getConfig().getBoolean("antispam-delay.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("antispam-delay.enabled") ? "Enabled!" : "Disabled!")); break;
						case "antispam-ip": Main.getPlugin().getConfig().set("antispam-ipspam.enabled", !Main.getPlugin().getConfig().getBoolean("antispam-ipspam.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("antispam-ipspam.enabled") ? "Enabled!" : "Disabled!")); break;
						case "antispam-website": Main.getPlugin().getConfig().set("antispam-websitespam.enabled", !Main.getPlugin().getConfig().getBoolean("antispam-websitespam.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("antispam-websitespam.enabled") ? "Enabled!" : "Disabled!")); break;
						case "playerjoin": Main.getPlugin().getConfig().set("playerjoin.enabled", !Main.getPlugin().getConfig().getBoolean("playerjoin.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("playerjoin.enabled") ? "Enabled!" : "Disabled!")); break;
						case "playerquit": Main.getPlugin().getConfig().set("playerquit.enabled", !Main.getPlugin().getConfig().getBoolean("playerquit.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("playerquit.enabled") ? "Enabled!" : "Disabled!")); break;
						case "playerdeath": Main.getPlugin().getConfig().set("playerdeath.enabled", !Main.getPlugin().getConfig().getBoolean("playerdeath.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("playerdeath.enabled") ? "Enabled!" : "Disabled!")); break;
						case "playerkick": Main.getPlugin().getConfig().set("playerkick.enabled", !Main.getPlugin().getConfig().getBoolean("playerkick.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("playerkick.enabled") ? "Enabled!" : "Disabled!")); break;
						case "playerchangedworld": Main.getPlugin().getConfig().set("playerchangedworld.enabled", !Main.getPlugin().getConfig().getBoolean("playerchangedworld.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("playerchangedworld.enabled") ? "Enabled!" : "Disabled!")); break;
						case "playerlevelchange": Main.getPlugin().getConfig().set("playerlevelchange.enabled", !Main.getPlugin().getConfig().getBoolean("playerlevelchange.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("playerlevelchange.enabled") ? "Enabled!" : "Disabled!")); break;
						case "playergamemodechange": Main.getPlugin().getConfig().set("playergamemodechange.enabled", !Main.getPlugin().getConfig().getBoolean("playergamemodechange.enabled")); Functions.sendSenderMessage(sender, (Main.getPlugin().getConfig().getBoolean("playergamemodechange.enabled") ? "Enabled!" : "Disabled!")); break;
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
