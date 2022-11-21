package fr.aureprod.tarkox.instance;

import java.util.ArrayList;
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
    private int taskRunnerId;

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
    }

    public int getTaskRunnerId() {
        return this.taskRunnerId;
    }

    private void startTimer() {
        this.setStatusInGame();
        this.resetCurrentTime();

        this.taskRunnerId = this.runGameRunner();
    }

    private void stopTimer() {
        this.setStatusWaiting();
        this.resetCurrentTime();
    }

    private int runGameRunner() {
        return this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new TarkoxInstanceRunnable(this), 0L, 20L);
    }

    private void removePlayer(Player player, Boolean needClearInventory, Boolean needSpawnTeleportation) throws TarkoxPlayerNotInInstanceException {
        if (!this.isInGamePlayer(player)) throw new TarkoxPlayerNotInInstanceException(player);
    
        TarkoxInstancePlayer tarkoxInstancePlayer = this.getTarkoxInstancePlayerByPlayer(player);

        tarkoxInstancePlayer.removeScoreBoard();
        
        this.removeInGamePlayer(tarkoxInstancePlayer);

        if (this.getInGamePlayersCount() < this.getMinPlayers()) this.stopInstance();
        
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

    public void extactPlayer(Player player) throws TarkoxPlayerNotInInstanceException, TarkoxInstanceNotStartedException {
        if (!this.isInGame()) throw new TarkoxInstanceNotStartedException();
        
        this.removePlayer(player, false, true);

        this.addPlayerExtracted(player);
    }

    public void killedPlayer(Player player) throws TarkoxPlayerNotInInstanceException, TarkoxInstanceNotStartedException {
        if (!this.isInGame()) throw new TarkoxInstanceNotStartedException();

        this.removePlayer(player, false, false);

        this.addPlayerDead(player);
    }

    public void startInstance() throws TarkoxInstanceAlreadyStartedException, TarkoxInstanceEnoughPlayersForStartException {
        if (this.getInGamePlayersCount() < this.getMinPlayers()) throw new TarkoxInstanceEnoughPlayersForStartException();
        if (this.isInGame()) throw new TarkoxInstanceAlreadyStartedException();
        
        System.out.println("[Tarkox] Instance " + this.getName() + " started");

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

        List<Player> players = new ArrayList<Player>(this.getInGamePlayers()); 

        for (Player player : players) {
            try {
                this.removePlayer(player, true, true);
            } 
            catch (TarkoxPlayerNotInInstanceException e) {}
        }

        this.clearPlayersInGame();
        this.clearPlayersQuit();
        this.clearPlayersKicks();
        this.clearPlayersExtracted();
        this.clearPlayersDead();

        System.out.println("[Tarkox] Instance " + this.getName() + " stopped");
    }   
}
