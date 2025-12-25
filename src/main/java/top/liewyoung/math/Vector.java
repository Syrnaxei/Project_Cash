package top.liewyoung.math;

import java.awt.*;

/**
 * 向量
 *
 * @author LiewYoung
 * @since 2025/12/23
 */
public record Vector(double x, double y) {
    /**
     * 生成单位方向向量
     *
     * @param point1 第1点
     * @param point2 第2点
     * @return {@link Vector }
     */
    public static Vector generateVectorUnit(Point point1, Point point2) {
        int dx = point2.x - point1.x;
        int dy = point2.y - point1.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0)
            return new Vector(0, 0);
        return new Vector(dx / length, dy / length);
    }

    public double dot(Vector other) {
        return this.x * other.x + this.y * other.y;
    }

    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    public Vector normalize() {
        double length = getLength();
        if (length == 0)
            return new Vector(0, 0);
        return new Vector(x / length, y / length);
    }

    public Vector rotate90() {
        return new Vector(-y, x);
    }

    public double vectorProjection(Vector vector, Vector projection) {
        double cos = getCos(vector, projection);
        return cos * vector.getLength();
    }

    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    public double getCos(Vector vector1, Vector vector2) {
        double son = vector1.dot(vector2);
        double mom = vector1.getLength() * vector2.getLength();
        if (mom == 0)
            return 0;
        return son / mom;
    }
}