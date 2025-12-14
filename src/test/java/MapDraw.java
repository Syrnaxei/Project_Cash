import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.view.ColorSystem.MaterialPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Optimized MapDraw
 * 1. 逻辑与渲染分离
 * 2. 高级抗锯齿渲染
 * 3. 性能优化 (减少对象创建)
 */
public class MapDraw extends JPanel {
    // 全局布局设置
    private static final int MAP_WIDTH_COUNT = 8;  // 格子数量 (宽)
    private static final int MAP_HEIGHT_COUNT = 8; // 格子数量 (高)
    private static final int MARGIN = 50;
    private static final int SPACING = 10;
    private static final int TILE_WIDTH = 90;
    private static final int TILE_HEIGHT = 70;
    private static final int PLAYER_SIZE = 24; // 稍微调大一点

    // 缓存字体，避免重复创建
    private static final Font TILE_FONT = UIManager.getFont("Label.font").deriveFont(Font.BOLD, 13f);

    // 存储地图数据
    private final List<Tile> tiles = new ArrayList<>();
    private final Point playerPos = new Point(0, 0); // 使用 java.awt.Point 替代自定义类

    // 骰子动画相关字段
    private Timer diceTimer;
    private int diceValue;
    private int diceRollCount;
    private Runnable diceCallback;

    public MapDraw() {
        // 设置面板透明，由外部决定背景色 (或者继承 FlatLaf 背景)
        setOpaque(true);
        setBackground(MaterialPalette.MOSS.surface()); // 假设这是你的背景色

        // 建议设置一个首选大小，防止布局压缩
        int totalW = MARGIN * 2 + MAP_WIDTH_COUNT * (TILE_WIDTH + SPACING);
        int totalH = MARGIN * 2 + MAP_HEIGHT_COUNT * (TILE_HEIGHT + SPACING);
        setPreferredSize(new Dimension(totalW, totalH));

        // 初始化数据 (只执行一次)
        initMapData();
    }

    /**
     * 初始化地图数据 logic
     */
    private void initMapData() {
        tiles.clear();
        Random r = new Random();

        // 辅助方法：生成并添加 Tile
        // 顶边
        for (int x = 0; x < MAP_WIDTH_COUNT; x++) {
            addTile(x, 0, r);
        }
        // 右边
        for (int y = 1; y < MAP_HEIGHT_COUNT - 1; y++) {
            addTile(MAP_WIDTH_COUNT - 1, y, r);
        }
        // 底边
        for (int x = MAP_WIDTH_COUNT - 1; x >= 0; x--) {
            addTile(x, MAP_HEIGHT_COUNT - 1, r);
        }
        // 左边
        for (int y = MAP_HEIGHT_COUNT - 2; y > 0; y--) {
            addTile(0, y, r);
        }
    }

    private void addTile(int x, int y, Random r) {
        // 特殊逻辑：如果是 (1,0) 位置固定
        TitlesTypes type;
        if (x == 1 && y == 0) {
            // 假设这里你有特殊逻辑，比如固定是 OPPORTUNITY
            // index = 1 in your code
            type = TitlesTypes.values()[1];
        } else {
            type = TitlesTypes.values()[r.nextInt(1, 6)]; // 假设 1-6 是有效范围
        }
        tiles.add(new Tile(x, y, type));
    }

    // --- 外部接口 ---

    /**
     * 模拟骰子滚动动画
     * @param value 骰子最终值
     * @param callback 动画完成后的回调函数
     */
    public void rollDice(int value, Runnable callback) {
        this.diceValue = value;
        this.diceCallback = callback;
        this.diceRollCount = 0;
        
        // 启动定时器模拟骰子滚动效果
        if (diceTimer != null && diceTimer.isRunning()) {
            diceTimer.stop();
        }
        
        diceTimer = new Timer(100, e -> {
            diceRollCount++;
            // 重绘以显示新的骰子画面
            repaint();
            
            // 模拟滚动一段时间后停止
            if (diceRollCount >= 10) { // 滚动10次后停止
                diceTimer.stop();
                // 执行回调
                if (diceCallback != null) {
                    diceCallback.run();
                }
            }
        });
        diceTimer.start();
    }

    public void updatePlayerPosition(int x, int y) {
        playerPos.setLocation(x, y);
        repaint(); // 仅触发重绘，不重新计算地图
    }

    public void refreshMap() {
        initMapData();
        repaint();
    }

    public TitlesTypes getType(int x, int y) {
        return tiles.stream()
                .filter(t -> t.x() == x && t.y() == y)
                .map(Tile::type)
                .findFirst()
                .orElse(null);
    }

    // --- 核心绘制 ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 必须调用，用于清除背景

        Graphics2D g2d = (Graphics2D) g.create(); // 总是 create 一个副本，防止污染外部 g

        // 1. 开启高级渲染质量 (抗锯齿 + 文本平滑)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 2. 绘制所有方块
        drawTiles(g2d);

