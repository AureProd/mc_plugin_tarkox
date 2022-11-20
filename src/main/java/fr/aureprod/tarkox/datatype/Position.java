package fr.aureprod.tarkox.datatype;

import org.bukkit.Location;
import org.bukkit.World;

public class Position {
    private World world;
    private Integer x;
    private Integer y;
    private Integer z;

    public Position(World world, Integer x, Integer y, Integer z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(Location location) {
        this.world = location.getWorld();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public World getWorld() {
        return world;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public Location toLocation() {
        return new Location(world, x, y, z);
    }
}
