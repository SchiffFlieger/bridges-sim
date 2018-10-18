package de.karstenkoehler.bridges.model;

import java.util.Objects;

public class Island {
    private final int id;
    private final int x, y;
    private final int requiredBridges;
    public Island (int id, int x, int y, int requiredBridges) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.requiredBridges = requiredBridges;
    }

    public int getId () {
        return id;
    }

    public int getRequiredBridges () {
        return requiredBridges;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Island island = (Island) o;
        return id == island.id &&
                x == island.x &&
                y == island.y &&
                requiredBridges == island.requiredBridges;
    }

    @Override
    public int hashCode () {
        return Objects.hash(id, x, y, requiredBridges);
    }

    @Override
    public String toString() {
        return "Island{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", requiredBridges=" + requiredBridges +
                '}';
    }
}
