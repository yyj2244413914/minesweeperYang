package minesweeper;

import java.util.Random;
/**
 * 这个类型的目的是为了生成当前游戏中地雷的分布位置，形象的用Map来命名
 */
public class MineMap {
    int numMines;
    boolean[][] isMined;
    private Random random = new Random();

    // 支持自定义行列和地雷数
    public MineMap(int rows, int cols, int numMines) {
        this.numMines = numMines;
        isMined = new boolean[rows][cols];
        clearMines();
        placeMines(numMines);
    }

    // 重新布雷方法（可用于重开一局）
    public void newMineMap(int numMines) {
        clearMines();
        placeMines(numMines);
    }

    // 清空地雷
    private void clearMines() {
        for (int row = 0; row < isMined.length; row++) {
            for (int col = 0; col < isMined[0].length; col++) {
                isMined[row][col] = false;
            }
        }
    }

    // 随机布雷
    private void placeMines(int numMines) {
        int rows = isMined.length;
        int cols = isMined[0].length;
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (!isMined[row][col]) {
                isMined[row][col] = true;
                minesPlaced++;
            }
        }
    }
}