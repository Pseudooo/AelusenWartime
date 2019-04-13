package me.pseudo.Aelusen;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import me.pseudo.Aelusen.Commands.ListWartimes;
import me.pseudo.Aelusen.Commands.Reload;
import me.pseudo.Aelusen.EventListeners.PlayerJoin;
import me.pseudo.Aelusen.EventListeners.PlayerLeave;

public class AelusenWartime extends JavaPlugin {
	
	private final WartimeScheduler wartimes = new WartimeScheduler(this);
	
	public void onEnable() {
		
		// Initialize Config
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		
		// Load wartimes
		loadWartimes();
		
		// Schedule all tasks
		this.wartimes.scheduleAllTasks();
		
		// Instantiate/Register Events
		new PlayerJoin(this, this.wartimes);
		new PlayerLeave(this, this.wartimes);
		
		// Register Commands
		this.getCommand("listwartimes").setExecutor(new ListWartimes(this.wartimes));
		this.getCommand("wartimereload").setExecutor(new Reload(this, this.wartimes));
		
		this.getLogger().info("AelusenWartime is now enabled!");
	}
	
	public void onDisable() {
		
		// Unschedule all tasks for shutdown or reloading
		this.wartimes.unscheduleAllTasks();
		this.wartimes.unregisterAll();
		
		this.getLogger().info("AelusenWartime is now disabled!");
	}
	
	public void loadWartimes() {
		
		// Iterate through configuration for potentially n wartimes
		// Wartimes are assumed to not overlap
		for(String key : this.getConfig().getConfigurationSection("wartimes").getKeys(false)) {
			
			// Initialize starting and ending dates
			Date startTime = null, endTime = null;
			DateFormat formatter = new SimpleDateFormat("HH:mm");
			
			// Attempt parsing the dates
			try {
				
				startTime = formatter.parse(
						this.getConfig().getString("wartimes." + key + ".wartime-start"));
				
				endTime = formatter.parse(
						this.getConfig().getString("wartimes." + key + ".wartime-end"));
				
			}catch(ParseException e) {
				// Parse failed
				this.getLogger().severe("There was a problem parsing the dates for " + key
						+ " in the configuration!");
				this.getLogger().severe("Wartime will be skipped!"); // Disable plugin and print to console
				continue;
			}
			
			// Create boss bar based on defined config
			BossBar bar = Bukkit.createBossBar(
					ChatColor.translateAlternateColorCodes('&', 
							this.getConfig().getString("wartimes." + key + ".wartime-active-text")), 
					BarColor.valueOf(this.getConfig().getString("wartimes."+ key + ".bar-color")), BarStyle.SOLID);
			
			// Create instance of Wartime with given dates
			Wartime wartime = new Wartime(startTime, endTime, bar);
			this.wartimes.register(wartime); // Register with container class
			
		}
		
	}
	
}
