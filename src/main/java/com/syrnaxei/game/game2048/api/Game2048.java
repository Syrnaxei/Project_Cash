package com.syrnaxei.game.game2048.api;

import com.syrnaxei.game.game2048.core.Board;
import com.syrnaxei.game.game2048.core.MergeLogic;
import com.syrnaxei.game.game2048.gui.GameGUI;

import javax.swing.SwingUtilities;

// 2048对外调用的唯一入口
public class Game2048 {
    // 启动2048游戏，并设置结束回调
    public static void start(Game2048Listener listener) {
        SwingUtilities.invokeLater(() -> {
            Board board = new Board();
            MergeLogic mergeLogic = new MergeLogic(board);
            board.createBoard();

            // 给Board绑定回调（当游戏结束时，触发listener的onGameEnd）
            board.setListener((score) -> {
                if (listener != null) {
                    listener.onGameEnd(score);
                }
            });

            // 显示界面
            GameGUI gui = new GameGUI(board, mergeLogic);
            gui.setVisible(true);
        });
    }
}