package com.gmail.raynlegends.RoboticStaff;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Functions {

	private static HashMap<Player, Long> chatAllowedTime = new HashMap<Player, Long>();
	
	/** 
	 * Check for updates
	 * 
	 * @param pluginFile -> The file (roboticstaff.jar) of the plugin
	 */
	public static void checkUpdates(File pluginFile) {
		Main.getPlugin().getLogger().info(Messages.checkingVersion);
		try {
			Updater updater = new Updater(Main.getPlugin(), "roboticstaff", pluginFile, Updater.UpdateType.NO_DOWNLOAD, false);
			String latestVersion = updater.getLatestVersionString().replace("RoboticStaff - ", "").replace("Alpha v", "").replace("Beta v", "").replace("Release v", "");
			if (!latestVersion.contains(" | BUGGED")) {
				if (!latestVersion.equals(Main.getVersion())) {
					Main.getPlugin().getLogger().info(Messages.updateAvaible.replace("%s", updater.getLatestVersionString()));
				} else {
					Main.getPlugin().getLogger().info(Messages.upToDate);
				}
			} else {
				Main.getPlugin().getLogger().info(Messages.upToDate);
			}
		} catch (Exception e) {
			Main.getPlugin().getLogger().warning(Messages.updateCheckError.replace("%s", e.getMessage()));
		}
	}

	/**
	 *  Check if the config is up to date <br />
	 *  If the config is outdated, the old config will be renamed to config_old.yml
	 */
	public static void checkConfig() {
		Main.getPlugin().getLogger().info(Messages.checkingConfig);
		if (!Main.getVersion().equals(Main.getConfigVersion())) {
			File veryOldConfigFile = new File(Main.getPlugin().getDataFolder(), "config_old.yml");
			veryOldConfigFile.delete();

			File configFile = new File(Main.getPlugin().getDataFolder(), "config.yml");
			configFile.renameTo(new File(Main.getPlugin().getDataFolder(), "config_old.yml"));

			File oldConfigFile = new File(Main.getPlugin().getDataFolder(), "config.yml");
			oldConfigFile.delete();

			Main.getPlugin().saveDefaultConfig();
			Main.getPlugin().getLogger().info(Messages.configRewrited);

			Main.getPlugin().reloadConfig();
		} else {
			Main.getPlugin().getLogger().info(Messages.configChecked);
		}
	}

	/** 
	 * Send a message to the player, with the prefix [RoboticStaff]
	 * @param player -> The player to send the message
	 * @param string -> The message
	 */
	public static void sendPlayerMessage(Player player, String string) {
		string = ChatColor.translateAlternateColorCodes('&', string);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("roboticstaff-prefix")) + string);
	}

	/** 
	 * Send a message to the player, with the prefix [RoboticStaff]
	 * @param sender -> The sender to send the message
	 * @param string -> The message
	 */
	public static void sendSenderMessage(CommandSender sender, String string) {
		string = ChatColor.translateAlternateColorCodes('&', string);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("roboticstaff-prefix")) + string);
	}

	/** 
	 * Broadcast a message, with the prefix [RoboticStaff]
	 * @param string -> The message
	 */
	public static void sendBroadcastMessage(String string) {
		string = ChatColor.translateAlternateColorCodes('&', string);
		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.getPlugin().getConfig().getString("roboticstaff-prefix")) + string);
	}

	/**
	 * Send a message to the player, without the prefix
	 * @param player
	 * @param string
	 */
	public static void sendNoprefixMessage(Player player, String string) {
		string = ChatColor.translateAlternateColorCodes('&', string);
		player.sendMessage(string);
	}

	/**
	 * Reloads the configuration, some variables and do some stuffs
	 */
	public static void reloadPlugin(CommandSender sender) {
		checkAskAndTags(sender);
		sendSenderMessage(sender, Messages.pluginReloaded);
	}

	/**
	 * Check if the number of ask and ask_answer and of tag and tag_answer is equal
	 * @param player -> The player
	 */
	public static void checkAskAndTags(CommandSender sender) {
		if (Main.getPlugin().getConfig().getStringList("helpme-asks").size() != Main.getPlugin().getConfig().getStringList("helpme-answers").size()) {
			sendSenderMessage(sender, Messages.disequalAskAndAnswers);
		}

		if (Main.getPlugin().getConfig().getStringList("autoanswer-tags").size() != Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers").size()) {
			sendSenderMessage(sender, Messages.disequalTagAndTagAnswers);
		}
	}

	/**
	 * Parse and execute the answer
	 * @param player -> The player
	 * @param answer -> The answer
	 */
	public static void executeAutoanswerAnswer(final Player player, final String answer) {
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
			public void run() {
				String answerReplaced = answer.replace("%player%", player.getName());
				answerReplaced = answerReplaced.replace("@delete", "");
				
				if (answerReplaced.startsWith("/")) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), answerReplaced.replace("/", ""));
				} else if (answerReplaced.startsWith("@broadcast")) {
					answerReplaced = answerReplaced.replace("@broadcast", "");
					sendBroadcastMessage(answerReplaced);
				} else if (answerReplaced.startsWith("@noprefix")) {
					answerReplaced = answerReplaced.replace("@noprefix", "");
					sendNoprefixMessage(player, answerReplaced);
				} else {
					sendPlayerMessage(player, answerReplaced);
				}
			}
		}, 1);
	}
	
	/**
	 * Returns the timestamp from when the player can send a message
	 * @param player -> The player
	 */
	public static Long getChatAllowedTime(Player player) {
		return chatAllowedTime.get(player);
	}
	
	/**
	 * Set the timestamp from when the player can send a message
	 * @param player -> The player
	 * @param timestamp -> The timestamp
	 */
	public static void setChatAllowedTime(Player player, Long timestamp) {
		chatAllowedTime.put(player, timestamp);
	}
	
	/**
	 * Removes the timestamp from when the player can send a message
	 * @param player -> The player
	 */
	public static void removeChatAllowedTime(Player player) {
		chatAllowedTime.remove(player);
	}
}
