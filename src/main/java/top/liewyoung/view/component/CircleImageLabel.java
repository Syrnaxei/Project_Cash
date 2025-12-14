package top.liewyoung.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;


/**
 * 圆形图片标签
 * @author LiewYoung
 * @since 2025/12/14
 */
public class CircleImageLabel extends JLabel {
    private Image image;
    private int diameter;

    public CircleImageLabel(ImageIcon imageIcon, int diameter) {
        this.image = imageIcon.getImage();
        this.diameter = diameter;
        // 设置组件的首选大小，确保布局管理器留出足够的空间
        setPreferredSize(new Dimension(diameter, diameter));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 创建 Graphics2D 副本以避免影响其他组件的绘制
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        Shape circleShape = new Ellipse2D.Float(0, 0, diameter, diameter);
        g2d.setClip(circleShape);

        g2d.drawImage(image, 0, 0, diameter, diameter, this);

        g2d.dispose();
    }
}