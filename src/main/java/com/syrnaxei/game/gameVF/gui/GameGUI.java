package com.syrnaxei.game.gameVF.gui;

import com.syrnaxei.game.gameVF.core.GamePanel;

import javax.swing.*;

/**
 * 游戏图形用户界面类
 * 负责创建和管理游戏窗口及面板
 *
 * @author syrnaxei
 * @since 2025/12/19
 */
public class GameGUI {
    private final GamePanel gamePanel;
    private final JFrame gameFrame;

    /**
     * 构造函数，初始化游戏界面
     * 创建游戏窗口，设置基本属性并显示
     */
    public GameGUI() {
        gameFrame = new JFrame("Project_Cash_Game_VF");
        gamePanel = new GamePanel(gameFrame);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }

    /**
     * 获取游戏面板对象
     * @return GamePanel对象
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    }

}
