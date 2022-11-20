package fr.aureprod.tarkox.exception;

import org.bukkit.entity.Player;

public class TarkoxInstancePlayerKickedException extends TarkoxPlayerException {
    public TarkoxInstancePlayerKickedException(Player player) {
        super(player);
    }
}
