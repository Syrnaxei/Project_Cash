package com.syrnaxei.game.game2048.core;

import com.syrnaxei.game.game2048.api.Game2048Listener;
import java.util.Random;

/**
 * 2048游戏棋盘类
 * 管理游戏状态、分数计算、游戏逻辑判断等功能
 *
 * @author Syrnaxei
 * @since 2025/12/15
 */
public class Board {

    private int[][] board;
    private int score = 0;

    private Game2048Listener endListener;
    private int remainingSeconds = GameConfig.INITIAL_COUNTDOWN; // 添加倒计时实例变量
    private final Random random = new Random();

    //===================================  创建棋盘 方法  ===================================
    /**
     * 初始化游戏棋盘
     */
    public void createBoard() {
        try {
            board = new int[GameConfig.BOARD_SIZE][GameConfig.BOARD_SIZE];
            addNumber();
            addNumber();
        } catch (Exception e) {
            throw new RuntimeException("创建游戏棋盘失败", e);
        }
    }

    //===================================  添加数字 方法  ===================================
    /**
     * 在棋盘随机空白位置添加数字(2或4)
     */
    public void addNumber() {
        try {
            int row, col;
            if (!hasEmptyLocation()) {
                return;
            }

            // 寻找空位置
            do {
                row = random.nextInt(GameConfig.BOARD_SIZE);
                col = random.nextInt(GameConfig.BOARD_SIZE);
            } while (board[row][col] != 0);

            // 根据概率生成2或4
            if (random.nextInt(100) > GameConfig.S_FOUR_P) {
                board[row][col] = 2;
            } else {
                board[row][col] = 4;
            }
        } catch (Exception e) {
            throw new RuntimeException("添加数字到棋盘失败", e);
        }
    }

    /**
     * 判断棋盘是否存在空位置
     * @return 存在空位置返回true，否则返回false
     */
    public boolean hasEmptyLocation() {
        for (int[] row : board) {
            for (int num : row) {
                if (num == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    //====================================  计分 方法  ====================================
    /**
     * 获取当前得分
     * @return 当前分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 添加分数到当前总分
     *
     * @param score 要增加的分数
     */
    public void addScore(int score) {
        if (score > 0) {
            this.score += score;
        }
    }

    /**
     * 设置总分为指定值（主要用于重置）
     *
     * @param score 新的总分
     */
    public void setScore(int score) {
        if (score >= 0) {
            this.score = score;
        }
    }

    //===================================  游戏结束 方法  ===================================
    /**
     * 判断游戏是否结束
     * @return 游戏结束返回true，否则返回false
     */
    public boolean isGameOver() {
        try {
            // 检查棋盘上是否有空位
            for (int i = 0; i < GameConfig.BOARD_SIZE; i++) {
                for (int j = 0; j < GameConfig.BOARD_SIZE; j++) {
                    if (board[i][j] == 0) {
                        return false;
                    }
                }
            }

            // 检查棋盘横向是否有相同的可合并的数字
            for (int i = 0; i < GameConfig.BOARD_SIZE; i++) {
                for (int j = 0; j < GameConfig.BOARD_SIZE - 1; j++) {
                    if (board[i][j] == board[i][j + 1]) {
                        return false;
                    }
                }
            }

            // 检查棋盘纵向是否有相同的可合并的数字
            for (int i = 0; i < GameConfig.BOARD_SIZE - 1; i++) {
                for (int j = 0; j < GameConfig.BOARD_SIZE; j++) {
                    if (board[i][j] == board[i + 1][j]) {
                        return false;
                    }
                }
            }

            return true;
        } catch (Exception e) {
            throw new RuntimeException("检查游戏是否结束时发生错误", e);
        }
    }

    //===================================  检查是否达到2048 方法  ===================================
    /**
     * 判断是否达到2048
     * @return 达到2048返回true，否则返回false
     */
    public boolean hasReached2048() {
        for (int i = 0; i < GameConfig.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConfig.BOARD_SIZE; j++) {
                if (board[i][j] == 2048) {
                    return true;
                }
            }
        }
        return false;
    }

    //===================================  棋盘调用 方法  ===================================
    /**
     * 获取当前棋盘状态
     * @return 棋盘二维数组
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * 设置棋盘状态
     * @param board 新的棋盘状态
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * 重置棋盘
     */
    public void resetBoard() {
        try {
            board = new int[GameConfig.BOARD_SIZE][GameConfig.BOARD_SIZE];
            score = 0;
            resetRemainingSeconds(); // 重置倒计时
            addNumber();
            addNumber();
        } catch (Exception e) {
            throw new RuntimeException("重置游戏棋盘失败", e);
        }
    }

    /**
     * 设置游戏结束监听器
     * @param listener 监听器实例
     */
    public void setListener(Game2048Listener listener) {
        this.endListener = listener;
    }

    //===================================  倒计时 方法  ===================================
    /**
     * 获取剩余时间
     * @return 剩余秒数
     */
    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    /**
     * 设置剩余时间
     * @param seconds 剩余秒数
     */
    public void setRemainingSeconds(int seconds) {
        this.remainingSeconds = Math.max(0, seconds);
    }

    /**
     * 减少剩余时间
     */
    public void decrementRemainingSeconds() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
    }

    /**
     * 重置剩余时间
     */
    public void resetRemainingSeconds() {
        this.remainingSeconds = GameConfig.INITIAL_COUNTDOWN;
    }

    /**
     * 判断时间是否耗尽
     * @return 时间耗尽返回true，否则返回false
     */
    public boolean isTimeUp() {
        return remainingSeconds <= 0;
    }

    /**
     * 触发游戏结束
     */
    public void triggerGameOver() {
        try {
            // 重置倒计时，为下次游戏做准备
            resetRemainingSeconds();
            if (endListener != null) {
                endListener.onGameEnd(this.score); // 触发回调返回分数
            }
        } catch (Exception e) {
            throw new RuntimeException("触发游戏结束时发生错误", e);
        }

    }

    //==================================  测试 方法  ====================================
    /**
     * 测试方法：通过对话框设置分数
     */
    public void boardTest() {
        String input = javax.swing.JOptionPane.showInputDialog("请输入要设置的分数:");
        if(input != null && !input.trim().isEmpty()) {
            try {
                int newScore = Integer.parseInt(input);
                if(newScore >= 0) {
                    this.score = newScore;
                    javax.swing.JOptionPane.showMessageDialog(null, "分数已设置为: " + newScore);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(null, "分数不能为负数!");
                }
            } catch(NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(null, "输入格式错误!");
            }
        }
    }
}

