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

        Position locationMin = new Position(
            this.min.getWorld(),
            Math.min(this.min.getX(), this.max.getX()),
            Math.min(this.min.getY(), this.max.getY()),
            Math.min(this.min.getZ(), this.max.getZ())
        );
        Position locationMax = new Position(
            this.min.getWorld(),
            Math.max(this.min.getX(), this.max.getX()),
            Math.max(this.min.getY(), this.max.getY()),
            Math.max(this.min.getZ(), this.max.getZ())
        );

        if ((location.getX() < locationMin.getX()) || (location.getX() > locationMax.getX())) return false;
        if ((location.getY() < locationMin.getY()) || (location.getY() > locationMax.getY())) return false;
        if ((location.getZ() < locationMin.getZ()) || (location.getZ() > locationMax.getZ())) return false;

        return true;
    }

    public Boolean isInZone(Player player) {
        Position position = new Position(player.getLocation());

        return this.isInZone(position);
    }
}
