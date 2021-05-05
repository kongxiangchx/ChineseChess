package com.example.a77304.chessgame;

import android.util.Log;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by 77304 on 2021/4/30.
 */

public class KnowledgeBase implements Serializable{
    private static final long serialVersionUID = -320946536323096337L;

    public LinkedList<BestMove> bestMoves=new LinkedList<BestMove>();
    public final int Max=10000;

    public Move readBestMoves(int ZobristKey,long ZobristKeyCheck,int depth){
        int index=-1;
        for(BestMove bestMove : bestMoves){
            index++;
            if(ZobristKey==bestMove.ZobristKey&&ZobristKeyCheck==bestMove.ZobristKeyCheck){
                if(depth<=bestMove.depth){
                    bestMoves.remove(index);
                    bestMoves.addFirst(bestMove);
                    return bestMove.move;
                }
                else{
                    return null;
                }
            }
        }
        return null;
    }
    public void saveBestMove(int ZobristKey,long ZobristKeyCheck,int depth,Move move){
        int index=-1;
        for(BestMove bestMove : bestMoves){
            index++;
            if(ZobristKey==bestMove.ZobristKey&&ZobristKeyCheck==bestMove.ZobristKeyCheck){
                if(depth>=bestMove.depth){
                    bestMoves.remove(index);
                    bestMoves.addFirst(new BestMove(ZobristKey,ZobristKeyCheck,depth,move));
                    return ;
                }
                else{
                    return ;
                }
            }
        }
        bestMoves.addFirst(new BestMove(ZobristKey,ZobristKeyCheck,depth,move));
        if(bestMoves.size()>Max){
            bestMoves.removeLast();
        }
    }
}
