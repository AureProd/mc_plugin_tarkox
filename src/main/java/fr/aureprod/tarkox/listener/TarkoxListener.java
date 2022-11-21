package fr.aureprod.tarkox.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.exception.TarkoxInstanceNotStartedException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;
import fr.aureprod.tarkox.instance.TarkoxInstance;
import fr.aureprod.tarkox.instance.TarkoxInstancePlayer;

public class TarkoxListener implements Listener {
    Plugin plugin;

    public TarkoxListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!this.plugin.instanceController.isPlayerInInstance(player)) return;

        try {
            TarkoxInstance tarkoxInstance = this.plugin.instanceController.getTarkoxInstanceByPlayer(player);
        
            // get the player's killer if he has one
            Player killer = player.getKiller();
            if (killer != null) {
                TarkoxInstancePlayer tarkoxInstanceKiller = this.plugin.instanceController.getTarkoxInstancePlayerByPlayer(killer);
                tarkoxInstanceKiller.addKilledPlayer(player);
            }

            tarkoxInstance.killedPlayer(player);
        } 
        catch (TarkoxPlayerNotInInstanceException e) {}
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        try {
            if (!this.plugin.instanceController.isPlayerInInstance(player)) return;

            TarkoxInstance tarkoxInstance = this.plugin.instanceController.getTarkoxInstanceByPlayer(player);
            
            tarkoxInstance.leavePlayer(player);
        }
        catch (TarkoxPlayerNotInInstanceException e) {}
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent event) {
        Player player = event.getPlayer();

        try {
            if (!this.plugin.instanceController.isPlayerInInstance(player)) return;

            TarkoxInstance tarkoxInstance = this.plugin.instanceController.getTarkoxInstanceByPlayer(player);
            
            tarkoxInstance.leavePlayer(player);
        }
        catch (TarkoxPlayerNotInInstanceException e) {}
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        try {
            if (!this.plugin.instanceController.isPlayerInInstance(player)) return;

            TarkoxInstance tarkoxInstance = this.plugin.instanceController.getTarkoxInstanceByPlayer(player);

            if (!tarkoxInstance.isInGame()) return;
            TarkoxInstancePlayer tarkoxInstancePlayer = this.plugin.instanceController.getTarkoxInstancePlayerByPlayer(player);

            if (tarkoxInstancePlayer.hasExtractionArea()) return;
            if (!tarkoxInstance.isInExtractionArea(player)) return;
            
            tarkoxInstance.startExtractPlayer(player);
        }
        catch (TarkoxPlayerNotInInstanceException e) {
            e.printStackTrace();
        } 
        catch (TarkoxInstanceNotStartedException e) {
            e.printStackTrace();
        }
    }
}
