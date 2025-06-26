package minesweeper;

import java.util.Random;
import static minesweeper.MineSweeperConstants.COLS;
import static minesweeper.MineSweeperConstants.ROWS;
/**
 * 这个类型的目的是为了生成当前游戏中地雷的分布位置，形象的用Map来命名
 */
public class MineMap {
    int numMines;
    boolean[][] isMined = new boolean[ROWS][COLS];
    private Random random = new Random();

    // 在后续的应用完善中，行列数不应该是固定的，而是可以定制的。
    public void newMineMap(int numMines) {
        this.numMines = numMines;
        
        // 初始化所有位置为无地雷
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                isMined[row][col] = false;
            }
        }
        
        // 随机布雷
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int row = random.nextInt(ROWS);
            int col = random.nextInt(COLS);
            
            // 如果该位置还没有地雷，则放置地雷
            if (!isMined[row][col]) {
                isMined[row][col] = true;
                minesPlaced++;
            }
        }
    }
}