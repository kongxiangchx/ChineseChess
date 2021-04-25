package com.example.a77304.chessgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 77304 on 2021/4/5.
 */

public class ChessInfo implements Cloneable, Serializable {
    private static final long serialVersionUID = 5810196428873080501L;

    public int[] Select = new int[]{-1, -1};
    public boolean IsRedGo = true;
    public boolean IsChecked = false;
    public Pos prePos = new Pos(-1, -1);
    public Pos curPos = new Pos(-1, -1);
    public int status = 1;    //1 2 3
    public List<Pos> ret = new ArrayList<Pos>();
    public int[][] piece = new int[][]{
            {5, 4, 3, 2, 1, 2, 3, 4, 5},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 6, 0, 0, 0, 0, 0, 6, 0},
            {7, 0, 7, 0, 7, 0, 7, 0, 7},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},

            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {14, 0, 14, 0, 14, 0, 14, 0, 14},
            {0, 13, 0, 0, 0, 0, 0, 13, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {12, 11, 10, 9, 8, 9, 10, 11, 12},
    };

    public ChessInfo() {

    }

    public void setInfo(ChessInfo chessInfo) throws CloneNotSupportedException {
        this.Select = chessInfo.Select.clone();
        this.IsRedGo = chessInfo.IsRedGo;
        this.IsChecked = chessInfo.IsChecked;
        this.prePos = (Pos) chessInfo.prePos.clone();
        this.curPos = (Pos) chessInfo.curPos.clone();
        this.status = chessInfo.status;
        this.ret = new ArrayList<Pos>(chessInfo.ret);
        for (int i = 0; i < this.piece.length; i++) {
            this.piece[i] = chessInfo.piece[i].clone();
        }
    }
}
