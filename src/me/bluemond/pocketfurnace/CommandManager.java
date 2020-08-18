package me.bluemond.pocketfurnace;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandManager implements CommandExecutor {

    private final PocketFurnace plugin;

    public CommandManager(PocketFurnace plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {

        //check that a player is using this
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED + "/pocketfurnace ONLY USABLE BY PLAYERS!");
            return true;
        }

        // check that an argument is used
        if(args.length < 1) return false;

        // check that a number is used
        int index;
        try{
            index = Integer.parseInt(args[0].trim());
        }catch(NumberFormatException e){
            return false;
        }

        // check index is within bounds
        if(!(index >= 1 && index <= 4)) return false;

        // run only if they have permission
        if(commandSender.hasPermission("pocketfurnace.use." + index)){
            Player player = (Player) commandSender;
            plugin.getAllocationManager().openFurnace(player, index);

        }else{
            commandSender.sendMessage(ChatColor.RED + "You do not have permission to use this.");
        }

        return true;
    }

}
