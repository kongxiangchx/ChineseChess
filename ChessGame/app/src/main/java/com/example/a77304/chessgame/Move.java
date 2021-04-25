package com.example.a77304.chessgame;

/**
 * Created by 77304 on 2021/4/6.
 */

public class Move {
    public Pos fromPos;
    public Pos toPos;
    public Move(Pos fromPos,Pos toPos){
        this.fromPos=fromPos;
        this.toPos=toPos;
    }
}
