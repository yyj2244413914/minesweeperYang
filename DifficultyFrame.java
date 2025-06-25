package minesweeper;

import java.awt.*;
import javax.swing.*;

public class DifficultyFrame extends JFrame {
    public DifficultyFrame(MineSweeperMain mainFrame) {
        setTitle("选择难度");
        setSize(300, 300);
        setLocationRelativeTo(mainFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton btnEasy = new JButton("初级");
        JButton btnMedium = new JButton("中级");
        JButton btnHard = new JButton("高级");
        JButton btnBack = new JButton("返回");

        Dimension btnSize = new Dimension(200, 40);
        btnEasy.setMaximumSize(btnSize);
        btnMedium.setMaximumSize(btnSize);
        btnHard.setMaximumSize(btnSize);
        btnBack.setMaximumSize(btnSize);

        btnEasy.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMedium.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHard.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(30));
        panel.add(btnEasy);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnMedium);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnHard);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnBack);

        add(panel);

        // 按钮事件
        btnEasy.addActionListener(e -> {
            MineSweeperConstants.setEasy();
            mainFrame.startGame();
            dispose();
        });
        btnMedium.addActionListener(e -> {
            MineSweeperConstants.setMedium();
            mainFrame.startGame();
            dispose();
        });
        btnHard.addActionListener(e -> {
            MineSweeperConstants.setHard();
            mainFrame.startGame();
            dispose();
        });
        btnBack.addActionListener(e -> dispose());
    }
} 