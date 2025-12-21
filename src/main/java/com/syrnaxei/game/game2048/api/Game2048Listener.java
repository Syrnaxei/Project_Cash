package com.syrnaxei.game.game2048.api;

/**
 * 2048内置游戏结束监听器接口
 * 该接口定义了2048游戏结束时的回调方法，用于接收游戏最终得分。
 *
 * @author Syrnaxei
 * @since 2025/12/15
 */
public interface Game2048Listener {

    /**
     * 当2048游戏结束时调用的回调方法
     * 该方法在游戏结束时被触发，传递游戏的最终得分作为参数。
     * 实现类可以根据最终得分执行相应的业务逻辑。
     *
     * @param finalScore 游戏结束时的最终得分，非负整数
     */
    void onGameEnd(int finalScore); // 游戏结束时返回分数
}
