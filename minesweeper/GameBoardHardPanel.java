package minesweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardHardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int HARD_ROWS = 16;
    public static final int HARD_COLS = 30;
    public static final int HARD_MINES = 99;
    public static final int CELL_SIZE = 18;
    public static final int CANVAS_WIDTH  = CELL_SIZE * HARD_COLS;
    public static final int CANVAS_HEIGHT = CELL_SIZE * HARD_ROWS;

    private static final String BG_IMAGE_PATH = "minesweeper/Iverson3.png"; // 可替换为你想要的图片
    private Image backgroundImage;

    private JLabel timerLabel = new JLabel("时间: 0s");
    private BasketballProgressPanel progressPanel = new BasketballProgressPanel(0, 100, "minesweeper/basketball.png");
    private JLabel mineCountLabel = new JLabel("标记: 0/0");
    private int markedMines = 0;
    private int realMines = HARD_MINES;
    private Timer timer;
    private int seconds = 0;

    private int rows = HARD_ROWS;
    private int cols = HARD_COLS;
    private int numMines = HARD_MINES;
    Cell cells[][] = new Cell[rows][cols];
    private MineSweeperMain mainFrame;

    public GameBoardHardPanel(MineSweeperMain mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

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

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem restartItem = new JMenuItem("重新开始");
        JMenuItem exitItem = new JMenuItem("退出");
        popupMenu.add(restartItem);
        popupMenu.add(exitItem);
        menuButton.addActionListener(e -> popupMenu.show(menuButton, 0, menuButton.getHeight()));
        restartItem.addActionListener(e -> {
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
        exitItem.addActionListener(e -> {
            timer.stop();
            SwingUtilities.getWindowAncestor(this).dispose();
            SwingUtilities.invokeLater(() -> {
                mainFrame.setVisible(true);
                mainFrame.showCoverPanel();
            });
        });

        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        boardPanel.setLayout(new GridLayout(rows, cols, 0, 0));
        add(boardPanel, BorderLayout.CENTER);

        try {
            backgroundImage = new ImageIcon(BG_IMAGE_PATH).getImage();
        } catch (Exception e) {
            backgroundImage = null;
        }

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < cols; ++col) {
                cells[row][col] = new Cell(row, col);
                cells[row][col].setOpaque(false);
                boardPanel.add(cells[row][col]);
            }
        }

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
        timer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText("时间: " + seconds + "s");
        });
        timer.start();
        newGame();
    }

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

    private int getSurroundingMines(int srcRow, int srcCol) {
        int numMines = 0;
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int colOffset = -1; colOffset <= 1; colOffset++) {
                if (rowOffset == 0 && colOffset == 0) {
                    continue;
                }
                int neighborRow = srcRow + rowOffset;
                int neighborCol = srcCol + colOffset;
                if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols) {
                    if (cells[neighborRow][neighborCol].isMined) {
                        numMines++;
                    }
                }
            }
        }
        return numMines;
    }

    private void revealCell(int srcRow, int srcCol) {
        if (cells[srcRow][srcCol].isRevealed || cells[srcRow][srcCol].isFlagged) {
            return;
        }
        cells[srcRow][srcCol].isRevealed = true;
        int numMines = getSurroundingMines(srcRow, srcCol);
        if (numMines == 0) {
            cells[srcRow][srcCol].setNumber(0);
            cells[srcRow][srcCol].paint();
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    if (rowOffset == 0 && colOffset == 0) continue;
                    int neighborRow = srcRow + rowOffset;
                    int neighborCol = srcCol + colOffset;
                    if (neighborRow >= 0 && neighborRow < rows && neighborCol >= 0 && neighborCol < cols) {
                        revealCell(neighborRow, neighborCol);
                    }
                }
            }
        } else {
            cells[srcRow][srcCol].setNumber(numMines);
            cells[srcRow][srcCol].paint();
        }
    }

    public boolean hasWon() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMined && !cells[row][col].isRevealed) {
                    return false;
                }
            }
        }
        return true;
    }

    private class CellMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Cell sourceCell = (Cell)e.getSource();
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (sourceCell.isMined) {
                    timer.stop();
                    gameOver(false, null);
                } else {
                    revealCell(sourceCell.row, sourceCell.col);
                    if (hasWon()) {
                        timer.stop();
                        gameOver(true, null);
                    }
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
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

    private void gameOver(boolean isWin, String customMsg) {
        // 保存游戏记录
        DocumentPanel.addGameRecord("高级", seconds, isWin);
        if (isWin) {
            DocumentPanel.updateBestScore("高级", seconds);
        }
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isMined) {
                    cells[row][col].isMineHighlighted = true;
                    cells[row][col].setText("💣");
                    cells[row][col].setOpaque(true);
                    cells[row][col].setBackground(new Color(255, 182, 193));
                    cells[row][col].paint();
                }
            }
        }
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