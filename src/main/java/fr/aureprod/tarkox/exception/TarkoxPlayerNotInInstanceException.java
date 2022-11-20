package fr.aureprod.tarkox.exception;

import org.bukkit.entity.Player;

public class TarkoxPlayerNotInInstanceException extends TarkoxPlayerException {
    public TarkoxPlayerNotInInstanceException(Player player) {
        super(player);
    }
}
