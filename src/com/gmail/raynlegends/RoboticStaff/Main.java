package com.gmail.raynlegends.RoboticStaff;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private Main plugin;
	private Listeners listener;
	private Functions functions;
	private Commands commands;
	
	protected String version = "2.0";
	protected String config_version = getConfig().getString("config-version");
	
	public String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("roboticstaff-prefix"));
	public List<String> asks = getConfig().getStringList("helpme-asks");
	public List<String> answers = getConfig().getStringList("helpme-answers");
	public List<String> tags = getConfig().getStringList("autoanswer-tags");
	public List<String> tag_answers = getConfig().getStringList("autoanswer-tag-answers");
	public List<String> words = getConfig().getStringList("antiswearing-words");
	public String beep = getConfig().getString("antiswearing-beep");
	public String helpme_help = getConfig().getString("helpme-help");
	public String helpme_error = getConfig().getString("helpme-error");
	public String helpme_error_1 = getConfig().getString("helpme-error-1");
	public String helpme_error_2 = getConfig().getString("helpme-error-2");
	public String helpme_error_3 = getConfig().getString("helpme-error-2");
	
	public final HashMap<Player, Long> chatMap = new HashMap<Player, Long>();

	public void onEnable() {
		plugin = this;
		listener = new Listeners(plugin, functions);
		functions = new Functions(plugin);
		commands = new Commands(plugin, functions);
		getServer().getPluginManager().registerEvents(listener, this);
		
		// Update checker
		
		if (getConfig().getString("update-checker").equalsIgnoreCase("true")) {
			functions.checkUpdates(getFile());
		}
		
		// Config version checker
		
		saveDefaultConfig();
		functions.checkConfig();
		
		// Check asks and tags
		functions.checkAskAndTags(getServer().getConsoleSender());
		
		// Set command executor
		getCommand("roboticstaff").setExecutor(commands);
		getCommand("helpme").setExecutor(commands);
		
		// Latest messages
		
		getLogger().info(Messages.askPluginPage);
		getLogger().info(Messages.pluginPage);
	}
	
	public void onDisable() {
		getLogger().info(Messages.successfullyDisabled);
	}
}
