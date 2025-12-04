package top.liewyoung.view;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

class MyFrame extends JFrame {
    JLabel label;
    public MyFrame() {
        super("Hello World");
        label = new JLabel("Hello World");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        super.add(label);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setResizable(false);
        setVisible(true);
    }
}

public class Runner {
    static MyFrame frame;
    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame = new MyFrame();
            }
        });

        TimeUnit.SECONDS.sleep(1);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.label.setText("Hello Liewyoung");
            }
        });
    }
}
