package fr.aureprod.tarkox.instance;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.datatype.ExtractionArea;
import fr.aureprod.tarkox.scoreboard.ScoreBoardController;

public class TarkoxInstancePlayer {
    private Player player;
    private Integer timeInExctractionArea;
    private ExtractionArea extractionArea;
    private List<Player> killedPlayers;
    private ScoreBoardController scoreBoardController;
    private int waitBeforeTpTaskRunnerId;
    private TarkoxInstanceStatus status;

    public TarkoxInstancePlayer(Plugin plugin, TarkoxInstance tarkoxInstance, Player player) {
        this.player = player;

        this.timeInExctractionArea = 0;
        this.extractionArea = null;
        this.status = TarkoxInstanceStatus.WAITING;

        this.killedPlayers = new ArrayList<Player>();
        this.scoreBoardController = new ScoreBoardController(plugin, tarkoxInstance, this);
    }

    public Player getPlayer() {
        return this.player;
    }

    public Integer getTimeInExctractionArea() {
        return this.timeInExctractionArea;
    }

    public void incrementTimeInExctractionArea() {
        this.timeInExctractionArea++;
    }

    public ExtractionArea getExtractionArea() {
        return this.extractionArea;
    }

    public void setExtractionArea(ExtractionArea extractionArea) {
        this.extractionArea = extractionArea;
    }

    public Boolean hasExtractionArea() {
        return this.extractionArea != null;
    }

    public void resetExctractionWaitTime() {
        this.timeInExctractionArea = 0;
    }

    public void resetExctractionInformations() {
        this.resetExctractionWaitTime();
        this.extractionArea = null;
    }

    public Integer getKilledPlayersCount() {
        return this.killedPlayers.size();
    }

    public void addKilledPlayer(Player player) {
        this.killedPlayers.add(player);
    }

    public void applyOrUpdateScoreBoard() {
        this.player.setScoreboard(this.scoreBoardController.getScoreboard());
    }

    public void removeScoreBoard() {
        this.player.setScoreboard(this.scoreBoardController.getEmptyScoreboard());
    }

    public Boolean isInGame() {
        return this.status == TarkoxInstanceStatus.INGAME;
    }

    public Boolean isWaiting() {
        return this.status == TarkoxInstanceStatus.WAITING;
    }

    public void setInGame() {
        this.status = TarkoxInstanceStatus.INGAME;
    }

    public int getWaitBeforeTpTaskRunnerId() {
        return this.waitBeforeTpTaskRunnerId;
    }

    public void setWaitBeforeTpTaskRunnerId(int waitBeforeTpTaskRunnerId) {
        this.waitBeforeTpTaskRunnerId = waitBeforeTpTaskRunnerId;
    }
}
