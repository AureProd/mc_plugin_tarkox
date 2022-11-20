package fr.aureprod.tarkox.exception;

import org.bukkit.entity.Player;

public class TarkoxInstancePlayerQuitException extends TarkoxPlayerException {
    public TarkoxInstancePlayerQuitException(Player player) {
        super(player);
    }
}
