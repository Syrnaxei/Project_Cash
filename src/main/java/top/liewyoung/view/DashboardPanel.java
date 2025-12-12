package top.liewyoung.view;

import javax.swing.*;
import java.awt.*;

/**
 * 简单的仪表盘
 *
 * @author LiewYoung
 * @since 2025/12/10
 */

public class DashboardPanel extends JPanel {
    private final JPanel contentSheet;//内容面板

    private final JPanel headerContent;//表头
    private final JPanel perContent;//资产仪表板

    private final JPanel headerEvent;//事件表头
    private final JPanel eventContent;//事件面板
    private final JPanel dataPanel;
    private JPanel eventLog;
    private final JButton diceButton;

    private double income;//收入
    private double outcome;//支出
    private double depreciation;//折旧
    private double cost;//维护花费
    private int lastDiced;


    public DashboardPanel() {
        contentSheet = new JPanel(new GridLayout(2, 4, 5, 5));
        JLabel title = new JLabel("资产损益表");

        // 收入支出
        JLabel income = new JLabel("收入", SwingConstants.LEFT);
        JLabel incomeValue = new JLabel(String.valueOf(this.income));
        JLabel outcome = new JLabel("支出", SwingConstants.LEFT);
        JLabel outcomeValue = new JLabel(String.valueOf(this.outcome));


        contentSheet.add(title);

        //维护卡片布局整齐
        for (int i = 0; i < 3; i++) {
            contentSheet.add(new JLabel());
        }

        contentSheet.add(income);
        contentSheet.add(incomeValue);
        contentSheet.add(outcome);
        contentSheet.add(outcomeValue);


        headerContent = new JPanel(new GridLayout(1, 4, 5, 5));
        headerContent.add(new JLabel("资产"));
        headerContent.add(new JLabel("贬值"));
        headerContent.add(new JLabel("支出"));
        headerContent.add(new JLabel("状态"));
        headerContent.setOpaque(true);
        headerContent.setBackground(Color.LIGHT_GRAY);

        perContent = new JPanel(new GridLayout(2, 1, 5, 5));
        perContent.add(headerContent);

        dataPanel = new JPanel(new GridLayout(0, 1, 5, 5));


        JScrollPane scrollPane = new JScrollPane(dataPanel);
        perContent.add(scrollPane);

        headerContent.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        contentSheet.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        eventContent = new JPanel(new GridLayout(0, 1, 5, 5));

        headerEvent = new JPanel(new GridLayout(1, 1, 5, 5));
        headerEvent.add(new JLabel("事件日志"));

        eventLog = new JPanel(new GridLayout(0, 1, 5, 5));
        eventLog.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        eventLog.setBackground(Color.pink);
        JScrollPane eventLogScrollPane = new JScrollPane(eventLog);

        eventContent.add(headerEvent);
        eventContent.add(eventLogScrollPane);

        diceButton = new JButton("投骰子");
        diceButton.setSize(0,20);
        diceButton.addActionListener(e -> {
            int dice = (int) (Math.random() * 6 + 1);
            lastDiced = dice;
            JOptionPane.showMessageDialog(null, "投掷结果为：" + dice);
            insertEvent("投掷骰子，结果为：" + dice);
        });


        setLayout(new GridLayout(0, 1, 10, 5));
        add(contentSheet);
        add(perContent);
        add(eventContent);
        add(diceButton);

       setBorder(BorderFactory.createEmptyBorder(13, 13, 13, 13));
       setSize(500,getHeight());

    }

    public void insertProperty(String name, double depreciation, double outcome, String  status) {
        JPanel row = new JPanel(new GridLayout(1, 4, 5, 5));
        row.add(new JLabel(name));
        row.add(new JLabel(String.valueOf(depreciation)));
        row.add(new JLabel(String.valueOf(outcome)));
        row.add(new JLabel(status));

        dataPanel.add(row);
        dataPanel.revalidate();
        dataPanel.repaint();
    }

    public void insertEvent(String event) {
        JLabel label = new JLabel(event);
        eventLog.add(label);
        eventLog.revalidate();
        eventLog.repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new DashboardPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);

    }
}
