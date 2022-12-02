package fr.aureprod.tarkox.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.datatype.ExtractionArea;
import fr.aureprod.tarkox.datatype.SpawnPosition;
import fr.aureprod.tarkox.exception.TarkoxInstanceAlreadyStartedException;
import fr.aureprod.tarkox.exception.TarkoxInstanceEnoughPlayersForStartException;
import fr.aureprod.tarkox.exception.TarkoxInstanceFullException;
import fr.aureprod.tarkox.exception.TarkoxInstanceNotStartedException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerDeadException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerExtractedException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerQuitException;
import fr.aureprod.tarkox.exception.TarkoxPlayerAlreadyInInstanceException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;

public class TarkoxInstance extends TarkoxInstancePlayerController {
    private int taskRunnerId;

    public TarkoxInstance(
        Plugin plugin, 
        String name, 
        Integer durationTime,
        Integer waitTimeBeforeTp,
        Integer maxPlayers,
        List<ExtractionArea> extractionAreas, 
        HashMap<String, List<Chest>> chests,
        List<SpawnPosition> spawns
    ) {
        super(plugin, name, durationTime, waitTimeBeforeTp, maxPlayers, extractionAreas, chests, spawns);
    }

    public int getTaskRunnerId() {
        return this.taskRunnerId;
    }

    private void startTimer() {
        this.setStatusInGame();
        this.resetCurrentTime();

        this.taskRunnerId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new TarkoxInstanceRunnable(this), 0L, 20L);
    }

    private void stopTimer() {
        this.setStatusWaiting();
        this.resetCurrentTime();
    }

    private void removePlayer(Player player, Boolean needClearInventory, Boolean needSpawnTeleportation) throws TarkoxPlayerNotInInstanceException {
        if (!this.isInGamePlayer(player)) throw new TarkoxPlayerNotInInstanceException(player);
    
        TarkoxInstancePlayer tarkoxInstancePlayer = this.getTarkoxInstancePlayerByPlayer(player);

        tarkoxInstancePlayer.removeScoreBoard();
        
        this.removeInGamePlayer(tarkoxInstancePlayer);

        if (this.getInGamePlayersCount() < 1) this.stopInstance();
        
        if (needClearInventory) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        if (needSpawnTeleportation) player.teleport(this.plugin.configController.getWorldSpawn().toLocation());
    }
    
    public void joinPlayer(Player player) throws TarkoxInstancePlayerQuitException, TarkoxPlayerAlreadyInInstanceException, TarkoxInstanceFullException, TarkoxInstancePlayerExtractedException, TarkoxInstancePlayerDeadException, TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        if (this.plugin.instanceController.isPlayerInInstance(player)) throw new TarkoxPlayerAlreadyInInstanceException(player);
        if (this.getInGamePlayersCount() >= this.getMaxPlayers()) throw new TarkoxInstanceFullException();
        if (this.isInGamePlayer(player)) throw new TarkoxPlayerAlreadyInInstanceException(player);
        if (this.isPlayerQuit(player)) throw new TarkoxInstancePlayerQuitException(player);
        if (this.isPlayerExtracted(player)) throw new TarkoxInstancePlayerExtractedException(player);
        if (this.isPlayerDead(player)) throw new TarkoxInstancePlayerDeadException(player);

        TarkoxInstancePlayer tarkoxInstancePlayer = new TarkoxInstancePlayer(this.plugin, this, player);

        this.addInGamePlayer(tarkoxInstancePlayer);

        if (this.isWaiting()) this.startInstance();

        // timer of 10 seconds before add player in game
        int taskRunnerId = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new TarkoxInstanceRunnableBeforePlayerTp(this, tarkoxInstancePlayer), 0L, 20L);
    
        tarkoxInstancePlayer.setWaitBeforeTpTaskRunnerId(taskRunnerId);
    }

    public void leavePlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        this.removePlayer(player, true, true);

        if (this.isInGame()) this.addPlayerQuit(player);
    }

    public void startExtractPlayer(Player player) throws TarkoxPlayerNotInInstanceException, TarkoxInstanceNotStartedException {
        if (!this.isInGamePlayer(player)) throw new TarkoxPlayerNotInInstanceException(player);
        if (!this.isInGame()) throw new TarkoxInstanceNotStartedException();

        TarkoxInstancePlayer tarkoxInstancePlayer = this.getTarkoxInstancePlayerByPlayer(player);
        if (tarkoxInstancePlayer.hasExtractionArea()) return;

        ExtractionArea extractionArea = this.getExtractionArea(player);

        tarkoxInstancePlayer.resetExctractionWaitTime();
        tarkoxInstancePlayer.setExtractionArea(extractionArea);

        String string = this.plugin.configController.getString("you_start_your_extraction");
        player.sendMessage(string);
    }

    public void extactPlayer(Player player) throws TarkoxPlayerNotInInstanceException, TarkoxInstanceNotStartedException {
        if (!this.isInGame()) throw new TarkoxInstanceNotStartedException();
        
        this.removePlayer(player, false, true);

        String string = this.plugin.configController.getString("you_has_extracted");
        player.sendMessage(string);

        // reset player life and food
        player.setHealth(20);
        player.setFoodLevel(20);

        this.addPlayerExtracted(player);
    }

    public void killedPlayer(Player player) throws TarkoxPlayerNotInInstanceException, TarkoxInstanceNotStartedException {
        if (!this.isInGame()) throw new TarkoxInstanceNotStartedException();

        this.removePlayer(player, false, false);

        this.addPlayerDead(player);
    }

    public void startInstance() throws TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        if (this.getInGamePlayersCount() < 1) throw new TarkoxInstanceEnoughPlayersForStartException();
        if (this.isInGame()) throw new TarkoxInstanceAlreadyStartedException();
        
        System.out.println("[Tarkox] Instance " + this.getName() + " started");

        this.startTimer();

        // foreach hasmap
        for (Entry<String, List<Chest>> entry : this.chests.entrySet()) {
            String lootsType = entry.getKey();
            List<Chest> chestsList = entry.getValue();

            for (Chest chest : chestsList) {
                chest.getInventory().clear();
    
                List<ItemStack> items = this.plugin.configController.getRandomLoots(lootsType);
                for (ItemStack item : items) {
                    chest.getInventory().addItem(item);
                }
            }
        }
    }

    public void stopInstance() {
        this.stopTimer();

        List<Player> players = new ArrayList<Player>(this.getInGamePlayers()); 

        for (Player player : players) {
            try {
                this.removePlayer(player, true, true);
            } 
            catch (TarkoxPlayerNotInInstanceException e) {}
        }

        // wait 5 seconds before remove instance
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                clearPlayersInGame();
                clearPlayersQuit();
                clearPlayersExtracted();
                clearPlayersDead();

                System.out.println("[Tarkox] Instance " + getName() + " stopped");
            }
        }, 100L);
    }   
}
