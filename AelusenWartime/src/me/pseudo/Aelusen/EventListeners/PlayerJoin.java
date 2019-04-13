package me.pseudo.Aelusen.EventListeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.pseudo.Aelusen.Wartime;
import me.pseudo.Aelusen.WartimeScheduler;

public class PlayerJoin implements Listener{
	
	/*
	 * When a player joins, if there's an active wartime they'll need to be
	 * added to the wartime's BossBar
	 */
	
	private final WartimeScheduler wartimes;
	
	public PlayerJoin(JavaPlugin plugin, WartimeScheduler wartimes) { // Register
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.wartimes = wartimes; // Assign pointer
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		
		Wartime activeWartime = this.wartimes.getActiveWartime();
		
		if(activeWartime == null) return; // Null means no active
		
		// Add player
		activeWartime.getBossBar().addPlayer(e.getPlayer());
		
	}
}
