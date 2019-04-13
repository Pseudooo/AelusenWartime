package me.pseudo.Aelusen.EventListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.pseudo.Aelusen.Wartime;
import me.pseudo.Aelusen.WartimeScheduler;

public class PlayerLeave implements Listener{

	/*
	 * If a player leaves while a Wartime is active they'll
	 * need to be removed from the boss bar that's displayed
	 */
	
	private final WartimeScheduler wartimes;
	
	public PlayerLeave(JavaPlugin plugin, WartimeScheduler wartimes) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.wartimes = wartimes; // assign pointer
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		
		Wartime activeWartime = this.wartimes.getActiveWartime();
		
		if(activeWartime == null) return;
		
		// Remove
		activeWartime.getBossBar().removePlayer(e.getPlayer());
		
	}
	
}
