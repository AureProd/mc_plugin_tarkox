package fr.aureprod.tarkox.command.tarkox;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;

public class TarkoxTabCompleter extends TarkoxController implements TabCompleter {
    public TarkoxTabCompleter(Plugin plugin) {
        super(plugin);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;
        if (!command.getName().equalsIgnoreCase("tarkox")) return null;

        List<String> preTraited = new ArrayList<String>();
        List<String> results = new ArrayList<String>();

        if (args.length == 1) {
            preTraited.add(this.getJoinSubCommand());
            preTraited.add(this.getLeaveSubCommand());
        } 
        else if (args.length == 2) {
            String subCommand = args[0];
            if (subCommand.equalsIgnoreCase(this.getJoinSubCommand())) {
                List<String> mapsNames = plugin.instanceController.getNamesTarkoxInstances();
                preTraited.addAll(mapsNames);
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
