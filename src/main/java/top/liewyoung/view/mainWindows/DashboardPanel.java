package top.liewyoung.view.mainWindows;

import java.awt.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import top.liewyoung.strategy.MapPostition;
import top.liewyoung.view.component.MDbutton;
import top.liewyoung.view.component.MDialog;

/**
 * @author LiewYoung
 * @since 2025/12/14
 */
// 简单的字体记录类
record FontSize(int title, int heavy, int normal) {}

public class DashboardPanel extends JPanel {

    private final InfoPanel infoPanel;
    private final PropertyPanel propertyPanel;

    // 数据变量
    private int income = 0;
    private int outcome = 0;

    // Surface: 整个窗口的背景，稍微带一点点灰/绿的暖白
    private final Color MD_SURFACE = new Color(253, 253, 245);
    // Primary: 最主要的按钮、强调色 (深苔藓绿)
    private final Color MD_PRIMARY = new Color(56, 106, 32);
    // OnPrimary: Primary 之上的文字颜色 (白色)
    private final Color MD_ON_PRIMARY = new Color(255, 255, 255);
    // PrimaryContainer: 概览卡片的背景色 (浅绿，但不是荧光绿)
    private final Color MD_PRIMARY_CONTAINER = new Color(216, 232, 203);
    // OnPrimaryContainer: 概览卡片里的文字颜色 (极深绿)
    private final Color MD_ON_PRIMARY_CONTAINER = new Color(16, 32, 5);
    // Outline: 边框颜色 (中性灰绿)
    private final Color MD_OUTLINE = new Color(116, 121, 109);
    // SurfaceVariant: 表格表头背景 (比 Surface 稍微深一点)
    private final Color MD_SURFACE_VARIANT = new Color(226, 227, 219);

    private final FontSize fontSize = new FontSize(16, 28, 14); // 数字字体稍微加大
    private final Font FONT_BOLD = new Font(
        "微软雅黑",
        Font.BOLD,
        fontSize.heavy()
    );
    private final Font FONT_NORMAL = new Font(
        "微软雅黑",
        Font.PLAIN,
        fontSize.normal()
    );
    private final Font FONT_TITLE = new Font(
        "微软雅黑",
        Font.BOLD,
        fontSize.title()
    );

    private final Random dice = new Random();
    private final MapDraw map;
    private final MapPostition mapPostition = new MapPostition();
    private int playerPosition = 0;

    private static int lastDice = 0;

