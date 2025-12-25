package top.liewyoung.view.mainWindows;

import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.view.ColorSystem.MaterialPalette;
import top.liewyoung.strategy.MapPostition;
import top.liewyoung.strategy.Position;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 地图绘制组件 - Material Design 风格
 * 
 * <p>该组件负责绘制游戏地图，包括方块和玩家位置。
 * 使用 Material Design 配色方案，提供流畅的视觉体验。</p>
 * 吐槽：注释不规范，重构两行泪。
 * @author LiewYoung
 * @since 2025/12/13
 */
public class MapDraw extends JPanel {
    
    // ==================== 常量定义 ====================
    
    /** 地图高度（格子数） */
    private static final int MAP_HEIGHT = 8;
    
    /** 地图宽度（格子数） */
    private static final int MAP_WIDTH = 8;
    
    /** 边距 */
    private static final int MARGIN = 50;
    
    /** 方块间距 */
    private static final int SPACING = 10;
    
    /** 方块宽度 */
    private static final int TILE_WIDTH = 90;
    
    /** 方块高度 */
    private static final int TILE_HEIGHT = 70;
    
    /** 玩家圆形大小 */
    private static final int PLAYER_SIZE = 20;
    
    /** 方块圆角半径 - 宽度方向 */
    private static final int TILE_CORNER_WIDTH = 16;
    
    /** 方块圆角半径 - 高度方向 */
    private static final int TILE_CORNER_HEIGHT = 10;
    
    /** 文字字体名称 */
    private static final String FONT_NAME = "微软雅黑";
    
    /** 文字字体大小 */
    private static final int FONT_SIZE = 12;
    
    /** 背景颜色 */
    private static final Color BACKGROUND_COLOR = new Color(253, 253, 245);
    
    /** 随机类型的起始索引（不包含） */
    private static final int RANDOM_TYPE_START_INDEX = 1;
    
    /** 随机类型的结束索引(不包含) */
    private static final int RANDOM_TYPE_END_INDEX = 6;
    
    /** 动画持续时间（每一格的移动时间，毫秒） */
    private static final int ANIMATION_DURATION_PER_GRID = 300;
    
    /** 动画帧间隔（毫秒） */
    private static final int ANIMATION_FRAME_INTERVAL = 16; // 约60fps
    
    /** 跳跃高度（像素） */
    private static final int JUMP_HEIGHT = 30;
    
    
    // ==================== 实例变量 ====================
    
    /** 存储所有方块 */
    private final List<Tile> tiles = new ArrayList<>();
    
    /** 玩家位置 */
    private final PlayerPosition playerPosition = new PlayerPosition(0, 0);
    
    /** 动画Timer */
    private Timer animationTimer;
    
    /** 动画进度 (0.0 到 1.0) */
    private double animationProgress = 0.0;
    
    /** 动画起始位置 */
    private Point animationStartPos;
    
    /** 动画目标位置 */
    private Point animationTargetPos;
    
    /** 当前动画路径（格子坐标列表） */
    private List<Point> animationPath;
    
    /** 当前路径索引 */
    private int currentPathIndex = 0;
    
    /** 是否显示骰子 */
    private boolean showDice = false;
    
    /** 骰子旋转角度 */
    private double diceRotation = 0.0;
    
    /** 骰子动画Timer */
    private Timer diceAnimationTimer;
    
    /** 骰子动画进度 (0.0 到 1.0) */
    private double diceAnimationProgress = 0.0;
    
    /** 骰子目标数字 */
    private int diceTargetNumber = 1;
    
    /** 骰子动画完成回调 */
    private Runnable diceOnComplete;
    
    /** 骰子大小 */
    private static final int DICE_SIZE = 100;
    
    /** 骰子动画持续时间（毫秒） */
    private static final int DICE_ANIMATION_DURATION = 1000;
    
    /** 骰子动画帧间隔（毫秒） */
    private static final int DICE_FRAME_INTERVAL = 16;
    
