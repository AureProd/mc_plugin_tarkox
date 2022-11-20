package fr.aureprod.tarkox.exception;

import org.bukkit.entity.Player;

public class TarkoxPlayerException extends TarkoxException {
    private Player player;
    
    public TarkoxPlayerException(Player player) {
        super();

        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Boolean isPlayer(Player player) {
        return this.player.equals(player);
    }
}
