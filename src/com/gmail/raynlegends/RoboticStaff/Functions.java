package com.gmail.raynlegends.RoboticStaff;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Functions {

	private Main plugin;

	public Functions(Main instance) {
		plugin = instance;
	}

	/** 
	 * Check for updates
	 * 
	 * @param pluginFile -> The file (roboticstaff.jar) of the plugin
	 */
	public void checkUpdates(File pluginFile) {
		plugin.getLogger().info(Messages.checkingVersion);
		try {
			Updater updater = new Updater(plugin, "roboticstaff", pluginFile, Updater.UpdateType.NO_DOWNLOAD, false);
			String latestVersion = updater.getLatestVersionString().replace("RoboticStaff - ", "").replace("Alpha v", "").replace("Beta v", "").replace("Release v", "");
			if (!latestVersion.contains(" | BUGGED")) {
				if (!latestVersion.equals(plugin.version)) {
					plugin.getLogger().info(Messages.updateAvaible.replace("%s", updater.getLatestVersionString()));
				} else {
					plugin.getLogger().info(Messages.upToDate);
				}
			} else {
				plugin.getLogger().info(Messages.upToDate);
			}
		} catch (Exception e) {
			plugin.getLogger().warning(Messages.updateCheckError.replace("%s", e.getMessage()));
		}
	}

	/**
	 *  Check if the config is up to date <br />
	 *  If the config is outdated, the old config will be renamed to config_old.yml
	 */
	public void checkConfig() {
		plugin.getLogger().info(Messages.checkingConfig);
		if (!plugin.version.equals(plugin.config_version)) {
			File veryOldConfigFile = new File(plugin.getDataFolder(), "config_old.yml");
			veryOldConfigFile.delete();

			File configFile = new File(plugin.getDataFolder(), "config.yml");
			configFile.renameTo(new File(plugin.getDataFolder(), "config_old.yml"));

			File oldConfigFile = new File(plugin.getDataFolder(), "config.yml");
			oldConfigFile.delete();

			plugin.saveDefaultConfig();
			plugin.getLogger().info(Messages.configRewrited);

			plugin.reloadConfig();
		} else {
			plugin.getLogger().info(Messages.configChecked);
		}
	}

	/** 
	 * Send a message to the player, with the prefix [RoboticStaff]
	 * @param player -> The player to send the message
	 * @param string -> The message
	 */
	public void sendPlayerMessage(Player player, String string)
	{
		string = ChatColor.translateAlternateColorCodes('&', string);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix) + string);
	}

	/** 
	 * Send a message to the player, with the prefix [RoboticStaff]
	 * @param sender -> The sender to send the message
	 * @param string -> The message
	 */
	public void sendSenderMessage(CommandSender sender, String string)
	{
		string = ChatColor.translateAlternateColorCodes('&', string);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix) + string);
	}

	/** 
	 * Broadcast a message, with the prefix [RoboticStaff]
	 * @param string -> The message
	 */
	public void sendBroadcastMessage(String string) {
		string = ChatColor.translateAlternateColorCodes('&', string);
		plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.prefix) + string);
	}

	/**
	 * Send a message to the player, without the prefix
	 * @param player
	 * @param string
	 */
	public void sendNoprefixMessage(Player player, String string) {
		string = ChatColor.translateAlternateColorCodes('&', string);
		player.sendMessage(string);
	}

	/**
	 * Reloads the configuration, some variables and do some stuffs
	 */
	public void reloadPlugin(CommandSender sender) {
		plugin.reloadConfig();
		plugin.prefix = plugin.getConfig().getString("roboticstaff-prefix");
		plugin.join = plugin.getConfig().getString("roboticstaff-join");
		plugin.asks = plugin.getConfig().getStringList("helpme-asks");
		plugin.answers = plugin.getConfig().getStringList("helpme-answers");
		plugin.tags = plugin.getConfig().getStringList("autoanswer-tags");
		plugin.tag_answers = plugin.getConfig().getStringList("autoanswer-tag-answers");
		plugin.words = plugin.getConfig().getStringList("antiswearing-words");
		plugin.beep = plugin.getConfig().getString("antiswearing-beep");
		plugin.helpme_help = plugin.getConfig().getString("helpme-help");
		plugin.helpme_error = plugin.getConfig().getString("helpme-error");
		plugin.helpme_error_1 = plugin.getConfig().getString("helpme-error-1");
		plugin.helpme_error_2 = plugin.getConfig().getString("helpme-error-2");
		plugin.helpme_error_3 = plugin.getConfig().getString("helpme-error-2");
		checkAskAndTags(sender);
		sendSenderMessage(sender, Messages.pluginReloaded);
	}

	/**
	 * Check if the number of ask and ask_answer and of tag and tag_answer is equal
	 * @param player -> The player
	 */
	public void checkAskAndTags(CommandSender sender) {
		if (plugin.asks.size() != plugin.answers.size()) {
			sendSenderMessage(sender, Messages.disequalAskAndAnswers);
		}

		if (plugin.tags.size() != plugin.tag_answers.size()) {
			sendSenderMessage(sender, Messages.disequalTagAndTagAnswers);
		}
	}

	public void executeAutoanswerAnswer(final Player player, final String answer) {
		plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
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
}
