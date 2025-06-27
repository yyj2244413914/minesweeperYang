package minesweeper;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * 历史记录窗口
 * 包含标题框（置顶）、游戏历史记录区域（中部）、玩家最好成绩（底部）
 */
public class DocumentPanel extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // 组件
    private JTextArea historyTextArea;
    private JTextArea bestScoresTextArea;
    private JScrollPane historyScrollPane;
    
    // 数据文件路径
    private static final String HISTORY_FILE = "game_history.txt";
    private static final String BEST_SCORES_FILE = "best_scores.txt";
    
    private MineSweeperMain mainFrame;

    public DocumentPanel(MineSweeperMain mainFrame) {
        this.mainFrame = mainFrame;
        setTitle("游戏历史记录");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 创建标题面板（置顶）
        createTitlePanel(mainPanel);
        
        // 创建历史记录区域（中部）
        createHistoryPanel(mainPanel);
        
        // 创建底部按钮面板（包含最好成绩区域）
        createButtonPanel(mainPanel);
        
        setContentPane(mainPanel);
        
        // 加载历史数据
        loadHistoryData();
        loadBestScoresData();

        // 自动为所有按钮绑定音效
        SoundUtil.bindSoundToAllButtons(this.getContentPane());
    }
    
    /**
     * 创建标题面板（置顶）
     */
    private void createTitlePanel(JPanel mainPanel) {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(173, 216, 230)); // 浅蓝色背景
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 标题
        JLabel title = new JLabel("游戏历史 Game History", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(10));
        
        // 分隔线
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        separator.setForeground(Color.LIGHT_GRAY);
        titlePanel.add(separator);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
    }
    
    /**
     * 创建历史记录区域（中部）
     */
    private void createHistoryPanel(JPanel mainPanel) {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.BLUE, 2),
            "游戏历史记录",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 16),
            Color.BLUE
        ));
        
        historyTextArea = new JTextArea();
        historyTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        historyTextArea.setEditable(false);
        historyTextArea.setLineWrap(true);
        historyTextArea.setWrapStyleWord(true);
        historyTextArea.setBackground(new Color(248, 248, 255)); // 浅蓝白色背景
        
        historyScrollPane = new JScrollPane(historyTextArea);
        historyScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        historyScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);
        
        // 设置历史记录区域的高度
        historyPanel.setPreferredSize(new Dimension(0, 300));
        
        mainPanel.add(historyPanel, BorderLayout.CENTER);
    }
    
    /**
     * 创建底部按钮面板
     */
    private void createButtonPanel(JPanel mainPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton clearHistoryButton = new JButton("清空历史数据");
        JButton backButton = new JButton("返回主菜单");
        
        // 设置按钮样式
        clearHistoryButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        backButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        
        // 按钮事件
        clearHistoryButton.addActionListener(e -> {
            SoundUtil.playClickSound();
            int result = JOptionPane.showConfirmDialog(
                this,
                "确定要清空所有历史数据吗？（最好成绩保留）",
                "确认清空",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                clearHistoryData();
            }
        });
        
        backButton.addActionListener(e -> {
            SoundUtil.playClickSound();
            dispose();
            mainFrame.setVisible(true);
        });
        
        buttonPanel.add(clearHistoryButton);
        buttonPanel.add(backButton);
        
        // 创建底部面板，包含最好成绩和按钮
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // 重新创建最好成绩面板
        JPanel bestScoresPanel = new JPanel(new BorderLayout());
        bestScoresPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GREEN, 2),
            "玩家最好成绩",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("微软雅黑", Font.BOLD, 16),
            Color.GREEN
        ));
        
        // 创建最好成绩文本区域
        bestScoresTextArea = new JTextArea();
        bestScoresTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        bestScoresTextArea.setEditable(false);
        bestScoresTextArea.setLineWrap(true);
        bestScoresTextArea.setWrapStyleWord(true);
        bestScoresTextArea.setBackground(new Color(240, 255, 240)); // 浅绿色背景
        
        bestScoresPanel.add(bestScoresTextArea, BorderLayout.CENTER);
        bestScoresPanel.setPreferredSize(new Dimension(0, 130));
        
        bottomPanel.add(bestScoresPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 加载历史记录数据
     */
    private void loadHistoryData() {
        StringBuilder history = new StringBuilder();
        history.append("=== 游戏历史记录 ===\n\n");
        
        try {
            File file = new File(HISTORY_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    history.append(line).append("\n");
                }
                reader.close();
            } else {
                history.append("暂无游戏历史记录\n");
                history.append("开始游戏后，您的游戏记录将显示在这里\n");
            }
        } catch (IOException e) {
            history.append("读取历史记录失败: ").append(e.getMessage()).append("\n");
        }
        
        historyTextArea.setText(history.toString());
        historyTextArea.setCaretPosition(0); // 滚动到顶部
    }
    
    /**
     * 加载最好成绩数据
     */
    private void loadBestScoresData() {
        StringBuilder bestScores = new StringBuilder();
        bestScores.append("=== 玩家最好成绩 ===\n\n");
        
        Map<String, Integer> scores = new HashMap<>();
        scores.put("初级难度", -1);
        scores.put("中级难度", -1);
        scores.put("高级难度", -1);
        
        try {
            File file = new File(BEST_SCORES_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("难度")) {
                        String[] parts = line.split(":");
                        if (parts.length >= 2) {
                            String diff = parts[0].trim();
                            int score = Integer.parseInt(parts[1].trim().replaceAll("[^0-9]", ""));
                            scores.put(diff, score);
                        }
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            bestScores.append("读取最好成绩失败: ").append(e.getMessage()).append("\n");
        }
        
        // 显示三个难度的最好成绩
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String difficulty = entry.getKey();
            Integer score = entry.getValue();
            if (score == -1) {
                bestScores.append(difficulty).append(": 暂无记录\n");
            } else {
                bestScores.append(difficulty).append(": ").append(score).append("秒\n");
            }
        }
        
        bestScoresTextArea.setText(bestScores.toString());
    }
    
    /**
     * 清空历史记录数据
     */
    private void clearHistoryData() {
        try {
            File historyFile = new File(HISTORY_FILE);
            if (historyFile.exists()) {
                historyFile.delete();
            }
            loadHistoryData();
            JOptionPane.showMessageDialog(this, "历史数据已清空！（最好成绩未受影响）", "清空完成", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "清空历史数据失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 添加游戏记录
     */
    public static void addGameRecord(String difficulty, int time, boolean isWin) {
        try {
            File file = new File(HISTORY_FILE);
            FileWriter writer = new FileWriter(file, true); // 追加模式
            BufferedWriter bw = new BufferedWriter(writer);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = sdf.format(new Date());
            String result = isWin ? "胜利" : "失败";
            
            String record = String.format("[%s] %s难度 - 用时:%d秒 - %s\n", 
                timestamp, difficulty, time, result);
            
            bw.write(record);
            bw.close();
        } catch (IOException e) {
            System.err.println("保存游戏记录失败: " + e.getMessage());
        }
    }
    
    /**
     * 更新最好成绩
     */
    public static void updateBestScore(String difficulty, int time) {
        try {
            File file = new File(BEST_SCORES_FILE);
            Map<String, Integer> bestScores = new HashMap<>();
            
            // 读取现有最好成绩
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("难度")) {
                        String[] parts = line.split(":");
                        if (parts.length >= 2) {
                            String diff = parts[0].trim();
                            int score = Integer.parseInt(parts[1].trim().replaceAll("[^0-9]", ""));
                            bestScores.put(diff, score);
                        }
                    }
                }
                reader.close();
            }
            
            // 更新最好成绩
            String diffKey = difficulty + "难度";
            if (!bestScores.containsKey(diffKey) || time < bestScores.get(diffKey)) {
                bestScores.put(diffKey, time);
            }
            
            // 写入文件
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            
            bw.write("=== 玩家最好成绩 ===\n");
            for (Map.Entry<String, Integer> entry : bestScores.entrySet()) {
                bw.write(entry.getKey() + ": " + entry.getValue() + "秒\n");
            }
            
            bw.close();
        } catch (IOException e) {
            System.err.println("更新最好成绩失败: " + e.getMessage());
        }
    }
}
