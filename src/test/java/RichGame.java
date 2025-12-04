import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 进阶版 Java Swing 大富翁 - 适合已掌握贪吃蛇逻辑的同学
 * 核心技术点：
 * 1. 自定义组件绘图 (画地图)
 * 2. 复杂布局管理 (左图右文)
 * 3. 面向对象设计 (Tile, Player)
 */
public class RichGame extends JFrame {

    // --- 配置参数 ---
    private static final int TILE_SIZE = 60; // 每个格子大小
    private static final int BOARD_WIDTH = 8;  // 横向格子数
    private static final int BOARD_HEIGHT = 8; // 纵向格子数
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 650;

    // --- 核心组件 ---
    private GameBoardPanel boardPanel;
    private JTextArea logArea;
    private JButton rollDiceBtn;
    private JLabel statusLabel;

    // --- 游戏数据 ---
    private Player player;
    private List<Tile> mapTiles;
    private MockLLMService llmService; // 模拟 AI 服务

    public RichGame() {
        setTitle("AI 现金流大富翁 (Java 进阶版)");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 初始化数据
        initGameData();

        // 1. 左侧：游戏地图面板 (自定义绘图，类似贪吃蛇的画法)
        boardPanel = new GameBoardPanel();
        boardPanel.setPreferredSize(new Dimension(600, 600));

        // 2. 右侧：控制与交互面板
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(280, 600));
        sidePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidePanel.setBackground(new Color(240, 248, 255));

