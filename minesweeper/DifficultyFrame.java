package minesweeper;

import java.awt.*;
import javax.swing.*;
/**
 难度选择面板，基本可以套main程序的代码框架。
 */
public class DifficultyFrame extends JFrame {
    private static final long serialVersionUID = 1L;  // to prevent serial warning
    private JPanel coverPanel;
    private JPanel buttonPanel;

    // 背景图片路径
    private static final String BG_IMAGE_PATH2 = "C:/Users/31996/OneDrive/桌面/minesweeper/minesweeper/难度选择背景.png";

    // 自定义按钮区面板，绘制背景图片
    static class ButtonPanelWithBg2 extends JPanel {
        private final Image bg;
        public ButtonPanelWithBg2(String imgPath) {
            bg = new ImageIcon(imgPath).getImage();
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public DifficultyFrame() {
        setTitle("难度选择");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);

        // 创建封面面板
        coverPanel = new JPanel(new BorderLayout());

        // 标题和分隔线的容器
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(173, 216, 230)); // 浅蓝色

        // 横向放置图标和标题
        JPanel titleRow = new JPanel();
        titleRow.setLayout(new BoxLayout(titleRow, BoxLayout.X_AXIS));
        titleRow.setOpaque(false);
        // 加载并缩放图片为70x50
        ImageIcon StarIconLeft = new ImageIcon("C:/Users/31996/OneDrive/桌面/minesweeper/minesweeper/星星左.png");
        Image imgleft = StarIconLeft.getImage();
        Image scaledImgLeft = imgleft.getScaledInstance(70, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIconLeft = new ImageIcon(scaledImgLeft);
        JLabel leftstar = new JLabel(scaledIconLeft);
        ImageIcon StarIconRight = new ImageIcon("C:/Users/31996/OneDrive/桌面/minesweeper/minesweeper/星星右.png");
        Image imgRight = StarIconRight.getImage();
        Image scaledImgRight = imgRight.getScaledInstance(70, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIconRight = new ImageIcon(scaledImgRight);
        JLabel rightstar = new JLabel(scaledIconRight);
        JLabel title = new JLabel("难度Difficulty选择", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleRow.add(leftstar);
        titleRow.add(Box.createHorizontalStrut(10));
        titleRow.add(title);
        titleRow.add(Box.createHorizontalStrut(10));
        titleRow.add(rightstar);

        titlePanel.add(titleRow);
        titlePanel.add(Box.createVerticalStrut(5));
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setForeground(Color.LIGHT_GRAY);
        titlePanel.add(separator);

        coverPanel.add(titlePanel, BorderLayout.NORTH);

        // 使用自定义按钮区面板，分割线以下显示背景图片
        buttonPanel = new ButtonPanelWithBg2(BG_IMAGE_PATH2);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton btnEasyGame = new JButton("初级");
        JButton btnMediumGame = new JButton("中级");
        JButton btnHardGame = new JButton("高级");
        Dimension btnSize = new Dimension(100, 40);
        btnEasyGame.setPreferredSize(btnSize);
        btnEasyGame.setMaximumSize(btnSize);
        btnMediumGame.setPreferredSize(btnSize);
        btnMediumGame.setMaximumSize(btnSize);
        btnHardGame.setPreferredSize(btnSize);
        btnHardGame.setMaximumSize(btnSize);
        btnEasyGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMediumGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHardGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(Box.createVerticalStrut(40));
        buttonPanel.add(btnEasyGame);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnMediumGame);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnHardGame);
        coverPanel.add(buttonPanel, BorderLayout.CENTER);

        setContentPane(coverPanel);
        setVisible(true);

        // 按钮事件
        btnEasyGame.addActionListener(e -> {
            // 创建游戏棋盘窗口
            JFrame gameFrame = new JFrame("扫雷游戏 - 初级");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.setSize(600, 500);
            gameFrame.setLocationRelativeTo(null);
            
            // 创建游戏面板
            GameBoardEasyPanel gamePanel = new GameBoardEasyPanel();
            gameFrame.add(gamePanel);
            
            // 初始化新游戏
            gamePanel.newGame();
            
            // 显示游戏窗口
            gameFrame.setVisible(true);
            
            // 关闭难度选择窗口
            dispose();
        });

        btnMediumGame.addActionListener(e -> {
            // 创建游戏棋盘窗口
            JFrame gameFrame = new JFrame("扫雷游戏 - 中级");
            gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            gameFrame.setLocationRelativeTo(null);
            
            // 创建游戏面板
            GameBoardMediumPanel gamePanel = new GameBoardMediumPanel();
            gameFrame.add(gamePanel);
            
            // 初始化新游戏
            gamePanel.newGame();
            
            // 自动适应内容大小
            gameFrame.pack();
            gameFrame.setVisible(true);
            
            // 关闭难度选择窗口
            dispose();
        });
    }
}