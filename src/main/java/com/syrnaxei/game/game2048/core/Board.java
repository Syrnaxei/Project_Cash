package com.syrnaxei.game.game2048.core;

import com.syrnaxei.game.game2048.api.Game2048Listener;

import java.util.Random;

public class Board {
    private int[][] board;
    private int score = 0;
    private boolean gameOver;
    private Game2048Listener endListener;
    Random random = new Random();

    //===================================  创建棋盘 方法  ===================================
    public void createBoard() {
        board = new int[GameConfig.BOARD_SIZE][GameConfig.BOARD_SIZE];
        addNumber();
        addNumber();
    }

    //===================================  添加数字 方法  ===================================
    public void addNumber() {
        int row,col;
        if(!hasEmptyLocation()){
            return;
        }
        //random到的坐标如果没数字（0）结束循环
        do{
            row = random.nextInt(GameConfig.BOARD_SIZE);
            col = random.nextInt(GameConfig.BOARD_SIZE);
        }while(board[row][col]!=0);
        //随机函数生成0-100的数，大于SFP（生成4的概率数字20）即百分之八十概率生成2
        if(random.nextInt(100)>GameConfig.S_FOUR_P){
            board[row][col] = 2;
        }else{
            board[row][col] = 4;
        }
    }

    public boolean hasEmptyLocation() {
        for(int[] row : board){
            for(int num : row){
                if(num == 0){
                    return true;
                }
            }
        }
        return false;
    }

    //====================================  计分 方法  ====================================
    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score += score;
    }

    //===================================  游戏结束 方法  ===================================
    public boolean isGameOver() {
        //检查棋盘上是否有空位
        for(int i = 0; i < GameConfig.BOARD_SIZE; i++){
            for(int j = 0; j < GameConfig.BOARD_SIZE; j++){
                if(board[i][j] == 0){
                    return false;
                }
            }
        }
        //检查棋盘横向是否有相同的可合并的数字
        for(int i = 0; i < GameConfig.BOARD_SIZE; i++){
            for(int j = 0; j < GameConfig.BOARD_SIZE - 1; j++){
                if(board[i][j] == board[i][j+1]){
                    return false;
                }
            }
        }
        //检查棋盘纵向是否有相同的可合并的数字
        for(int i = 0; i < GameConfig.BOARD_SIZE - 1; i++){
            for(int j = 0; j < GameConfig.BOARD_SIZE; j++){
                if(board[i][j] == board[i+1][j]){
                    return false;
                }
            }
        }
        return true;
    }

    //===================================  棋盘调用 方法  ===================================
    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board){
        this.board = board;
    }

    public void resetBoard() {
        board = new int[GameConfig.BOARD_SIZE][GameConfig.BOARD_SIZE];
        score = 0;
        addNumber();
        addNumber();
    }

    public void setListener(Game2048Listener listener) {
        this.endListener = listener;
    }

    public void triggerGameOver() {
        this.gameOver = true;
        if (endListener != null) {
            endListener.onGameEnd(this.score); // 触发回调返回分数
        }
    }
}