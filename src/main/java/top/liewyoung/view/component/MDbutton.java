package top.liewyoung.view.component;

import com.formdev.flatlaf.FlatClientProperties;
import top.liewyoung.view.ColorSystem.MaterialPalette;

import javax.swing.*;
import java.awt.*;


/**
 * Material Design 按钮
 * @author LiewYoung
 * @since 2025/12/14
 */
public class MDbutton extends JButton {
    private MaterialPalette palette = MaterialPalette.MOSS;

    public MDbutton(String text) {
        super(text);
        setPreferredSize(new Dimension(100, 40));
        putClientProperty(FlatClientProperties.STYLE,"arc: 40");
        setBorderPainted(false);
        setBackground(palette.primary());
        setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 14f));
        setForeground(palette.onPrimary());
    }
}
