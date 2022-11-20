package fr.aureprod.tarkox.datatype;

import org.bukkit.entity.Player;

public class Area {
    private Position min;
    private Position max;

    public Area(Position min, Position max) {
        this.min = min;
        this.max = max;
    }

    public Position getMin() {
        return min;
    }

    public void setMin(Position min) {
        this.min = min;
    }

    public Position getMax() {
        return max;
    }

    public void setMax(Position max) {
        this.max = max;
    }

    public Boolean isInZone(Position location) {
        if (!location.getWorld().equals(this.min.getWorld())) return false;
        if ((location.getX() < this.min.getX()) || (location.getX() > this.max.getX())) return false;
        if ((location.getY() < this.min.getY()) || (location.getY() > this.max.getY())) return false;
        if ((location.getZ() < this.min.getZ()) || (location.getZ() > this.max.getZ())) return false;

        return true;
    }

    public Boolean isInZone(Player player) {
        Position position = new Position(player.getLocation());

        return this.isInZone(position);
    }
}
