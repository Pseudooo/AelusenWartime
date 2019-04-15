package me.pseudo.Aelusen.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pseudo.Aelusen.AelusenWartime;

public class Reload implements CommandExecutor {
	
	private final AelusenWartime plugin;
	
	public Reload(AelusenWartime plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Permission check
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("aelusen.wartime.reload")) {
				// Permission denied
				player.sendMessage(ChatColor.RED+"You don't have permission do that!");
				return true;
			}
		}
		
		// Reload plugin
		sender.sendMessage(ChatColor.GREEN+"Reloading...");
		
		this.plugin.reloadConfig(); // Reload file
		this.plugin.getWartimeScheduler().unscheduleAllTasks(); // Unschedule and unregister all current tasks
		this.plugin.getWartimeScheduler().unregisterAll();
		this.plugin.loadWartimes();
		this.plugin.getWartimeScheduler().scheduleAllTasks(); // Schedule new tasks
		
		sender.sendMessage(ChatColor.GREEN+"Done!");
		
		return true;
	}
	
}
