package fr.aureprod.tarkox.instance;

import org.bukkit.entity.Player;

import fr.aureprod.tarkox.datatype.SpawnPosition;

public class TarkoxInstanceRunnableBeforePlayerTp implements Runnable {
    private TarkoxInstance instance;
    private TarkoxInstancePlayer tarkoxInstancePlayer;
    private Player player;
    private int timer;
    private int timerMax;

    public TarkoxInstanceRunnableBeforePlayerTp(TarkoxInstance instance, TarkoxInstancePlayer tarkoxInstancePlayer) {
        this.instance = instance;
        this.tarkoxInstancePlayer = tarkoxInstancePlayer;
        this.player = tarkoxInstancePlayer.getPlayer();

        this.timer = 0;
        this.timerMax = 10;
    }

    @Override
    public void run() {
        if (instance.isInGame()) {
            if (this.timer >= this.timerMax) {
                SpawnPosition spawn = instance.getRandomSpawn();
                player.teleport(spawn.toLocation());

                String titleInTitle = this.instance.plugin.configController.getString("you_join_game_title_of_title");
                String subTitleInTitle = this.instance.plugin.configController.getString("you_join_game_subtitle_of_title");
                player.sendTitle(titleInTitle, subTitleInTitle, 10, 70, 20);
                
                tarkoxInstancePlayer.setInGame();

                // Stop the timer
                instance.plugin.getServer().getScheduler().cancelTask(tarkoxInstancePlayer.getWaitBeforeTpTaskRunnerId());
            }
            else {
                Integer timeLeft = this.timerMax - this.timer;
                String string = this.instance.plugin.configController.getString("you_join_game_wait_time_tp", "TIMER", timeLeft);
                player.sendMessage(string);
            }
        }
        else {
            // Stop the timer
            instance.plugin.getServer().getScheduler().cancelTask(tarkoxInstancePlayer.getWaitBeforeTpTaskRunnerId());
        }

        this.timer++;
    }
}
