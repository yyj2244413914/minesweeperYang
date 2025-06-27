package minesweeper;

import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingPanel extends JFrame {
    private MineSweeperMain mainFrame;
    public SettingPanel(MineSweeperMain mainFrame) {
        this.mainFrame = mainFrame;
        setTitle("设置");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton btnBack = new JButton("返回");
        btnBack.setFont(new java.awt.Font("微软雅黑", java.awt.Font.BOLD, 16));
        btnBack.addActionListener(e -> {
            SoundUtil.playClickSound();
            dispose();
            if (mainFrame != null) {
                mainFrame.setVisible(true);
                mainFrame.showCoverPanel();
            }
        });
        // 修改底部面板，支持图片
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(btnBack);
        bottomPanel.add(btnPanel, BorderLayout.NORTH);
        // Iverson4图片面板
        IversonImagePanel iversonPanel = new IversonImagePanel();
        bottomPanel.add(iversonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // 中部设置区
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 30, 0, 30));
        centerPanel.setOpaque(false);

        // 音效开关
        JCheckBox soundCheckBox = new JCheckBox("开启音效");
        soundCheckBox.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 16));
        soundCheckBox.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(soundCheckBox);
        centerPanel.add(Box.createVerticalStrut(20));
        soundCheckBox.addActionListener(e -> SoundUtil.playClickSound());

        // 音量调节
        JPanel volumePanel = new JPanel();
        volumePanel.setOpaque(false);
        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.X_AXIS));
        JLabel volumeLabel = new JLabel("音量：");
        volumeLabel.setFont(new java.awt.Font("微软雅黑", java.awt.Font.PLAIN, 16));
        JSlider volumeSlider = new JSlider(0, 100, 60);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setPreferredSize(new java.awt.Dimension(200, 50));
        volumePanel.add(volumeLabel);
        volumePanel.add(volumeSlider);
        centerPanel.add(volumePanel);
        centerPanel.add(Box.createVerticalGlue());
        add(centerPanel, BorderLayout.CENTER);

        // 音量滑块监听，调节图片亮度
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = volumeSlider.getValue();
                iversonPanel.setBrightness(value / 100f);
                SoundUtil.volume = value / 100f;
            }
        });
        // 初始亮度和音量
        float initialVolume = volumeSlider.getValue() / 100f;
        iversonPanel.setBrightness(initialVolume);
        SoundUtil.volume = initialVolume;

        // 音效开关联动滑块
        soundCheckBox.addActionListener(e -> {
            boolean enabled = soundCheckBox.isSelected();
            volumeSlider.setEnabled(enabled);
            SoundUtil.soundEnabled = enabled;
            if (!enabled) {
                SoundUtil.volume = 0f;
            } else {
                SoundUtil.volume = volumeSlider.getValue() / 100f;
            }
        });
        // 初始化滑块可用性
        volumeSlider.setEnabled(soundCheckBox.isSelected());

        // 自动为所有按钮绑定音效
        SoundUtil.bindSoundToAllButtons(this.getContentPane());
    }

    // 底部图片面板，支持亮度调节
    static class IversonImagePanel extends JPanel {
        private Image img;
        private float brightness = 0.6f;
        public IversonImagePanel() {
            try {
                img = new ImageIcon("minesweeper/Iverson4.png").getImage();
            } catch (Exception e) {
                img = null;
            }
            setPreferredSize(new Dimension(120, 170));
            setOpaque(false);
        }
        public void setBrightness(float b) {
            brightness = Math.max(0f, Math.min(1f, b));
            repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                int w = getWidth(), h = getHeight();
                g2d.drawImage(img, 0, 0, w, h, this);
                // 亮度蒙版
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f - brightness));
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        }
    }
}
