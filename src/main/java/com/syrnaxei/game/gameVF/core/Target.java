package com.syrnaxei.game.gameVF.core;

/**
 * VF游戏中的目标对象
 *
 * @author Syrnaxei
 * @since 2025/12/19
 */
public class Target {
    private final int x;
    private final int y;
    private final int size;
    private boolean isAlive;

    /**
     * 构造一个新的目标对象，指定其位置和大小。
     *
     * @param x 目标的x坐标
     * @param y 目标的y坐标
     * @param size 目标的大小
     */
    public Target(int x,int y,int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.isAlive = true;
    }

    /**
     * 获取目标的x坐标。
     *
     * @return x坐标
     */
    public int getX() {
        return x;
    }

    /**
     * 获取目标的y坐标。
     *
     * @return y坐标
     */
    public int getY() {
        return y;
    }

    /**
     * 获取目标的大小。
     *
     * @return 大小
     */
    public int getSize() {
        return size;
    }

    /**
     * 检查目标是否存活。
     *
     * @return 如果目标存活返回true，否则返回false
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * 设置目标的存活状态。
     *
     * @param alive 新的存活状态
     */
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * 检查目标是否被鼠标点击命中。
     *
     * @param mouseX 鼠标点击的x坐标
     * @param mouseY 鼠标点击的y坐标
     * @param targetSize 用于碰撞检测的目标大小
     * @return 如果目标被命中且存活返回true，否则返回false
     */
    public boolean isHit(int mouseX, int mouseY , int targetSize) {
        return isAlive && mouseX >= x && mouseX <= x + targetSize && mouseY >= y && mouseY <= y + targetSize;
    }
}
