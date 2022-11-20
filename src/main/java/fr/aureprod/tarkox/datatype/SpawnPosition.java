package fr.aureprod.tarkox.datatype;

import org.bukkit.World;

public class SpawnPosition extends Position {
    public SpawnPosition(World wolrd, Integer x, Integer y, Integer z) {
        super(wolrd, x, y + 1, z);
    }
}
