package top.liewyoung.view.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import top.liewyoung.view.ColorSystem.MaterialPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * Material Design 3 风格对话框
 * @author LiewYoung
 * @since 2025/12/14
 */
public class MDialog extends JDialog {
    private MaterialPalette palette = MaterialPalette.MOSS;

    public MDialog(String content, String closeButtonText) {
        setBackground(palette.surface());
        setLayout(new BorderLayout());
        setSize(new Dimension(400, 200));

        Container contentPanel = getContentPane();
        contentPanel.setBackground(palette.surface());


        JPanel textPanel = new JPanel(new FlowLayout());
        textPanel.setBackground(palette.surface());
        textPanel.add(contentLabelFactory(content));
        textPanel.setBorder(new EmptyBorder(10, 10, 10, 10));


        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(palette.surface());
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        buttonPanel.add(buttonFactory(closeButtonText));

        add(textPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);


    }

    protected JButton buttonFactory(String text) {
        JButton button = new MDbutton(text);
        button.addActionListener(e -> dispose());


        return button;
    }



    // 内容标签
    protected JLabel contentLabelFactory(String content) {
        JLabel label = new JLabel(content);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        label.setForeground(palette.onSurface());

        return label;
    }


    public static void main(String[] args) {
        FlatLightLaf.setup();
        JFrame frame = new JFrame("MD3 Dialog");
        JButton button = new JButton("Open Dialog");
        button.addActionListener(e -> {
            MDialog dialog = new MDialog("Hello Java", "关闭");
            dialog.setVisible(true);
        });
        frame.add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
