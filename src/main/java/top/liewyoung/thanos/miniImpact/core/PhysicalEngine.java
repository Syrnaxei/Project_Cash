package top.liewyoung.thanos.miniImpact.core;

import top.liewyoung.math.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于球类碰撞的物理引擎
 *
 * @author LiewYoung
 * @since 2025/12/25
 */
record GridInfo(int xCount, int yCount, int space) {
}

public class PhysicalEngine {

    /**
     * 基本属性储存
     */
    private final MiniImpact panel;
    private final int R; // 球的半径
    private GridInfo gridInfo; // 网格信息

    /* 数据储存 */
    private static final List<Ball> allBalls = new ArrayList<Ball>(); // 地图上显示的球
    private static final Map<Point, List<Ball>> gridBalls = new HashMap<Point, List<Ball>>(); // 利用网格快速判定球的位置

    // 物理常数
    private static int GRAVITY = 2; // 重力加速度
    private static double FRICTION = 0.3; // 地面碰撞能量损失系数
    private static double Collision = 0.2; // 碰撞损失系数

    /**
     * 物理引擎
     *
     * @param miniImpact 接受一个游戏地图
     */
    public PhysicalEngine(MiniImpact miniImpact) {
        this.panel = miniImpact;
        this.R = miniImpact.getR();
        this.gridInfo = calculateSize(miniImpact.getWidth(), miniImpact.getHeight());

    }

    public static List<Ball> getAllBalls() {
        return allBalls;
    }

    /**
     * 设置物理属性
     *
     * @param GRAVITY   重力
     * @param FRICTION  地面碰撞能量损失系数
     * @param Collision 碰撞能量损失系数
     */
    public void setPhysical(int GRAVITY, int FRICTION, int Collision) {
        PhysicalEngine.GRAVITY = GRAVITY;
        PhysicalEngine.FRICTION = FRICTION;
        PhysicalEngine.Collision = Collision;
    }

    /**
     * 网格尺寸计算
     *
     * @return {@link GridInfo }
     */
    public GridInfo calculateSize(int height, int width) {
        // 使用一个合理的快照或动态计算
        int xCount = width / (R * 2);
        int yCount = height / (R * 2);

        return new GridInfo(xCount, yCount, 2 * R);
    }

    /**
     * 添加新球
     *
     * @param ball 球
     */
    public void newBall(Ball ball) {
        allBalls.add(ball);
        checkGrid(ball);
    }



    /**
     * 碰撞更新
     *
     */
    public void update() {
        int width = panel.getWidth();
        int height = panel.getHeight();

        // 刷新属性同步更改
        gridBalls.clear();
        for (Ball ball : allBalls) {
            gridInfo = calculateSize(height, width);
            checkGrid(ball);
        }

        for (Ball home : allBalls) {
            // 应用重力
            home.setVy(home.getVy() + (GRAVITY * 0.1));

            // 获取当前球属于的格子
            int cX = (int) home.getX() / gridInfo.space();
            int cY = (int) home.getY() / gridInfo.space();

            for (int directionX = -1; directionX <= 1; directionX++) {
                for (int directionY = -1; directionY <= 1; directionY++) {
                    /*
                     * 防止遍历到没有球的位置
                     * 不需要关心越界问题，因为球在网格中不会越界
                     */
                    for (Ball neighbor : gridBalls.getOrDefault(new Point(cX + directionX, cY + directionY),
                            new ArrayList<>())) {
                        if (home == neighbor)
                            continue; // 自己不能碰撞自己
                        if (isColliding(home, neighbor)) {
                            // 处理碰撞
                            CollidingDo(home, neighbor);
                        }
                    }
                }
            }

            // 边界检测
            if (home.getY() + R >= height) {
                home.setY(height - R); // 防止卡在边界
                home.setVy(-(home.getVy() * (1 - FRICTION)));
            }
            if (home.getY() - R <= 0) {
                home.setY(R);
                home.setVy(-(home.getVy() * (1 - FRICTION)));
            }

            if (home.getX() + R >= width) {
                home.setX(width - R);
                home.setVx(-(home.getVx() * (1 - FRICTION)));
            }
            if (home.getX() - R <= 0) {
                home.setX(R);
                home.setVx(-(home.getVx() * (1 - FRICTION)));
            }



            if(Math.abs( home.getVy()) < 0.5&& panel.getHeight() - home.getY()-R < 2){
                home.setVy(0);
            }
            if(Math.abs( home.getVx()) < 0.5){
                home.setVx(0);
            }

//            System.out.println(panel.getHeight() - home.getY()-R);

            home.setX(home.getX() + home.getVx());
            home.setY(home.getY() + home.getVy());

        }
    }

