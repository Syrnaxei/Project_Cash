package com.syrnaxei.game.game2048.gui;

import com.syrnaxei.game.game2048.core.Board;
import com.syrnaxei.game.game2048.core.GameConfig;
import com.syrnaxei.game.game2048.core.MergeLogic;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;

/**
 * 2048内置游戏图形界面类
 *
 * @author LiewYoung
 * @since 2025/12/15
 */
public class GameGUI extends JFrame {

    /**
     * 游戏核心逻辑板
     */
    private final Board board;
    
    /**
     * 合并逻辑处理器
     */
    private final MergeLogic mergeLogic;
    
    /**
     * 游戏格子面板数组
     */
    private TilePanel[][] tilePanels;
    
    /**
     * 分数显示标签
     */
    private JLabel scoreLabel;
    
    /**
     * 倒计时显示标签
     */
    private JLabel countdownLabel;
    
    /**
     * Swing计时器
     */
    private javax.swing.Timer swingTimer;
    
    /**
     * 游戏活动状态标志
     */
    private final AtomicBoolean gameActive = new AtomicBoolean(true);

    private static final ImageIcon gameIcon = createGameIcon();

    /**
     * 创建游戏图标
     * @return 图标对象，加载失败时返回null
     */
    private static ImageIcon createGameIcon() {
        try {
            return new ImageIcon(
                Objects.requireNonNull(
                    GameGUI.class.getResource("/images/icon/game_icon2048.png")
                )
            );
        } catch (Exception e) {
            System.err.println("加载游戏图标失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 构造函数，初始化游戏界面
     * @param board 游戏板对象
     * @param mergeLogic 合并逻辑对象
     */
    public GameGUI(Board board, MergeLogic mergeLogic) {
        this.board = board;
        this.mergeLogic = mergeLogic;

        try {
            initializeUI();
            setupWindowListener();
            setupKeyListener();
            startCountdown();
        } catch (Exception e) {
            handleInitializationError(e);
        }
    }

    /**
     * 初始化用户界面
     * @throws Exception 初始化过程中可能出现的异常
     */
    private void initializeUI() throws Exception {
        setTitle("Project_Cash_Game_2048");
        if (gameIcon != null) {
            setIconImage(gameIcon.getImage());
        }
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 防止直接关闭
        setResizable(false);
        setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建顶部面板（分数和倒计时）
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 创建游戏网格面板
        JPanel gridPanel = createGridPanel();
        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // 创建底部面板（说明）
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        pack();
    }

    /**
     * 创建顶部面板（包含分数和倒计时）
     * @return 顶部面板
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        // 分数面板
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel scoreText = new JLabel("分数: ");
        scoreText.setFont(new Font("微软雅黑", Font.BOLD, 16));
        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        scoreLabel.setForeground(new Color(0, 100, 0));
        scorePanel.add(scoreText);
        scorePanel.add(scoreLabel);
        topPanel.add(scorePanel, BorderLayout.WEST);

        // 倒计时面板
        JPanel countdownPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel countdownText = new JLabel("剩余时间: ");
        countdownText.setFont(new Font("微软雅黑", Font.BOLD, 16));
        countdownLabel = new JLabel(
            String.valueOf(board.getRemainingSeconds())
        );
        countdownLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        countdownLabel.setForeground(new Color(200, 0, 0));
        countdownPanel.add(countdownText);
        countdownPanel.add(countdownLabel);
        topPanel.add(countdownPanel, BorderLayout.EAST);

        return topPanel;
    }

    /**
     * 创建游戏网格面板
     * @return 网格面板
     */
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(
            new GridLayout(GameConfig.BOARD_SIZE, GameConfig.BOARD_SIZE, 10, 10)
        );
        gridPanel.setBorder(
            BorderFactory.createLineBorder(new Color(187, 173, 160), 5)
        );
        gridPanel.setBackground(new Color(187, 173, 160));

        tilePanels =
            new TilePanel[GameConfig.BOARD_SIZE][GameConfig.BOARD_SIZE];
        for (int i = 0; i < GameConfig.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConfig.BOARD_SIZE; j++) {
                tilePanels[i][j] = new TilePanel();
                gridPanel.add(tilePanels[i][j]);
            }
        }

        refreshBoard();
        return gridPanel;
    }

    /**
     * 创建底部说明面板
     * @return 底部面板
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel instructionLabel = new JLabel(
            "使用方向键移动方块，相同数字合并，获得2048获胜！"
        );
        instructionLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bottomPanel.add(instructionLabel);
        return bottomPanel;
    }

    /**
     * 设置窗口监听器
     */
    private void setupWindowListener() {
        addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    handleWindowClosing();
                }
            }
        );
    }

    /**
     * 处理窗口关闭事件
     */
    private void handleWindowClosing() {
        if (gameActive.get()) {
            int result = JOptionPane.showConfirmDialog(
                this,
                "游戏仍在进行中，确定要退出吗？",
                "确认退出",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                cleanupAndClose();
            }
        } else {
            cleanupAndClose();
        }
    }

    /**
     * 清理资源并关闭窗口
     */
    private void cleanupAndClose() {
        board.triggerGameOver(); // 通知游戏结束
        dispose(); // 关闭窗口
    }

    /**
     * 启动倒计时
     */
    private void startCountdown() {
        // 确保在EDT中执行
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::startCountdown);
            return;
        }

        swingTimer = new javax.swing.Timer(1000, e -> updateCountdown());
        swingTimer.start();
    }

    /**
     * 更新倒计时
     */
    private void updateCountdown() {
        if (!gameActive.get()) {
            return;
        }

        board.decrementRemainingSeconds();
        updateCountdownDisplay();

        if (board.isTimeUp()) {
            stopCountdown();
            onCountdownFinish();
        }
    }

    /**
     * 更新倒计时显示
     */
    private void updateCountdownDisplay() {
        int seconds = board.getRemainingSeconds();
        countdownLabel.setText(String.valueOf(seconds));

        // 根据剩余时间改变颜色
        if (seconds <= 10) {
            countdownLabel.setForeground(Color.RED);
        } else if (seconds <= 20) {
            countdownLabel.setForeground(Color.ORANGE);
        } else {
            countdownLabel.setForeground(new Color(0, 100, 0));
        }
    }

    /**
     * 停止倒计时
     */
    private void stopCountdown() {
        if (swingTimer != null && swingTimer.isRunning()) {
            swingTimer.stop();
        }
    }

    /**
     * 倒计时结束处理
     */
    private void onCountdownFinish() {
        SwingUtilities.invokeLater(() -> {
            if (gameActive.get()) {
                countdownLabel.setText("Time's up!");
                board.triggerGameOver();
                dispose();
            }
        });
    }

    /**
     * 设置键盘监听器
     */
    private void setupKeyListener() {
        addKeyListener(
            new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (!gameActive.get()) {
                        return;
                    }

                    try {
                        int[][] originalBoard = copyBoard(board.getBoard());

                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                            case KeyEvent.VK_W:
                                mergeLogic.mergeUp();
                                break;
                            case KeyEvent.VK_DOWN:
                            case KeyEvent.VK_S:
                                mergeLogic.mergeDown();
                                break;
                            case KeyEvent.VK_LEFT:
                            case KeyEvent.VK_A:
                                mergeLogic.mergeLeft();
                                break;
                            case KeyEvent.VK_RIGHT:
                            case KeyEvent.VK_D:
                                mergeLogic.mergeRight();
                                break;
                            case KeyEvent.VK_R:
                                // R键重置游戏
                                if (e.isControlDown()) {
                                    resetGame();
                                }
                                return;
                            case KeyEvent.VK_C:
                                board.boardTest();
                                refreshBoard();
                                break;
                            case KeyEvent.VK_ESCAPE:
                                // ESC键退出
                                handleWindowClosing();
                                return;
                            default:
                                return;
                        }

                        // 检查棋盘是否有变化
                        if (!boardsEqual(originalBoard, board.getBoard())) {
                            board.addNumber();
                            refreshBoard();
                            checkGameOver();
                            scoreLabel.setText(
                                String.valueOf(board.getScore())
                            );

                            // 检查是否达到2048
                            if (board.hasReached2048()) {
                                SwingUtilities.invokeLater(() -> {
                                    if (gameActive.get()) {
                                        board.triggerGameOver();
                                        dispose();
                                    }
                                });
                            }
                        }
                    } catch (Exception ex) {
                        handleGameError("处理按键事件时发生错误", ex);
                    }
                }
            }
        );

        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * 重置游戏
     */
    private void resetGame() {
        try {
            board.resetBoard();
            refreshBoard();
            scoreLabel.setText("0");
            updateCountdownDisplay();
            gameActive.set(true);
            requestFocusInWindow();
        } catch (Exception e) {
            handleGameError("重置游戏时发生错误", e);
        }
    }

    /**
     * 复制游戏板
     * @param source 源游戏板
     * @return 复制的游戏板
     */
    private int[][] copyBoard(int[][] source) {
        int[][] copy = new int[source.length][source[0].length];
        for (int i = 0; i < source.length; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, source[0].length);
        }
        return copy;
    }

    /**
     * 比较两个游戏板是否相等
     * @param board1 第一个游戏板
     * @param board2 第二个游戏板
     * @return 是否相等
     */
    private boolean boardsEqual(int[][] board1, int[][] board2) {
        if (
            board1.length != board2.length ||
            board1[0].length != board2[0].length
        ) {
            return false;
        }
        for (int i = 0; i < board1.length; i++) {
            for (int j = 0; j < board1[0].length; j++) {
                if (board1[i][j] != board2[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 刷新游戏板显示
     */
    public void refreshBoard() {
        int[][] currentBoard = board.getBoard();
        for (int i = 0; i < GameConfig.BOARD_SIZE; i++) {
            for (int j = 0; j < GameConfig.BOARD_SIZE; j++) {
                tilePanels[i][j].setValue(currentBoard[i][j]);
            }
        }
    }

    /**
     * 检查游戏是否结束
     */
    private void checkGameOver() {
        if (board.isGameOver()) {
            SwingUtilities.invokeLater(() -> {
                if (gameActive.get()) {
                    board.triggerGameOver();
                    dispose();
                }
            });
        }
    }

    /**
     * 处理游戏错误
     * @param message 错误信息
     * @param ex 异常对象
     */
    private void handleGameError(String message, Exception ex) {
        System.err.println(message + ": " + ex.getMessage());
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
            this,
            message + "，游戏将关闭。",
            "错误",
            JOptionPane.ERROR_MESSAGE
        );

        cleanupAndClose();
    }

    /**
     * 处理初始化错误
     * @param ex 异常对象
     */
    private void handleInitializationError(Exception ex) {
        System.err.println("初始化游戏界面失败: " + ex.getMessage());
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
            null,
            "初始化游戏界面失败: " + ex.getMessage() + "\n游戏无法启动。",
            "严重错误",
            JOptionPane.ERROR_MESSAGE
        );

        System.exit(1);
    }

    /**
     * 关闭窗口并清理资源
     */
    public void dispose() {
        try {
            gameActive.set(false);
            stopCountdown();
            super.dispose();
        } catch (Exception e) {
            System.err.println("关闭窗口时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 游戏格子面板内部类
     */
    private static class TilePanel extends JPanel {

        private static final Color EMPTY_COLOR = new Color(205, 193, 180);
        private JLabel valueLabel;

        /**
         * 构造函数，初始化格子面板
         */
        public TilePanel() {
            initializeTile();
        }

        /**
         * 初始化格子面板
         */
        private void initializeTile() {
            setLayout(new BorderLayout());
            setBackground(EMPTY_COLOR);
            setPreferredSize(new Dimension(80, 80));
            setBorder(
                BorderFactory.createLineBorder(new Color(187, 173, 160), 5)
            );

            valueLabel = new JLabel("", SwingConstants.CENTER);
            valueLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
            add(valueLabel, BorderLayout.CENTER);
        }

        /**
         * 设置格子的值并更新显示
         * @param value 格子的数值
         */
        public void setValue(int value) {
            if (value == 0) {
                valueLabel.setText("");
                setBackground(EMPTY_COLOR);
            } else {
                valueLabel.setText(String.valueOf(value));
                setColorByValue(value);
            }
        }

        /**
         * 根据数值设置格子的颜色
         * @param value 数值
         */
        private void setColorByValue(int value) {
            Color bgColor;
            Color fgColor = Color.BLACK;

            switch (value) {
                case 2:
                    bgColor = new Color(238, 228, 218);
                    break;
                case 4:
                    bgColor = new Color(237, 224, 200);
                    break;
                case 8:
                    bgColor = new Color(242, 177, 121);
                    fgColor = Color.WHITE;
                    break;
                case 16:
                    bgColor = new Color(245, 149, 99);
                    fgColor = Color.WHITE;
                    break;
                case 32:
                    bgColor = new Color(246, 124, 95);
                    fgColor = Color.WHITE;
                    break;
                case 64:
                    bgColor = new Color(246, 94, 59);
                    fgColor = Color.WHITE;
                    break;
                case 128:
                    bgColor = new Color(237, 207, 114);
                    fgColor = Color.WHITE;
                    break;
                case 256:
                    bgColor = new Color(237, 204, 97);
                    fgColor = Color.WHITE;
                    break;
                case 512:
                    bgColor = new Color(237, 200, 80);
                    fgColor = Color.WHITE;
                    break;
                case 1024:
                    bgColor = new Color(237, 197, 63);
                    fgColor = Color.WHITE;
                    break;
                case 2048:
                    bgColor = new Color(0, 45, 95);
                    fgColor = Color.WHITE;
                    break;
                default:
                    bgColor = new Color(60, 58, 50);
                    fgColor = Color.WHITE;
                    break;
            }

            setBackground(bgColor);
            valueLabel.setForeground(fgColor);
        }
    }
}
