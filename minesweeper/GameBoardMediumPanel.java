package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardMediumPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // 定义每一个单元格的大小（单位为像素）
    public static final int CELL_SIZE = 60;
    public static final int MEDIUM_ROWS = 16;
    public static final int MEDIUM_COLS = 16;
    public static final int MEDIUM_MINES = 40;
    public static final int CANVAS_WIDTH  = CELL_SIZE * MEDIUM_COLS; // Game board width/height
    public static final int CANVAS_HEIGHT = CELL_SIZE * MEDIUM_ROWS;
    
    // 背景图片路径
    private static final String BG_IMAGE_PATH = "minesweeper/Iverson2.jpg";
    private Image backgroundImage;

    // 顶部信息区
    private JLabel timerLabel = new JLabel("时间: 0s");
    private BasketballProgressPanel progressPanel = new BasketballProgressPanel(0, 100, "minesweeper/basketball.png");
    private JLabel mineCountLabel = new JLabel("标记: 0/0");
    private int markedMines = 0;
    private int realMines = MEDIUM_MINES;
    private Timer timer;
    private int seconds = 0;

    /** 游戏的整个界面面板应该包含的单元格数量是：ROWS*COLS */
    private int rows = MEDIUM_ROWS;
    private int cols = MEDIUM_COLS;
    private int numMines = MEDIUM_MINES;
    Cell cells[][] = new Cell[rows][cols];

    private MineSweeperMain mainFrame;

    public GameBoardMediumPanel(MineSweeperMain mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 顶部信息面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        timerLabel.setPreferredSize(new Dimension(100, 30));
        progressPanel.setPreferredSize(new Dimension(200, 30));
        mineCountLabel.setPreferredSize(new Dimension(150, 30));
        JButton menuButton = new JButton("菜单");
        topPanel.add(timerLabel);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(progressPanel);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(mineCountLabel);
        topPanel.add(menuButton);
        add(topPanel, BorderLayout.NORTH);

        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        topPanel.setMinimumSize(new Dimension(0, 60));

        // 菜单弹出
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem restartItem = new JMenuItem("重新开始");
        JMenuItem exitItem = new JMenuItem("退出");
        popupMenu.add(restartItem);
        popupMenu.add(exitItem);
        menuButton.addActionListener(e -> {
            SoundUtil.playClickSound();
            popupMenu.show(menuButton, 0, menuButton.getHeight());
        });
        // 重新开始
        restartItem.addActionListener(e -> {
            SoundUtil.playClickSound();
            timer.stop();
            seconds = 0;
            timerLabel.setText("用时: 0s");
            markedMines = 0;
            mineCountLabel.setText("标记: 0/" + realMines);
            progressPanel.setValue(0);
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    cells[row][col].isFlagged = false;
                    cells[row][col].isRevealed = false;
                    cells[row][col].isMineHighlighted = false;
                    cells[row][col].setText("");
                    cells[row][col].paint();
                }
            }
            newGame();
            timer.start();
        });
        // 退出
        exitItem.addActionListener(e -> {
            SoundUtil.playClickSound();
            timer.stop();
            SwingUtilities.getWindowAncestor(this).dispose();
            SwingUtilities.invokeLater(() -> {
                mainFrame.setVisible(true);
                mainFrame.showCoverPanel();
            });
        });

        // 棋盘面板
        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        boardPanel.setLayout(new GridLayout(rows, cols, 3, 3));
        add(boardPanel, BorderLayout.CENTER);

        // 加载背景图片
        try {
            backgroundImage = new ImageIcon(BG_IMAGE_PATH).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        // 将每一个Cell单元格对象加入的面板中.
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                cells[row][col] = new Cell(row, col);
                cells[row][col].setOpaque(false); // 设置格子透明
                boardPanel.add(cells[row][col]);
            }
        }
        
        // 创建鼠标事件监听器并添加到所有格子
        CellMouseListener mouseListener = new CellMouseListener();
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                cells[row][col].addMouseListener(mouseListener);
            }
        }

        setPreferredSize(new Dimension(CELL_SIZE * cols, CELL_SIZE * rows + 40));
        progressPanel.setMax(realMines);
        progressPanel.setValue(markedMines);
        mineCountLabel.setText("标记: " + markedMines + "/" + numMines);
        realMines = numMines;
        markedMines = 0;
        // 启动计时器
        timer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText("时间: " + seconds + "s");
        });
        timer.start();
        // 关键：初始化棋盘地雷和数字
        newGame();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // 绘制背景图片，缩放到面板大小
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        // 绘制分割线
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2.0f)); // 设置线条粗细
        
        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;
        
        // 绘制垂直线
        for (int i = 1; i < cols; i++) {
            int x = i * cellWidth;
            g2d.drawLine(x, 0, x, getHeight());
        }
        
        // 绘制水平线
        for (int i = 1; i < rows; i++) {
            int y = i * cellHeight;
            g2d.drawLine(0, y, getWidth(), y);
        }
    }

    // 初始化一个新游戏所需要调用的方法
    public void newGame() {
        MineMap mineMap = new MineMap(rows, cols, numMines);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col].newGame(mineMap.isMined[row][col]);
            }
        }
        revalidate();
        repaint();
    }

    // 这个方法用来对位置为（srcRow,srcCol）的单元格的8个邻居统计地雷数量，并将这个数量作为函数的返回值
    private int getSurroundingMines(int srcRow, int srcCol) {
        int numMines = 0;
        
        // 遍历周围8个邻居位置
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                // 跳过中心位置本身
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }
                
                // 计算邻居位置
                int neighborRow = srcRow + rowOffset;
                int neighborCol = srcCol + colOffset;
                
                // 检查边界：确保邻居位置在有效范围内
                if (neighborRow >= 0 && neighborRow < rows && 
                    neighborCol >= 0 && neighborCol < cols) {
                    // 如果邻居位置有地雷，计数加1
                    if (cells[neighborRow][neighborCol].isMined) {
                        numMines++;
                    }
                }
            }
        }
        
        return numMines;
    }

    // 位置为(srcRow, srcCol)的单元格执行打开操作（Reveal）
    // 如果打开的这个单元格的地雷数量是0，那么游戏必须要递归地将8个邻居中地雷数量为0的单元格依次打开。
    private void revealCell(int srcRow, int srcCol) {
        // 如果格子已经暴露或已标记，直接返回
        if (cells[srcRow][srcCol].isRevealed || cells[srcRow][srcCol].isFlagged) {
            return;
        }
        
        // 标记格子为已暴露
        cells[srcRow][srcCol].isRevealed = true;
        
        // 获取周围地雷数量
        int numMines = getSurroundingMines(srcRow, srcCol);
        
        if (numMines == 0) {
            cells[srcRow][srcCol].setNumber(0);
            cells[srcRow][srcCol].paint();
            // 递归打开周围8个邻居
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    // 跳过中心位置
                    if (rowOffset == 0 && colOffset == 0) {
                        continue;
                    }
                    int neighborRow = srcRow + rowOffset;
                    int neighborCol = srcCol + colOffset;
                    // 检查边界
                    if (neighborRow >= 0 && neighborRow < rows && 
                        neighborCol >= 0 && neighborCol < cols) {
                        revealCell(neighborRow, neighborCol);
                    }
                }
            }
        } else {
            cells[srcRow][srcCol].setNumber(numMines);
            cells[srcRow][srcCol].paint();
        }
    }

    // 如果玩家将所有的没有地雷的单元格打开，那么就判断该玩家赢了比赛，返回true。
    public boolean hasWon() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // 如果非地雷格子没有暴露，则游戏未胜利
                if (!cells[row][col].isMined && !cells[row][col].isRevealed) {
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] 使用内部类的方式实现监听器类型，这个监听器用来监听对每一个单元格的鼠标点击事件（尤其是要区分鼠标左键单击和鼠标右键单击）这个监听器类创建的并不完整

    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {         // Get the source object that fired the Event
            Cell sourceCell = (Cell)e.getSource();

            // 鼠标左键单击单元格的功能是打开（Reveal）单元格；
            // 鼠标右键单击单元格的功能是给单元格添加/删除旗帜标记，这个标记的作用是用来标记地雷。
            if (e.getButton() == MouseEvent.BUTTON1) {  // 鼠标左键单击
                // 如果当前单元格里面有地雷，那么游戏结束；如果当前单元格里没有地雷，那么就执行对该单元格的打开（Reveal）操作。
                if (sourceCell.isMined) {
                    timer.stop();
                    gameOver(false, null);
                } else {
                    // 打开格子
                    revealCell(sourceCell.row, sourceCell.col);
                    
                    // 检查是否胜利
                    if (hasWon()) {
                        timer.stop();
                        gameOver(true, null);
                    }
                }

            } else if (e.getButton() == MouseEvent.BUTTON3) { // 鼠标右键单击
                // 如果当前单元格已经有旗帜标记，那么就删除掉这个旗帜标记；如果当前单元格没有旗帜标记，那么就添加旗帜标记。
                if (!sourceCell.isRevealed) {
                    sourceCell.isFlagged = !sourceCell.isFlagged;
                    if (sourceCell.isFlagged) {
                        sourceCell.setText("🚩");
                        markedMines++;
                    } else {
                        sourceCell.setText("");
                        markedMines--;
                    }
                    sourceCell.paint();
                    mineCountLabel.setText("标记: " + markedMines + "/" + realMines);
                    progressPanel.setValue(markedMines);
                    if (markedMines > realMines) {
                        timer.stop();
                        gameOver(false, "游戏结束，标记地雷过多");
                    }
                }
            }
        }
    }
    
    // 游戏结束处理
    private void gameOver(boolean isWin, String customMsg) {
        // 保存游戏记录
        DocumentPanel.addGameRecord("中级", seconds, isWin);
        if (isWin) {
            DocumentPanel.updateBestScore("中级", seconds);
        }
        
        // 显示所有地雷
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isMined) {
                    cells[row][col].isMineHighlighted = true;
                    cells[row][col].setText("💣");
                    cells[row][col].setOpaque(true);
                    cells[row][col].setBackground(new Color(255, 182, 193)); // 浅红色
                    cells[row][col].paint();
                }
            }
        }
        
        // 显示游戏结果，使用退出按钮
        String message = customMsg != null ? customMsg : (isWin ? "恭喜你赢了！" : "游戏结束，你踩到地雷了！");
        JButton exitButton = new JButton("退出");
        Object[] options = { exitButton };
        JOptionPane pane = new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, exitButton);
        JDialog dialog = pane.createDialog(this, "游戏结果");
        exitButton.addActionListener(e -> {
            dialog.dispose();
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) window.dispose();
            mainFrame.setVisible(true);
            mainFrame.showCoverPanel();
        });
        dialog.setVisible(true);
    }

    // 自定义进度条面板，篮球悬浮在进度条上
    class BasketballProgressPanel extends JPanel {
        private int min, max, value;
        private Image basketballImg;
        public BasketballProgressPanel(int min, int max, String iconPath) {
            this.min = min;
            this.max = max;
            this.value = min;
            ImageIcon icon = new ImageIcon(iconPath);
            basketballImg = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            setPreferredSize(new Dimension(220, 40));
            setOpaque(false);
        }
        public void setValue(int v) {
            this.value = v;
            repaint();
        }
        public void setMax(int max) {
            this.max = max;
            repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth() - 24;
            int h = getHeight();
            // 画进度条
            int barY = h / 2 + 6;
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(12, barY, w, 12, 12, 12);
            g.setColor(Color.BLUE);
            int filled = 0;
            if (max > min) {
                filled = (int) (w * (value - min) / (double) (max - min));
            }
            g.fillRoundRect(12, barY, filled, 12, 12, 12);
            // 画篮球（悬浮在进度条上方）
            int x = 12 + filled - 12;
            int y = barY - 24;
            g.drawImage(basketballImg, x, y, 24, 24, this);
        }
    }
}
