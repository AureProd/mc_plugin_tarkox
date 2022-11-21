package fr.aureprod.tarkox.instance;

import java.util.List;

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
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerKickedException;
import fr.aureprod.tarkox.exception.TarkoxInstancePlayerQuitException;
import fr.aureprod.tarkox.exception.TarkoxPlayerAlreadyInInstanceException;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TarkoxInstance extends TarkoxInstancePlayerController {
    private int taskRunner;

    public TarkoxInstance(
        Plugin plugin, 
        String name, 
        Integer minPlayers, 
        Integer maxPlayers,
        SpawnPosition waitAreaSpawn, 
        List<ExtractionArea> extractionAreas, 
        List<Chest> chests,
        List<SpawnPosition> spawns
    ) {
        super(plugin, name, minPlayers, maxPlayers, waitAreaSpawn, extractionAreas, chests, spawns);
    
        this.taskRunner = 0;
    }

    private void startTimer() {
        this.setStatusInGame();
        this.resetCurrentTime();

        this.taskRunner = this.runGameRunner();
    }

    private void stopTimer() {
        this.setStatusWaiting();
        this.resetCurrentTime();

        this.taskRunner = 0;
    }

    private int runGameRunner() {
        return this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                if (isInGame()) {
                    if (getCurrentTime() >= plugin.configController.getGameMaxTime()) stopInstance();
                    else {
                        incrementCurrentTime();

                        for (TarkoxInstancePlayer tarkoxInstancePlayer : getInGameTarkoxInstancePlayers()) {
                            tarkoxInstancePlayer.applyOrUpdateScoreBoard();
                            Player player = tarkoxInstancePlayer.getPlayer();

                            if (tarkoxInstancePlayer.hasExtractionArea() && tarkoxInstancePlayer.getExtractionArea().isInZone(player)) {
                                if (tarkoxInstancePlayer.getExtractionArea().getWaitTime() <= tarkoxInstancePlayer.getTimeInExctractionArea()) {
                                    try {
                                        extactPlayer(player);
                                    } catch (TarkoxPlayerNotInInstanceException e) {}
                                } 
                                else {
                                    tarkoxInstancePlayer.incrementTimeInExctractionArea();

                                    Integer timeLeft = tarkoxInstancePlayer.getExtractionArea().getWaitTime() - tarkoxInstancePlayer.getTimeInExctractionArea();
                                    player.sendMessage("time before extraction : " + timeLeft + "s"); // TODO:
                                }
                            }
                            else tarkoxInstancePlayer.resetExctractionInformations();
                        }
                    }
                }
                else {
                    // Stop the timer
                    plugin.getServer().getScheduler().cancelTask(taskRunner);
                }
            }
        }, 0L, 20L);
    }

    private void removePlayer(Player player, Boolean needClearInventory, Boolean needSpawnTeleportation) throws TarkoxPlayerNotInInstanceException {
        if (!this.isInGamePlayer(player)) throw new TarkoxPlayerNotInInstanceException(player);
        
        TarkoxInstancePlayer tarkoxInstancePlayer = this.getTarkoxInstancePlayerByPlayer(player);

        tarkoxInstancePlayer.removeScoreBoard();
        
        this.removeInGamePlayer(tarkoxInstancePlayer);
        
        if (needClearInventory) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        if (needSpawnTeleportation) player.teleport(this.plugin.configController.getWorldSpawn().toLocation());
    }
    
    public void joinPlayer(Player player) throws TarkoxInstancePlayerQuitException, TarkoxPlayerAlreadyInInstanceException, TarkoxInstanceFullException, TarkoxInstancePlayerExtractedException, TarkoxInstancePlayerKickedException, TarkoxInstancePlayerDeadException, TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        if (this.plugin.instanceController.isPlayerInInstance(player)) throw new TarkoxPlayerAlreadyInInstanceException(player);
        if (this.getInGamePlayersCount() >= this.getMaxPlayers()) throw new TarkoxInstanceFullException();
        if (this.isInGamePlayer(player)) throw new TarkoxPlayerAlreadyInInstanceException(player);
        if (this.isPlayerQuit(player)) throw new TarkoxInstancePlayerQuitException(player);
        if (this.isPlayerKick(player)) throw new TarkoxInstancePlayerKickedException(player);
        if (this.isPlayerExtracted(player)) throw new TarkoxInstancePlayerExtractedException(player);
        if (this.isPlayerDead(player)) throw new TarkoxInstancePlayerDeadException(player);
        
        TarkoxInstancePlayer tarkoxInstancePlayer = new TarkoxInstancePlayer(this.plugin, this, player);

        this.addInGamePlayer(tarkoxInstancePlayer);

        player.teleport(this.getWaitAreaSpawn().toLocation());

        if (this.getInGamePlayersCount() >= this.getMinPlayers()) this.startInstance();
    }

    public void leavePlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        this.removePlayer(player, true, true);

        if (this.isInGame()) this.addPlayerQuit(player);
    }

    public void kickPlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        this.removePlayer(player, true, true);

        this.addPlayerKick(player);
    }

    public void startExtractPlayer(Player player) throws TarkoxPlayerNotInInstanceException, TarkoxInstanceNotStartedException {
        if (!this.isInGamePlayer(player)) throw new TarkoxPlayerNotInInstanceException(player);
        if (!this.isInGame()) throw new TarkoxInstanceNotStartedException();

        TarkoxInstancePlayer tarkoxInstancePlayer = this.getTarkoxInstancePlayerByPlayer(player);
        if (tarkoxInstancePlayer.hasExtractionArea()) return;

        ExtractionArea extractionArea = this.getExtractionArea(player);

        tarkoxInstancePlayer.resetExctractionWaitTime();
        tarkoxInstancePlayer.setExtractionArea(extractionArea);

        player.sendMessage("start extraction"); // TODO:
    }

    public void extactPlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        this.removePlayer(player, false, true);

        if (this.isInGame()) this.addPlayerExtracted(player);
    }

    public void killedPlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        this.removePlayer(player, false, false);

        if (this.isInGame()) this.addPlayerDead(player);
    }

    public void startInstance() throws TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        if (this.getInGamePlayersCount() < this.getMinPlayers()) throw new TarkoxInstanceEnoughPlayersForStartException();
        if (this.isInGame()) throw new TarkoxInstanceAlreadyStartedException();
        
        this.startTimer();

        for (Chest chest : this.chests) {
            chest.getInventory().clear();

            List<ItemStack> items = this.plugin.configController.getRandomLootsList();
            for (ItemStack item : items) {
                chest.getInventory().addItem(item);
            }
        }

        for (Player player : this.getInGamePlayers()) {
            SpawnPosition spawn = this.getRandomSpawn();
            player.teleport(spawn.toLocation());

            player.sendTitle("La game démarre", "Fouillez les coffres, survivez et extractez-vous", 10, 70, 20);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Hello, world!"));
        }
    }

    public void stopInstance() {
        this.stopTimer();

        for (Player player : this.getInGamePlayers()) {
            try {
                this.removePlayer(player, true, true);
            } catch (TarkoxPlayerNotInInstanceException e) {}
        }

        this.clearPlayersInGame();
        this.clearPlayersQuit();
        this.clearPlayersKicks();
        this.clearPlayersExtracted();
        this.clearPlayersDead();
    }   
}
