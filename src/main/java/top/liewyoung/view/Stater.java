package top.liewyoung.view;

import top.liewyoung.view.mainWindows.DashboardPanel;
import top.liewyoung.view.mainWindows.MapDraw;

import javax.swing.*;
import java.awt.*;


/**
 *
 * @author LiewYoung
 * @since 2025/12/19
 */
public class Stater {
    public static MapDraw map;
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("CashFlow");
        frame.setLayout(new BorderLayout());
        map = new MapDraw();
        map.setBackground(new Color(253, 253, 245));
        frame.add(map, BorderLayout.CENTER);
        frame.add(new DashboardPanel(map), BorderLayout.EAST);
        frame.setBackground(new Color(253, 253, 245));
        frame.setSize(1200, 835);
        frame.setLocation(300, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.setVisible(true);
    }
}
