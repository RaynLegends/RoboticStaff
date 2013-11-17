package com.gmail.raynlegends.RoboticStaff;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RoboticStaff extends JavaPlugin implements Listener {

	public static void main(String[] args)
	{
		//TODO: Auto-generated method stub
	}

	public final HashMap<Player, Long> chatMap = new HashMap<Player, Long>();

	@Override
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);

		//TODO: Remember to change version from here, from config and from plugin.yml
		String version = "1.9";
		String config_version = getConfig("config-version");

		getLogger().info("Check the BukkitDev page for info!");
		getLogger().info("http://dev.bukkit.org/server-mods/roboticstaff/");

		saveDefaultConfig();

		if (!version.equals(config_version)) {
			File veryOldConfigFile = new File(getDataFolder(), "config_old.yml");
			veryOldConfigFile.delete();

			File configFile = new File(getDataFolder(), "config.yml");
			configFile.renameTo(new File(getDataFolder(), "config_old.yml"));

			File oldConfigFile = new File(getDataFolder(), "config.yml");
			oldConfigFile.delete();

			saveDefaultConfig();
			getLogger().info("Configuration rewrited!");

			reloadConfig();
		}

		List<String> ask = getConfig().getStringList("helpme-ask");
		List<String> answer = getConfig().getStringList("helpme-answer");
		if (ask.size() != answer.size()) {
			getLogger().info("There are not the same number of ask and answer!");
		}

		List<String> tag = getConfig().getStringList("autoanswer-tag");
		List<String> tag_answer = getConfig().getStringList("autoanswer-tag-answer");
		if (tag.size() != tag_answer.size()) {
			getLogger().info("There are not the same number of tag and answer!");
		}

		if (getConfig("update-checker").equalsIgnoreCase("true")) {
			try {
				Updater updater = new Updater(this, "roboticstaff", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
				String latestVersion = updater.getLatestVersionString().replace("RoboticStaff - ", "").replace("Alpha v", "").replace("Beta v", "").replace("Release v", "");
				if (!latestVersion.contains(" | BUGGED")) {
					if (!latestVersion.equals(version)) {
						getLogger().info(updater.getLatestVersionString() + " is avaible! Check out the bukkitdev page :D");
					}
				}
			} catch (Exception e) {
			}
		}

		getLogger().info("Successfully enabled!");
	}

	@Override
	public void onDisable()
	{
		getLogger().info("Successfully disabled!");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!getConfig().getString("roboticstaff-join").equalsIgnoreCase("")) {
			sendPlayerMessage(player, getConfig("roboticstaff-join"));
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("roboticstaff")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("roboticstaff.reload")) {
						reloadConfig();

						List<String> ask = getConfig().getStringList("helpme-ask");
						List<String> answer = getConfig().getStringList("helpme-answer");
						if (ask.size() != answer.size()) {
							sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + "There are not the same number of ask and answer!!!");
						}

						List<String> tag = getConfig().getStringList("autoanswer-tag");
						List<String> tag_answer = getConfig().getStringList("autoanswer-tag-answer");
						if (tag.size() != tag_answer.size()) {
							sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + "There are not the same number of tag and tag answer!!!");
						}

						sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-reload"));
					}
				} else if (args[0].equalsIgnoreCase("tag")) {
					if (sender.hasPermission("roboticstaff.autoanswer.list")) {
						List<?> tag = getConfig().getList("autoanswer-tag");
						String[] array_tag = (String[])tag.toArray(new String[tag.size()]);
						int number_tag = array_tag.length - 1;
						sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-tag"));
						for (int i = 0; i <= number_tag; i++) {
							sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + array_tag[i]);
						}
					}
				}
				else if (args[0].equalsIgnoreCase("words")) {
					if (sender.hasPermission("roboticstaff.antiswearing.list")) {
						List<?> words = getConfig().getList("antiswearing-words");
						String[] array_words = (String[])words.toArray(new String[words.size()]);
						int number_words = array_words.length - 1;
						sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-words"));
						for (int i = 0; i <= number_words; i++) {
							sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + array_words[i]);
						}
					}
				}
				else if (args[0].equalsIgnoreCase("blocked")) {
					if (sender.hasPermission("roboticstaff.blockedcommands.list")) {
						List<?> blocked = getConfig().getList("blockedcommands-general");
						String[] array_blocked = (String[])blocked.toArray(new String[blocked.size()]);
						int number_blocked = array_blocked.length - 1;
						sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-blocked"));
						for (int i = 0; i <= number_blocked; i++) {
							sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + array_blocked[i]);
						}
						sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-blocked-perworld"));
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + getConfig().getString("roboticstaff-error-1"));
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("blocked")) {
					if (sender.hasPermission("roboticstaff.blockedcommands.list")) {
						try {
							List<?> blocked = getConfig().getList("blockedcommands-perworld."+args[1]);
							String[] array_blocked = (String[])blocked.toArray(new String[blocked.size()]);
							int number_blocked = array_blocked.length - 1;
							sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-blocked"));
							for (int i = 0; i <= number_blocked; i++) {
								sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + array_blocked[i]);
							}
						} catch (Exception e) {
							sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-error-2"));
						}
					}
				} else {
					sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + getConfig().getString("roboticstaff-error-1"));
				}
			}
			else sender.sendMessage(ChatColor.GOLD + "[RoboticStaff] " + ChatColor.YELLOW + getConfig().getString("roboticstaff-basic"));

			return true;
		}

		if (cmd.getName().equalsIgnoreCase("helpme"))
		{
			List<?> ask = getConfig().getList("helpme-ask");
			String[] array_ask = (String[])ask.toArray(new String[ask.size()]);
			int number_ask = array_ask.length - 1;

			List<?> answer = getConfig().getList("helpme-answer");
			String[] array_answer = (String[])answer.toArray(new String[answer.size()]);
			int number_answer = array_answer.length - 1;

			if (args.length == 0) {
				sender.sendMessage(getConfig("roboticstaff-prefix") + getConfig().getString("helpme-help"));
				for (int i = 0; i <= number_ask; i++)
					sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + i + " - " + array_ask[i]);
			}
			else if (args.length == 1) {
				try {
					int int_args = Integer.parseInt(args[0]);
					if (int_args <= number_answer)
						sender.sendMessage(ChatColor.RED + ">>" + ChatColor.YELLOW + " " + int_args + " - " + array_answer[int_args]);
					else
						sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + getConfig().getString("helpme-error-1"));
				}
				catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + getConfig().getString("helpme-error-2"));
				}
			} else if (args.length >= 2) {
				sender.sendMessage(ChatColor.RED + "ERROR:" + ChatColor.YELLOW + " " + getConfig().getString("helpme-error-3"));
			}
			return true;
		}
		return false;
	}

	@EventHandler
	public void sendsChatMessage(AsyncPlayerChatEvent event)
	{
		if (!event.getPlayer().hasPermission("roboticstaff.antispam.bypass")) {
			if(this.chatMap.get(event.getPlayer()) > System.currentTimeMillis()){
				event.setCancelled(true);
				long time = this.chatMap.get(event.getPlayer()) - System.currentTimeMillis();
				sendPlayerMessage(event.getPlayer(), getConfig("antispam-message").replace("%time%", "" + time));
			} else {
				this.chatMap.remove(event.getPlayer());
				this.chatMap.put(event.getPlayer(), System.currentTimeMillis()+ getConfig().getInt("antispam-delay")*10000);
			}
		}

		// Autoanswer

		if (!event.getPlayer().hasPermission("roboticstaff.autoanswer.bypass")) {
			List<?> tag = getConfig().getList("autoanswer-tag");
			String[] array_tag = (String[])tag.toArray(new String[tag.size()]);

			List<?> tag_answer = getConfig().getList("autoanswer-tag-answer");
			String[] array_tag_answer = (String[])tag_answer.toArray(new String[tag.size()]);

			for (int i = 0; i < array_tag.length; i++)
			{
				String cleaned_tag = array_tag[i].toLowerCase().replace(" ", "");
				if (cleaned_tag.contains("%or%")) {
					String[] splitted_cleaned_tag = cleaned_tag.split("%or%");
					for (int j = 0; j < splitted_cleaned_tag.length; j++)
						if (splitted_cleaned_tag[j].contains(",")) {
							String[] very_splitted_cleaned_tag = splitted_cleaned_tag[j].split(",");
							int message_tags = 0;
							String is_replied = "false";
							for (int k = 0; k < very_splitted_cleaned_tag.length; k++) {
								if (event.getMessage().toLowerCase().replace(" ", "").contains(very_splitted_cleaned_tag[k])) {
									message_tags++;
								}
								if (message_tags == very_splitted_cleaned_tag.length) {
									if (array_tag_answer[i].startsWith("/")) {
										Player player = event.getPlayer();
										String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
										String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
									} else if (array_tag_answer[i].startsWith("%noprefix%")) {
										Player player = event.getPlayer();
										String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
										String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
										String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%noprefix%", "");
										sendNoprefixMessage(event.getPlayer(), very_very_cleaned_tag_answer);
									} else {
										Player player = event.getPlayer();
										String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
										String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
										sendPlayerMessage(event.getPlayer(), very_cleaned_tag_answer);
									}

									event.setCancelled(true);

									is_replied = "true";
									break;
								}
							}
							if (is_replied == "true") {
								is_replied = "false";
								break;
							}
						} else if (event.getMessage().toLowerCase().replace(" ", "").contains(splitted_cleaned_tag[j])) {
							if (array_tag_answer[i].startsWith("/")) {
								Player player = event.getPlayer();
								String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
								String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
							} else if (array_tag_answer[i].startsWith("%noprefix%")) {
								Player player = event.getPlayer();
								String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
								String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
								String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%noprefix%", "");
								sendNoprefixMessage(event.getPlayer(), very_very_cleaned_tag_answer);
							} else {
								Player player = event.getPlayer();
								String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
								String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
								sendPlayerMessage(event.getPlayer(), very_cleaned_tag_answer);
							}

							event.setCancelled(true);

							break;
						}
				}
				else if (cleaned_tag.contains(",")) {
					String[] very_splitted_cleaned_tag = cleaned_tag.split(",");
					int message_tags = 0;
					String is_replied = "false";
					for (int k = 0; k < very_splitted_cleaned_tag.length; k++) {
						if (event.getMessage().toLowerCase().replace(" ", "").contains(very_splitted_cleaned_tag[k])) {
							message_tags++;
						}
						if (message_tags == very_splitted_cleaned_tag.length) {
							if (array_tag_answer[i].startsWith("/")) {
								Player player = event.getPlayer();
								String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
								String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
							} else if (array_tag_answer[i].startsWith("%noprefix%")) {
								Player player = event.getPlayer();
								String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
								String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
								String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%noprefix%", "");
								sendNoprefixMessage(event.getPlayer(), very_very_cleaned_tag_answer);
							} else {
								Player player = event.getPlayer();
								String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
								String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
								sendPlayerMessage(event.getPlayer(), very_cleaned_tag_answer);
							}

							event.setCancelled(true);

							is_replied = "true";
							break;
						}
					}
					if (is_replied == "true") {
						is_replied = "false";
						break;
					}
				} else if (event.getMessage().toLowerCase().replace(" ", "").contains(cleaned_tag))
				{
					if (array_tag_answer[i].startsWith("/")) {
						Player player = event.getPlayer();
						String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
						String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), very_cleaned_tag_answer.replace("/", ""));
					} else if (array_tag_answer[i].startsWith("%noprefix%")) {
						Player player = event.getPlayer();
						String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
						String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
						String very_very_cleaned_tag_answer = very_cleaned_tag_answer.replace("%noprefix%", "");
						sendNoprefixMessage(event.getPlayer(), very_very_cleaned_tag_answer);
					} else {
						Player player = event.getPlayer();
						String cleaned_tag_answer = array_tag_answer[i].replace("%player%", player.getName());
						String very_cleaned_tag_answer = cleaned_tag_answer.replace("%delete%", "");
						sendPlayerMessage(event.getPlayer(), very_cleaned_tag_answer);
					}

					event.setCancelled(true);

					break;
				}
			}
		}

		if(!event.getPlayer().hasPermission("roboticstaff.antiswearing.bypass")) {

			List<?> words = getConfig().getList("antiswearing-words");
			String[] array_words = (String[])words.toArray(new String[words.size()]);

			String beep = getConfig().getString("antiswearing-beep");
			for (int i = 0; i < array_words.length; i++)
			{
				String cleaned_words = array_words[i].toLowerCase();
				event.setMessage(event.getMessage().replaceAll("(?i)" + cleaned_words, beep));
			}
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if(!p.hasPermission("roboticstaff.blockedcommands.bypass")) {
			List<String> cmds = RoboticStaff.this.getConfig().getStringList("blockedcommands-general");
			try {
				List<String> cmdsPW = RoboticStaff.this.getConfig().getStringList("blockedcommands-perworld."+event.getPlayer().getWorld().getName());
				for (String command : cmdsPW) {
					if (event.getMessage().toLowerCase().startsWith("/" + command)) {
						event.setCancelled(true);
						p.sendMessage(getConfig("blockedcommands-message"));
					}
				}
			} catch (Exception e) {
				//TODO: Report that?
			}
			for (String command : cmds) {
				if (event.getMessage().toLowerCase().startsWith("/" + command)) {
					event.setCancelled(true);
					p.sendMessage(getConfig("blockedcommands-message"));
				}
			}
		}
	}

	public void sendPlayerMessage(Player player, String string)
	{
		string = getColors(string);
		player.sendMessage(getConfig("roboticstaff-prefix") + string);
	}

	public void sendBroadcastMessage(Player player, String string) {
		string = getColors(string);
		Bukkit.broadcastMessage(getConfig("roboticstaff-prefix") + string);
	}

	public void sendNoprefixMessage(Player player, String string) {
		string = getColors(string);
		player.sendMessage(string);
	}

	public String getColors(String string) {
		string = string.replace("&0", "" + ChatColor.BLACK);
		string = string.replace("&1", "" + ChatColor.DARK_BLUE);
		string = string.replace("&2", "" + ChatColor.DARK_GREEN);
		string = string.replace("&3", "" + ChatColor.DARK_AQUA);
		string = string.replace("&4", "" + ChatColor.DARK_RED);
		string = string.replace("&5", "" + ChatColor.DARK_PURPLE);
		string = string.replace("&6", "" + ChatColor.GOLD);
		string = string.replace("&7", "" + ChatColor.GRAY);
		string = string.replace("&8", "" + ChatColor.DARK_GRAY);
		string = string.replace("&9", "" + ChatColor.BLUE);
		string = string.replace("&a", "" + ChatColor.GREEN);
		string = string.replace("&b", "" + ChatColor.AQUA);
		string = string.replace("&c", "" + ChatColor.RED);
		string = string.replace("&d", "" + ChatColor.LIGHT_PURPLE);
		string = string.replace("&e", "" + ChatColor.YELLOW);
		string = string.replace("&f", "" + ChatColor.WHITE);
		string = string.replace("&k", "" + ChatColor.MAGIC);
		string = string.replace("&l", "" + ChatColor.BOLD);
		string = string.replace("&m", "" + ChatColor.STRIKETHROUGH);
		string = string.replace("&n", "" + ChatColor.UNDERLINE);
		string = string.replace("&o", "" + ChatColor.ITALIC);
		string = string.replace("&r", "" + ChatColor.RESET);
		return string;
	}

	public String getConfig(String string) {
		string = getConfig().getString(string);
		string = string.replace("&0", "" + ChatColor.BLACK);
		string = string.replace("&1", "" + ChatColor.DARK_BLUE);
		string = string.replace("&2", "" + ChatColor.DARK_GREEN);
		string = string.replace("&3", "" + ChatColor.DARK_AQUA);
		string = string.replace("&4", "" + ChatColor.DARK_RED);
		string = string.replace("&5", "" + ChatColor.DARK_PURPLE);
		string = string.replace("&6", "" + ChatColor.GOLD);
		string = string.replace("&7", "" + ChatColor.GRAY);
		string = string.replace("&8", "" + ChatColor.DARK_GRAY);
		string = string.replace("&9", "" + ChatColor.BLUE);
		string = string.replace("&a", "" + ChatColor.GREEN);
		string = string.replace("&b", "" + ChatColor.AQUA);
		string = string.replace("&c", "" + ChatColor.RED);
		string = string.replace("&d", "" + ChatColor.LIGHT_PURPLE);
		string = string.replace("&e", "" + ChatColor.YELLOW);
		string = string.replace("&f", "" + ChatColor.WHITE);
		string = string.replace("&k", "" + ChatColor.MAGIC);
		string = string.replace("&l", "" + ChatColor.BOLD);
		string = string.replace("&m", "" + ChatColor.STRIKETHROUGH);
		string = string.replace("&n", "" + ChatColor.UNDERLINE);
		string = string.replace("&o", "" + ChatColor.ITALIC);
		string = string.replace("&r", "" + ChatColor.RESET);
		return string;
	}
}
