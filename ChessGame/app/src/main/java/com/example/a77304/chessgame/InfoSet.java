package com.example.a77304.chessgame;

import java.io.Serializable;
import java.util.Stack;

/**
 * Created by 77304 on 2021/4/6.
 */

public class InfoSet implements Serializable{
    private static final long serialVersionUID = 2194052829864919744L;

    public Stack<ChessInfo> preInfo=new Stack<ChessInfo>();
    public ChessInfo curInfo=new ChessInfo();
    public InfoSet(){

    }
    public void pushInfo(ChessInfo chessInfo) throws CloneNotSupportedException {
        ChessInfo tmp=new ChessInfo();
        tmp.setInfo(curInfo);
        preInfo.push(tmp);
        curInfo.setInfo(chessInfo);
    }
    public void newInfo() throws CloneNotSupportedException {
        preInfo.clear();
        curInfo.setInfo(new ChessInfo());
    }
}
