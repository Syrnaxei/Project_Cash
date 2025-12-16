package com.syrnaxei.game.game2048.api;

// 游戏结束/分数回调接口
public interface Game2048Listener {
    void onGameEnd(int finalScore); // 游戏结束时返回分数
}