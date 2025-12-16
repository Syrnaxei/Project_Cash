package com.syrnaxei.test;

import com.syrnaxei.game.game2048.api.Game2048;
import com.syrnaxei.game.game2048.api.Game2048Listener;

public class CallTest2048 {
    public static void main(String[] args) {
        // 调用2048
        Game2048.start(new Game2048Listener() {
            @Override
            public void onGameEnd(int finalScore) {
                // 返回2048的分数
                System.out.println("Over,Score:" + finalScore);
            }
        });
    }
}