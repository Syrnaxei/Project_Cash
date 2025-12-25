package top.liewyoung.thanos.miniImpact.core;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import top.liewyoung.view.ColorSystem.MaterialPalette;
import top.liewyoung.view.component.MDbutton;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * 赌球游戏 MiniImpact
 * 玩法：玩家输入幸运数字，生成三个球，其中一个是红色球
 * 只有红色球落地在选择的位置才能获得奖励
 *
 * @author LiewYoung
 * @since 2025/12/25
 */
public class MiniImpact extends JPanel {
    private Timer timer;
    private final int r; // 球的半径
    private final PhysicalEngine engine;
    private final Random random = new Random();

    // 使用 CashFlow 的配色系统
    private static final MaterialPalette palette = MaterialPalette.MOSS;

    // 游戏状态
    private boolean gameRunning = false;
    private int luckyNumber = -1;
    private Ball redBall = null; // 红色球引用
    private boolean redBallLanded = false; // 红球是否落地
    private int score = 0; // 玩家分数

    // API 回调
    private GameEndCallback gameEndCallback = null;

    /**
     * 游戏结束回调接口
     */
    public interface GameEndCallback {
        void onGameEnd(boolean isWin, int score);
    }

    // UI 组件
    private MDbutton startButton;
    private JLabel scoreLabel;
    private JLabel statusLabel;
    private JLabel titleLabel;

    public MiniImpact(int r) {
        this.r = r;
        setLayout(null); // 使用绝对布局来放置按钮
        setBackground(palette.surface()); // 使用 CashFlow 的 surface 颜色

        // 初始化物理引擎
        engine = new PhysicalEngine(this);

        // 创建UI组件
        initUI();

        // 创建定时器，每14毫秒触发一次
        timer = new Timer(14, e -> {
            if (gameRunning) {
                engine.update();
                checkRedBallLanded();
            }
            repaint();
        });
        timer.start();
    }

