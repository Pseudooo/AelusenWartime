package me.pseudo.Aelusen;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.bukkit.boss.BossBar;

import me.pseudo.Aelusen.Tasks.ClearBossBarTask;
import me.pseudo.Aelusen.Tasks.DisplayBossBarTask;

public class Wartime {
	
	private Date startTime;
	private long duration;
	private final UUID id; // UUID for comparison/dynamic modifications in future versions
	
	private ScheduledFuture<?> startHandler = null, endHandler = null;
	
	private final BossBar bar;
	
	public Wartime(Date startTime, Date endTime, BossBar bar) {
		
		// Assign variables
		this.startTime = startTime;
		this.duration = endTime.getTime() - startTime.getTime();
		
		// Handle wrapping of midnight
		if(this.duration < 0) this.duration += 24 * 60 * 60 * 1000L;
		
		// Assign an ID
		this.id = UUID.randomUUID();
		
		this.bar = bar;
		
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
	
	public void scheduleTasks(ScheduledExecutorService scheduler, TimeZone timeZone) {
		
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
		DisplayBossBarTask displayTask = new DisplayBossBarTask(this.bar);
		ClearBossBarTask clearTask = new ClearBossBarTask(this.bar);
		
		// Schedule starting and ending tasks
		startHandler = scheduler.scheduleAtFixedRate(displayTask, initDelay, 24*60*60*1000L,
				TimeUnit.MILLISECONDS);
		endHandler = scheduler.scheduleAtFixedRate(clearTask, initDelay + this.duration, 24*60*60*1000L,
				TimeUnit.MILLISECONDS);
		
	}
	
	public void cancelScheduledTasks() {
		
		// Cancel running tasks
		if(startHandler != null) startHandler.cancel(true);
		if(endHandler != null) endHandler.cancel(true);
		
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
