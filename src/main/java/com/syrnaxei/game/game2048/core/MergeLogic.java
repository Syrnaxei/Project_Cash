package com.syrnaxei.game.game2048.core;

/**
 * 处理2048游戏方块合并逻辑的类
 *
 * @author Syrnaxei
 * @since 2025/12/15
 */
public class MergeLogic {

    private final Board board;

    /**
     * 构造函数
     * @param board 游戏面板对象
     */
    public MergeLogic(Board board) {
        this.board = board;
    }

    //==================================  merge方法  ====================================
    
    /**
     * 向右合并所有行
     */
    public void mergeRight() {
        int[][] data = board.getBoard();
        for (int row = 0; row < GameConfig.BOARD_SIZE; row++) {
            int[] currRow = data[row];

            // Step1: 先紧凑（非0数字向右靠，消除中间0）
            int[] compacted = compactRight(currRow);

            // Step2: 再合并（从右往左遍历，合并相邻相同数字）
            int[] merged = adjacentRight(compacted);

            // Step3: 替换原行
            data[row] = merged;
        }
        board.setBoard(data);
    }

    /**
     * 向左合并所有行
     */
    public void mergeLeft() {
        int[][] data = board.getBoard();
        for (int row = 0; row < GameConfig.BOARD_SIZE; row++) {
            int[] currRow = data[row];
            int[] compacted = compactLeft(currRow);
            int[] merged = adjacentLeft(compacted);
            data[row] = merged;
        }
        board.setBoard(data);
    }

    /**
     * 向下合并所有列
     */
    public void mergeDown() {
        int[][] data = board.getBoard();
        for (int col = 0; col < GameConfig.BOARD_SIZE; col++) {
            // 提取当前列
            int[] tempCol = extractCol(data, col);
            int[] compactedCol = compactDown(tempCol); // 向下紧凑
            int[] mergedCol = adjacentDown(compactedCol); // 向下合并
            // 写回列
            writeColumn(data, mergedCol, col);
        }
        board.setBoard(data);
    }

    /**
     * 向上合并所有列
     */
    public void mergeUp() {
        int[][] data = board.getBoard();
        for (int col = 0; col < GameConfig.BOARD_SIZE; col++) {
            int[] tempCol = extractCol(data, col);
            int[] compactedCol = compactUp(tempCol);
            int[] mergedCol = adjacentUp(compactedCol);
            writeColumn(data, mergedCol, col);
        }
        board.setBoard(data);
    }

    //==================================  adjacent方法  =================================
    
    /**
     * 向右合并单行
     * @param compactedRow 已经紧凑处理的行
     * @return 合并后的行
     */
    public int[] adjacentRight(int[] compactedRow) {
        int[] newRow = new int[GameConfig.BOARD_SIZE];
        int index = GameConfig.BOARD_SIZE - 1;
        int i = GameConfig.BOARD_SIZE - 1;

        while (i >= 0) {
            int tempNum = compactedRow[i];
            if (tempNum == 0) {
                i--;
                continue;
            }
            if (i - 1 >= 0 && tempNum == compactedRow[i - 1]) {
                newRow[index] = tempNum * 2;
                board.addScore(tempNum * 2);
                index--;
                i -= 2;
            } else {
                newRow[index] = tempNum;
                index--;
                i--;
            }
        }

        return newRow;
    }

    /**
     * 向左合并单行
     * @param compactedRow 已经紧凑处理的行
     * @return 合并后的行
     */
    public int[] adjacentLeft(int[] compactedRow) {
        int[] newRow = new int[GameConfig.BOARD_SIZE];
        int index = 0;
        int i = 0;

        while (i < GameConfig.BOARD_SIZE) {
            int tempNum = compactedRow[i];
            if (tempNum == 0) {
                i++;
                continue;
            }
            if (
                i + 1 < GameConfig.BOARD_SIZE && tempNum == compactedRow[i + 1]
            ) {
                newRow[index] = tempNum * 2;
                board.addScore(tempNum * 2);
                index++;
                i += 2;
            } else {
                newRow[index] = tempNum;
                index++;
                i++;
            }
        }
        return newRow;
    }