        // 2.1 顶部状态
        statusLabel = new JLabel("准备就绪");
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));

        // 2.2 中间日志 (显示 AI 剧情)
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("宋体", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(logArea);

        // 2.3 底部按钮
        rollDiceBtn = new JButton("🎲 掷骰子");
        rollDiceBtn.setFont(new Font("微软雅黑", Font.BOLD, 16));
        rollDiceBtn.setBackground(new Color(100, 149, 237));
        rollDiceBtn.setForeground(Color.WHITE);
        rollDiceBtn.setFocusPainted(false);

        rollDiceBtn.addActionListener(e -> playTurn());

        sidePanel.add(statusLabel, BorderLayout.NORTH);
        sidePanel.add(scrollPane, BorderLayout.CENTER);
        sidePanel.add(rollDiceBtn, BorderLayout.SOUTH);

        // 组装主界面
        add(boardPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);

        log("欢迎来到 AI 大富翁！点击掷骰子开始游戏。");
        updateStatus();
    }

    // 初始化地图数据 (生成一个回字形路径)
    private void initGameData() {
        player = new Player("玩家1", 5000);
        llmService = new MockLLMService();
        mapTiles = new ArrayList<>();

        // 这是一个算法逻辑：生成围着边缘一圈的格子
        // 上边 (0 -> W-1)
        for (int i = 0; i < BOARD_WIDTH; i++) mapTiles.add(new Tile(i, 0));
        // 右边 (1 -> H-1)
        for (int i = 1; i < BOARD_HEIGHT; i++) mapTiles.add(new Tile(BOARD_WIDTH - 1, i));
        // 下边 (W-2 -> 0)
        for (int i = BOARD_WIDTH - 2; i >= 0; i--) mapTiles.add(new Tile(i, BOARD_HEIGHT - 1));
        // 左边 (H-2 -> 1)
        for (int i = BOARD_HEIGHT - 2; i > 0; i--) mapTiles.add(new Tile(0, i));

        // 为格子随机分配类型
        String[] types = {"机会", "命运", "房产", "空地", "银行"};
        Random r = new Random();
        for (Tile t : mapTiles) {
            t.type = types[r.nextInt(types.length)];
            // 起点特殊处理
            if (t.x == 0 && t.y == 0) t.type = "起点";
        }
    }

    // --- 核心游戏逻辑 ---
    private void playTurn() {
        rollDiceBtn.setEnabled(false); // 防止狂点

        // 1. 掷骰子动画逻辑 (这里简化直接出结果)
        int steps = new Random().nextInt(6) + 1;
        log("\n>>> 你掷出了 " + steps + " 点！");

        // 2. 移动玩家
        // 类似贪吃蛇的坐标更新，但这里是沿着 List 索引移动
        int currentIndex = player.currentTileIndex;
        int nextIndex = (currentIndex + steps) % mapTiles.size();

        // 简单的“动画”效果：使用 Timer 逐步移动
        Timer moveTimer = new Timer(200, null);
        final int targetIndex = nextIndex;

        moveTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.currentTileIndex = (player.currentTileIndex + 1) % mapTiles.size();
                boardPanel.repaint(); // 重绘界面

                // 到达目的地
                if (player.currentTileIndex == targetIndex) {
                    moveTimer.stop();
                    triggerTileEvent(mapTiles.get(targetIndex));
                    rollDiceBtn.setEnabled(true);
                }
            }
        });
        moveTimer.start();
    }

    // 触发格子事件 (接入 LLM)
    private void triggerTileEvent(Tile tile) {
        log("你停在了 [" + tile.type + "] 格子。");

        // 模拟调用 AI 生成剧情
        String aiStory = llmService.generateStory(tile.type, player.cash);
        log("🤖 AI GM: " + aiStory);

        // 简单的数值变动逻辑
        if (tile.type.equals("机会") || tile.type.equals("房产")) {
            // 这里可以弹窗让用户选择，为了代码简单直接扣钱/加钱
            if (Math.random() > 0.5) {
                int gain = 500;
                player.cash += gain;
                log("系统结算: 收益 +$" + gain);
            } else {
                int cost = 200;
                player.cash -= cost;
                log("系统结算: 支出 -$" + cost);
            }
        }
        updateStatus();
    }

    private void updateStatus() {
        statusLabel.setText(String.format("玩家: %s | 现金: $%d | 位置: %d",
                player.name, player.cash, player.currentTileIndex));
    }

    private void log(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength()); // 自动滚动到底部
    }

    // --- 内部类：地图面板 (核心绘图区) ---
    // 这部分和贪吃蛇的 paintComponent 很像
    class GameBoardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 开启抗锯齿，画出来好看点
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1. 绘制背景
            g2d.setColor(new Color(220, 220, 220));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // 2. 绘制所有格子
            for (int i = 0; i < mapTiles.size(); i++) {
                Tile t = mapTiles.get(i);
                int screenX = 50 + t.x * TILE_SIZE; // 50是边距
                int screenY = 50 + t.y * TILE_SIZE;

                // 绘制格子矩形
                if (t.type.equals("起点")) g2d.setColor(new Color(255, 100, 100));
                else if (t.type.equals("机会")) g2d.setColor(new Color(100, 200, 100));
                else if (t.type.equals("房产")) g2d.setColor(new Color(100, 100, 255));
                else g2d.setColor(Color.WHITE);

                g2d.fillRoundRect(screenX + 2, screenY + 2, TILE_SIZE - 4, TILE_SIZE - 4, 10, 10);

                // 绘制边框
                g2d.setColor(Color.GRAY);
                g2d.drawRoundRect(screenX + 2, screenY + 2, TILE_SIZE - 4, TILE_SIZE - 4, 10, 10);

                // 绘制文字
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("黑体", Font.PLAIN, 12));
                // 文字居中简单算法
                FontMetrics fm = g2d.getFontMetrics();
                int textW = fm.stringWidth(t.type);
                g2d.drawString(t.type, screenX + (TILE_SIZE - textW)/2, screenY + TILE_SIZE/2 + 5);
            }

            // 3. 绘制玩家 (像贪吃蛇的蛇头)
            Tile playerTile = mapTiles.get(player.currentTileIndex);
            int px = 50 + playerTile.x * TILE_SIZE + TILE_SIZE/4;
            int py = 50 + playerTile.y * TILE_SIZE + TILE_SIZE/4;

            g2d.setColor(Color.RED);
            g2d.fillOval(px, py, TILE_SIZE/2, TILE_SIZE/2);
            // 给玩家加个金边
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(px, py, TILE_SIZE/2, TILE_SIZE/2);
        }
    }

    // --- 数据类 ---
    static class Tile {
        int x, y; // 逻辑网格坐标
        String type;
        public Tile(int x, int y) { this.x = x; this.y = y; }
    }

    static class Player {
        String name;
        int cash;
        int currentTileIndex = 0; // 在 List 中的索引
        public Player(String name, int cash) {
            this.name = name;
            this.cash = cash;
        }
    }

    // --- 模拟 LLM 服务 ---
    // 这里展示如何用代码模拟 AI，后期可以将 generateStory 替换为 HTTP 请求
    static class MockLLMService {
        private Random random = new Random();

        public String generateStory(String tileType, int currentCash) {
            if (tileType.equals("房产")) {
                String[] plots = {
                        "你发现了一栋因主人出国急售的别墅，虽然看起来有些破旧。",
                        "市中心的一个小公寓正在拍卖，竞争者看起来不多。",
                        "这是一个偏远的仓库，但听说附近规划了新的地铁线。"
                };
                return plots[random.nextInt(plots.length)] + " (当前资金: " + currentCash + ")";
            } else if (tileType.equals("机会")) {
                String[] plots = {
                        "你在路边捡到了一张未兑奖的彩票。",
                        "一个老同学邀请你投资他的奶茶店。",
                        "股市突然崩盘，但你之前看好的一支股票似乎被低估了。"
                };
                return plots[random.nextInt(plots.length)];
            } else {
                return "这是平淡无奇的一天，你在路边买了个煎饼果子。";
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RichGame().setVisible(true));
    }
}