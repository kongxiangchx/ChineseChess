package com.example.a77304.chessgame;

import java.io.Serializable;

/**
 * Created by 77304 on 2021/4/30.
 */

public class TransformInfo implements Serializable{
    private static final long serialVersionUID = -4301157684495562556L;

    long ZobristKeyCheck;
    int depth;
    int value;
    public TransformInfo(long ZobristKeyCheck,int depth,int value){
        this.ZobristKeyCheck=ZobristKeyCheck;
        this.depth=depth;
        this.value=value;
    }
}
