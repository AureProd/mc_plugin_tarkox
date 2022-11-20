package fr.aureprod.tarkox.exception;

import org.bukkit.entity.Player;

public class TarkoxInstancePlayerExtractedException extends TarkoxPlayerException {
    public TarkoxInstancePlayerExtractedException(Player player) {
        super(player);
    }
}
