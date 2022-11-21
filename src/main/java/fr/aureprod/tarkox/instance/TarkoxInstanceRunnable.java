package fr.aureprod.tarkox.instance;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import fr.aureprod.tarkox.exception.TarkoxInstanceNotStartedException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;

public class TarkoxInstanceRunnable implements Runnable {
    private TarkoxInstance instance;

    public TarkoxInstanceRunnable(TarkoxInstance instance) {
        this.instance = instance;
    }

    @Override
    public void run() {
        if (instance.isInGame()) {
            if (instance.getCurrentTime() >= instance.plugin.configController.getGameMaxTime()) instance.stopInstance();
            else {
                instance.incrementCurrentTime();

                List<TarkoxInstancePlayer> tarkoxInstancePlayers = new ArrayList<TarkoxInstancePlayer>(instance.getInGameTarkoxInstancePlayers()); 

                for (TarkoxInstancePlayer tarkoxInstancePlayer : tarkoxInstancePlayers) {
                    tarkoxInstancePlayer.applyOrUpdateScoreBoard();
                    Player player = tarkoxInstancePlayer.getPlayer();

                    if (tarkoxInstancePlayer.hasExtractionArea() && tarkoxInstancePlayer.getExtractionArea().isInZone(player)) {
                        if (tarkoxInstancePlayer.getExtractionArea().getWaitTime() <= tarkoxInstancePlayer.getTimeInExctractionArea()) {
                            try {
                                instance.extactPlayer(player);
                            } 
                            catch (TarkoxPlayerNotInInstanceException | TarkoxInstanceNotStartedException e) {}
                        } 
                        else {
                            tarkoxInstancePlayer.incrementTimeInExctractionArea();

                            Integer timeLeft = tarkoxInstancePlayer.getExtractionArea().getWaitTime() - tarkoxInstancePlayer.getTimeInExctractionArea();
                            player.sendMessage("time before extraction : " + (timeLeft + 1) + "s"); // TODO:
                        }
                    }
                    else tarkoxInstancePlayer.resetExctractionInformations();
                }                    
            }
        }
        else {
            // Stop the timer
            instance.plugin.getServer().getScheduler().cancelTask(instance.getTaskRunnerId());
        }
    }
    
}
