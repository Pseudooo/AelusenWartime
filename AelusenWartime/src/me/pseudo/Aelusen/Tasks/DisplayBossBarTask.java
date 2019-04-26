package me.pseudo.Aelusen.Tasks;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.pseudo.Aelusen.Wartime;

public class DisplayBossBarTask implements Runnable{
	
	private final Wartime wartime;
	private final ScheduledExecutorService scheduler;
	
	private ScheduledFuture<?>[] updateTasks;
	
	public DisplayBossBarTask(Wartime wartime, ScheduledExecutorService scheduler) {
		
		this.wartime = wartime;
		this.scheduler = scheduler;
		
	}
	
	public void cancel() {
		
		// Cancel all future tasks for smoothing of boss bar
		if(this.updateTasks == null) return; // Only applicable if active
		
		for(ScheduledFuture<?> taskhandler : this.updateTasks) {
			taskhandler.cancel(true);
		}
		
	}
	
	@Override
	public void run() {
		
		// Add all players to the boss bar
		for(Player p : Bukkit.getOnlinePlayers()) {
			this.wartime.getBossBar().addPlayer(p);
		}
		
		int tasks = Math.floorDiv((int) this.wartime.getDuration(), 5000);
		double increment = 1.0 / tasks;
		
		this.updateTasks = new ScheduledFuture<?>[tasks];
		
		// Iterate for each task to schedule all appropriate updates
		for(int i = 1; i <= tasks; i++) {
			
			// Instantiate task to be scheduled
			UpdateBossBarTask task = new UpdateBossBarTask(
					this.wartime.getBossBar(),
					i * increment);
			
			try {
				// Store handler for potential need to cancel
				updateTasks[i-1] = scheduler.schedule(task, i*5, TimeUnit.SECONDS);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
	}
	
}
