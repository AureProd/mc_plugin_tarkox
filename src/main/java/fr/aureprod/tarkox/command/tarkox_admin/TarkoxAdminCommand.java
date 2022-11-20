package fr.aureprod.tarkox.command.tarkox_admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.exception.TarkoxInstanceAlreadyStartedException;
import fr.aureprod.tarkox.exception.TarkoxInstanceDoesNotExistException;
import fr.aureprod.tarkox.exception.TarkoxInstanceEnoughPlayersForStartException;
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
                if (subCommand.equalsIgnoreCase("kick")) {
                    if (args.length == 3) {
                        String mapName = args[1];
                        String playerName = args[2];
                        Player kickedPlayer = this.plugin.getServer().getPlayer(playerName);
    
                        this.kickAction(sender, mapName, kickedPlayer);
    
                        return true;
                    }
                    else this.sendKickSubCommandTemplate(sender);
                }
                else if (subCommand.equalsIgnoreCase("status")) {
                    if (args.length == 2) {
                        String mapName = args[1];
    
                        this.statusAction(sender, mapName);
    
                        return true;
                    }
                    else this.sendStatusSubCommandTemplate(sender);
                }
                else if (subCommand.equalsIgnoreCase("start")) {
                    if (args.length == 2) {
                        String mapName = args[1];
    
                        this.startAction(sender, mapName);

                        return true;
                    }
                    else this.sendStartSubCommandTemplate(sender);
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
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstanceDoesNotExistException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxPlayerNotInInstanceException e) {
                // TODO: faire exceptions avec des messages du config
            }
            catch (TarkoxInstanceAlreadyStartedException e) {
                // TODO: faire exceptions avec des messages du config
            } 
            catch (TarkoxInstanceEnoughPlayersForStartException e) {
                // TODO: faire exceptions avec des messages du config
            }
        } 
        else this.sendTarkoxAdminCommandTemplate(sender);
        
        return false;
    }

    private void kickAction(CommandSender sender, String mapName, Player kickedPlayer) throws TarkoxInstanceDoesNotExistException, TarkoxPlayerNotInInstanceException, TarkoxSenderIsNotPlayerException {
        TarkoxInstance tarkoxInstance = this.getTarkoxInstanceBySenderAndName(sender, mapName);
    
        tarkoxInstance.kickPlayer(kickedPlayer);

        sender.sendMessage("§a The player '" + kickedPlayer.getName() + "' has been kicked from the map '" + mapName + "'."); // TODO: message dans le config
    }

    private void statusAction(CommandSender sender, String mapName) throws TarkoxInstanceDoesNotExistException, TarkoxPlayerNotInInstanceException, TarkoxSenderIsNotPlayerException {
        TarkoxInstance tarkoxInstance = this.getTarkoxInstanceBySenderAndName(sender, mapName);
    
        sender.sendMessage("§aMap: '" + tarkoxInstance.getName() + "'");
        sender.sendMessage("§aPlayers: " + tarkoxInstance.getInGamePlayersCount().toString());
        sender.sendMessage("§aStatus: " + tarkoxInstance.getStatus());
    }

    private void startAction(CommandSender sender, String mapName) throws TarkoxInstanceDoesNotExistException, TarkoxPlayerNotInInstanceException, TarkoxSenderIsNotPlayerException, TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        TarkoxInstance tarkoxInstance = this.getTarkoxInstanceBySenderAndName(sender, mapName);
    
        tarkoxInstance.startInstance();

        sender.sendMessage("§aThe map '" + mapName + "' has been started."); // TODO: message dans le config
    }

    private void stopAction(CommandSender sender, String mapName) throws TarkoxInstanceDoesNotExistException, TarkoxPlayerNotInInstanceException, TarkoxSenderIsNotPlayerException {
        TarkoxInstance tarkoxInstance = this.getTarkoxInstanceBySenderAndName(sender, mapName);
    
        tarkoxInstance.stopInstance();

        sender.sendMessage("§a The map '" + mapName + "' has been stopped."); // TODO: message dans le config
    }
}
