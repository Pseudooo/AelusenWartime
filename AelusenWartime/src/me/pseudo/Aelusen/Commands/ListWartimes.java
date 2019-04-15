package me.pseudo.Aelusen.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pseudo.Aelusen.Wartime;
import me.pseudo.Aelusen.WartimeScheduler;

public class ListWartimes implements CommandExecutor {
	
	/*
	 * List wartimes command will display all of the wartimes
	 * that are currently configured and loaded
	 */
	
	private final WartimeScheduler wartimes;
	
	public ListWartimes(WartimeScheduler wartimes) {
		this.wartimes = wartimes;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("aelusen.wartime.listwartimes")) {
				// No permission
				player.sendMessage(ChatColor.RED+"You don't have permission to do that!");
				return true;
			}
			
			// In-game font isn't monospaced so width formatting is pointless
			for(Wartime wartime : wartimes.getWartimes()) {
				player.sendMessage("WartimeID: "+wartime.getID().toString());
				player.sendMessage(String.format("Millis: %d", wartime.getMillisUntil()));
				player.sendMessage(String.format("Seconds: %d", wartime.getMillisUntil() / 1000));
				player.sendMessage(String.format("Minutes: %d", wartime.getMillisUntil() / (60 * 1000)));
				player.sendMessage(String.format("Hours: %d", wartime.getMillisUntil() / (60 * 60 * 1000)));
				player.sendMessage(" - - - - - - ");
			}
			
		}else {
			
			// Console font is monospaced so formatting is beneficial
			for(Wartime wartime : wartimes.getWartimes()) {
				sender.sendMessage("WartimeID: "+wartime.getID().toString());
				sender.sendMessage(String.format("%8s %8d", "Millis:", wartime.getMillisUntil()));
				sender.sendMessage(String.format("%8s %8d", "Seconds:", wartime.getMillisUntil() / 1000));
				sender.sendMessage(String.format("%8s %8d", "Minutes:", wartime.getMillisUntil() / (60 * 1000)));
				sender.sendMessage(String.format("%8s %8d", "Hours:", wartime.getMillisUntil() / (60 * 60 * 1000)));
				sender.sendMessage(" - - - - - - ");
			}
		}
		
		return true;
	}
	
}
