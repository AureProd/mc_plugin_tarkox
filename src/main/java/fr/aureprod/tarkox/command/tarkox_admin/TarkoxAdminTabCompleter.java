package fr.aureprod.tarkox.command.tarkox_admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.exception.TarkoxInstanceDoesNotExistException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;
import fr.aureprod.tarkox.instance.TarkoxInstance;

public class TarkoxAdminTabCompleter extends TarkoxAdminController implements TabCompleter {
    public TarkoxAdminTabCompleter(Plugin plugin) {
        super(plugin);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;
        if (!command.getName().equalsIgnoreCase("tarkox-admin")) return null;

        Player player = (Player) sender;

        List<String> preTraited = new ArrayList<String>();
        List<String> results = new ArrayList<String>();

        if (args.length == 1) {
            preTraited.add(this.getKickSubCommand());
            preTraited.add(this.getStatusSubCommand());
            preTraited.add(this.getStartSubCommand());
            preTraited.add(this.getStopSubCommand());
        } 
        else if (args.length == 2) {
            String subCommand = args[0];
            if (
                subCommand.equalsIgnoreCase(this.getKickSubCommand()) ||
                subCommand.equalsIgnoreCase(this.getStatusSubCommand()) ||
                subCommand.equalsIgnoreCase(this.getStartSubCommand()) ||
                subCommand.equalsIgnoreCase(this.getStopSubCommand())
            ) {
                List<String> tarkoxInstancesNames = this.plugin.instanceController.getNamesTarkoxInstances();
            
                try {
                    this.plugin.instanceController.getTarkoxInstanceByPlayer(player);

                    preTraited.add("this");
                } catch (TarkoxPlayerNotInInstanceException e) {}

                for (String tarkoxInstanceName : tarkoxInstancesNames) {
                    preTraited.add(tarkoxInstanceName);
                }
            } 
        }
        else if (args.length == 3) {
            String subCommand = args[0];
            if (subCommand.equalsIgnoreCase(this.getKickSubCommand())) {
                String tarkoxInstanceName = args[1];
                
                try {
                    TarkoxInstance tarkoxInstance = this.plugin.instanceController.getTarkoxInstanceByName(tarkoxInstanceName);
                
                    List<Player> tarkoxInstancePlayers = tarkoxInstance.getInGamePlayers();

                    for (Player tarkoxInstancePlayer : tarkoxInstancePlayers) {
                        preTraited.add(tarkoxInstancePlayer.getName());
                    }
                } catch (TarkoxInstanceDoesNotExistException e) {}
            } 
        }
        
        String searchField = args[args.length - 1];
        for (String element : preTraited) {
            if (element.toLowerCase().startsWith(searchField.toLowerCase())) {
                results.add(element);
            }
        }

        return results;
    }
    
}
