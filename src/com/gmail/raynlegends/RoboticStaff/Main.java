package com.gmail.raynlegends.RoboticStaff;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Main plugin;
	private Listeners listener;
	private Functions functions;
	private Commands commands;

	protected String version = "2.0";
	protected String config_version;

	public String prefix;
	public String join;
	public List<String> asks;
	public List<String> answers;
	public List<String> tags;
	public List<String> tag_answers;
	public List<String> words;
	public String beep;
	public String helpme_help;
	public String helpme_error;
	public String helpme_error_1;
	public String helpme_error_2;
	public String helpme_error_3;

	public final HashMap<Player, Long> chatMap = new HashMap<Player, Long>();

	public void onEnable() {
		plugin = this;
		functions = new Functions(plugin);
		listener = new Listeners(plugin, functions);
		commands = new Commands(plugin, functions);

		// Config loader & version checker

		saveDefaultConfig();
		config_version = getConfig().getString("config-version");
		functions.checkConfig();

		// Get variables from configuration

		prefix = getConfig().getString("roboticstaff-prefix");
		join = getConfig().getString("roboticstaff-join");
		asks = getConfig().getStringList("helpme-asks");
		answers = getConfig().getStringList("helpme-answers");
		tags = getConfig().getStringList("autoanswer-tags");
		tag_answers = getConfig().getStringList("autoanswer-tag-answers");
		words = getConfig().getStringList("antiswearing-words");
		beep = getConfig().getString("antiswearing-beep");
		helpme_help = getConfig().getString("helpme-help");
		helpme_error = getConfig().getString("helpme-error");
		helpme_error_1 = getConfig().getString("helpme-error-1");
		helpme_error_2 = getConfig().getString("helpme-error-2");
		helpme_error_3 = getConfig().getString("helpme-error-2");

		// Update checker

		if (getConfig().getString("update-checker").equalsIgnoreCase("true")) {
			functions.checkUpdates(getFile());
		}

		// Check asks and tags
		functions.checkAskAndTags(getServer().getConsoleSender());

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
}
