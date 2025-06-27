package minesweeper;

import java.awt.*;
import javax.swing.*;
/**
 * 扫雷游戏的简单规则介绍：
 * 对单元格执行鼠标左键单击操作的功能是打开这个单元格；
 * 对单元格执行鼠标右键单击操作的功能是为这个单元格进行旗帜标记的添加或者删除；
 * 当所有的没有地雷的单元格被打开了，那么玩家就赢得了该轮游戏；
 * 如果有一个包含地雷的单元格被打开了，那么玩家就输掉了该轮游戏。
 */
public class MineSweeperMain extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    private JPanel coverPanel;
    private JPanel buttonPanel;

    // 背景图片路径
    private static final String BG_IMAGE_PATH = "minesweeper/Iverson1.jpg";

    // 自定义按钮区面板，绘制背景图片
    static class ButtonPanelWithBg extends JPanel {
        private final Image bg;
        public ButtonPanelWithBg(String imgPath) {
            bg = new ImageIcon(imgPath).getImage();
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public MineSweeperMain() {
        setTitle("扫雷游戏");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        // 创建封面面板
        coverPanel = new JPanel(new BorderLayout());

        // 标题和分隔线的容器
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        // 横向放置地雷图标和标题
        JPanel titleRow = new JPanel();
        titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
        titleRow.setOpaque(false);
        // 加载并缩放图片为70x50
        ImageIcon mineIcon = new ImageIcon("minesweeper/mine.png");
        Image img = mineIcon.getImage();
        Image scaledImg = img.getScaledInstance(70, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel leftMine = new JLabel(scaledIcon);
        JLabel rightMine = new JLabel(scaledIcon);
        JLabel title = new JLabel("扫雷 Minesweeper", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 25));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        // 艺术字
        JLabel artLabel = new JLabel(" X Iverson");
        artLabel.setFont(new Font("微软雅黑", Font.BOLD | Font.ITALIC, 16));
        artLabel.setForeground(new Color(60, 60, 180));
        // 横向拼接标题和艺术字
        JPanel titleTextPanel = new JPanel();
        titleTextPanel.setLayout(new BoxLayout(titleTextPanel, BoxLayout.X_AXIS));
        titleTextPanel.setOpaque(false);
        titleTextPanel.add(title);
        titleTextPanel.add(artLabel);
        titleRow.add(leftMine);
        titleRow.add(Box.createHorizontalStrut(10));
        titleRow.add(titleTextPanel);
        titleRow.add(Box.createHorizontalStrut(10));
        titleRow.add(rightMine);

        titlePanel.add(titleRow);
        titlePanel.add(Box.createVerticalStrut(5));
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(Color.LIGHT_GRAY);
        titlePanel.add(separator);

        coverPanel.add(titlePanel, BorderLayout.NORTH);

        // 使用自定义按钮区面板，分割线以下显示背景图片
        buttonPanel = new ButtonPanelWithBg(BG_IMAGE_PATH);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton btnStartGame = new JButton("开始游戏");
        JButton btnHistory = new JButton("历史记录");
        JButton btnSettings = new JButton("设置");
        Dimension btnSize = new Dimension(100, 40);
        btnStartGame.setPreferredSize(btnSize);
        btnStartGame.setMaximumSize(btnSize);
        btnHistory.setPreferredSize(btnSize);
        btnHistory.setMaximumSize(btnSize);
        btnSettings.setPreferredSize(btnSize);
        btnSettings.setMaximumSize(btnSize);
        btnStartGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(Box.createVerticalStrut(40));
        buttonPanel.add(btnStartGame);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnHistory);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnSettings);
        coverPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(coverPanel);
        setVisible(true);

        // 自动为所有按钮绑定音效
        SoundUtil.bindSoundToAllButtons(this.getContentPane());

        // 按钮事件
        btnStartGame.addActionListener(e -> {
            SoundUtil.playClickSound();
            new DifficultyFrame(this).setVisible(true);
        });

        btnHistory.addActionListener(e -> {
            SoundUtil.playClickSound();
            setVisible(false);
            new DocumentPanel(this).setVisible(true);
        });

        btnSettings.addActionListener(e -> {
            SoundUtil.playClickSound();
            setVisible(false);
            SettingPanel settingPanel = new SettingPanel(this);
            settingPanel.setVisible(true);
        });
    }

    public void showCoverPanel() {
        setContentPane(coverPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MineSweeperMain::new);
    }
}