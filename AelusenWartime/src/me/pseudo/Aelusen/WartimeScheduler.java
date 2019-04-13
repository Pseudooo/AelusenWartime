package me.pseudo.Aelusen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.plugin.java.JavaPlugin;

public class WartimeScheduler {
	
	private ArrayList<Wartime> wartimes; // All wartimes in server
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final JavaPlugin plugin;
	
	public WartimeScheduler(JavaPlugin plugin) {
		// Instantiate
		this.wartimes = new ArrayList<Wartime>();
		this.plugin = plugin;
	}
	
	public void register(Wartime wartime) {
		// Register a wartime with this scheduler
		this.wartimes.add(wartime);
	}
	
	public boolean isRegistered(Wartime wartime) {
		// Check if a wartime is registered
		return this.wartimes.contains(wartime);
	}
	
	public void unregister(Wartime wartime) {
		// Unregister a wartime
		this.wartimes.remove(wartime);
	}
	
	public void unregisterAll() {
		TimeZone timeZone = TimeZone.getTimeZone(this.plugin.getConfig().getString("timezone"));
		
		// Iterate through wartimes and check for a boss bar
		for(Wartime wartime : this.wartimes) {
			if(wartime.isActive(timeZone)) {
				wartime.getBossBar().removeAll();
				break; // Only one wartime active at a time so once found break
			}
		}
		
		this.wartimes.clear(); // Clear arr
	}
	
	public Collection<Wartime> getWartimes() {
		return this.wartimes;
	}
	
	public void scheduleAllTasks() {
		
		// Schedule all of the appropriate tasks for the n wartimes
		
		TimeZone timeZone = TimeZone.getTimeZone(this.plugin.getConfig().getString("timezone"));
		
		// Iter and schedule
		for(Wartime wartime : this.wartimes) {
			wartime.scheduleTasks(this.scheduler, timeZone);
		}
		
	}
	
	public void unscheduleAllTasks() {
		
		// Unschedule all running tasks for n wartimes
		
		for(Wartime wartime : this.wartimes) {
			wartime.cancelScheduledTasks();
		}
		
	}
	
	public Wartime getActiveWartime() {
		
		Wartime activeWartime = null; // null return means no active wartimes
		TimeZone timeZone = TimeZone.getTimeZone(this.plugin.getConfig().getString("timezone"));
		
		// Check for currently active wartime
		for(Wartime wartime : this.wartimes) {
			if(wartime.isActive(timeZone)) {
				activeWartime = wartime;
				break; // Found, break
			}
		}
		
		// Return found/null
		return activeWartime;
		
	}
	
}
