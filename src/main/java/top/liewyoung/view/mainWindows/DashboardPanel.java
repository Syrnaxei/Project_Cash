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
import org.atom.Player;
import top.liewyoung.strategy.asset.Asset;
import top.liewyoung.strategy.MapPostition;
import top.liewyoung.strategy.TitlesTypes;
import top.liewyoung.view.Stater;
import top.liewyoung.view.component.MDbutton;
import top.liewyoung.view.component.MDialog;
import top.liewyoung.view.tools.EventProcessor;

/**
 *
 * @author LiewYoung
 * @since 2025/12/14
 */


record FontSize(int title, int heavy, int normal) {
}

public class DashboardPanel extends JPanel {

    private final InfoPanel infoPanel;
    private final PropertyPanel propertyPanel;

    // Surface: 整个窗口的背景，稍微带一点点灰/绿的暖白
    private final Color MD_SURFACE = new Color(253, 253, 245);
    // PrimaryContainer: 概览卡片的背景色 (浅绿，但不是荧光绿)
    private final Color MD_PRIMARY_CONTAINER = new Color(216, 232, 203);
    // OnPrimaryContainer: 概览卡片里的文字颜色 (极深绿)
    private final Color MD_ON_PRIMARY_CONTAINER = new Color(16, 32, 5);
    // Outline: 边框颜色 (中性灰绿)
    private final Color MD_OUTLINE = new Color(116, 121, 109);
    // SurfaceVariant: 表格表头背景 (比 Surface 稍微深一点)
    private final Color MD_SURFACE_VARIANT = new Color(226, 227, 219);

    private final FontSize fontSize = new FontSize(16, 28, 14); // 数字字体稍微加大
    private final Font FONT_NORMAL = new Font(
            "微软雅黑",
            Font.PLAIN,
            fontSize.normal());
    private final Font FONT_TITLE = new Font(
            "微软雅黑",
            Font.BOLD,
            fontSize.title());

    private final Random dice = new Random();
    private final MapDraw map;
    private final MapPostition mapPostition = new MapPostition();
    private int playerPosition = 0;
    private Player currentPlayer;
    private EventProcessor eventProcessor;

    private static int lastDice = 0;
    private final JFrame mainframe;

    public DashboardPanel(MapDraw map,JFrame mainframe) {
        this.mainframe = mainframe;
        setPreferredSize(new Dimension(300, getHeight()));
        this.map = map;
        // 全局设置
        setLayout(new BorderLayout(0, 16)); // MD3 通常间距稍大，设为 16px
        setBorder(new EmptyBorder(16, 16, 16, 16));
        setBackground(MD_SURFACE); // 设置底色

        // 初始化默认玩家
        initializeDefaultPlayer();

        // 初始化子面板
        infoPanel = new InfoPanel();
        propertyPanel = new PropertyPanel();

        // 刷新玩家信息显示
        infoPanel.refreshData();

        // 底部按钮区域
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

    /**
     * 初始化默认玩家
     */
    private void initializeDefaultPlayer() {
        // 创建默认玩家（普通职员）
        Player defaultPlayer = new Player("普通职员", 1000, 2000, 1000);
        setCurrentPlayer(defaultPlayer);
    }

    public void diceEvent() {
        DashboardPanel.lastDice = dice.nextInt(1, 7);


        // 先触发骰子滚动动画
        map.rollDice(DashboardPanel.lastDice, () -> {
            // 动画完成后执行后续逻辑
            playerPosition += DashboardPanel.lastDice;
            playerPosition = playerPosition % 28;

            // 获取当前位置的格子类型
            TitlesTypes currentType = map.getType(
                    mapPostition.mapOrder.get(playerPosition).x(),
                    mapPostition.mapOrder.get(playerPosition).y());



            map.updatePlayerPosition(
                    mapPostition.mapOrder.get(playerPosition).x(),
                    mapPostition.mapOrder.get(playerPosition).y());



            // 触发事件处理
            if (eventProcessor != null && !gameOver(currentPlayer)) {
                currentPlayer.setCash(0);
                // 更新所有资产价值（每次骰子后）
                currentPlayer.getAssetManager().updateAllAssets(dice);
                eventProcessor.processEvent(currentType);
                // 更新玩家信息显示
                infoPanel.refreshData();
                // 刷新资产表格
                propertyPanel.refreshAssets();
            }
        });
    }

    public void updateStats(int newIncome, int newOutcome) {
        infoPanel.refreshData();
    }

    /**
     * 设置当前玩家并初始化事件处理器
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        this.eventProcessor = new EventProcessor(player);
        // 设置 UI 刷新回调
        this.eventProcessor.setUiRefreshCallback(() -> {
            infoPanel.refreshData();
            propertyPanel.refreshAssets();
        });
        // 设置资产添加回调
        player.getAssetManager().setOnAssetAdded(asset -> {
            propertyPanel.addAssetRow(asset);
        });
    }

    /**
     * 获取当前玩家
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * 获取事件处理器
     */
    public EventProcessor getEventProcessor() {
        return eventProcessor;
    }

    private JButton buttonFactory(String text) {
        JButton Button = new MDbutton(text);
        return Button;
    }

    /**
     * 游戏结束
     *
     * @param player
     */
    private boolean gameOver(Player player) {
        if(player.getCash() <= 0) {
            mainframe.dispose();
            MDialog dialog = new MDialog("游戏结束", "重新开始");
            dialog.setVisible(true);
            SwingUtilities.invokeLater(() -> {
                Stater.main(null);
            });
            return true;
        }
        return false;
    }

    /**
     * 顶部玩家信息面板
     */
    class InfoPanel extends JPanel {

        private final JLabel cashLabel;
        private final JLabel salaryLabel;
        private final JLabel expensesLabel;
        private final JLabel passiveIncomeLabel;
        private final JLabel cashflowLabel;

        public InfoPanel() {
            setLayout(new GridLayout(5, 1, 0, 5));
            setBackground(MD_PRIMARY_CONTAINER); // 容器色背景

            TitledBorder titledBorder = new TitledBorder(
                    new LineBorder(MD_PRIMARY_CONTAINER, 0), // 边框颜色与背景一致，隐藏线条
                    "玩家信息",
                    TitledBorder.CENTER,
                    TitledBorder.TOP,
                    FONT_TITLE,
                    MD_ON_PRIMARY_CONTAINER // 标题颜色
            );
            setBorder(
                    new CompoundBorder(
                            new EmptyBorder(10, 20, 15, 20),
                            titledBorder));

            cashLabel = createInfoLabel("现金: 0");
            salaryLabel = createInfoLabel("工资: 0");
            expensesLabel = createInfoLabel("月支出: 0");
            passiveIncomeLabel = createInfoLabel("被动收入: 0");
            cashflowLabel = createInfoLabel("现金流: 0");

            add(cashLabel);
            add(salaryLabel);
            add(expensesLabel);
            add(passiveIncomeLabel);
            add(cashflowLabel);
        }

        // 简单的圆角绘制，让 InfoPanel 看起来像个圆角卡片
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24); // 24px 圆角
        }



