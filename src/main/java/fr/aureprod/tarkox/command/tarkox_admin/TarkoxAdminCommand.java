package fr.aureprod.tarkox.command.tarkox_admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.exception.TarkoxInstanceDoesNotExistException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;
import fr.aureprod.tarkox.exception.TarkoxSenderIsNotPlayerException;
import fr.aureprod.tarkox.instance.TarkoxInstance;

public class TarkoxAdminCommand extends TarkoxAdminController implements CommandExecutor {
    public TarkoxAdminCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("tarkox-admin")) return false;

        if (args.length > 0) {
            String subCommand = args[0];

            try {
                if (subCommand.equalsIgnoreCase("status")) {
                    if (args.length == 2) {
                        String mapName = args[1];
    
                        this.statusAction(sender, mapName);
    
                        return true;
                    }
                    else this.sendStatusSubCommandTemplate(sender);
                }
                else if (subCommand.equalsIgnoreCase("stop")) {
                    if (args.length == 2) {
                        String mapName = args[1];
    
                        this.stopAction(sender, mapName);
    
                        return true;
                    }
                    else this.sendStopSubCommandTemplate(sender);
                }
                else this.sendTarkoxAdminCommandTemplate(sender);
            } 
            catch (TarkoxSenderIsNotPlayerException e) {
                sender.sendMessage(this.plugin.configController.getString("error_command_sender_is_not_player"));
            } 
            catch (TarkoxInstanceDoesNotExistException e) {
                sender.sendMessage(this.plugin.configController.getString("error_game_does_not_exist"));
            } 
            catch (TarkoxPlayerNotInInstanceException e) {
                String message = this.plugin.configController.getString("error_player_not_in_game", e.getPlayer());
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    
                    if (e.isPlayer(player)) message = this.plugin.configController.getString("error_you_are_not_in_game");
                }

                sender.sendMessage(message);
            }
        } 
        else this.sendTarkoxAdminCommandTemplate(sender);
        
        return false;
    }

    private void statusAction(CommandSender sender, String mapName) throws TarkoxInstanceDoesNotExistException, TarkoxPlayerNotInInstanceException, TarkoxSenderIsNotPlayerException {
        TarkoxInstance tarkoxInstance = this.getTarkoxInstanceBySenderAndName(sender, mapName);
    
        String stringMap = this.plugin.configController.getString("status_map_name", "MAP", tarkoxInstance.getName());
        String stringPlayers = this.plugin.configController.getString("status_players_count", "PLAYERS", tarkoxInstance.getInGamePlayersCount());
        String stringStatus = this.plugin.configController.getString("status_map_status", "STATUS", tarkoxInstance.getStatus());
        
        sender.sendMessage(stringMap);
        sender.sendMessage(stringPlayers);
        sender.sendMessage(stringStatus);
    }

    private void stopAction(CommandSender sender, String mapName) throws TarkoxInstanceDoesNotExistException, TarkoxPlayerNotInInstanceException, TarkoxSenderIsNotPlayerException {
        TarkoxInstance tarkoxInstance = this.getTarkoxInstanceBySenderAndName(sender, mapName);
    
        tarkoxInstance.stopInstance();

        String string = this.plugin.configController.getString("map_has_stopped", "MAP", tarkoxInstance.getName());
        sender.sendMessage(string);
    }
}
