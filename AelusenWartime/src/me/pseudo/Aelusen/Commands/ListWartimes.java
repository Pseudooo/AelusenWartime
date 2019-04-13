package me.pseudo.Aelusen.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.pseudo.Aelusen.Wartime;
import me.pseudo.Aelusen.WartimeScheduler;

public class ListWartimes implements CommandExecutor {
	
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
			
			for(Wartime wartime : wartimes.getWartimes()) { // MC Font is not monospaced so no inline formatting
				player.sendMessage("WartimeID: "+wartime.getID().toString());
				player.sendMessage(String.format("Millis: %d", wartime.getMillisUntil()));
				player.sendMessage(String.format("Seconds: %d", wartime.getMillisUntil() / 1000));
				player.sendMessage(String.format("Minutes: %d", wartime.getMillisUntil() / (60 * 1000)));
				player.sendMessage(String.format("Hours: %d", wartime.getMillisUntil() / (60 * 60 * 1000)));
			}
			
		}else {
			for(Wartime wartime : wartimes.getWartimes()) { // Console is monospaced so rigid formatting is possible
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
