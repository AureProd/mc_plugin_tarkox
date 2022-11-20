package fr.aureprod.tarkox.command.tarkox;

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
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerKickedException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerQuitException;
import fr.aureprod.tarkox.exception.TarkoxPlayerAlreadyInInstanceException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;
import fr.aureprod.tarkox.instance.TarkoxInstance;

public class TarkoxCommand extends TarkoxController implements CommandExecutor {
    public TarkoxCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (!command.getName().equalsIgnoreCase("tarkox")) return false;

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
                else this.sendTarkoxCommandTemplate(player);
            }
            catch (TarkoxInstanceDoesNotExistException e) {
                // TODO: faire exceptions avec des messages du config
            }
            catch (TarkoxPlayerNotInInstanceException e) {
                // TODO: faire exceptions avec des messages du config
            }
            catch (TarkoxInstancePlayerQuitException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxPlayerAlreadyInInstanceException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstancePlayerExtractedException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstancePlayerKickedException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstancePlayerDeadException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstanceFullException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstanceAlreadyStartedException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstanceEnoughPlayersForStartException e) {
                // TODO: faire exceptions avec des messages du config
            }
        } 
        else this.sendTarkoxCommandTemplate(player);
        
        return false;
    }

    private void joinAction(Player player, String mapName) throws TarkoxInstanceDoesNotExistException, TarkoxInstancePlayerQuitException, TarkoxPlayerAlreadyInInstanceException, TarkoxInstancePlayerExtractedException, TarkoxInstancePlayerKickedException, TarkoxInstancePlayerDeadException, TarkoxInstanceFullException, TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        TarkoxInstance tarkoxInstance = plugin.instanceController.getTarkoxInstanceByName(mapName);

        tarkoxInstance.joinPlayer(player);

        player.sendMessage("§aYou have joined the game."); // TODO: message dans le config
    }

    private void leaveAction(Player player) throws TarkoxPlayerNotInInstanceException {
        TarkoxInstance tarkoxInstance = plugin.instanceController.getTarkoxInstanceByPlayer(player);

        tarkoxInstance.leavePlayer(player);

        player.sendMessage("§aYou have left the game."); // TODO: message dans le config
    }
}
