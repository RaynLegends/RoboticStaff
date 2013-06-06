package com.gmail.raynlegends.RoboticStaff;

// Import
import java.io.*;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;    
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

// Main class
public class RoboticStaff extends JavaPlugin implements Listener {
  /**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		// Create version string
		String version = "1.5";
		String config_version = (RoboticStaff.this.getConfig().getString("config-version"));
		// Enable log
		getLogger().info("Check the BukkitDev page for info!");
		getLogger().info("http://dev.bukkit.org/server-mods/roboticstaff/");
		// Config saving/deleting/rewriting
		this.saveDefaultConfig();
		try {
			double double_config_version = Double.parseDouble(config_version);
			double double_version = Double.parseDouble(version);
			if (double_version != double_config_version) {
				// Delete an possible old config_old.yml file
				File veryOldConfigFile = new File(RoboticStaff.this.getDataFolder(), "config_old.yml");
				veryOldConfigFile.delete();
				// Rename config.yml into config_old.yml
				File configFile = new File(RoboticStaff.this.getDataFolder(), "config.yml");
				configFile.renameTo(new File(RoboticStaff.this.getDataFolder(), "config_old.yml"));
				// Delete config.yml
				File oldConfigFile = new File(RoboticStaff.this.getDataFolder(), "config.yml");
				oldConfigFile.delete();
				// Create new config.yml
				this.saveDefaultConfig();
				getLogger().info("Configuration rewrited!");
			}
		} catch (Exception e) {
			// Delete an possible old config_old.yml file
			File veryOldConfigFile = new File(RoboticStaff.this.getDataFolder(), "config_old.yml");
			veryOldConfigFile.delete();
			// Rename config.yml into config_old.yml
			File configFile = new File(RoboticStaff.this.getDataFolder(), "config.yml");
			configFile.renameTo(new File(RoboticStaff.this.getDataFolder(), "config_old.yml"));
			// Delete config.yml
			File oldConfigFile = new File(RoboticStaff.this.getDataFolder(), "config.yml");
			oldConfigFile.delete();
			// Create new config.yml
			this.saveDefaultConfig();
			getLogger().info("Configuration rewrited!");
		}
		// Config checking
		List<?> ask = RoboticStaff.this.getConfig().getList("helpme-ask");
		String[] array_ask = ask.toArray(new String[ask.size()]);
		int number_ask = array_ask.length - 1;
		List<?> answer = RoboticStaff.this.getConfig().getList("helpme-answer");
		String[] array_answer = answer.toArray(new String[answer.size()]);
		int number_answer = array_answer.length - 1;
		if (number_ask != number_answer) {
			getLogger().info("WARNING: There are not the same number of ask and answer!!!");
		}
		List<?> tag = RoboticStaff.this.getConfig().getList("autoanswer-tag");
		String[] array_tag = tag.toArray(new String[tag.size()]);
		int number_tag = array_tag.length - 1;
		List<?> tag_answer = RoboticStaff.this.getConfig().getList("autoanswer-tag-answer");
		String[] array_tag_answer = tag_answer.toArray(new String[tag_answer.size()]);
		int number_tag_answer = array_tag_answer.length - 1;
		if (number_tag != number_tag_answer) {
			getLogger().info("WARNING: There are not the same number of tag and tag answer!!!");
		}
		// Final log
		getLogger().info("Successfully enabled!");
	}

	@Override
	public void onDisable() {
		getLogger().info("Successfully disabled!");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if (RoboticStaff.this.getConfig().getString("roboticstaff-join") != "") {
			player.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + RoboticStaff.this.getConfig().getString("roboticstaff-join"));
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

		// Start of general command

		if(cmd.getName().equalsIgnoreCase("roboticstaff")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					this.reloadConfig();
					// Config checking
					List<?> ask = RoboticStaff.this.getConfig().getList("helpme-ask");
					String[] array_ask = ask.toArray(new String[ask.size()]);
					int number_ask = array_ask.length - 1;
					List<?> answer = RoboticStaff.this.getConfig().getList("helpme-answer");
					String[] array_answer = answer.toArray(new String[answer.size()]);
					int number_answer = array_answer.length - 1;
					if (number_ask != number_answer) {
						sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + "WARNING: There are not the same number of ask and answer!!!");
					}
					List<?> tag = RoboticStaff.this.getConfig().getList("autoanswer-tag");
					String[] array_tag = tag.toArray(new String[tag.size()]);
					int number_tag = array_tag.length - 1;
					List<?> tag_answer = RoboticStaff.this.getConfig().getList("autoanswer-tag-answer");
					String[] array_tag_answer = tag_answer.toArray(new String[tag_answer.size()]);
					int number_tag_answer = array_tag_answer.length - 1;
					if (number_tag != number_tag_answer) {
						sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + "WARNING: There are not the same number of tag and tag answer!!!");
					}
					sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + RoboticStaff.this.getConfig().getString("roboticstaff-reload"));
				} else if (args[0].equalsIgnoreCase("tag")) {
					List<?> tag = RoboticStaff.this.getConfig().getList("autoanswer-tag");
					String[] array_tag = tag.toArray(new String[tag.size()]);
					int number_tag = array_tag.length - 1;
					sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + RoboticStaff.this.getConfig().getString("roboticstaff-tag"));
					for (int i=0; i<=number_tag; i++) {
						sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + array_tag[i]);
					}
				} else if (args[0].equalsIgnoreCase("words")) {
					List<?> words = RoboticStaff.this.getConfig().getList("antiswearing-words");
					String[] array_words = words.toArray(new String[words.size()]);
					int number_words = array_words.length - 1;
					sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + RoboticStaff.this.getConfig().getString("roboticstaff-words"));
					for (int i=0; i<=number_words; i++) {
						sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + array_words[i]);
					}
				} else {
					sender.sendMessage(ChatColor.RED + "ERROR:"+  ChatColor.YELLOW + " " + RoboticStaff.this.getConfig().getString("roboticstaff-error-1"));
				}
			} else {
				sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + RoboticStaff.this.getConfig().getString("roboticstaff-basic"));
			}
			return true;
		}

		// End of general command and start of Helpme

		if(cmd.getName().equalsIgnoreCase("helpme")){ // If anyone type /helpme
			// Array ask
			List<?> ask = RoboticStaff.this.getConfig().getList("helpme-ask");
			String[] array_ask = ask.toArray(new String[ask.size()]);
			int number_ask = array_ask.length - 1;
			// Array answer
			List<?> answer = RoboticStaff.this.getConfig().getList("helpme-answer");
			String[] array_answer = answer.toArray(new String[answer.size()]);
			int number_answer = array_answer.length - 1;
			// Check if debug is true or false
			if (args.length == 0) { // Code /helpme
				sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+  ChatColor.YELLOW + RoboticStaff.this.getConfig().getString("helpme-help"));
				for (int i=0; i<=number_ask; i++) {
					sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " "+ i +" - "+ array_ask[i]);
				}
			} else if (args.length == 1) {
				try { 
					int int_args = Integer.parseInt(args[0]);
					if (int_args <= number_answer) {
						sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + int_args +" - " + array_answer[int_args]);
					} else {
						sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + RoboticStaff.this.getConfig().getString("helpme-error-1"));
					}
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + RoboticStaff.this.getConfig().getString("helpme-error-2"));
				}
			} else if (args.length >= 2) {
				sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + RoboticStaff.this.getConfig().getString("helpme-error-3"));
			}
			return true; //If this has happened the function will return true.
		} 
		return false; // If this hasn't happened the a value of false will be returned.
	}

	// End of Helpme and start of AutoAnswer and AntiSwearing

	@EventHandler
	public void sendsChatMessage(AsyncPlayerChatEvent event) {
		// Array tag
		List<?> tag = RoboticStaff.this.getConfig().getList("autoanswer-tag");
		String[] array_tag = tag.toArray(new String[tag.size()]);
		// Array tag_answer
		List<?> tag_answer = RoboticStaff.this.getConfig().getList("autoanswer-tag-answer");
		String[] array_tag_answer = tag_answer.toArray(new String[tag.size()]);
		// Do the for
		for(int i =0; i < array_tag.length; i++)
		{
			String cleaned_tag = array_tag[i].toLowerCase().replace(" ", "");
			if (cleaned_tag.contains("%or%")) {
				String[] splitted_cleaned_tag = cleaned_tag.split("%or%");
				for(int j =0; j < splitted_cleaned_tag.length; j++) {
					if (splitted_cleaned_tag[j].contains(",")) {
						String[] very_splitted_cleaned_tag = splitted_cleaned_tag[j].split(",");
						int message_tags = 0;
						String is_replied = "false";
						for(int k =0; k < very_splitted_cleaned_tag.length; k++) {
							if(event.getMessage().toLowerCase().replace(" ", "").contains(very_splitted_cleaned_tag[k])) {
								message_tags = message_tags + 1;
							}
							if(message_tags == very_splitted_cleaned_tag.length) {
								if (array_tag_answer[i].startsWith("/")) {
									Player player = event.getPlayer();
									String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
									String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
								} else if (array_tag_answer[i].startsWith("%alias%")){
									Player player = event.getPlayer();
									String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
									String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
									String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%alias%", "");
									sendAliasMessage(event.getPlayer(), very_very_cleaned_tag_answer);
								} else {
									Player player = event.getPlayer();
									String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
									String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
									sendMessage(event.getPlayer(), very_cleaned_tag_answer);
									// event.getPlayer().sendMessage(ChatColor.GOLD + "[RoboticStaff] "+ ChatColor.YELLOW + very_cleaned_tag_answer);
								}
								event.setCancelled(true);
								/* TODO WITH DELAY if (array_tag_answer[i].contains("%delete%")) {
									event.setCancelled(true);
								} */
								is_replied = "true";
								break;
							}
						}
						if (is_replied == "true") {
							is_replied = "false";
							break;
						}
					} else if(event.getMessage().toLowerCase().replace(" ", "").contains(splitted_cleaned_tag[j])) {
						if (array_tag_answer[i].startsWith("/")) {
							Player player = event.getPlayer();
							String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
							String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
						} else if (array_tag_answer[i].startsWith("%alias%")){
							Player player = event.getPlayer();
							String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
							String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
							String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%alias%", "");
							sendAliasMessage(event.getPlayer(), very_very_cleaned_tag_answer);
						} else {
							Player player = event.getPlayer();
							String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
							String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
							sendMessage(event.getPlayer(), very_cleaned_tag_answer);
							// event.getPlayer().sendMessage(ChatColor.GOLD + "[RoboticStaff] "+ ChatColor.YELLOW + very_cleaned_tag_answer);
						}
						event.setCancelled(true);
						/* TODO WITH DELAY if (array_tag_answer[i].contains("%delete%")) {
							event.setCancelled(true);
						} */
						break;
					}
				}
			} else if (cleaned_tag.contains(",")) {
				String[] very_splitted_cleaned_tag = cleaned_tag.split(",");
				int message_tags = 0;
				String is_replied = "false";
				for(int k =0; k < very_splitted_cleaned_tag.length; k++) {
					if(event.getMessage().toLowerCase().replace(" ", "").contains(very_splitted_cleaned_tag[k])) {
						message_tags = message_tags + 1;
					}
					if(message_tags == very_splitted_cleaned_tag.length) {
						if (array_tag_answer[i].startsWith("/")) {
							Player player = event.getPlayer();
							String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
							String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
						} else if (array_tag_answer[i].startsWith("%alias%")){
							Player player = event.getPlayer();
							String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
							String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
							String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%alias%", "");
							sendAliasMessage(event.getPlayer(), very_very_cleaned_tag_answer);
						} else {
							Player player = event.getPlayer();
							String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
							String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
							sendMessage(event.getPlayer(), very_cleaned_tag_answer);
							// event.getPlayer().sendMessage(ChatColor.GOLD + "[RoboticStaff] "+ ChatColor.YELLOW + very_cleaned_tag_answer);
						}
						event.setCancelled(true);
						/* TODO WITH DELAY if (array_tag_answer[i].contains("%delete%")) {
							event.setCancelled(true);
						} */
						is_replied = "true";
						break;
					}
				}
				if (is_replied == "true") {
					is_replied = "false";
					break;
				}
			} else if(event.getMessage().toLowerCase().replace(" ", "").contains(cleaned_tag)) {
				// If the message contains a normal tag
				if (array_tag_answer[i].startsWith("/")) {
					Player player = event.getPlayer();
					String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
					String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
				} else if (array_tag_answer[i].startsWith("%alias%")){
					Player player = event.getPlayer();
					String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
					String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
					String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%alias%", "");
					sendAliasMessage(event.getPlayer(), very_very_cleaned_tag_answer);
				} else {
					Player player = event.getPlayer();
					String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
					String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
					sendMessage(event.getPlayer(), very_cleaned_tag_answer);
					// event.getPlayer().sendMessage(ChatColor.GOLD + "[RoboticStaff] "+ ChatColor.YELLOW + very_cleaned_tag_answer);
				}
				event.setCancelled(true);
				/* TODO WITH DELAY if (array_tag_answer[i].contains("%delete%")) {
					event.setCancelled(true);
				} */
				break;
			}
		}
		// Array words
		List<?> words = RoboticStaff.this.getConfig().getList("antiswearing-words");
		String[] array_words = words.toArray(new String[words.size()]);
		String beep = RoboticStaff.this.getConfig().getString("antiswearing-beep");
		for(int i =0; i < array_words.length; i++)
		{
			String cleaned_words = array_words[i].toLowerCase();
			if(event.getMessage().toLowerCase().contains(cleaned_words)) {
				event.setMessage(event.getMessage().replaceAll("(?i)" + cleaned_words, beep));
			}
		}
	}

	// End of AutoAnswer and AntiSwearing and start of PluginMessage(TODO: Finish this)

	public void sendMessage(Player player, String string) {
		player.sendMessage(ChatColor.GOLD + "[RoboticStaff] "+ ChatColor.YELLOW + string);
	}

	public void sendAliasMessage(Player player, String string) {
		String colored_alias = RoboticStaff.this.getConfig().getString("autoanswer-alias");
		colored_alias = colored_alias.replace("&0", "" + ChatColor.BLACK);
		colored_alias = colored_alias.replace("&1", "" + ChatColor.DARK_BLUE);
		colored_alias = colored_alias.replace("&2", "" + ChatColor.DARK_GREEN);
		colored_alias = colored_alias.replace("&3", "" + ChatColor.DARK_AQUA);
		colored_alias = colored_alias.replace("&4", "" + ChatColor.DARK_RED);
		colored_alias = colored_alias.replace("&5", "" + ChatColor.DARK_PURPLE);
		colored_alias = colored_alias.replace("&6", "" + ChatColor.GOLD);
		colored_alias = colored_alias.replace("&7", "" + ChatColor.GRAY);
		colored_alias = colored_alias.replace("&8", "" + ChatColor.DARK_GRAY);
		colored_alias = colored_alias.replace("&9", "" + ChatColor.BLUE);
		colored_alias = colored_alias.replace("&a", "" + ChatColor.GREEN);
		colored_alias = colored_alias.replace("&b", "" + ChatColor.AQUA);
		colored_alias = colored_alias.replace("&c", "" + ChatColor.RED);
		colored_alias = colored_alias.replace("&d", "" + ChatColor.LIGHT_PURPLE);
		colored_alias = colored_alias.replace("&e", "" + ChatColor.YELLOW);
		colored_alias = colored_alias.replace("&f", "" + ChatColor.WHITE);
		colored_alias = colored_alias.replace("&k", "" + ChatColor.MAGIC);
		colored_alias = colored_alias.replace("&l", "" + ChatColor.BOLD);
		colored_alias = colored_alias.replace("&m", "" + ChatColor.STRIKETHROUGH);
		colored_alias = colored_alias.replace("&n", "" + ChatColor.UNDERLINE);
		colored_alias = colored_alias.replace("&o", "" + ChatColor.ITALIC);
		colored_alias = colored_alias.replace("&r", "" + ChatColor.RESET);
		player.sendMessage(colored_alias + string);
	}
}
