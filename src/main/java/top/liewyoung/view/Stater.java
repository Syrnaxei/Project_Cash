package top.liewyoung.view;

import top.liewyoung.view.mainWindows.DashboardPanel;
import top.liewyoung.view.mainWindows.MapDraw;

import javax.swing.*;
import java.awt.*;

public class Stater {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        MapDraw map = new MapDraw();
        map.setBackground(new Color(253, 253, 245));
        frame.add(map, BorderLayout.CENTER);
        frame.add(new DashboardPanel(), BorderLayout.EAST);
        frame.setBackground(new Color(253, 253, 245));
        frame.setSize(1000, 735);

        frame.setResizable(false);
        frame.setVisible(true);
    }
}
