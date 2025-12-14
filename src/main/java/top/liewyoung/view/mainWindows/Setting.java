package top.liewyoung.view.mainWindows;

import top.liewyoung.view.ColorSystem.MaterialPalette;
import top.liewyoung.view.Stater;
import top.liewyoung.view.component.CircleImageLabel;
import top.liewyoung.view.component.MDbutton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 关于界面
 *
 * @author LiewYoung
 * @since 2025/12/14
 */
public class Setting extends JPanel {
    private MaterialPalette palette = MaterialPalette.MOSS;

    public Setting() {
        setLayout(new GridBagLayout());
        setBackground(palette.surface());
        setPreferredSize(new Dimension(1000, 600));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setOpaque(false);

        JLabel title = new JLabel("关于");
        title.setFont(new Font("微软雅黑", Font.BOLD, 24));
        title.setForeground(palette.onSurface());
        title.setBackground(palette.surface());
        listPanel.add(title);

        ImageIcon icon = new ImageIcon("src/main/resources/Avstar.jpg");
        listPanel.add(cardFactory("LiewYoung", "<html>North China University of Water Resources and Electric Power<br><b>I just do almost all the Project<b><html>", icon));

        ImageIcon imageIcon = new ImageIcon("src/main/resources/Avstar_1.jpg");
        listPanel.add(cardFactory("刘瑞翔", "North China University of Water Resources and Electric Power", imageIcon));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton buttonRest = new MDbutton("重置地图");
        buttonRest.addActionListener(e -> {
            Stater.map.refreshMap();
        });
        buttonPanel.add(buttonRest);

        listPanel.add(buttonPanel);

        add(listPanel);
    }

    private JPanel cardFactory(String name, String desc, ImageIcon icon) {
        CircleImageLabel circleImageLabel = new CircleImageLabel(icon, 100);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(palette.surface());

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(desc);
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descLabel);

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 0));
        p.setBackground(palette.surface());
        p.setBorder(new EmptyBorder(20, 20, 20, 20));
        p.add(circleImageLabel);
        p.add(textPanel);

        return p;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("关于");

        frame.add(new Setting());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}