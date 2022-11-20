package fr.aureprod.tarkox.scoreboard;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.aureprod.tarkox.Plugin;
import fr.aureprod.tarkox.instance.TarkoxInstance;
import fr.aureprod.tarkox.instance.TarkoxInstancePlayer;

public class ScoreBoardController {
    private Plugin plugin;
    private TarkoxInstance tarkoxInstance;
    private TarkoxInstancePlayer tarkoxInstancePlayer;
    private ScoreboardManager scoreboardManager;
    private String scoreboardUniqueId;

    public ScoreBoardController(Plugin plugin, TarkoxInstance tarkoxInstance, TarkoxInstancePlayer tarkoxInstancePlayer) {
        this.plugin = plugin;
        this.tarkoxInstance = tarkoxInstance;
        this.tarkoxInstancePlayer = tarkoxInstancePlayer;
        this.scoreboardManager = Bukkit.getScoreboardManager();
        
        // generate string unique for each player in the game of 10 characters
        this.scoreboardUniqueId = this.tarkoxInstancePlayer.getPlayer().getUniqueId().toString().substring(0, 10);
    }

    public Scoreboard getScoreboard() {
        Scoreboard board = scoreboardManager.getNewScoreboard();

        String title = this.plugin.configController.getScoreBoardTitle();
        Objective objective = board.registerNewObjective(scoreboardUniqueId, "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Integer alivePlayers = this.tarkoxInstance.getInGamePlayersCount();
        Integer extractedPlayers = this.tarkoxInstance.getExtractionAreaPlayersCount();
        Integer totalPlayers = alivePlayers + extractedPlayers;
        Integer playerKills = this.tarkoxInstancePlayer.getKilledPlayersCount();

        Integer leftTimeInSeconds = this.tarkoxInstance.getLeftTime();
        String time = String.format("%02d:%02d:%02d", leftTimeInSeconds / 3600, (leftTimeInSeconds % 3600) / 60, (leftTimeInSeconds % 60));

        List<String> scoreBoardLines = this.plugin.configController.getScoreBoardLines();
        for (String scoreBoardLine : scoreBoardLines) {
            Integer scoreBoardLineIndex = scoreBoardLines.indexOf(scoreBoardLine);

            scoreBoardLine = scoreBoardLine.replace("<TIME>", time);
            scoreBoardLine = scoreBoardLine.replace("<PLAYERS>", totalPlayers.toString());
            scoreBoardLine = scoreBoardLine.replace("<ALIVE>", alivePlayers.toString());
            scoreBoardLine = scoreBoardLine.replace("<EXTRACTED>", extractedPlayers.toString());
            scoreBoardLine = scoreBoardLine.replace("<KILLS>", playerKills.toString());

            Score score = objective.getScore(scoreBoardLine);
            score.setScore(- (scoreBoardLineIndex - scoreBoardLines.size()));
        }

        return board;
    }

    public Scoreboard getEmptyScoreboard() {
        return scoreboardManager.getNewScoreboard();
    }
}
