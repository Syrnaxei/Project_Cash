package org;

public class Tile {
    public enum TileType {
        OPPORTUNITY, RISK, MARKET, PAYDAY, START
    }

    private int x;         // 坐标 x
    private int y;         // 坐标 y
    private TileType type; // 格子类型

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public TileType getType() {
        return type;
    }
}