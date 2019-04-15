package me.pseudo.Aelusen.Tasks;

import org.bukkit.boss.BossBar;

public class UpdateBossBarTask implements Runnable {
	
	private final double hp;
	private final BossBar bar;
	
	public UpdateBossBarTask(BossBar bar, double hp) {
		// 1 = full health and hp should decrease as time progresses
		this.hp = 1 - hp;
		this.bar = bar;
	}
	
	@Override
	public void run() {
		// Update the health
		this.bar.setProgress(hp);
	}
	
}
