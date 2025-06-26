package minesweeper;
/**
 * 为这个游戏定义全局通用的具名常量.
 * 在游戏后续的开发中，如果还需要其他的常量，都可以定义在这个类型中。
 */
public class MineSweeperConstants {
    /** 该游戏的行数 */
    public static int ROWS;
    /** 该游戏的列数 */
    public static int COLS;
    /** 该游戏的地雷数量 */
    public static int MINES;

    /** 设置游戏难度为初级 */
    public static void setEasy() {
        ROWS = 8;
        COLS = 8;
        MINES = 10;
    }

    /** 设置游戏难度为中级 */
    public static void setMedium() {
        ROWS = 16;
        COLS = 16;
        MINES = 40;
    }

    /** 设置游戏难度为高级 */
    public static void setHard() {
        ROWS = 16;
        COLS = 30;
        MINES = 99;
    }
}