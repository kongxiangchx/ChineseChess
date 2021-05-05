package com.example.a77304.chessgame;

import java.io.Serializable;

/**
 * Created by 77304 on 2021/4/6.
 */

public class Move implements Serializable{
    private static final long serialVersionUID = -1608509463525143473L;

    public Pos fromPos;
    public Pos toPos;

    public Move(Pos fromPos, Pos toPos) {
        this.fromPos = fromPos;
        this.toPos = toPos;
    }
}
