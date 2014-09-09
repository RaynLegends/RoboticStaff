package com.gmail.raynlegends.RoboticStaff;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Events listener;
	private Commands commands;
	private static Main plugin;

	public void onEnable() {
		plugin = this;
		listener = new Events();
		commands = new Commands();

		// Config loader & version checker

		saveDefaultConfig();
		Functions.checkConfig();

		/*prefix = Main.getPlugin().getConfig().getString("roboticstaff-prefix");
		join = Main.getPlugin().getConfig().getString("roboticstaff-join");
		asks = Main.getPlugin().getConfig().getStringList("helpme-asks");
		answers = Main.getPlugin().getConfig().getStringList("helpme-answers");
		tags = Main.getPlugin().getConfig().getStringList("autoanswer-tags");
		tag_answers = Main.getPlugin().getConfig().getStringList("autoanswer-tag-answers");
		words = Main.getPlugin().getConfig().getStringList("antiswearing-words");
		beep = Main.getPlugin().getConfig().getString("antiswearing-beep");
		helpme_help = Main.getPlugin().getConfig().getString("helpme-help");
		helpme_error = Main.getPlugin().getConfig().getString("helpme-error");
		helpme_error_1 = Main.getPlugin().getConfig().getString("helpme-error-1");
		helpme_error_2 = Main.getPlugin().getConfig().getString("helpme-error-2");
		helpme_error_3 = Main.getPlugin().getConfig().getString("helpme-error-2");*/

		// Update checker

		if (getConfig().getString("update-checker").equalsIgnoreCase("true")) {
			Functions.checkUpdates(getFile());
		}

		// Check asks and tags
		Functions.checkAskAndTags(getServer().getConsoleSender());

		// Set command executor
		getCommand("roboticstaff").setExecutor(commands);
		getCommand("helpme").setExecutor(commands);

		// Register listener and print latest messages

		getServer().getPluginManager().registerEvents(listener, this);

		getLogger().info(Messages.askPluginPage);
		getLogger().info(Messages.pluginPage);
	}

	public void onDisable() {
		getLogger().info(Messages.successfullyDisabled);
	}
	
	public static Main getPlugin() {
		return plugin;
	}
	
	public static String getConfigVersion() {
		return plugin.getConfig().getString("config-version");
	}
	
	public static String getVersion() {
		return plugin.getDescription().getVersion();
	}
}
