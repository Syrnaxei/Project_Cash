package top.liewyoung.thanos.miniImpact.core;

import java.awt.*;

/**
 * 储存球的相关信息
 *
 * @author LiewYoung
 * @since 2025/12/23
 */
public class Ball {
    private double x;
    private double y;
    private Color color;
    private double Vx;
    private double Vy;
    private int Weight;

    public Ball(double x, double y, Color color, double Vx, double Vy, int Weight) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.Vx = Vx;
        this.Vy = Vy;
        this.Weight = Weight;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getVx() {
        return Vx;
    }

    public void setVx(double Vx) {
        this.Vx = Vx;
    }

    public double getVy() {
        return Vy;
    }

    public void setVy(double Vy) {
        this.Vy = Vy;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int Weight) {
        this.Weight = Weight;
    }
}