    /**
     * 碰撞事件处理
     * 使用一维弹性碰撞模型转换到二维法线方向
     *
     * @param home     核心球
     * @param neighbor 邻居球
     */
    private void CollidingDo(Ball home, Ball neighbor) {
        // 计算两球之间的连线向量（法线方向）
        Vector collisionLine = new Vector(neighbor.getX() - home.getX(),
                neighbor.getY() - home.getY());

        if (collisionLine.getLength() == 0)
            return;

        collisionLine = collisionLine.normalize(); // 单位化

        // 计算球的质量
        double m1 = home.getWeight();
        double m2 = neighbor.getWeight();

        // 获取当前速度向量（合成速度向量）
        Vector v1 = new Vector(home.getVx(), home.getVy());
        Vector v2 = new Vector(neighbor.getVx(), neighbor.getVy());

        // 计算速度在法线方向（collisionLine）上的分量
        double v1n = v1.dot(collisionLine);
        double v2n = v2.dot(collisionLine);

        // 如果两球已经在相互远离，则不处理碰撞（防止重叠导致的粘连）
        if (v1n - v2n < 0.0000000001)
            return;

        // 根据弹性碰撞公式计算碰撞后的法向分量（动量定理）
        double v1nAfter = calculateV(v1n, v2n, m1, m2);
        double v2nAfter = calculateV(v2n, v1n, m2, m1);

        // 考虑碰撞损失系数
        v1nAfter *= (1 - Collision);
        v2nAfter *= (1 - Collision);

        // 计算切线方向
        Vector tangentLine = collisionLine.rotate90();

        // 计算速度在切线方向的分量（碰撞中切向速度保持不变）
        double v1t = v1.dot(tangentLine);
        double v2t = v2.dot(tangentLine);

        // 合成新的速度向量
        // 新速度 = 新法向速度向量 + 原切向速度向量
        Vector newV1 = collisionLine.multiply(v1nAfter).add(tangentLine.multiply(v1t));
        Vector newV2 = collisionLine.multiply(v2nAfter).add(tangentLine.multiply(v2t));

        // 更新球的速度
        home.setVx(newV1.x());
        home.setVy(newV1.y());
        neighbor.setVx(newV2.x());
        neighbor.setVy(newV2.y());
    }

    /**
     * 一维弹性碰撞速度计算公式
     */
    private double calculateV(double v1, double v2, double m1, double m2) {
        return (v1 * (m1 - m2) + 2 * m2 * v2) / (m1 + m2);
    }

    /**
     * 计算球在网格中的位置
     *
     * @param ball 球
     */
    private void checkGrid(Ball ball) {
        int baseX = (int) ball.getX() / gridInfo.space();
        int baseY = (int) ball.getY() / gridInfo.space();

        gridBalls.computeIfAbsent(new Point(baseX, baseY), k -> new ArrayList<>()).add(ball);
    }

    /**
     * 检测碰撞
     *
     * @param home     核心球
     * @param neighbor 邻居球
     * @return boolean
     */
    private boolean isColliding(Ball home, Ball neighbor) {
        int dx = (int) (home.getX() - neighbor.getX());
        int dy = (int) (home.getY() - neighbor.getY());

        return dx * dx + dy * dy < 4 * R * R;
    }

}
