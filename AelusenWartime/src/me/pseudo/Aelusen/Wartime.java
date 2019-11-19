package me.pseudo.Aelusen;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import me.pseudo.Aelusen.Tasks.ClearBossBarTask;
import me.pseudo.Aelusen.Tasks.DisplayBossBarTask;
import me.pseudo.Aelusen.Tasks.DisplayWarningBarTask;
import net.md_5.bungee.api.ChatColor;

public class Wartime {
	
	private Date startTime;
	private long duration;
	private final UUID id; // UUID for comparison/dynamic modifications in future versions
	private List<Integer> warnings;
	
	private ScheduledFuture<?> startHandler = null, endHandler = null;
	private List<ScheduledFuture<?>> warningHandlers = null;
	
	private final String warningMessage;
	
	private DisplayBossBarTask displayTask = null;
	
	private final BossBar bar;
	private final BarColor warningColor;
	
	public Wartime(Date startTime, Date endTime, BossBar bar, 
			List<Integer> warnings, String warningMessage, BarColor warningColor) {
		
		// Assign variables
		this.startTime = startTime;
		this.duration = endTime.getTime() - startTime.getTime();
		
		// Handle wrapping of midnight
		if(this.duration < 0) this.duration += 24 * 60 * 60 * 1000L;
		
		// Assign an ID
		this.id = UUID.randomUUID();
		
		this.bar = bar;
		
		this.warnings = warnings;
		
		this.warningMessage = warningMessage;
		
		this.warningColor = warningColor;
		
	}
	
	// **************************************************** START OF GETTERS
	
	public Date getStartTime() {
		return this.startTime;
	}
	
	public Date getEndTime() {
		return new Date(this.startTime.getTime() + duration);
	}
	
	public long getDuration() {
		return this.duration;
	}
	
	public UUID getID() {
		return this.id;
	}
	
	public BossBar getBossBar() {
		return this.bar;
	}
	
	public long getMillisUntil() {
		return startHandler.getDelay(TimeUnit.MILLISECONDS);
	}
	
	// **************************************************** END OF GETTERS
	
	// Schedule all respect tasks for this instance
	public void scheduleTasks(JavaPlugin plugin, 
			ScheduledExecutorService scheduler, TimeZone timeZone) {
		
		// Reset to midnight of current day
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(timeZone);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		
		// Setup a scheduled time for today
		Date scheduledStartTime = new Date(cal.getTimeInMillis() + this.startTime.getTime());
		if(scheduledStartTime.getTime() - new Date().getTime() < 0) {
			// Time has already passed so schedule for time next day
			scheduledStartTime.setTime(scheduledStartTime.getTime() + 24L*60*60*1000);
		}
		
		// Millis until wartime starts
		long initDelay = scheduledStartTime.getTime() - new Date().getTime();
		
		// Instantiate tasks for scheduling
		this.displayTask = new DisplayBossBarTask(this, scheduler);
		ClearBossBarTask clearTask = new ClearBossBarTask(this.bar);
		
		if(this.warnings != null) { // null warnings means none defined
			for(Integer warning : this.warnings) {
				
				String msg = ChatColor.translateAlternateColorCodes('&', 
						this.warningMessage.replaceAll("%mins%", warning.toString()));
				
				BossBar warningBar = Bukkit.createBossBar(msg, this.warningColor, BarStyle.SOLID);
				
				DisplayWarningBarTask warningBarTask = new DisplayWarningBarTask(plugin, warningBar);
				
				long warningCountdown = initDelay - warning*60*1000L;
				if(warningCountdown < 0) warningCountdown += 24L*60*60*1000;
				
				this.warningHandlers.add(scheduler.scheduleAtFixedRate(warningBarTask, 
						warningCountdown, 24*60*60*1000L, TimeUnit.MILLISECONDS));
				
			}
		}
		
		// Schedule starting and ending tasks
		startHandler = scheduler.scheduleAtFixedRate(displayTask, initDelay, 24*60*60*1000L,
				TimeUnit.MILLISECONDS);
		endHandler = scheduler.scheduleAtFixedRate(clearTask, initDelay + this.duration, 24*60*60*1000L,
				TimeUnit.MILLISECONDS);
		
	}
	
	public void cancelScheduledTasks() {
		
		// Cancel running tasks
		if(this.displayTask != null) this.displayTask.cancel();
		if(startHandler != null) startHandler.cancel(true);
		if(endHandler != null) endHandler.cancel(true);
		
		for(ScheduledFuture<?> handler : this.warningHandlers) {
			handler.cancel(true);
		}
	}
	
	public boolean isActive(TimeZone timeZone) {
		
		// Setup at midnight of cur date
		Calendar cal = new GregorianCalendar();
		cal.setTimeZone(timeZone);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0); // Reset all values assoc with day
		
		// Setup temp and calculate count down in millis
		Date tmp = new Date(this.startTime.getTime() + cal.getTimeInMillis());
		long timeUntil = tmp.getTime() - new Date().getTime();
		
		// If cur time is between starting and ending is active
		if(timeUntil < 0 && timeUntil + this.duration > 0) {
			return true;
		}else return false; // Else not
		
	}
	
	// Overrides for extended java.util.ArrayList functionality and future versions
	// with potential dynamic modifications that'll require real time checks/updates
	@Override
	public boolean equals(Object object) {
		if(object instanceof Wartime) {
			Wartime other = (Wartime) object;
			return other.id.equals(this.id);
		}else return false;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
}
