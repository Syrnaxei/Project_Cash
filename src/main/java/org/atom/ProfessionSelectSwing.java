package org.atom;

import javax.swing.*;
import java.awt.*;

// 职业实体类
class Profession {
    private String name;        // 职业名称
    private int initialCash;    // 初始现金
    private int salary;         // 月薪
    private int monthlyExpenses;// 月支出

    public Profession(String name, int initialCash, int salary, int monthlyExpenses) {
        this.name = name;
        this.initialCash = initialCash;
        this.salary = salary;
        this.monthlyExpenses = monthlyExpenses;
    }

    // Getter 方法
    public String getName() { return name; }
    public int getInitialCash() { return initialCash; }
    public int getSalary() { return salary; }
    public int getMonthlyExpenses() { return monthlyExpenses; }

    // 重写 toString，用于下拉框显示职业名称
    @Override
    public String toString() {
        return name;
    }
}

// 选职业主界面
public class ProfessionSelectSwing extends JFrame {
    // 定义职业列表
    private final Profession[] professions = {
            new Profession("程序员", 5000, 20000, 8000),
            new Profession("教师", 3000, 12000, 5000),
            new Profession("医生", 8000, 30000, 10000),
            new Profession("创业者", 1000, 50000, 15000),
            new Profession("自由职业者", 2000, 15000, 4000)
    };

    private JComboBox<Profession> professionComboBox;
    private JTextArea infoTextArea;

    public ProfessionSelectSwing() {
        // 窗口基本设置
        setTitle("选择你的职业");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中
        setResizable(false); // 禁止调整窗口大小
        setLayout(new BorderLayout(10, 10)); // 边框布局，带间距

        // 初始化组件并添加到窗口
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    // 顶部面板：标题和下拉框
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel titleLabel = new JLabel("请选择你的职业：");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 16));

        // 职业下拉框
        professionComboBox = new JComboBox<>(professions);
        professionComboBox.setFont(new Font("宋体", Font.PLAIN, 14));
        professionComboBox.setPreferredSize(new Dimension(200, 30));

        // 下拉框选择事件
        professionComboBox.addActionListener(e -> showProfessionInfo());

        topPanel.add(titleLabel);
        topPanel.add(professionComboBox);
        return topPanel;
    }

    // 中间面板：职业信息展示
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));

        JLabel infoLabel = new JLabel("职业详情：");
        infoLabel.setFont(new Font("宋体", Font.BOLD, 14));

        // 职业信息文本域
        infoTextArea = new JTextArea();
        infoTextArea.setFont(new Font("宋体", Font.PLAIN, 14));
        infoTextArea.setEditable(false); // 禁止编辑
        infoTextArea.setLineWrap(true); // 自动换行
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        scrollPane.setPreferredSize(new Dimension(500, 150));

        centerPanel.add(infoLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 初始化显示第一个职业的信息
        showProfessionInfo();

        return centerPanel;
    }

    // 底部面板：确认按钮
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton confirmButton = new JButton("确认选择");
        confirmButton.setFont(new Font("宋体", Font.BOLD, 14));
        confirmButton.setPreferredSize(new Dimension(120, 35));

        // 确认按钮点击事件
        confirmButton.addActionListener(e -> {
            Profession selected = (Profession) professionComboBox.getSelectedItem();
            JOptionPane.showMessageDialog(
                    this,
                    "你选择了【" + selected.getName() + "】职业！\n" +
                            "初始现金：" + selected.getInitialCash() + "元\n" +
                            "月薪：" + selected.getSalary() + "元\n" +
                            "月支出：" + selected.getMonthlyExpenses() + "元",
                    "选择成功",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // 这里可以添加后续逻辑（如进入游戏主界面）
        });

        bottomPanel.add(confirmButton);
        return bottomPanel;
    }

    // 显示选中职业的信息
    private void showProfessionInfo() {
        Profession selected = (Profession) professionComboBox.getSelectedItem();
        if (selected != null) {
            String info = "职业名称：" + selected.getName() + "\n" +
                    "初始现金：" + selected.getInitialCash() + " 元\n" +
                    "月工资：" + selected.getSalary() + " 元\n" +
                    "月支出：" + selected.getMonthlyExpenses() + " 元\n" +
                    "月现金流：" + (selected.getSalary() - selected.getMonthlyExpenses()) + " 元";
            infoTextArea.setText(info);
        }
    }

    // 主方法：运行程序
    public static void main(String[] args) {
        // Swing 组件需要在事件调度线程中运行
        SwingUtilities.invokeLater(() -> {
            new ProfessionSelectSwing().setVisible(true);
        });
    }
}