    /** 棋盘位置管理器 */
    private final MapPostition mapPostition = new MapPostition();
    

    
    

    
    /**
     * 开始骰子滚动动画
     * 
     * @param targetNumber 目标数字 (1-6)
     * @param onComplete 动画完成后的回调
     */
    public void rollDice(int targetNumber, Runnable onComplete) {
        // 停止当前动画（如果正在播放）
        if (diceAnimationTimer != null && diceAnimationTimer.isRunning()) {
            diceAnimationTimer.stop();
        }
        
        showDice = true;
        diceTargetNumber = Math.max(1, Math.min(6, targetNumber));
        diceOnComplete = onComplete;
        diceAnimationProgress = 0.0;
        diceRotation = 0.0;
        
        repaint();
        
        // 创建动画Timer
        diceAnimationTimer = new Timer(DICE_FRAME_INTERVAL, e -> {
            diceAnimationProgress += (double) DICE_FRAME_INTERVAL / DICE_ANIMATION_DURATION;
            
            if (diceAnimationProgress >= 1.0) {
                diceAnimationProgress = 1.0;
                diceAnimationTimer.stop();
                
                // 延迟一小段时间后隐藏骰子并执行回调
                Timer delayTimer = new Timer(500, evt -> {
                    showDice = false;
                    repaint();
                    if (diceOnComplete != null) {
                        diceOnComplete.run();
                    }
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            }
            
            // 更新旋转角度（多圈旋转）
            double eased = easeOutCubic(diceAnimationProgress);
            diceRotation = eased * Math.PI * 4; // 旋转4圈
            
            repaint();
        });
        
        diceAnimationTimer.start();
    }

    

    
    /**
     * 更新玩家位置并播放逐格跳动动画
     * 
     * @param x 新的 x 坐标
     * @param y 新的 y 坐标
     */
    public void updatePlayerPosition(int x, int y) {
        // 停止当前动画（如果正在播放）
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        // 计算从当前位置到目标位置的路径
        animationPath = calculatePath(playerPosition.x, playerPosition.y, x, y);
        
        // 如果路径为空或只有一个点（即当前位置），直接更新
        if (animationPath.isEmpty() || animationPath.size() == 1) {
            playerPosition.x = x;
            playerPosition.y = y;
            repaint();
            return;
        }
        
        // 开始动画
        currentPathIndex = 0;
        startNextGridAnimation();
    }
    
    /**
     * 开始下一格的跳动动画
     */
    private void startNextGridAnimation() {
        if (currentPathIndex >= animationPath.size() - 1) {
            // 动画完成
            return;
        }
        
        // 获取起始和目标格子
        Point startGrid = animationPath.get(currentPathIndex);
        Point targetGrid = animationPath.get(currentPathIndex + 1);
        
        animationStartPos = startGrid;
        animationTargetPos = targetGrid;
        animationProgress = 0.0;
        
        // 创建并启动动画Timer
        animationTimer = new Timer(ANIMATION_FRAME_INTERVAL, e -> {
            animationProgress += (double) ANIMATION_FRAME_INTERVAL / ANIMATION_DURATION_PER_GRID;
            
            if (animationProgress >= 1.0) {
                animationProgress = 1.0;
                // 更新玩家逻辑位置到目标格子
                playerPosition.x = animationTargetPos.x;
                playerPosition.y = animationTargetPos.y;
                
                // 停止当前格子的动画
                animationTimer.stop();
                
                // 移动到下一格
                currentPathIndex++;
                startNextGridAnimation();
            }
            
            repaint();
        });
        
        animationTimer.start();
    }
    
    /**
     * 计算从起点到终点的路径（格子坐标）
     * 
     * @param startX 起始 x 坐标
     * @param startY 起始 y 坐标
     * @param endX 结束 x 坐标  
     * @param endY 结束 y 坐标
     * @return 路径点列表
     */
    private List<Point> calculatePath(int startX, int startY, int endX, int endY) {
        List<Point> path = new ArrayList<>();
        
        // 找到起点和终点在mapOrder中的索引
        int startIndex = -1;
        int endIndex = -1;
        
        for (int i = 0; i < mapPostition.mapOrder.size(); i++) {
            Position pos = mapPostition.mapOrder.get(i);
            if (pos.x() == startX && pos.y() == startY) {
                startIndex = i;
            }
            if (pos.x() == endX && pos.y() == endY) {
                endIndex = i;
            }
        }
        
        // 如果找不到起点或终点，返回直接路径（降级处理）
        if (startIndex == -1 || endIndex == -1) {
            path.add(new Point(startX, startY));
            path.add(new Point(endX, endY));
            return path;
        }
        
        // 沿着mapOrder的顺序添加路径点
        int currentIndex = startIndex;
        while (currentIndex != endIndex) {
            Position pos = mapPostition.mapOrder.get(currentIndex);
            path.add(new Point(pos.x(), pos.y()));
            
            // 移动到下一个格子
            currentIndex = (currentIndex + 1) % mapPostition.mapOrder.size();
        }
        
        // 添加终点
        Position endPos = mapPostition.mapOrder.get(endIndex);
        path.add(new Point(endPos.x(), endPos.y()));
        
        return path;
    }
    
    /**
     * 获取指定位置的方块类型
     * 
     * @param x x 坐标
     * @param y y 坐标
     * @return 该位置的方块类型，如果不存在则返回 null
     */
    public TitlesTypes getType(int x, int y) {
        for (Tile tile : tiles) {
            if (tile.x == x && tile.y == y) {
                return tile.type;
            }
        }
        return null;
    }
    
    /**
     * 刷新地图，清除所有方块并重绘
     */
    public void refreshMap() {
        tiles.clear();
        repaint();
    }
    
    

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        enableAntialiasing(g2d);
        drawBackground(g2d);
        
        g2d.setColor(BACKGROUND_COLOR);
        
        calculateTiles();
        drawTiles(g);
        drawPlayer(g);
        
        // 绘制骰子（如果正在显示）
        if (showDice) {
            drawDice(g2d);
        }
        
        g2d.dispose();
    }
    
    /**
     * 绘制2D骰子
     * 
     * @param g2d Graphics2D 对象
     */
    private void drawDice(Graphics2D g2d) {
        // 在画布中心绘制骰子
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        // 保存原始变换
        Shape originalClip = g2d.getClip();
        g2d.setClip(null);
        
        // 应用旋转变换
        g2d.rotate(diceRotation, centerX, centerY);
        
        // 绘制骰子方块
        int x = centerX - DICE_SIZE / 2;
        int y = centerY - DICE_SIZE / 2;
        
        // 绘制阴影
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillRoundRect(x + 5, y + 5, DICE_SIZE, DICE_SIZE, 20, 20);
        
        // 绘制骰子主体
        g2d.setColor(MaterialPalette.MOSS.primaryContainer());
        g2d.fillRoundRect(x, y, DICE_SIZE, DICE_SIZE, 20, 20);
        
        // 绘制边框
        g2d.setColor(MaterialPalette.MOSS.primary());
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawRoundRect(x, y, DICE_SIZE, DICE_SIZE, 20, 20);
        
        // 如果动画完成，显示最终数字
        if (diceAnimationProgress >= 1.0) {
            drawDiceNumber(g2d, centerX, centerY, diceTargetNumber);
        }
        
        // 恢复原始变换和剪切区域
        g2d.rotate(-diceRotation, centerX, centerY);
        g2d.setClip(originalClip);
    }
    
    /**
     * 在骰子上绘制点数
     * 
     * @param g2d Graphics2D 对象
     * @param centerX 中心X坐标
     * @param centerY 中心Y坐标
     * @param number 数字 (1-6)
     */
    private void drawDiceNumber(Graphics2D g2d, int centerX, int centerY, int number) {
        g2d.setColor(MaterialPalette.MOSS.onPrimaryContainer());
        
        int dotSize = 12;
        int spacing = 30;
        
        switch (number) {
            case 1 -> {
                // 中间一个点
                g2d.fillOval(centerX - dotSize / 2, centerY - dotSize / 2, dotSize, dotSize);
            }
            case 2 -> {
                // 对角线两个点
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
            }
            case 3 -> {
                // 对角线三个点
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX - dotSize / 2, centerY - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
            }
            case 4 -> {
                // 四个角
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
            }
            case 5 -> {
                // 四个角加中间
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX - dotSize / 2, centerY - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
            }
            case 6 -> {
                // 两列，每列三个
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX - spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY - spacing - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY - dotSize / 2, dotSize, dotSize);
                g2d.fillOval(centerX + spacing - dotSize / 2, centerY + spacing - dotSize / 2, dotSize, dotSize);
            }
        }
    }
    
