package me.pseudo.Aelusen.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pseudo.Aelusen.AelusenWartime;
import me.pseudo.Aelusen.WartimeScheduler;

public class Reload implements CommandExecutor {
	
	private final AelusenWartime plugin;
	private final WartimeScheduler wartimes;
	
	public Reload(AelusenWartime plugin, WartimeScheduler wartimes) {
		this.plugin = plugin;
		this.wartimes = wartimes;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("aelusen.wartime.reload")) {
				// Permission check
				player.sendMessage(ChatColor.RED+"You don't have permission do that!");
				return true;
			}
		}
		
		sender.sendMessage(ChatColor.GREEN+"Reloading...");
		
		this.plugin.reloadConfig(); // Reload file
		this.wartimes.unscheduleAllTasks(); // Unschedule and unregister all current tasks
		this.wartimes.unregisterAll();
		this.plugin.loadWartimes();
		this.wartimes.scheduleAllTasks(); // Schedule new tasks
		
		sender.sendMessage(ChatColor.GREEN+"Done!");
		
		return true;
	}
	
}