        // 3. 绘制玩家
        drawPlayer(g2d);

        drawDice(g2d);

        g2d.dispose(); // 释放副本
    }

    private void drawDice(Graphics g) {
        // 只有当正在显示骰子动画时才绘制骰子
        if (diceTimer != null && diceTimer.isRunning()) {
            MaterialPalette palette = MaterialPalette.MOSS;

            Graphics2D g2d = (Graphics2D) g;

            // 骰子大小
            int size = 100;
            // 计算屏幕中心位置
            int centerX = getWidth() / 2 - size / 2;
            int centerY = getHeight() / 2 - size / 2;

            // 1. 画阴影 (让骰子看起来是悬浮的)
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillRoundRect(centerX + 5, centerY + 5, size, size, 20, 20);

            // 2. 画骰子底色 (白色)
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(centerX, centerY, size, size, 20, 20);

            // 3. 画边框 (稍微带点灰，更有质感)
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(centerX, centerY, size, size, 20, 20);

            // 4. 画数字
            g2d.setColor(palette.primary()); // 使用你的主题色，或者直接用 Color.BLACK
            g2d.setFont(new Font("微软雅黑", Font.BOLD, 50));

            // 让文字居中
            FontMetrics fm = g2d.getFontMetrics();
            
            // 显示当前的随机数字（模拟滚动效果）
            String text = String.valueOf((diceRollCount % 6) + 1);
            if (diceRollCount >= 9) { // 最后一次显示真实数值
                text = String.valueOf(diceValue);
            }
            
            int textX = centerX + (size - fm.stringWidth(text)) / 2;
            int textY = centerY + (size - fm.getHeight()) / 2 + fm.getAscent();

            g2d.drawString(text, textX, textY);
        }
    }

    private void drawTiles(Graphics2D g2d) {
        FontMetrics fm = g2d.getFontMetrics(TILE_FONT);
        int fontAscent = fm.getAscent();

        for (Tile tile : tiles) {
            int x = MARGIN + tile.x() * (SPACING + TILE_WIDTH);
            int y = MARGIN + tile.y() * (SPACING + TILE_HEIGHT);

            // 绘制背景 (圆角矩形)
            g2d.setColor(getTileColor(tile.type()));
            // 使用浮点数坐标 (RoundRectangle2D.Double) 会在缩放时更平滑
            g2d.fill(new RoundRectangle2D.Double(x, y, TILE_WIDTH, TILE_HEIGHT, 16, 16));

            // 绘制文字
            String text = getTileText(tile.type());
            int textWidth = fm.stringWidth(text);

            // 计算文字居中位置
            float textX = x + (TILE_WIDTH - textWidth) / 2f;
            float textY = y + (TILE_HEIGHT - fm.getHeight()) / 2f + fontAscent;

            if (tile.type() == TitlesTypes.FUNGAME) {
                g2d.setColor(MaterialPalette.MOSS.onSurface());
            } else {
                g2d.setColor(MaterialPalette.MOSS.surface());
            }
            g2d.setFont(TILE_FONT);
            g2d.drawString(text, textX, textY);
        }
    }

    private void drawPlayer(Graphics2D g2d) {
        int x = MARGIN + playerPos.x * (SPACING + TILE_WIDTH) + (TILE_WIDTH - PLAYER_SIZE) / 2;
        int y = MARGIN + playerPos.y * (SPACING + TILE_HEIGHT) + (TILE_HEIGHT - PLAYER_SIZE) / 2;

        // 绘制玩家 (加一点阴影或边框会让它更明显)
        g2d.setColor(Color.WHITE); // 边框
        g2d.fillOval(x - 2, y - 2, PLAYER_SIZE + 4, PLAYER_SIZE + 4);

        g2d.setColor(MaterialPalette.MOSS.PURPLE.error()); // 核心
        g2d.fillOval(x, y, PLAYER_SIZE, PLAYER_SIZE);
    }

    // --- 数据定义 ---

    // Java 14+ record: 自动生成构造器、getter、equals、hashCode
    private record Tile(int x, int y, TitlesTypes type) {}

    // 颜色和文字映射
    private String getTileText(TitlesTypes type) {
        return switch (type) {
            case START -> "开始";
            case OPPORTUNITY -> "机会";
            case MARKET -> "市场";
            case FATE -> "命运";
            case BANK -> "银行";
            default -> "游戏"; // FUNGAME etc.
        };
    }

    // 颜色不再每次 new，而是返回常量 (或者在这里 new，JVM现在的优化能力其实也还能接受)
    private Color getTileColor(TitlesTypes type) {
        return switch (type) {
            case START -> new Color(46, 204, 113);
            case OPPORTUNITY -> new Color(192, 202, 51);
            case MARKET -> new Color(76, 175, 80);
            case FATE -> new Color(0, 137, 123);
            case BANK -> new Color(27, 94, 32);
            default -> new Color(105, 240, 174);
        };
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CashFlow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new MapDraw());
        frame.setVisible(true);
    }

}