package top.liewyoung.strategy;

import top.liewyoung.view.mainWindows.MapDraw;

import java.util.ArrayList;

public class MapPostition {
    private static final int MAP_WIDTH = 8;
    private static final int MAP_HEIGHT = 8;
    public ArrayList<Position> mapOrder = new ArrayList<>();

    public MapPostition(){

        for(int i = 0; i < MAP_WIDTH; i++) mapOrder.add(new Position(i, 0));
        for(int i = 1;i < MAP_HEIGHT-1;i++) mapOrder.add(new Position(MAP_WIDTH-1, i));
        for(int i = MAP_WIDTH-1;i >= 0;i--) mapOrder.add(new Position(i, MAP_HEIGHT-1));
        for(int i = MAP_HEIGHT-2;i > 0;i--) mapOrder.add(new Position(0, i));
    }

}
