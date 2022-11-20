package fr.aureprod.tarkox.exception;

import org.bukkit.entity.Player;

public class TarkoxInstancePlayerDeadException extends TarkoxPlayerException {
    public TarkoxInstancePlayerDeadException(Player player) {
        super(player);
    }
}
