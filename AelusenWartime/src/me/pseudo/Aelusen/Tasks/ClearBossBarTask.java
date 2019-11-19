package me.pseudo.Aelusen.Tasks;

import org.bukkit.boss.BossBar;

public class ClearBossBarTask implements Runnable {
	
	private final BossBar bar;
	
	public ClearBossBarTask(BossBar bar) {
		this.bar = bar;
	}
	
	@Override
	public void run() { 
		this.bar.removeAll();
	}
	
}
