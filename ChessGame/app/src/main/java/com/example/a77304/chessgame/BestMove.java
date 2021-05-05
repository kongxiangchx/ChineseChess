package com.example.a77304.chessgame;

import java.io.Serializable;

/**
 * Created by 77304 on 2021/4/30.
 */

public class BestMove implements Serializable{
    private static final long serialVersionUID = -3102985870943786145L;

    int ZobristKey;
    long ZobristKeyCheck;
    int depth;
    Move move;
    public BestMove(int ZobristKey,long ZobristKeyCheck,int depth,Move move){
        this.ZobristKey=ZobristKey;
        this.ZobristKeyCheck=ZobristKeyCheck;
        this.depth=depth;
        this.move=move;
    }
}
