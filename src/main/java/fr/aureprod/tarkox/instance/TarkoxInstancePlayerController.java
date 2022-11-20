package fr.aureprod.tarkox.instance;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.datatype.ExtractionArea;
import fr.aureprod.tarkox.datatype.SpawnPosition;
import fr.aureprod.tarkox.exception.TarkoxPlayerNotInInstanceException;

public class TarkoxInstancePlayerController extends TarkoxInstanceParent {
    private List<TarkoxInstancePlayer> playersInGame;
    private List<Player> playersKicks;
    private List<Player> playersQuit;
    private List<Player> playersExtracted;
    private List<Player> playersDead;

    public TarkoxInstancePlayerController(
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
        
        this.playersInGame = new ArrayList<TarkoxInstancePlayer>();  
        this.playersQuit = new ArrayList<Player>();
        this.playersKicks = new ArrayList<Player>();
        this.playersExtracted = new ArrayList<Player>();
        this.playersDead = new ArrayList<Player>();
    }

    protected void addInGamePlayer(TarkoxInstancePlayer tarkoxInstancePlayer) {
        this.playersInGame.add(tarkoxInstancePlayer);
    }

    protected void removeInGamePlayer(TarkoxInstancePlayer tarkoxInstancePlayer) {
        this.playersInGame.remove(tarkoxInstancePlayer);
    }

    protected Boolean isInGamePlayer(Player player) {
        for (TarkoxInstancePlayer tarkoxInstancePlayer : this.playersInGame) {
            if (tarkoxInstancePlayer.getPlayer().equals(player)) {
                return true;
            }
        }

        return false;
    }

    protected void clearPlayersInGame() {
        this.playersInGame.clear();
    }

    protected void addPlayerQuit(Player player) {
        this.playersQuit.add(player);
    }

    protected void removePlayerQuit(Player player) {
        this.playersQuit.remove(player);
    }

    protected Boolean isPlayerQuit(Player player) {
        return this.playersQuit.contains(player);
    }

    protected void clearPlayersQuit() {
        this.playersQuit.clear();
    }

    protected void addPlayerKick(Player player) {
        this.playersKicks.add(player);
    }

    protected void removePlayerKick(Player player) {
        this.playersKicks.remove(player);
    }

    protected Boolean isPlayerKick(Player player) {
        return this.playersKicks.contains(player);
    }

    protected void clearPlayersKicks() {
        this.playersKicks.clear();
    }

    protected void addPlayerExtracted(Player player) {
        this.playersExtracted.add(player);
    }

    protected void removePlayerExtracted(Player player) {
        this.playersExtracted.remove(player);
    }

    protected Boolean isPlayerExtracted(Player player) {
        return this.playersExtracted.contains(player);
    }

    protected void clearPlayersExtracted() {
        this.playersExtracted.clear();
    }

    protected void addPlayerDead(Player player) {
        this.playersDead.add(player);
    }

    protected void removePlayerDead(Player player) {
        this.playersDead.remove(player);
    }

    protected Boolean isPlayerDead(Player player) {
        return this.playersDead.contains(player);
    }

    protected void clearPlayersDead() {
        this.playersDead.clear();
    }

    public List<Player> getInGamePlayers() {
        List<Player> players = new ArrayList<Player>();

        for (TarkoxInstancePlayer tarkoxInstancePlayer : this.playersInGame) {
            players.add(tarkoxInstancePlayer.getPlayer());
        }

        return players;
    }

    protected List<TarkoxInstancePlayer> getInGameTarkoxInstancePlayers() {
        return this.playersInGame;
    }

    protected TarkoxInstancePlayer getTarkoxInstancePlayerByPlayer(Player player) throws TarkoxPlayerNotInInstanceException {
        for (TarkoxInstancePlayer tarkoxInstancePlayer : this.playersInGame) {
            if (tarkoxInstancePlayer.getPlayer().equals(player)) {
                return tarkoxInstancePlayer;
            }
        }

        throw new TarkoxPlayerNotInInstanceException(player);
    }

    public Integer getInGamePlayersCount() {
        return this.playersInGame.size();
    }

    public Integer getExtractionAreaPlayersCount() {
        return this.playersExtracted.size();
    }

    public Boolean isInExtractionArea(Player player) {
        for (ExtractionArea extractionArea : this.extractionAreas) {
            if (extractionArea.isInZone(player)) return true;
        }
        
        return false;
    }

    public ExtractionArea getExtractionArea(Player player) {
        for (ExtractionArea extractionArea : this.extractionAreas) {
            if (extractionArea.isInZone(player)) return extractionArea;
        }

        throw new RuntimeException("Player not in extraction area");
    }
}
