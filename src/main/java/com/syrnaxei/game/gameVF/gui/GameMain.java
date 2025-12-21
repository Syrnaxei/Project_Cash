package com.syrnaxei.game.gameVF.gui;

import com.syrnaxei.game.gameVF.core.GamePanel;

import javax.swing.*;

/**
 * VF主类，用于测试游戏。
 */
public class GameMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI gameGUI = new GameGUI();

            // 获取游戏窗口并添加关闭监听器
            GamePanel gamePanel = gameGUI.getGamePanel();

            gamePanel.setGameOverCallback(finalScore -> System.out.println("游戏结束，最终得分: " + finalScore));
        });
    }
}
