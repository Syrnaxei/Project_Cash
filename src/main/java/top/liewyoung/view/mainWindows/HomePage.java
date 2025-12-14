package top.liewyoung.view.mainWindows;

import top.liewyoung.view.ColorSystem.MaterialPalette;
import top.liewyoung.view.Stater;
import top.liewyoung.view.component.MDbutton;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;

/**
 * 开始界面
 * @author LiewYoung
 * @since 2025/12/14
 */
public class HomePage extends JFrame {
    private MaterialPalette palette = MaterialPalette.MOSS;

    public HomePage() {
        setTitle("CashFlow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 850);
        setLocation(300, 100);
        setVisible(true);
        setBackground(palette.surface());

        Container container = getContentPane();
        container.setBackground(palette.surface());

        setLayout(new GridBagLayout());
        JLabel title = new JLabel("CashFlow");
        title.setFont(new Font("微软雅黑", Font.BOLD, 36));
        title.setForeground(palette.onSurface());

        MDbutton button = new MDbutton("开始游戏");
        button.addActionListener(e -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Stater.main(null);
                }
            });

            dispose();
        });

        JPanel textWithButton = new JPanel(new FlowLayout(FlowLayout.CENTER,25,15));
        textWithButton.setBackground(palette.surface());
        textWithButton.add(title);
        textWithButton.add(button);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(palette.surface());

        JLabel explanation = new JLabel("CashFlow是一款基于JavaSwing的桌面游戏，游戏内容由两名作者开发，游戏内容基于《Cash Flow》游戏。");
        explanation.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        explanation.setForeground(palette.onSurface());

        listPanel.add(textWithButton);
        listPanel.add(explanation);

        add(listPanel);
    }

    public static void main(String[] args) {
        new HomePage();
    }
}
