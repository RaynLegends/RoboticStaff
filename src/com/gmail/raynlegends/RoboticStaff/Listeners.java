package com.gmail.raynlegends.RoboticStaff;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {

	private Main plugin;
	private Functions functions;

	public Listeners(Main instance, Functions functionsInstance) {
		plugin = instance;
		functions = functionsInstance;
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!plugin.getConfig().getString("roboticstaff-join").equals("")) {
			functions.sendPlayerMessage(player, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("roboticstaff-join")));
		}
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
					functions.sendPlayerMessage(player, plugin.getConfig().getString("blockedcommands-message"));
				}
			}

			// Blocked commands (perworld)

			try {
				List<String> blockedCommandsPerWorld = plugin.getConfig().getStringList("blockedcommands-perworld." + event.getPlayer().getWorld().getName());
				for (String command : blockedCommandsPerWorld) {
					if (event.getMessage().toLowerCase().startsWith("/" + command)) {
						event.setCancelled(true);
						player.sendMessage(plugin.getConfig().getString("blockedcommands-message"));
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

		if (!player.hasPermission("roboticstaff.antispam.bypass")) {
			if (plugin.chatMap.get(player) > System.currentTimeMillis()) {
				event.setCancelled(true);
				long time = plugin.chatMap.get(player) - System.currentTimeMillis();
				functions.sendPlayerMessage(player, plugin.getConfig().getString("antispam-message").replace("%time%", "" + time));
			} else {
				plugin.chatMap.remove(player);
				plugin.chatMap.put(player, System.currentTimeMillis() + plugin.getConfig().getInt("antispam-delay") * 10000);
			}
		}

		// Autoanswer

		if (!player.hasPermission("roboticstaff.autoanswer.bypass")) {
			int tagInt = 0;
			for (String tag : plugin.tags) {
				tag.toLowerCase().replace(" ", "");
				String message = event.getMessage().toLowerCase().replace(" ", "");
				if (tag.contains("%or%")) {
					String[] splittedTagArray = tag.split("%or%");
					for (String splittedTag : Arrays.asList(splittedTagArray)) {
						if (splittedTag.contains(",")) {
							String[] doubleSplittedTagArray = splittedTag.split("%or%");
							int messageTags = 0;
							for (String doubleSplittedTag : Arrays.asList(doubleSplittedTagArray)) {
								if (message.contains(doubleSplittedTag)) {
									messageTags++;
								}
								if (messageTags == doubleSplittedTagArray.length) {
									functions.executeAutoanswerAnswer(player, plugin.tag_answers.get(tagInt));
									return;
								}
							}
						} else if (message.contains(splittedTag)) {
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
							functions.executeAutoanswerAnswer(player, plugin.tag_answers.get(tagInt));
							return;
						}
					}
				} else if (message.contains(tag)) {
					functions.executeAutoanswerAnswer(player, plugin.tag_answers.get(tagInt));
					return;
				}
				tagInt++;
			}
		}

		// AntiSwearing
		
		if(!event.getPlayer().hasPermission("roboticstaff.antiswearing.bypass")) {
			for (String word : plugin.words) {
				word.toLowerCase();
				event.setMessage(event.getMessage().replaceAll("(?i)" + word, plugin.beep));
			}
		}
	}
}