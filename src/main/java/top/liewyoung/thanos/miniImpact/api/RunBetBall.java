package top.liewyoung.thanos.miniImpact.api;

import com.formdev.flatlaf.FlatLightLaf;
import top.liewyoung.thanos.FunGame;
import top.liewyoung.thanos.miniImpact.core.MiniImpact;

import javax.swing.*;
import java.awt.*;

/**
 * 赌球游戏 API
 * 使用模态对话框，不会卡死
 *
 * @author LiewYoung
 * @since 2025/12/25
 */
public class RunBetBall implements FunGame {

    // 保存最终得分
    private int finalScore = 0;


    private JDialog dialog;

    @Override
    public void start() {
        // 重置分数
        finalScore = 0;

        // 先让用户输入幸运数字
        String input = JOptionPane.showInputDialog(
                null,
                "请输入幸运数字 (1-3):",
                "开始游戏",
                JOptionPane.QUESTION_MESSAGE);

        // 检查输入
        if (input == null || input.isEmpty()) {
            return;
        }

        int luckyNumber;
        try {
            luckyNumber = Integer.parseInt(input);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "请输入数字！");
            return;
        }

        if (luckyNumber < 1 || luckyNumber > 3) {
            JOptionPane.showMessageDialog(null, "请输入1-3之间的数字！");
            return;
        }

        // 创建模态对话框
        dialog = new JDialog((Frame) null, "幸运赌球", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 创建游戏面板
        MiniImpact game = new MiniImpact(30);
        dialog.add(game);

        // 设置游戏结束时的处理

        /*
         *小笔记：什么是回调？
         * 回调函数是一种设计模式，它允许一个函数在另一个函数的完成工作之后执行。
         * 是一种委托，允许一个对象将方法调用委托给另一个对象。
         */
        game.setGameEndCallback((isWin, score) -> {
            // 保存分数
            finalScore = score;

            Timer timer = new Timer(1500, e -> {
                dialog.dispose();
            });
            timer.setRepeats(false);
            timer.start();
        });


        Timer startTimer = new Timer(500, e -> {
            game.startGameWithNumber(luckyNumber);
        });
        startTimer.setRepeats(false);
        startTimer.start();

        dialog.setVisible(true);

    }

    @Override
    public int getScore() {
        return finalScore;
    }

    // 测试用
    public static void main(String[] args) {

        // 设置主题
        FlatLightLaf.setup();
        RunBetBall betBall = new RunBetBall();
        betBall.start();
        System.out.println("游戏结束！得分: " + betBall.getScore());
    }
}