    /**
     * 向下合并单列
     * @param compactedCol 已经紧凑处理的列
     * @return 合并后的列
     */
    public int[] adjacentDown(int[] compactedCol) {
        int[] newCol = new int[GameConfig.BOARD_SIZE];
        int index = GameConfig.BOARD_SIZE - 1;
        int i = GameConfig.BOARD_SIZE - 1;

        while (i >= 0) {
            int tempNum = compactedCol[i];
            if (tempNum == 0) {
                i--;
                continue;
            }
            if (i - 1 >= 0 && tempNum == compactedCol[i - 1]) {
                newCol[index] = tempNum * 2;
                board.addScore(tempNum * 2);
                index--;
                i -= 2;
            } else {
                newCol[index] = tempNum;
                index--;
                i -= 1;
            }
        }
        return newCol;
    }

    /**
     * 向上合并单列
     * @param compactedCol 已经紧凑处理的列
     * @return 合并后的列
     */
    public int[] adjacentUp(int[] compactedCol) {
        int[] newCol = new int[GameConfig.BOARD_SIZE];
        int index = 0;
        int i = 0;

        while (i < GameConfig.BOARD_SIZE) {
            int tempNum = compactedCol[i];
            if (tempNum == 0) {
                i++;
                continue;
            }
            if (
                i + 1 < GameConfig.BOARD_SIZE && tempNum == compactedCol[i + 1]
            ) {
                newCol[index] = tempNum * 2;
                board.addScore(tempNum * 2);
                index++;
                i += 2;
            } else {
                newCol[index] = tempNum;
                index++;
                i += 1;
            }
        }
        return newCol;
    }

    //==================================  compact方法  ==================================
    
    /**
     * 将行中的非零元素向右紧凑
     * @param row 原始行
     * @return 紧凑后的行
     */
    private int[] compactRight(int[] row) {
        int[] newRow = new int[GameConfig.BOARD_SIZE];
        int index = GameConfig.BOARD_SIZE - 1; // 从右侧开始填充
        for (int i = GameConfig.BOARD_SIZE - 1; i >= 0; i--) {
            if (row[i] != 0) {
                newRow[index--] = row[i];
            }
        }
        return newRow;
    }

    /**
     * 将行中的非零元素向左紧凑
     * @param row 原始行
     * @return 紧凑后的行
     */
    private int[] compactLeft(int[] row) {
        int[] newRow = new int[GameConfig.BOARD_SIZE];
        int index = 0;
        for (int num : row) {
            if (num != 0) {
                newRow[index++] = num;
            }
        }
        return newRow;
    }

    /**
     * 将列中的非零元素向下紧凑
     * @param col 原始列
     * @return 紧凑后的列
     */
    private int[] compactDown(int[] col) {
        int[] newCol = new int[GameConfig.BOARD_SIZE];
        int index = GameConfig.BOARD_SIZE - 1;
        for (int i = GameConfig.BOARD_SIZE - 1; i >= 0; i--) {
            if (col[i] != 0) {
                newCol[index--] = col[i];
            }
        }
        return newCol;
    }

    /**
     * 将列中的非零元素向上紧凑
     * @param col 原始列
     * @return 紧凑后的列
     */
    private int[] compactUp(int[] col) {
        int[] newCol = new int[GameConfig.BOARD_SIZE];
        int index = 0;
        for (int num : col) {
            if (num != 0) {
                newCol[index++] = num;
            }
        }
        return newCol;
    }

    //===========================  UP && DOWNs extra works  ===========================
    
    /**
     * 从二维数组中提取指定列
     * @param data 二维数组
     * @param col 列索引
     * @return 指定列的一维数组
     */
    private int[] extractCol(int[][] data, int col) {
        int[] extractedcol = new int[GameConfig.BOARD_SIZE];
        for (int row = 0; row < GameConfig.BOARD_SIZE; row++) {
            extractedcol[row] = data[row][col];
        }
        return extractedcol;
    }

    /**
     * 将一维数组写入二维数组的指定列
     * @param data 二维数组
     * @param newCol 要写入的一维数组
     * @param col 列索引
     */
    private void writeColumn(int[][] data, int[] newCol, int col) {
        for (int row = 0; row < GameConfig.BOARD_SIZE; row++) {
            data[row][col] = newCol[row];
        }
    }
}
