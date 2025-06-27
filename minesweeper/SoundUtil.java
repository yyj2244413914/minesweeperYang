package minesweeper;

import java.awt.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class SoundUtil {
    public static boolean soundEnabled = true;
    public static float volume = 0.6f; // 0~1
    public static void playClickSound() {
        if (!soundEnabled) return;
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                SoundUtil.class.getResource("/minesweeper/音效.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("无法播放音效: " + e.getMessage());
        }
    }

    // 递归为所有按钮绑定音效
    public static void bindSoundToAllButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).addActionListener(e -> SoundUtil.playClickSound());
            } else if (comp instanceof Container) {
                bindSoundToAllButtons((Container) comp);
            }
        }
    }
} 