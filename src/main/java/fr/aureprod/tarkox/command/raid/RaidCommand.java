package fr.aureprod.tarkox.command.raid;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.exception.TarkoxInstanceAlreadyStartedException;
import fr.aureprod.tarkox.exception.TarkoxInstanceDoesNotExistException;
import fr.aureprod.tarkox.exception.TarkoxInstanceEnoughPlayersForStartException;
import fr.aureprod.tarkox.exception.TarkoxInstanceFullException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerDeadException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerExtractedException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerQuitException;
import fr.aureprod.tarkox.exception.TarkoxPlayerAlreadyInInstanceException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;
import fr.aureprod.tarkox.instance.TarkoxInstance;

public class RaidCommand extends RaidController implements CommandExecutor {
    public RaidCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("raid")) return false;
        if (!(sender instanceof Player)) sender.sendMessage(this.plugin.configController.getString("error_command_sender_is_not_player"));

        Player player = (Player) sender;
        
        if (args.length > 0) {
            try {
                String subCommand = args[0];

                if (subCommand.equalsIgnoreCase(this.getJoinSubCommand())) {
                    if (args.length == 2) {
                        String mapName = args[1];
    
                        this.joinAction(player, mapName);
    
                        return true;
                    } 
                    else this.sendJoinSubCommandTemplate(player);
                } 
                else if (subCommand.equalsIgnoreCase(this.getLeaveSubCommand())) {
                    this.leaveAction(player);
    
                    return true;
                } 
                else this.sendRaidCommandTemplate(player);
            }
            catch (TarkoxInstanceDoesNotExistException e) {
                player.sendMessage(this.plugin.configController.getString("error_game_does_not_exist"));
            }
            catch (TarkoxPlayerNotInInstanceException e) {
                String message = this.plugin.configController.getString("error_player_not_in_game", e.getPlayer());
                if (e.isPlayer(player)) message = this.plugin.configController.getString("error_you_are_not_in_game");

                player.sendMessage(message);
            }
            catch (TarkoxInstancePlayerQuitException e) {
                String message = this.plugin.configController.getString("error_player_has_quit_game", e.getPlayer());
                if (e.isPlayer(player)) message = this.plugin.configController.getString("error_you_have_quit_game");

                player.sendMessage(message);
            } 
            catch (TarkoxPlayerAlreadyInInstanceException e) {
                String message = this.plugin.configController.getString("error_player_already_in_game", e.getPlayer());
                if (e.isPlayer(player)) message = this.plugin.configController.getString("error_you_are_already_in_game");

                player.sendMessage(message);
            } 
            catch (TarkoxInstancePlayerExtractedException e) {
                String message = this.plugin.configController.getString("error_player_has_extracted", e.getPlayer());
                if (e.isPlayer(player)) message = this.plugin.configController.getString("error_you_have_extracted");

                player.sendMessage(message);
            }  
            catch (TarkoxInstancePlayerDeadException e) {
                String message = this.plugin.configController.getString("error_player_has_died", e.getPlayer());
                if (e.isPlayer(player)) message = this.plugin.configController.getString("error_you_have_died");

                player.sendMessage(message);
            } 
            catch (TarkoxInstanceFullException e) {
                player.sendMessage(this.plugin.configController.getString("error_game_is_full"));
            } 
            catch (TarkoxInstanceAlreadyStartedException e) {
                player.sendMessage(this.plugin.configController.getString("error_game_already_started"));
            } 
            catch (TarkoxInstanceEnoughPlayersForStartException e) {
                player.sendMessage(this.plugin.configController.getString("error_game_enough_players_to_start"));
            }
        } 
        else this.sendRaidCommandTemplate(player);
        
        return false;
    }

    private void joinAction(Player player, String mapName) throws TarkoxInstanceDoesNotExistException, TarkoxInstancePlayerQuitException, TarkoxPlayerAlreadyInInstanceException, TarkoxInstancePlayerExtractedException, TarkoxInstancePlayerDeadException, TarkoxInstanceFullException, TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        TarkoxInstance tarkoxInstance = plugin.instanceController.getTarkoxInstanceByName(mapName);

        tarkoxInstance.joinPlayer(player);

        String string = this.plugin.configController.getString("you_join_game_command");
        player.sendMessage(string);
    }

    private void leaveAction(Player player) throws TarkoxPlayerNotInInstanceException {
        TarkoxInstance tarkoxInstance = plugin.instanceController.getTarkoxInstanceByPlayer(player);

        tarkoxInstance.leavePlayer(player);

        String string = this.plugin.configController.getString("you_leave_game_command");
        player.sendMessage(string);
    }
}
