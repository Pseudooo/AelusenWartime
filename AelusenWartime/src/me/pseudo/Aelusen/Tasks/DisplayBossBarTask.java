package me.pseudo.Aelusen.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class DisplayBossBarTask implements Runnable{
	
	private final BossBar bar;
	
	public DisplayBossBarTask(BossBar bar) {
		this.bar = bar;
	}
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			bar.addPlayer(p);
		}
	}
	
}
