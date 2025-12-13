package top.liewyoung.view.mainWindows;

import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.view.ColorSystem.MaterialPalette;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 我们进入全新的Mater Design时代
 *
 * @author LiewYoung
 * @since 2025/12/13
 */


public class MapDraw extends JPanel {
    //全局设置
    private static final int MAP_HEIGHT = 8;
    private static final int MAP_WIDTH = 8;
    private static final int MARGIN = 30;
    private static final int SPACING = 10;
    private static final int TILE_WIDTH = 70;
    private static final int TILE_HEIGHT = 70;

    //储存方块
    private List<Tile> tiles = new ArrayList<Tile>();


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // 开启抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(253, 253, 245));

        //计算方块
        calculateTiles();
        //绘制方块
        drawTiles(g);

        g2d.dispose();
    }

    public void calculateTiles() {
        Random r = new Random();
        for (int i = 0; i < MAP_WIDTH; i++) {
            int index;
            if (i == 1) {
                index = 1;
            } else {
                index = r.nextInt(1, 6);
            }
            TitlesTypes type = TitlesTypes.values()[index];
            Tile tile = new Tile(i, 0, type);
            tiles.add(tile);
        }

        for (int i = 1; i < MAP_HEIGHT - 1; i++) {
            int index = r.nextInt(1, 6);
            Tile tile = new Tile(0, i, TitlesTypes.values()[index]);
            tiles.add(tile);
        }

        for (int i = 1; i < MAP_HEIGHT - 1; i++) {
            int index = r.nextInt(1, 6);
            Tile tile = new Tile(MAP_WIDTH - 1, i, TitlesTypes.values()[index]);
            tiles.add(tile);
        }

        for (int i = 0; i < MAP_WIDTH; i++) {
            int index = r.nextInt(1, 6);
            Tile tile = new Tile(i, MAP_HEIGHT - 1, TitlesTypes.values()[index]);
            tiles.add(tile);
        }

    }


    public void drawTiles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        MaterialPalette palette = MaterialPalette.MOSS;

        for (Tile tile : tiles) {

            //遵守屏幕坐标系
            int x = MARGIN + tile.x * (SPACING + TILE_WIDTH);
            int y = MARGIN + tile.y * (SPACING + TILE_HEIGHT);

            g2d.setColor(getColor(tile.type));
            g2d.fillRoundRect(x, y, TILE_WIDTH, TILE_HEIGHT, 16, 10);


            String text = getText(tile.type);
            FontMetrics fm = g2d.getFontMetrics();
            int textWith = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            int textX = x + (TILE_WIDTH - textWith) / 2;
            int textY = y + (TILE_HEIGHT - textHeight) / 2 + textHeight;

            if(tile.type == TitlesTypes.FUNGAME){
                g2d.setColor(palette.onSurface());
            }else{
                g2d.setColor(palette.surface());
            }
            g2d.setFont(new Font("微软雅黑", Font.BOLD, 12));
            g2d.drawString(text, textX, textY);

        }
    }


    /**
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
     * <b>用于设置缤纷多彩的颜色</b>
     * @param type 枚举
     * @return 颜色
     */
    private Color getColor(TitlesTypes type) {
        switch (type) {
            case TitlesTypes.START:
                return new Color(46, 204, 113);
            case TitlesTypes.OPPORTUNITY:
                return new Color(192, 202, 51);
            case TitlesTypes.MARKET:
                return new Color(76, 175, 80);
            case TitlesTypes.FATE:
                return new Color(0, 137, 123);
            case TitlesTypes.BANK:
                return new Color(27, 94, 32);
            default:
                return new Color(105, 240, 174);
        }
    }

    /**
     * 未来保留的玩家类
     */
    class PlayerPosition {
        int x;
        int y;

        public PlayerPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Tile {
        int x;
        int y;
        TitlesTypes type;

        public Tile(int x, int y, TitlesTypes type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
}
