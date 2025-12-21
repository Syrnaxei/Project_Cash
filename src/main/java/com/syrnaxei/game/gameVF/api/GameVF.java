package com.syrnaxei.game.gameVF.api;

import com.syrnaxei.game.gameVF.gui.GameGUI;
import com.syrnaxei.game.gameVF.core.GamePanel;

import javax.swing.*;

/**
 * VF游戏对外调用的唯一入口
 *
 * @author Syrnaxei
 * @since 2025/12/19
 */
public class GameVF {
    /**
     * 启动VF游戏，并设置结束回调
     * @param listener 游戏结束监听器
     */
    public static void start(GameVFListener listener) {
        SwingUtilities.invokeLater(() -> {
            GameGUI gameGUI = new GameGUI();

            // 获取游戏窗口和面板
            GamePanel gamePanel = gameGUI.getGamePanel();


            // 设置游戏结束回调
            gamePanel.setGameOverCallback(finalScore -> {
                if (listener != null) {
                    listener.onGameEnd(finalScore);
                }
            });
        });
    }
}
