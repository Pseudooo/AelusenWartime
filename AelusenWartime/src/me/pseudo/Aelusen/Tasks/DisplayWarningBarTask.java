package me.pseudo.Aelusen.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DisplayWarningBarTask implements Runnable {
	
	private final BossBar bar;
	private final JavaPlugin plugin;
	
	public DisplayWarningBarTask(JavaPlugin plugin, BossBar bar) {
		this.bar = bar;
		this.plugin = plugin;
	}
	
	@Override
	public void run() {
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			this.bar.addPlayer(p);
		}
		
		ClearBossBarTask clear = new ClearBossBarTask(this.bar);
			
		this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, clear, 20 * 10);
		
	}
	
}
