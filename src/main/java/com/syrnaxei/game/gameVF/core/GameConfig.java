package com.syrnaxei.game.gameVF.core;

/**
 * VF游戏配置类，包含游戏运行所需的各种常量配置
 *
 * @author Syrnaxei
 * @since 2025/12/19
 */
public class GameConfig {
    /** 窗口宽度，单位为像素 */
    public static final int WINDOW_WIDTH = 800;
    /** 窗口高度，单位为像素 */
    public static final int WINDOW_HEIGHT = 600;
    /** 每帧延迟时间，单位为毫秒，用于控制游戏帧率 */
    public static final long FRAME_DELAY = 16;

    /** 目标物体的尺寸大小，单位为像素 */
    public static final int TARGET_SIZE = 80;
    /** 击中每个目标获得的分数 */
    public static final int SCORE_PER_TARGET = 15;
    /** 调试模式开关，开启后会显示额外的调试信息 */
    public static final boolean DEBUG_MODE = false;
}
