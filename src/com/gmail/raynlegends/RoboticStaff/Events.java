package com.gmail.raynlegends.RoboticStaff;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
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

public class Events implements Listener {

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();

		if(!player.hasPermission("roboticstaff.blockedcommands.bypass")) {

			// Blocked commands (general)

			List<String> blockedCommands = Main.getPlugin().getConfig().getStringList("blockedcommands-general");
			for (String command : blockedCommands) {
				if (event.getMessage().toLowerCase().startsWith("/" + command)) {
					event.setCancelled(true);
					Functions.sendNoprefixMessage(player, Main.getPlugin().getConfig().getString("blockedcommands-message"));
				}
			}

			// Blocked commands (perworld)

			try {
				List<String> blockedCommandsPerWorld = Main.getPlugin().getConfig().getStringList("blockedcommands-perworld." + event.getPlayer().getWorld().getName());
				for (String command : blockedCommandsPerWorld) {
					if (event.getMessage().toLowerCase().startsWith("/" + command)) {
						event.setCancelled(true);
						Functions.sendNoprefixMessage(player, Main.getPlugin().getConfig().getString("blockedcommands-message"));
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

		if (Main.getPlugin().getConfig().getBoolean("antispam-delay.enabled") && !event.getPlayer().hasPermission("roboticstaff.antispam.bypass.delay")) {
			if (Functions.getChatAllowedTime(player) == null) {
				Functions.setChatAllowedTime(player, System.currentTimeMillis());
			}
			if (Functions.getChatAllowedTime(player) > System.currentTimeMillis()) {
				event.setCancelled(true);
				long time = Functions.getChatAllowedTime(player) - System.currentTimeMillis();
				Functions.sendPlayerMessage(event.getPlayer(), Main.getPlugin().getConfig().getString("antispam-delay.message").replace("%time%", String.format("%d second(s)", TimeUnit.MILLISECONDS.toSeconds(time) + 1L - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)))));
				return;
			} else {
				try {
					Functions.setChatAllowedTime(player, System.currentTimeMillis() + Main.getPlugin().getConfig().getInt("antispam-delay.delay-between-messages") * 1000);
				} catch (Exception e) {
					Functions.setChatAllowedTime(player, System.currentTimeMillis() + 1000L);
				}
			}
		}

		if (!event.isCancelled()) {
			if (Main.getPlugin().getConfig().getBoolean("antispam-ipspam.enabled") && !event.getPlayer().hasPermission("roboticstaff.antispam.bypass.ipspam") && event.getMessage().toLowerCase().matches("(?s).*([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5]).*")) {
				String message_ipspam_replaced = event.getMessage().replaceAll("(?s)([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])", Main.getPlugin().getConfig().getString("antispam-ipspam.replace-with"));
				event.setMessage(message_ipspam_replaced);
				final String commandIpSpam = Main.getPlugin().getConfig().getString("antispam-ipspam.command-on-ipspam").replace("%player%", player.getName());
				if(!commandIpSpam.equals("")) {
					Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandIpSpam.replace("/", ""));
						}
					}, 1);
				}
			}
		}

		if (!event.isCancelled()) {
			if (Main.getPlugin().getConfig().getBoolean("antispam-websitespam.enabled") && !event.getPlayer().hasPermission("roboticstaff.antispam.bypass.websitespam") && event.getMessage().toLowerCase().matches(".*[-a-zA-Z0-9.][-a-zA-Z0-9.][-a-zA-Z0-9.]\\.[-a-zA-Z][-a-zA-Z].*")) {
				if (Main.getPlugin().getConfig().getString("antispam-websitespam.replace-with").equals("")) {
					event.setCancelled(true);
				} else {
					event.setMessage(Main.getPlugin().getConfig().getString("antispam-websitespam.replace-with"));
				}
				final String commandWebsiteSpam = Main.getPlugin().getConfig().getString("antispam-websitespam.command-on-websitespam").replace("%player%", player.getName());
				if(!commandWebsiteSpam.equals("")) {
					Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandWebsiteSpam.replace("/", ""));
						}
					}, 1);
				}
			}
		}

		// Autoanswer

		if (!event.isCancelled()) {
			if (Main.getPlugin().getConfig().getBoolean("autoanswer-enabled") && !player.hasPermission("roboticstaff.autoanswer.bypass")) {
				int tagInt = 0;
				for (String tag : Main.getPlugin().getConfig().getStringList("autoanswer-tags")) {
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
										if (Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt).contains("@delete")) {
											event.setCancelled(true);
										}
										Functions.executeAutoanswerAnswer(player, Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt));
										return;
									}
								}
							} else if (message.contains(splittedTag)) {
								if (Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt).contains("@delete")) {
									event.setCancelled(true);
								}
								Functions.executeAutoanswerAnswer(player, Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt));
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
								if (Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt).contains("@delete")) {
									event.setCancelled(true);
								}
								Functions.executeAutoanswerAnswer(player, Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt));
								return;
							}
						}
					} else if (message.contains(tag)) {
						if (Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt).contains("@delete")) {
							event.setCancelled(true);
						}
						Functions.executeAutoanswerAnswer(player, Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").get(tagInt));
						return;
					}
					tagInt++;
				}
			}
		}

		// AntiCaps

		if (!event.isCancelled()) {
			if (Main.getPlugin().getConfig().getBoolean("anticaps-enabled") && !event.getPlayer().hasPermission("roboticstaff.anticaps.bypass")) {
				if (event.getMessage().length() >= Main.getPlugin().getConfig().getInt("anticaps-minimum")) {
					Integer upperCase = 0;
					for (char character : event.getMessage().toCharArray()) {
						if (Character.isUpperCase(character)) {
							upperCase++;
						}
					}

					Double percentage = (upperCase * 1D) / (event.getMessage().length() * 1D) * 100D;
					if (percentage > Main.getPlugin().getConfig().getDouble("anticaps-percentage")) {
						char[] characters = event.getMessage().toLowerCase().toCharArray();
						characters[0] = Character.toUpperCase(characters[0]);
						event.setMessage(String.valueOf(characters));
					}
				}
			}
		}

		// AntiSwearing

		if (!event.isCancelled()) {
			if (Main.getPlugin().getConfig().getBoolean("antiswearing-enabled") && !player.hasPermission("roboticstaff.antiswearing.bypass")) {
				for (String word : Main.getPlugin().getConfig().getStringList("antiswearing-words")) {
					word.toLowerCase();
					event.setMessage(event.getMessage().replaceAll("(?i)" + word, ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("antiswearing-beep"))));
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		if (!Main.getPlugin().getConfig().getString("roboticstaff-join").equals("")) {
			Functions.sendPlayerMessage(player, Main.getPlugin().getConfig().getString("roboticstaff-join"));
		}

		if (Main.getPlugin().getConfig().getBoolean("playerjoin.enabled")) {
			for (String playerCommand : Main.getPlugin().getConfig().getStringList("playerjoin.commands")) {
				playerCommand = playerCommand.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event) {
		if (Main.getPlugin().getConfig().getBoolean("playerdeath.enabled")) {
			for (String playerCommand : Main.getPlugin().getConfig().getStringList("playerdeath.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getEntity().getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = event.getPlayer();

		if (Main.getPlugin().getConfig().getBoolean("playerquit.enabled")) {
			for (String playerCommand : Main.getPlugin().getConfig().getStringList("playerquit.commands")) {
				playerCommand = playerCommand.replace("%player%", player.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand.replace("/", ""));
			}
		}

		Functions.removeChatAllowedTime(player);
	}

	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
		if (Main.getPlugin().getConfig().getBoolean("playerchangedworld.enabled")) {
			for (String playerCommand : Main.getPlugin().getConfig().getStringList("playerchangedworld.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event) {
		if (Main.getPlugin().getConfig().getBoolean("playerlevelchange.enabled")) {
			for (String playerCommand : Main.getPlugin().getConfig().getStringList("playerlevelchange.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}

	@EventHandler
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		if (Main.getPlugin().getConfig().getBoolean("playergamemodechange.enabled")) {
			for (String playerCommand : Main.getPlugin().getConfig().getStringList("playergamemodechange.commands")) {
				playerCommand = playerCommand.replace("%player%", event.getPlayer().getName());
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), playerCommand.replace("/", ""));
			}
		}
	}
}