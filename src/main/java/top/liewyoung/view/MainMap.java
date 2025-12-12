package top.liewyoung.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LiewYoung
 * @since  2025/12/12
 */

public class MainMap extends JPanel {

    //方块长宽以及间隙定义
    private static final int TILE_WIDTH = 60;
    private static final int TILE_HEIGHT = 60;
    private static final int TILE_SPACING = 15;
    private static final int MAP_MARGIN = 50;
    
    // 地图尺寸 (8x8网格)
    private static final int MAP_WIDTH = 8;
    private static final int MAP_HEIGHT = 8;
    
    // 不同类型地块的颜色定义
    private static final Color START_COLOR = new Color(255, 215, 0);     // 金色
    private static final Color OPPORTUNITY_COLOR = new Color(50, 205, 50); // 绿色
    private static final Color MARKET_COLOR = new Color(65, 105, 225);    // 皇家蓝
    private static final Color FATE_COLOR = new Color(220, 20, 60);       // 苋菜紫红
    private static final Color BANK_COLOR = new Color(255, 215, 0);       // 金色
    private static final Color NORMAL_COLOR = new Color(245, 245, 245);   // 浅灰
    
    private List<Tile> tiles;
    private int playerPosition = 0;

    public MainMap() {
        setBackground(Color.WHITE);
        initializeTiles();
    }
    
    private void initializeTiles() {
        tiles = new ArrayList<>();
        
        // 创建环形地图数据结构
        // 上边 (从左到右)
        for (int i = 0; i < MAP_WIDTH; i++) {
            tiles.add(new Tile(i, 0, getTileType(tiles.size())));
        }
        
        // 右边 (从上到下，不包括角落)
        for (int i = 1; i < MAP_HEIGHT; i++) {
            tiles.add(new Tile(MAP_WIDTH - 1, i, getTileType(tiles.size())));
        }
        
        // 下边 (从右到左，不包括角落)
        for (int i = MAP_WIDTH - 2; i >= 0; i--) {
            tiles.add(new Tile(i, MAP_HEIGHT - 1, getTileType(tiles.size())));
        }
        
        // 左边 (从下到上，不包括角落)
        for (int i = MAP_HEIGHT - 2; i > 0; i--) {
            tiles.add(new Tile(0, i, getTileType(tiles.size())));
        }
        
        // 设置起点
        if (!tiles.isEmpty()) {
            tiles.get(0).type = "起点";
        }
    }
    
    private String getTileType(int index) {
        // 根据位置确定地块类型
        switch (index % 4) {
            case 0: return "机会";
            case 1: return "市场";
            case 2: return "命运";
            case 3: return "银行";
            default: return "普通";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        // 启用抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 绘制背景
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // 绘制所有地块
        drawTiles(g2d);
        
        // 绘制玩家
        drawPlayer(g2d);
        
        g2d.dispose();
    }
    
    private void drawTiles(Graphics2D g2d) {
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);

            /*
             *绘制地块
             * 只要把地块当成上下右边有间隙也就是一面没有间隙的方块就好了
             */
            int x = MAP_MARGIN + tile.x * (TILE_WIDTH + TILE_SPACING);
            int y = MAP_MARGIN + tile.y * (TILE_HEIGHT + TILE_SPACING);
            
            // 根据类型设置颜色
            Color tileColor = getTileColor(tile.type);
            g2d.setColor(tileColor);
            
            // 绘制圆角矩形地块
            g2d.fillRoundRect(x, y, TILE_WIDTH, TILE_HEIGHT, 15, 15);
            
            // 绘制边框
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(x, y, TILE_WIDTH, TILE_HEIGHT, 15, 15);
            
            // 绘制地块类型文字
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("微软雅黑", Font.BOLD, 12));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(tile.type);
            g2d.drawString(tile.type, 
                          x + (TILE_WIDTH - textWidth) / 2, 
                          y + TILE_HEIGHT / 2);
            
            // 绘制序号
            g2d.setFont(new Font("微软雅黑", Font.PLAIN, 10));
            fm = g2d.getFontMetrics();
            String indexStr = String.valueOf(i);
            int indexWidth = fm.stringWidth(indexStr);
            g2d.drawString(indexStr, 
                          x + (TILE_WIDTH - indexWidth) / 2, 
                          y + TILE_HEIGHT / 2 + 15);
        }
    }

    //绘制玩家也就是个圆
    private void drawPlayer(Graphics2D g2d) {
        if (tiles.isEmpty() || playerPosition < 0 || playerPosition >= tiles.size()) {
            return;
        }
        
        Tile playerTile = tiles.get(playerPosition);
        
        // 计算玩家在屏幕上的位置（地块中心偏上）
        int x = MAP_MARGIN + playerTile.x * (TILE_WIDTH + TILE_SPACING) + TILE_WIDTH / 2;
        int y = MAP_MARGIN + playerTile.y * (TILE_HEIGHT + TILE_SPACING) + TILE_HEIGHT / 3;
        
        // 绘制玩家棋子阴影
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillOval(x - 8, y - 8 + 3, 16, 16);
        
        // 绘制玩家棋子
        g2d.setColor(Color.RED);
        g2d.fillOval(x - 8, y - 8, 16, 16);
        
        // 绘制玩家边框
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - 8, y - 8, 16, 16);
    }



    //设置丰富多彩的颜色
    private Color getTileColor(String type) {
        switch (type) {
            case "起点": return START_COLOR;
            case "机会": return OPPORTUNITY_COLOR;
            case "市场": return MARKET_COLOR;
            case "命运": return FATE_COLOR;
            case "银行": return BANK_COLOR;
            default: return NORMAL_COLOR;
        }
    }
    
    // 设置玩家位置
    public void setPlayerPosition(int position) {
        this.playerPosition = position;
        repaint();
    }
    
    // 获取地图上地块总数
    public int getTileCount() {
        return tiles != null ? tiles.size() : 0;
    }
    
    // 内部类表示地图上的一个地块
    private static class Tile {
        int x, y;
        String type;
        
        Tile(int x, int y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
}