    public DashboardPanel(MapDraw map) {
        setPreferredSize(new Dimension(300, getHeight()));
        this.map = map;
        // 全局设置
        setLayout(new BorderLayout(0, 16)); // MD3 通常间距稍大，设为 16px
        setBorder(new EmptyBorder(16, 16, 16, 16));
        setBackground(MD_SURFACE); // 设置底色

        //  初始化子面板
        infoPanel = new InfoPanel();
        propertyPanel = new PropertyPanel();

        //  底部按钮区域
        JButton diceButton = buttonFactory("摇骰子");
        diceButton.addActionListener(e -> {
            diceEvent();
        });

        JButton helpButton = buttonFactory("关于");
        helpButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                JFrame frame = new JFrame("关于");
                frame.add(new Setting());
                frame.setLocation(300, 100);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            });
        });

        // 为了让按钮不被拉伸，放进一个 FlowLayout 的 Panel
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.setBackground(MD_SURFACE); // 与背景融合
        buttonContainer.add(diceButton);
        buttonContainer.add(helpButton);

        // 组装
        add(infoPanel, BorderLayout.NORTH);
        add(propertyPanel, BorderLayout.CENTER);
        add(buttonContainer, BorderLayout.SOUTH);
    }

    public void diceEvent() {
        DashboardPanel.lastDice = dice.nextInt(1, 7);

        // 先触发骰子滚动动画
        map.rollDice(DashboardPanel.lastDice, () -> {
            // 动画完成后执行后续逻辑
            playerPosition += DashboardPanel.lastDice;
            playerPosition = playerPosition % 28;
            String type = map
                .getType(
                    mapPostition.mapOrder.get(playerPosition).x(),
                    mapPostition.mapOrder.get(playerPosition).y()
                )
                .name();
            MDialog dialog = new MDialog(
                "你摇出了 " + DashboardPanel.lastDice + " 类型：" + type,
                "我知道了"
            );

            map.updatePlayerPosition(
                mapPostition.mapOrder.get(playerPosition).x(),
                mapPostition.mapOrder.get(playerPosition).y()
            );

            dialog.setLocationRelativeTo(this);
            dialog.setAlwaysOnTop(true);
            dialog.setModal(true);
            dialog.setVisible(true);
        });
    }

    public void updateStats(int newIncome, int newOutcome) {
        this.income = newIncome;
        this.outcome = newOutcome;
        infoPanel.refreshData();
    }

    private JButton buttonFactory(String text) {
        JButton Button = new MDbutton(text);
        return Button;
    }

    /**
     * 顶部收支概览面板
     */
    class InfoPanel extends JPanel {

        private final JLabel incomeDataLabel;
        private final JLabel outcomeDataLabel;

        public InfoPanel() {
            setLayout(new GridLayout(1, 2, 0, 0));
            setBackground(MD_PRIMARY_CONTAINER); // 容器色背景

            TitledBorder titledBorder = new TitledBorder(
                new LineBorder(MD_PRIMARY_CONTAINER, 0), // 边框颜色与背景一致，隐藏线条
                "收支概览",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                FONT_TITLE,
                MD_ON_PRIMARY_CONTAINER // 标题颜色
            );
            setBorder(
                new CompoundBorder(
                    new EmptyBorder(10, 20, 15, 20),
                    titledBorder
                )
            );

            incomeDataLabel = createDataLabel(income);
            outcomeDataLabel = createDataLabel(outcome);

            add(createItemPanel("收入", incomeDataLabel));
            add(createItemPanel("支出", outcomeDataLabel));
        }

        // 简单的圆角绘制，让 InfoPanel 看起来像个圆角卡片
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24); // 24px 圆角
        }

        private JLabel createDataLabel(int value) {
            JLabel label = new JLabel(String.valueOf(value));
            label.setForeground(MD_ON_PRIMARY_CONTAINER); // 深色文字对比浅色背景
            label.setFont(FONT_BOLD);
            return label;
        }

        private JPanel createItemPanel(String title, JLabel dataLabel) {
            JPanel p = new JPanel(new BorderLayout());
            p.setOpaque(false);

            JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
            titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            titleLabel.setForeground(MD_ON_PRIMARY_CONTAINER.brighter()); // 标题稍微淡一点

            dataLabel.setHorizontalAlignment(SwingConstants.CENTER);

            p.add(titleLabel, BorderLayout.NORTH);
            p.add(dataLabel, BorderLayout.CENTER);
            return p;
        }

        public void refreshData() {
            incomeDataLabel.setText(String.valueOf(income));
            outcomeDataLabel.setText(String.valueOf(outcome));
        }
    }

    /**
     * 资产列表面板 - 风格：Clean Data Table
     */
    class PropertyPanel extends JPanel {

        private final DefaultTableModel tableModel;
        private final JTable table;

        public PropertyPanel() {
            setLayout(new BorderLayout());
            setBackground(MD_SURFACE); // 背景与主背景一致

            // 细边框包裹表格
            setBorder(new LineBorder(MD_OUTLINE, 1, true));

            String[] columnNames = {
                "资产名称",
                "资产价值",
                "贬值率",
                "资产状态",
            };

            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            table = new JTable(tableModel);

            table.setRowHeight(40); // 更舒适的行高
            table.setFont(FONT_NORMAL);
            table.setForeground(new Color(28, 27, 31)); // 接近黑色的深灰
            table.setGridColor(MD_SURFACE_VARIANT); // 很淡的分割线
            table.setShowVerticalLines(false); // MD3 通常不显示竖向网格线
            table.setShowHorizontalLines(true);
            table.setSelectionBackground(MD_PRIMARY_CONTAINER); // 选中背景呼应顶部卡片
            table.setSelectionForeground(MD_ON_PRIMARY_CONTAINER);

            // 表头样式
            JTableHeader header = table.getTableHeader();
            header.setFont(new Font("微软雅黑", Font.BOLD, 13));
            header.setBackground(MD_PRIMARY_CONTAINER); // 区别于内容的背景色
            header.setForeground(MD_ON_PRIMARY_CONTAINER);
            header.setPreferredSize(new Dimension(0, 40));
            // 去除表头默认凸起边框
            ((JComponent) table
                    .getTableHeader()
                    .getDefaultRenderer()).setBorder(
                new EmptyBorder(0, 0, 0, 0)
            );

            DefaultTableCellRenderer centerRenderer =
                new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table
                    .getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(centerRenderer);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            scrollPane.getViewport().setBackground(MD_SURFACE); // 空白区域颜色

            add(scrollPane, BorderLayout.CENTER);
        }

        public void addProperty(
            String name,
            String value,
            String depreciation,
            String status
        ) {
            tableModel.addRow(
                new Object[] { name, value, depreciation, status }
            );
        }
    }

    public void addProperty(
        String name,
        String value,
        String depreciation,
        String status
    ) {
        propertyPanel.addProperty(name, value, depreciation, status);
    }
}
