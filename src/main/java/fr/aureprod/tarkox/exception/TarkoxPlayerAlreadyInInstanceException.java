package fr.aureprod.tarkox.exception;

import org.bukkit.entity.Player;

public class TarkoxPlayerAlreadyInInstanceException extends TarkoxPlayerException {
    public TarkoxPlayerAlreadyInInstanceException(Player player) {
        super(player);
    }
}