        private JLabel createInfoLabel(String text) {
            JLabel label = new JLabel(text);
            label.setForeground(MD_ON_PRIMARY_CONTAINER); // 深色文字对比浅色背景
            label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            label.setHorizontalAlignment(SwingConstants.LEFT);
            return label;
        }

        public void refreshData() {
            if (currentPlayer != null) {
                cashLabel.setText("现金: " + currentPlayer.getCash() + "元");
                salaryLabel.setText(
                        "工资: " + currentPlayer.getSalary() + "元");
                expensesLabel.setText(
                        "月支出: " + currentPlayer.getMonthlyExpenses() + "元");
                passiveIncomeLabel.setText(
                        "被动收入: " + currentPlayer.getPassiveIncome() + "元");
                cashflowLabel.setText(
                        "现金流: " + currentPlayer.calculateCashflow() + "元");
            }
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
                            new EmptyBorder(0, 0, 0, 0));

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table
                        .getColumnModel()
                        .getColumn(i)
                        .setCellRenderer(centerRenderer);
            }

            // 设置列宽，确保内容完整显示
            table.getColumnModel().getColumn(0).setPreferredWidth(120); // 资产名称
            table.getColumnModel().getColumn(1).setPreferredWidth(80); // 资产价值
            table.getColumnModel().getColumn(2).setPreferredWidth(60); // 贬值率
            table.getColumnModel().getColumn(3).setPreferredWidth(100); // 资产状态
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            scrollPane.getViewport().setBackground(MD_SURFACE); // 空白区域颜色

            add(scrollPane, BorderLayout.CENTER);
        }

        public void addProperty(
                String name,
                String value,
                String depreciation,
                String status) {
            tableModel.addRow(
                    new Object[] { name, value, depreciation, status });
        }

        /**
         * 添加资产行
         */
        public void addAssetRow(Asset asset) {
            tableModel.addRow(new Object[] {
                    asset.getType().getIcon() + " " + asset.getName(),
                    asset.getCurrentValue() + "元",
                    asset.getDepreciationRate(),
                    asset.getStatus()
            });
        }

        /**
         * 刷新所有资产数据
         */
        public void refreshAssets() {
            // 清空表格
            tableModel.setRowCount(0);
            // 重新添加所有资产
            if (currentPlayer != null) {
                for (Asset asset : currentPlayer.getAssetManager().getAssets()) {
                    addAssetRow(asset);
                }
            }
        }
    }

    public void addProperty(
            String name,
            String value,
            String depreciation,
            String status) {
        propertyPanel.addProperty(name, value, depreciation, status);
    }
}
