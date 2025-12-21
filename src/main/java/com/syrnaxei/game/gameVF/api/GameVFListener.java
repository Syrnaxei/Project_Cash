package com.syrnaxei.game.gameVF.api;

/**
 * VF游戏监听器接口，用于监听游戏事件。
 *
 * @author Syrnaxei
 * @since 2025/12/19
 */
public interface GameVFListener {
    /**
     * 当游戏结束时调用此方法。
     * 
     * @param score 游戏得分
     */
    void onGameEnd(int score);
}