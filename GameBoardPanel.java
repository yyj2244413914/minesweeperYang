package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static minesweeper.MineSweeperConstants.ROWS;
import static minesweeper.MineSweeperConstants.COLS;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L;  // to prevent serial warning

    // 定义每一个单元格的大小（单位为像素）
    public static final int CELL_SIZE = 60;
    public static final int CANVAS_WIDTH  = CELL_SIZE * COLS; // Game board width/height
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;


    /** 游戏的整个界面面板应该包含的单元格数量是：ROWS*COLS */
    Cell cells[][] = new Cell[ROWS][COLS];
    /** 地雷的数量，在后续的应用改善中，地雷的数量应该随面板的大小，随游戏的难度级数而不同，不应该像下面的代码这样被固定化了。 */
    int numMines = 10;

    public GameBoardPanel() {
        super.setLayout(new GridLayout(ROWS, COLS, 2, 2));  // JPanel

        // 将每一个Cell单元格对象加入的面板中.
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]);
            }
        }

        // [TODO 3] 创建一个MouseEventListener对象，所有的Cell对象都共用这一个事件监听器对象。

        // [TODO 4] 将每一个Cell对象都加入到事件监听器中。

        super.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
    }

    // 初始化一个新游戏所需要调用的方法
    public void newGame() {
        // 首先获得一个地雷分布地图对象
        MineMap mineMap = new MineMap();
        mineMap.newMineMap(numMines);

        // 根据地雷地图中的数据，将每一个Cell对象按照初始的状态进行绘制
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col].newGame(mineMap.isMined[row][col]);
            }
        }
    }

    // 这个方法用来对位置为（srcRow,srcCol）的单元格的8个邻居统计地雷数量，并将这个数量作为函数的返回值
    private int getSurroundingMines(int srcRow, int srcCol) {
        int numMines = 0;
        // [TODO 8] 实现统计周边地雷数量的代码，注意边界的单元格并不是有8个邻居。
        return numMines;
    }

    // 位置为(srcRow, srcCol)的单元格执行打开操作（Reveal）
    // 如果打开的这个单元格的地雷数量是0，那么游戏必须要递归地将8个邻居中地雷数量为0的单元格依次打开。
    private void revealCell(int srcRow, int srcCol) {
        int numMines = getSurroundingMines(srcRow, srcCol);
        // [TODO 9] 实现将地雷数为零的单元格打开并递归地将其8个邻居中地雷数量为零的单元格依次打开。
    }

    // 如果玩家将所有的没有地雷的单元格打开，那么就判断该玩家赢了比赛，返回true。
    public boolean hasWon() {
        // [TODO 10] 判断玩家是否赢得了比赛
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
                // [TODO 5]  如果当前单元格里面有地雷，那么游戏结束；如果当前单元格里没有地雷，那么就执行对该单元格的打开（Reveal）操作。

            } else if (e.getButton() == MouseEvent.BUTTON3) { // 鼠标右键单击
                // [TODO 6] 如果当前单元格已经有旗帜标记，那么就删除掉这个旗帜标记；如果当前单元格没有旗帜标记，那么就添加旗帜标记。

            }

            // [TODO 7] 在对一个单元格执行了打开操作之后，请判断一下玩家是否已经赢取了比赛。
        }
    }
}
