package top.liewyoung.view;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class MainGame {
    //基础元素
    private final GameMap map = new GameMap();
    private final Side side = new Side();

    //布局设定
    private final Color mainBack = new Color(200, 227, 227);
    private final Color sideBack = new Color(230, 230, 177);
    private final Color ReactColor = new Color(0, 91, 171);

    public MainGame() {
        JFrame frame = new JFrame("现金流");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setLocation(screenSize.width / 2 - 450, screenSize.height / 2 - 300);

        frame.setLayout(new BorderLayout());

        frame.add(map, BorderLayout.EAST);
        frame.add(side, BorderLayout.WEST);


        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /* 主面板 */
    class GameMap extends JPanel {
        private final int reactWidth = 50;
        private final int reactHeight = 50;
        private final int spaceWidth = 10;
        private final int spaceHeight = 10;
        private final int topSpacing = 35;
        private final int leftSpacing = 35;

        public GameMap() {
            setPreferredSize(new Dimension(600, 600)); // 给一点参考大小
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(mainBack);
            g.fillRect(0, 0, getWidth(), getHeight());


            g.setColor(ReactColor);
            for (int i = 0; i < 9; i++) {
                g.fillRect(i * (reactWidth + spaceWidth) + leftSpacing, topSpacing, reactWidth, reactHeight);
            }

            int num = (525 - topSpacing - reactHeight - spaceHeight) / 60;

            for (int i = 0; i < num; i++) {
                int y = i * (spaceHeight + reactHeight) + topSpacing + reactHeight + spaceHeight;
                g.fillRect(leftSpacing, y, reactWidth, reactHeight);
                g.fillRect(8 * (reactWidth + spaceWidth) + leftSpacing, y, reactWidth, reactHeight);
            }

            for (int i = 0; i < 9; i++) {
                g.fillRect(i * (reactWidth + spaceWidth) + leftSpacing, 515, reactWidth, reactHeight);
            }
        }
    }

    /* 黑色侧边栏 */
    class Side extends JPanel {
        public Side() {
            setPreferredSize(new Dimension(300, 600)); // 宽度 200，高度随便
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(sideBack);
            g.fillRect(0, 0, getWidth(), getHeight());

        }
    }

    public static void main(String[] args) {
        //函数式接口
        SwingUtilities.invokeLater(MainGame::new);

    }
}