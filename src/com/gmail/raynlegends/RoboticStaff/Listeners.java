package com.gmail.raynlegends.RoboticStaff;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

	private Main plugin;
	private Functions functions;

	public Listeners(Main instance, Functions functionsInstance) {
		plugin = instance;
		functions = functionsInstance;
	}

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission("roboticstaff.blockedcommands.bypass")) {

			// Blocked commands (general)

			List<String> blockedCommands = plugin.getConfig().getStringList("blockedcommands-general");
			for (String command : blockedCommands) {
				if (event.getMessage().toLowerCase().startsWith("/" + command)) {
					event.setCancelled(true);
					functions.sendNoprefixMessage(player, plugin.getConfig().getString("blockedcommands-message"));
				}
			}

			// Blocked commands (perworld)

			try {
				List<String> blockedCommandsPerWorld = plugin.getConfig().getStringList("blockedcommands-perworld." + event.getPlayer().getWorld().getName());
				for (String command : blockedCommandsPerWorld) {
					if (event.getMessage().toLowerCase().startsWith("/" + command)) {
						event.setCancelled(true);
						functions.sendNoprefixMessage(player, plugin.getConfig().getString("blockedcommands-message"));
					}
				}
			} catch (Exception e) {
				//TODO: Report that?
			}
		}
	}

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		// Antispam

		if (plugin.getConfig().getBoolean("antispam-delay.enabled") && !event.getPlayer().hasPermission("roboticstaff.antispam.bypass.delay")) {
			if (plugin.chatMap.get(event.getPlayer()) == null) {
				plugin.chatMap.put(event.getPlayer(), Long.valueOf(System.currentTimeMillis()));
			}
			if (plugin.chatMap.get(event.getPlayer()) > System.currentTimeMillis()) {
				event.setCancelled(true);
				long time = plugin.chatMap.get(event.getPlayer()) - System.currentTimeMillis();
				functions.sendPlayerMessage(event.getPlayer(), plugin.getConfig().getString("antispam-delay.message").replace("%time%", String.format("%d second(s)", TimeUnit.MILLISECONDS.toSeconds(time) + 1L - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)))));
				return;
			} else {
				plugin.chatMap.remove(event.getPlayer());
				try {
					plugin.chatMap.put(event.getPlayer(), System.currentTimeMillis() + plugin.getConfig().getInt("antispam-delay.delay-between-messages") * 1000);
				} catch (Exception e) {
					plugin.chatMap.put(event.getPlayer(), System.currentTimeMillis() + 1000L);
				}
			}
		}

		if (plugin.getConfig().getBoolean("antispam-ipspam.enabled") && !event.getPlayer().hasPermission("roboticstaff.antispam.bypass.ipspam") && event.getMessage().toLowerCase().matches("(?s).*([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]).*")) {
			String message_ipspam_replaced = event.getMessage().replaceAll("(?s)([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])", plugin.getConfig().getString("antispam-ipspam.replace-with"));
			event.setMessage(message_ipspam_replaced);
			String commandIpSpam = plugin.getConfig().getString("antispam-ipspam.command-on-ipspam").replace("%player%", player.getName());
			if(!commandIpSpam.equals("")) {
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), commandIpSpam.replace("/", ""));
			}
		}

		if (plugin.getConfig().getBoolean("antispam-websitespam.enabled") && !event.getPlayer().hasPermission("roboticstaff.antispam.bypass.websitespam") && event.getMessage().toLowerCase().matches(".*[-a-zA-Z0-9.][-a-zA-Z0-9.][-a-zA-Z0-9.]\\.[-a-zA-Z][-a-zA-Z].*")) {
			if (plugin.getConfig().getString("antispam-websitespam.replace-with").equals("")) {
				event.setCancelled(true);
			} else {
				event.setMessage(plugin.getConfig().getString("antispam-websitespam.replace-with"));
			}
			String commandWebsiteSpam = plugin.getConfig().getString("antispam-websitespam.command-on-websitespam").replace("%player%", player.getName());
			if(!commandWebsiteSpam.equals("")) {
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), commandWebsiteSpam.replace("/", ""));
			}
		}

		// Autoanswer

		if (plugin.getConfig().getBoolean("autoanswer-enabled") && !player.hasPermission("roboticstaff.autoanswer.bypass")) {
			int tagInt = 0;
			for (String tag : plugin.tags) {
				tag = tag.toLowerCase().replace(" ", "");
				String message = event.getMessage().toLowerCase().replace(" ", "");
				if (tag.contains("%or%")) {
					String[] splittedTagArray = tag.split("%or%");
					for (String splittedTag : Arrays.asList(splittedTagArray)) {
						if (splittedTag.contains(",")) {
							String[] doubleSplittedTagArray = splittedTag.split(",");
							int messageTags = 0;
							for (String doubleSplittedTag : Arrays.asList(doubleSplittedTagArray)) {
								if (message.contains(doubleSplittedTag)) {
									messageTags++;
								}
								if (messageTags == doubleSplittedTagArray.length) {
									if (plugin.tag_answers.get(tagInt).contains("@delete")) {
										event.setCancelled(true);
									}
									functions.executeAutoanswerAnswer(player, plugin.tag_answers.get(tagInt));
									return;
								}
							}
						} else if (message.contains(splittedTag)) {
							if (plugin.tag_answers.get(tagInt).contains("@delete")) {
								event.setCancelled(true);
							}
							functions.executeAutoanswerAnswer(player, plugin.tag_answers.get(tagInt));
							return;
						}
					}
				} else if (tag.contains(",")) {
					String[] splittedTagArray = tag.split(",");
					int messageTags = 0;
					for (String splittedTag : Arrays.asList(splittedTagArray)) {
						if (message.contains(splittedTag)) {
							messageTags++;
						}
						if (messageTags == splittedTagArray.length) {
							if (plugin.tag_answers.get(tagInt).contains("@delete")) {
								event.setCancelled(true);
							}
							functions.executeAutoanswerAnswer(player, plugin.tag_answers.get(tagInt));
							return;
						}
					}
				} else if (message.contains(tag)) {
					if (plugin.tag_answers.get(tagInt).contains("@delete")) {
						event.setCancelled(true);
					}
					functions.executeAutoanswerAnswer(player, plugin.tag_answers.get(tagInt));
					return;
				}
				tagInt++;
			}
		}

		// AntiSwearing

		if(plugin.getConfig().getBoolean("antiswearing-enabled") && !player.hasPermission("roboticstaff.antiswearing.bypass")) {
			for (String word : plugin.words) {
				word.toLowerCase();
				event.setMessage(event.getMessage().replaceAll("(?i)" + word, ChatColor.translateAlternateColorCodes('&', plugin.beep)));
			}
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!plugin.join.equals("")) {
			functions.sendPlayerMessage(player, plugin.join);
		}
		if (plugin.getConfig().getBoolean("playerjoin.enabled")) {
			for (String playerCommand : plugin.getConfig().getStringList("playerjoin.commands")) {
				playerCommand = playerCommand.replace("%player%", player.getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		if (plugin.getConfig().getBoolean("playerdeath.enabled")) {
			for (String playerCommand : plugin.getConfig().getStringList("playerdeath.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getEntity().getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if (plugin.getConfig().getBoolean("playerquit.enabled")) {
			for (String playerCommand : plugin.getConfig().getStringList("playerquit.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerKickEvent(PlayerQuitEvent event) {
		if (plugin.getConfig().getBoolean("playerkick.enabled")) {
			for (String playerCommand : plugin.getConfig().getStringList("playerkick.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		if (plugin.getConfig().getBoolean("playerchangedworld.enabled")) {
			for (String playerCommand : plugin.getConfig().getStringList("playerchangedworld.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event) {
		if (plugin.getConfig().getBoolean("playerlevelchange.enabled")) {
			for (String playerCommand : plugin.getConfig().getStringList("playerlevelchange.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		if (plugin.getConfig().getBoolean("playergamemodechange.enabled")) {
			for (String playerCommand : plugin.getConfig().getStringList("playergamemodechange.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}
}