    private void initUI() {
        // 标题标签
        titleLabel = new JLabel("幸运赌球");
        titleLabel.setBounds(20, 15, 200, 30);
        titleLabel.setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 22f));
        titleLabel.setForeground(palette.primary());
        add(titleLabel);

        // 开始按钮 - 使用 MDbutton
        startButton = new MDbutton("开始游戏");
        startButton.setBounds(20, 55, 120, 40);
        startButton.addActionListener(e -> startGame());
        add(startButton);

        // 分数标签 - 使用 primaryContainer 背景
        JPanel scorePanel = new JPanel();
        scorePanel.setBounds(160, 55, 150, 40);
        scorePanel.setBackground(palette.primaryContainer());
        scorePanel.setLayout(new BorderLayout());
        scorePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");

        scoreLabel = new JLabel("💰 分数: 0", SwingConstants.CENTER);
        scoreLabel.setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 16f));
        scoreLabel.setForeground(palette.onPrimaryContainer());
        scorePanel.add(scoreLabel, BorderLayout.CENTER);
        add(scorePanel);

        // 状态标签
        statusLabel = new JLabel("点击按钮开始游戏!");
        statusLabel.setBounds(330, 55, 450, 40);
        statusLabel.setFont(UIManager.getFont("defaultFont").deriveFont(Font.PLAIN, 14f));
        statusLabel.setForeground(palette.onSurface());
        add(statusLabel);
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        if (gameRunning) {
            JOptionPane.showMessageDialog(this, "游戏进行中，请等待球落地！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 创建自定义输入对话框
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(palette.surface());

        JLabel promptLabel = new JLabel("<html><center>请输入你的幸运数字 (1-3):<br/>选择哪个球会是红球？</center></html>");
        promptLabel.setFont(UIManager.getFont("defaultFont").deriveFont(Font.PLAIN, 14f));
        promptLabel.setForeground(palette.onSurface());

        JTextField inputField = new JTextField();
        inputField.setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 18f));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.putClientProperty(FlatClientProperties.STYLE, "arc: 10");

        inputPanel.add(promptLabel, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "输入幸运数字",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return; // 用户取消
        }

        String input = inputField.getText();
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            luckyNumber = Integer.parseInt(input.trim());
            if (luckyNumber < 1 || luckyNumber > 3) {
                JOptionPane.showMessageDialog(this, "请输入 1-3 之间的数字！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "请输入有效的数字！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 清除旧球
        PhysicalEngine.getAllBalls().clear();
        redBall = null;
        redBallLanded = false;
        gameRunning = true;

        // 随机选择哪个球是红色
        int redBallIndex = random.nextInt(3) + 1; // 1, 2, 或 3

        statusLabel.setText("🎯 你选择了: " + luckyNumber + " | 红球位置: ???");

        // 生成三个球
        generateBalls(redBallIndex);
    }

    /**
     * 生成三个球
     * 
     * @param redBallIndex 红球的索引 (1-3)
     */
    private void generateBalls(int redBallIndex) {
        int panelWidth = getWidth();
        int startY = 120; // 从顶部开始（给UI留空间）
        int spacing = panelWidth / 4; // 球之间的间距

        // 使用 CashFlow 的配色生成球
        Color[] ballColors = {
                palette.primary(), // 主色 - 深绿
                new Color(33, 150, 243), // 蓝色
                new Color(156, 39, 176), // 紫色
                new Color(0, 150, 136), // 青绿
                new Color(255, 152, 0) // 橙色
        };

        for (int i = 1; i <= 3; i++) {
            // 随机速度
            double vx = (random.nextDouble() - 0.5) * 10; // -5 到 5
            double vy = random.nextDouble() * 3 + 1; // 1 到 4 向下

            // 球的颜色
            Color ballColor;
            if (i == redBallIndex) {
                ballColor = palette.error(); // 使用 error 颜色作为红色
            } else {
                // 随机其他颜色
                ballColor = ballColors[random.nextInt(ballColors.length)];
            }

            // 计算球的X位置
            int x = spacing * i;

            Ball ball = new Ball(x, startY, ballColor, vx, vy, r);
            engine.newBall(ball);

            // 记录红球引用
            if (i == redBallIndex) {
                redBall = ball;
            }
        }
    }

    /**
     * 检查红球是否落地
     */
    private void checkRedBallLanded() {
        if (redBall == null || redBallLanded)
            return;

        int height = getHeight();

        // 检查所有球是否都落地
        boolean allLanded = true;
        for (Ball ball : PhysicalEngine.getAllBalls()) {
            if (Math.abs(ball.getVy()) > 0.3 || ball.getY() + r < height - 15) {
                allLanded = false;
                break;
            }
        }

        if (allLanded) {
            redBallLanded = true;
            gameRunning = false;

            // 找出红球最终落地的顺序位置（从左到右）
            java.util.List<Ball> balls = PhysicalEngine.getAllBalls();
            java.util.List<Ball> sortedBalls = new java.util.ArrayList<>(balls);
            sortedBalls.sort((a, b) -> Double.compare(a.getX(), b.getX()));
            int redBallFinalPosition = sortedBalls.indexOf(redBall) + 1;

            boolean isRight = isRight(redBallFinalPosition);

            // 通知回调
            if (gameEndCallback != null) {
                gameEndCallback.onGameEnd(isRight, isRight ? 500 : 0);
            }
        }
    }

    public int getR() {
        return r;
    }

    /**
     * 设置游戏结束回调
     *
     * @param callback 回调接口
     */
    public void setGameEndCallback(GameEndCallback callback) {
        this.gameEndCallback = callback;
    }

    /**
     * 以 API 模式启动游戏（自动开始，不需要点击按钮）
     * 
     * @param luckyNum 幸运数字 (1-3)
     */
    public void startGameWithNumber(int luckyNum) {
        if (luckyNum < 1 || luckyNum > 3) {
            throw new IllegalArgumentException("幸运数字必须在 1-3 之间");
        }

        // 清除旧球
        PhysicalEngine.getAllBalls().clear();
        redBall = null;
        redBallLanded = false;
        gameRunning = true;
        luckyNumber = luckyNum;

        // 随机选择哪个球是红色
        int redBallIndex = random.nextInt(3) + 1;

        statusLabel.setText("你选择了: " + luckyNumber + " | 红球位置: ???");

        // 生成三个球
        generateBalls(redBallIndex);
    }

    private boolean isRight(int redBallFinalPosition) {
        if (luckyNumber == redBallFinalPosition) {
            score += 500;
            scoreLabel.setText("分数: +500");
            statusLabel.setText("恭喜！红球在位置 " + redBallFinalPosition + "，你猜对了！+500分");
            return true;
        } else {
            statusLabel.setText("很遗憾！红球在位置 " + redBallFinalPosition + "，你选的是 " + luckyNumber);
            return false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制装饰性背景
        drawBackground(g2d);

        // 绘制位置标记
        drawPositionMarkers(g2d);

        // 绘制所有球
        for (Ball ball : PhysicalEngine.getAllBalls()) {
            drawBall(g2d, ball);
        }

        // 绘制地面
        drawGround(g2d);

        g2d.dispose();
    }

    /**
     * 绘制装饰性背景
     */
    private void drawBackground(Graphics2D g2d) {
        // 绘制柔和的网格线（装饰）
        g2d.setColor(new Color(palette.surfaceVariant().getRGB() & 0x40FFFFFF, true));
        int gridSize = 40;
        for (int x = 0; x < getWidth(); x += gridSize) {
            g2d.drawLine(x, 100, x, getHeight());
        }
        for (int y = 100; y < getHeight(); y += gridSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }

        // 顶部分隔线
        g2d.setColor(palette.outline());
        g2d.drawLine(0, 100, getWidth(), 100);
    }

    /**
     * 绘制位置标记 (1, 2, 3)
     */
    private void drawPositionMarkers(Graphics2D g2d) {
        int panelWidth = getWidth();
        int height = getHeight();
        int spacing = panelWidth / 4;

        g2d.setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 24f));

        for (int i = 1; i <= 3; i++) {
            int x = spacing * i;

            // 绘制位置区域背景
            int zoneWidth = spacing - 20;
            g2d.setColor(new Color(palette.primaryContainer().getRGB() & 0x30FFFFFF, true));
            g2d.fillRoundRect(x - zoneWidth / 2, height - 80, zoneWidth, 60, 15, 15);

            // 绘制数字
            g2d.setColor(palette.primary());
            String num = String.valueOf(i);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(num);
            g2d.drawString(num, x - textWidth / 2, height - 45);
        }
    }

    /**
     * 绘制球（带阴影和高光效果）
     */
    private void drawBall(Graphics2D g2d, Ball ball) {
        int x = (int) ball.getX();
        int y = (int) ball.getY();
        int ballR = r;

        // 绘制阴影
        g2d.setColor(new Color(0, 0, 0, 40));
        g2d.fillOval(x - ballR + 4, y - ballR + 4, 2 * ballR, 2 * ballR);

        // 绘制球体（渐变效果）
        Color baseColor = ball.getColor();
        RadialGradientPaint ballGradient = new RadialGradientPaint(
                x - ballR / 3f, y - ballR / 3f, ballR * 1.5f,
                new float[] { 0f, 0.5f, 1f },
                new Color[] {
                        brighten(baseColor, 1.3f),
                        baseColor,
                        darken(baseColor, 0.7f)
                });
        g2d.setPaint(ballGradient);
        g2d.fillOval(x - ballR, y - ballR, 2 * ballR, 2 * ballR);

        // 绘制高光
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.fillOval(x - ballR / 2, y - ballR / 2, ballR / 2, ballR / 3);

        // 如果是红球且游戏进行中，绘制问号标记
        if (ball == redBall && gameRunning) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 16f));
            FontMetrics fm = g2d.getFontMetrics();
            String mark = "?";
            int textWidth = fm.stringWidth(mark);
            g2d.drawString(mark, x - textWidth / 2, y + fm.getAscent() / 3);
        }
    }

    /**
     * 绘制地面
     */
    private void drawGround(Graphics2D g2d) {
        int groundY = getHeight() - 15;

        // 使用 primary 颜色绘制地面
        GradientPaint groundGradient = new GradientPaint(
                0, groundY, palette.primary(),
                0, getHeight(), darken(palette.primary(), 0.7f));
        g2d.setPaint(groundGradient);
        g2d.fillRoundRect(10, groundY, getWidth() - 20, 12, 6, 6);

        // 地面高光
        g2d.setColor(brighten(palette.primary(), 1.2f));
        g2d.drawLine(15, groundY + 1, getWidth() - 15, groundY + 1);
    }

    // 工具方法：使颜色变亮
    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() * factor));
        int g = Math.min(255, (int) (color.getGreen() * factor));
        int b = Math.min(255, (int) (color.getBlue() * factor));
        return new Color(r, g, b);
    }

    // 工具方法：使颜色变暗
    private Color darken(Color color, float factor) {
        int r = (int) (color.getRed() * factor);
        int g = (int) (color.getGreen() * factor);
        int b = (int) (color.getBlue() * factor);
        return new Color(r, g, b);
    }

    public static void main(String[] args) {
        // 使用 FlatLaf 主题（与 CashFlow 一致）
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("赌球");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.getContentPane().setBackground(MaterialPalette.MOSS.surface());

            MiniImpact miniImpact = new MiniImpact(30);
            frame.add(miniImpact);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