    /**
     * 缓动函数：ease-out cubic
     * 
     * @param t 进度 (0.0 到 1.0)
     * @return 缓动后的值
     */
    private double easeOutCubic(double t) {
        return 1 - Math.pow(1 - t, 3);
    }


    /**
     * 绘制背景
     *
     * @param g2d G2D
     */
    private void drawBackground(Graphics2D g2d) {
        // 绘制柔和的网格线（装饰）
        g2d.setColor(new Color(MaterialPalette.MOSS.surfaceVariant().getRGB() & 0x40FFFFFF, true));
        int gridSize = 40;
        for (int x = 0; x < getWidth(); x += gridSize) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += gridSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }

    }

    
    /**
     * 绘制玩家（支持动画）
     * 
     * @param g Graphics 对象
     */
    public void drawPlayer(Graphics g) {
        int x, y;
        
        // 如果正在播放动画，使用插值位置
        if (animationTimer != null && animationTimer.isRunning() && animationStartPos != null && animationTargetPos != null) {
            // 计算插值位置（屏幕坐标）
            int startX = calculateTileX(animationStartPos.x) + (TILE_WIDTH - PLAYER_SIZE) / 2;
            int startY = calculateTileY(animationStartPos.y) + (TILE_HEIGHT - PLAYER_SIZE) / 2;
            int endX = calculateTileX(animationTargetPos.x) + (TILE_WIDTH - PLAYER_SIZE) / 2;
            int endY = calculateTileY(animationTargetPos.y) + (TILE_HEIGHT - PLAYER_SIZE) / 2;
            
            // 使用缓动函数（ease-in-out）
            double eased = easeInOutQuad(animationProgress);
            
            x = (int) (startX + (endX - startX) * eased);
            y = (int) (startY + (endY - startY) * eased);
            
            // 添加跳跃效果（抛物线）
            // 在动画中间达到最高点
            double jumpOffset = Math.sin(animationProgress * Math.PI) * JUMP_HEIGHT;
            y -= (int) jumpOffset;
        } else {
            // 使用静态位置
            x = calculatePlayerX();
            y = calculatePlayerY();
        }
        
        Graphics2D g2d = (Graphics2D) g.create();
        enableAntialiasing(g2d);
        
        g2d.setColor(MaterialPalette.MOSS.PURPLE.error());
        g2d.fillOval(x, y, PLAYER_SIZE, PLAYER_SIZE);
        
        g2d.dispose();
    }
    
    /**
     * 缓动函数：ease-in-out quad
     * 
     * @param t 进度 (0.0 到 1.0)
     * @return 缓动后的值
     */
    private double easeInOutQuad(double t) {
        return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2;
    }
    
    /**
     * 绘制所有方块
     * 
     * @param g Graphics 对象
     */
    protected void drawTiles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        MaterialPalette palette = MaterialPalette.MOSS;
        
        for (Tile tile : tiles) {
            int x = calculateTileX(tile.x);
            int y = calculateTileY(tile.y);
            
            // 绘制方块背景
            g2d.setColor(getColor(tile.type));
            g2d.fillRoundRect(x, y, TILE_WIDTH, TILE_HEIGHT, TILE_CORNER_WIDTH, TILE_CORNER_HEIGHT);
            
            // 绘制方块文字
            drawTileText(g2d, tile, x, y, palette);
        }
        g2d.dispose();
    }
    
    

    
    /**
     * 计算并生成所有方块
     */
    protected void calculateTiles() {
        if (!tiles.isEmpty()) {
            return;
        }
        
        Random random = new Random();
        
        // 生成第一行（顶部）
        generateTopRow(random);
        
        // 生成左侧列（不包括角落）
        generateLeftColumn(random);
        
        // 生成右侧列（不包括角落）
        generateRightColumn(random);
        
        // 生成最后一行（底部）
        generateBottomRow(random);
    }
    
    /**
     * 计算玩家的屏幕 X 坐标
     */
    private int calculatePlayerX() {
        return MARGIN + playerPosition.x * (SPACING + TILE_WIDTH) + (TILE_WIDTH - PLAYER_SIZE) / 2;
    }
    
    /**
     * 计算玩家的屏幕 Y 坐标
     */
    private int calculatePlayerY() {
        return MARGIN + playerPosition.y * (SPACING + TILE_HEIGHT) + (TILE_HEIGHT - PLAYER_SIZE) / 2;
    }
    
    /**
     * 计算方块的屏幕 X 坐标
     */
    private int calculateTileX(int gridX) {
        return MARGIN + gridX * (SPACING + TILE_WIDTH);
    }
    
    /**
     * 计算方块的屏幕 Y 坐标
     */
    private int calculateTileY(int gridY) {
        return MARGIN + gridY * (SPACING + TILE_HEIGHT);
    }
    
    
    // ==================== 方块生成方法 ====================
    
    /**
     * 生成顶部行的方块
     */
    private void generateTopRow(Random random) {
        for (int i = 0; i < MAP_WIDTH; i++) {
            int index = (i == 1) ? 1 : random.nextInt(RANDOM_TYPE_START_INDEX, RANDOM_TYPE_END_INDEX);
            TitlesTypes type = TitlesTypes.values()[index];
            tiles.add(new Tile(i, 0, type));
        }
    }
    
    /**
     * 生成左侧列的方块（不包括角落）
     */
    private void generateLeftColumn(Random random) {
        for (int i = 1; i < MAP_HEIGHT - 1; i++) {
            int index = random.nextInt(RANDOM_TYPE_START_INDEX, RANDOM_TYPE_END_INDEX);
            TitlesTypes type = TitlesTypes.values()[index];
            tiles.add(new Tile(0, i, type));
        }
    }
    
    /**
     * 生成右侧列的方块（不包括角落）
     */
    private void generateRightColumn(Random random) {
        for (int i = 1; i < MAP_HEIGHT - 1; i++) {
            int index = random.nextInt(RANDOM_TYPE_START_INDEX, RANDOM_TYPE_END_INDEX);
            TitlesTypes type = TitlesTypes.values()[index];
            tiles.add(new Tile(MAP_WIDTH - 1, i, type));
        }
    }
    
    /**
     * 生成底部行的方块
     */
    private void generateBottomRow(Random random) {
        for (int i = 0; i < MAP_WIDTH; i++) {
            int index = random.nextInt(RANDOM_TYPE_START_INDEX, RANDOM_TYPE_END_INDEX);
            TitlesTypes type = TitlesTypes.values()[index];
            tiles.add(new Tile(i, MAP_HEIGHT - 1, type));
        }
    }
    
    

    
    /**
     * 启用抗锯齿
     */
    private void enableAntialiasing(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    /**
     * 绘制方块上的文字
     */
    private void drawTileText(Graphics2D g2d, Tile tile, int x, int y, MaterialPalette palette) {
        String text = getText(tile.type);
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        
        int textX = x + (TILE_WIDTH - textWidth) / 2;
        int textY = y + (TILE_HEIGHT - textHeight) / 2 + textHeight;
        
        // 根据方块类型选择文字颜色
        if (tile.type == TitlesTypes.FUNGAME) {
            g2d.setColor(palette.onSurface());
        } else {
            g2d.setColor(palette.surface());
        }
        
        g2d.setFont(new Font(FONT_NAME, Font.BOLD, FONT_SIZE));
        g2d.drawString(text, textX, textY);
    }
    
    /**
     * 根据方块类型获取对应的文本
     *
     * @param type 枚举类型
     * @return 对应的文本
     */
    private String getText(TitlesTypes type) {
        return switch (type) {
            case TitlesTypes.START -> "开始";
            case TitlesTypes.OPPORTUNITY -> "机会";
            case TitlesTypes.MARKET -> "市场";
            case TitlesTypes.FATE -> "命运";
            case TitlesTypes.BANK -> "银行";
            default -> "游戏";
        };
    }
    
    /**
     * 根据方块类型获取对应的颜色
     * 
     * <p>使用 Material Design 配色方案，提供缤纷多彩的视觉效果</p>
     *
     * @param type 方块类型枚举
     * @return 对应的颜色
     */
    private Color getColor(TitlesTypes type) {
        return switch (type) {
            case TitlesTypes.START -> new Color(46, 204, 113);        // 翠绿色
            case TitlesTypes.OPPORTUNITY -> new Color(192, 202, 51);  // 黄绿色
            case TitlesTypes.MARKET -> new Color(76, 175, 80);        // 绿色
            case TitlesTypes.FATE -> new Color(0, 137, 123);          // 青色
            case TitlesTypes.BANK -> new Color(27, 94, 32);           // 深绿色
            default -> new Color(105, 240, 174);                       // 浅绿色
        };
    }
    
    

    
    /**
     * 玩家位置类
     * 
     * <p>用于存储和管理玩家在地图上的位置坐标</p>
     */
    class PlayerPosition {
        /** X 坐标（格子坐标，非像素坐标） */
        int x;
        
        /** Y 坐标（格子坐标，非像素坐标） */
        int y;
        
        /**
         * 构造函数
         * 
         * @param x 初始 X 坐标
         * @param y 初始 Y 坐标
         */
        public PlayerPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    /**
     * 方块类
     * 
     * <p>表示地图上的一个方块，包含位置和类型信息</p>
     */
    class Tile {
        /** X 坐标（格子坐标，非像素坐标） */
        int x;
        
        /** Y 坐标（格子坐标，非像素坐标） */
        int y;
        
        /** 方块类型 */
        TitlesTypes type;
        
        /**
         * 构造函数
         * 
         * @param x 格子 X 坐标
         * @param y 格子 Y 坐标
         * @param type 方块类型
         */
        public Tile(int x, int y, TitlesTypes type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
}
