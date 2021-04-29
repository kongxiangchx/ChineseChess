package com.example.a77304.chessgame;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 77304 on 2021/4/6.
 */

public class AI {
    public static int[][][] pieceValue = new int[][][]{
            //黑将
            {
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
            },
            //黑士
            {
                    {0, 0, 0, 20, 0, 20, 0, 0, 0},
                    {0, 0, 0, 0, 23, 0, 0, 0, 0},
                    {0, 0, 0, 20, 0, 20, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
            },
            //黑象
            {
                    {0, 0, 20, 0, 0, 0, 20, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {18, 0, 0, 0, 23, 0, 0, 0, 18},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 20, 0, 0, 0, 20, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
            },
            //黑马
            {
                    {88, 85, 90, 88, 90, 88, 90, 85, 88},
                    {85, 90, 92, 93, 78, 93, 92, 90, 85},
                    {93, 92, 94, 95, 92, 95, 94, 92, 93},
                    {92, 94, 98, 95, 98, 95, 98, 94, 92},
                    {90, 98, 101, 102, 103, 102, 101, 98, 90},

                    {90, 100, 99, 103, 104, 103, 99, 100, 90},
                    {93, 108, 100, 107, 100, 107, 100, 108, 93},
                    {92, 98, 99, 103, 99, 103, 99, 98, 92},
                    {90, 96, 103, 97, 94, 97, 103, 96, 90},
                    {90, 90, 90, 96, 90, 96, 90, 90, 90},
            },
            //黑车
            {
                    {194, 206, 204, 212, 200, 212, 204, 206, 194},
                    {200, 208, 206, 212, 200, 212, 206, 208, 200},
                    {198, 208, 204, 212, 212, 212, 204, 208, 198},
                    {204, 209, 204, 212, 214, 212, 204, 209, 204},
                    {208, 212, 212, 214, 215, 214, 212, 212, 208},

                    {208, 211, 211, 214, 215, 214, 211, 211, 208},
                    {206, 213, 213, 216, 216, 216, 213, 213, 206},
                    {206, 208, 207, 214, 216, 214, 207, 208, 206},
                    {206, 212, 209, 216, 233, 216, 209, 212, 206},
                    {206, 208, 207, 213, 214, 213, 207, 208, 206},
            },
            //黑炮
            {
                    {96, 96, 97, 99, 99, 99, 97, 96, 96},
                    {96, 97, 98, 98, 98, 98, 98, 97, 96},
                    {97, 96, 100, 99, 101, 99, 100, 96, 97},
                    {96, 96, 96, 96, 96, 96, 96, 96, 96},
                    {95, 96, 99, 96, 100, 96, 99, 96, 95},

                    {96, 96, 96, 96, 100, 96, 96, 96, 96},
                    {96, 99, 99, 98, 100, 98, 99, 99, 96},
                    {97, 97, 96, 91, 92, 91, 96, 97, 97},
                    {98, 98, 96, 92, 89, 92, 96, 98, 98},
                    {100, 100, 96, 91, 90, 91, 96, 100, 100},
            },
            //黑卒
            {

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {7, 0, 7, 0, 15, 0, 7, 0, 7},
                    {7, 0, 13, 0, 16, 0, 13, 0, 7},

                    {14, 18, 20, 27, 29, 27, 20, 18, 14},
                    {19, 23, 27, 29, 30, 29, 27, 23, 19},
                    {19, 24, 32, 37, 37, 37, 32, 24, 19},
                    {19, 24, 34, 42, 44, 42, 34, 24, 19},
                    {9, 9, 9, 11, 13, 11, 9, 9, 9},
            },
            //红帅
            {
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0},
                    {0, 0, 0, 8888, 8888, 8888, 0, 0, 0}
            },
            //红士
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 20, 0, 20, 0, 0, 0},
                    {0, 0, 0, 0, 23, 0, 0, 0, 0},
                    {0, 0, 0, 20, 0, 20, 0, 0, 0}
            },
            //红象
            {
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},

                    {0, 0, 20, 0, 0, 0, 20, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {18, 0, 0, 0, 23, 0, 0, 0, 18},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 20, 0, 0, 0, 20, 0, 0}
            },
            //马
            {
                    {90, 90, 90, 96, 90, 96, 90, 90, 90},
                    {90, 96, 103, 97, 94, 97, 103, 96, 90},
                    {92, 98, 99, 103, 99, 103, 99, 98, 92},
                    {93, 108, 100, 107, 100, 107, 100, 108, 93},
                    {90, 100, 99, 103, 104, 103, 99, 100, 90},

                    {90, 98, 101, 102, 103, 102, 101, 98, 90},
                    {92, 94, 98, 95, 98, 95, 98, 94, 92},
                    {93, 92, 94, 95, 92, 95, 94, 92, 93},
                    {85, 90, 92, 93, 78, 93, 92, 90, 85},
                    {88, 85, 90, 88, 90, 88, 90, 85, 88}
            },
            //车
            {
                    {206, 208, 207, 213, 214, 213, 207, 208, 206},
                    {206, 212, 209, 216, 233, 216, 209, 212, 206},
                    {206, 208, 207, 214, 216, 214, 207, 208, 206},
                    {206, 213, 213, 216, 216, 216, 213, 213, 206},
                    {208, 211, 211, 214, 215, 214, 211, 211, 208},

                    {208, 212, 212, 214, 215, 214, 212, 212, 208},
                    {204, 209, 204, 212, 214, 212, 204, 209, 204},
                    {198, 208, 204, 212, 212, 212, 204, 208, 198},
                    {200, 208, 206, 212, 200, 212, 206, 208, 200},
                    {194, 206, 204, 212, 200, 212, 204, 206, 194}
            },
            //炮
            {

                    {100, 100, 96, 91, 90, 91, 96, 100, 100},
                    {98, 98, 96, 92, 89, 92, 96, 98, 98},
                    {97, 97, 96, 91, 92, 91, 96, 97, 97},
                    {96, 99, 99, 98, 100, 98, 99, 99, 96},
                    {96, 96, 96, 96, 100, 96, 96, 96, 96},

                    {95, 96, 99, 96, 100, 96, 99, 96, 95},
                    {96, 96, 96, 96, 96, 96, 96, 96, 96},
                    {97, 96, 100, 99, 101, 99, 100, 96, 97},
                    {96, 97, 98, 98, 98, 98, 98, 97, 96},
                    {96, 96, 97, 99, 99, 99, 97, 96, 96}
            },
            //卒
            {
                    {9, 9, 9, 11, 13, 11, 9, 9, 9},
                    {19, 24, 34, 42, 44, 42, 34, 24, 19},
                    {19, 24, 32, 37, 37, 37, 32, 24, 19},
                    {19, 23, 27, 29, 30, 29, 27, 23, 19},
                    {14, 18, 20, 27, 29, 27, 20, 18, 14},

                    {7, 0, 13, 0, 16, 0, 13, 0, 7},
                    {7, 0, 7, 0, 15, 0, 7, 0, 7},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0}
            }
    };
    public static final int INF = 0x3f3f3f3f;
    public static boolean isPlayerRed;

    public AI() {

    }

    public static Move getGoodMove(int[][] piece, boolean isRedGo, int depth) {
        Move goodMove = new Move(new Pos(-1, -1), new Pos(-1, -1));
        int[][] pieceClone = new int[10][9];
        for (int i = 0; i <= 9; i++) {
            pieceClone[i] = piece[i].clone();
        }

        isPlayerRed = isRedGo;
        boolean FoundPv = false;
        int value = -INF;
        int alpha = -INF, beta = INF;
        boolean isKingAlive = true;

        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if ((isRedGo == false && pieceClone[i][j] >= 1 && pieceClone[i][j] <= 7) || (isRedGo == true && pieceClone[i][j] >= 8 && pieceClone[i][j] <= 14)) {
                    List<Pos> ret = Rule.PossibleMoves(pieceClone, j, i, pieceClone[i][j]);
                    Pos fromPos = new Pos(j, i);
                    Iterator<Pos> it = ret.iterator();
                    while (it.hasNext()) {
                        Pos toPos = it.next();

                        int pieceID = pieceClone[toPos.y][toPos.x];
                        pieceClone[toPos.y][toPos.x] = pieceClone[fromPos.y][fromPos.x];
                        pieceClone[fromPos.y][fromPos.x] = 0;

                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = false;
                        }

                        if (FoundPv) {
                            value = -PVS(pieceClone, (isRedGo == true) ? false : true, depth - 1, -alpha - 1, -alpha, isKingAlive);
                            if ((value > alpha) && (value < beta)) {
                                value = -PVS(pieceClone, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                            }
                        } else {
                            value = -PVS(pieceClone, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                        }

                        pieceClone[fromPos.y][fromPos.x] = pieceClone[toPos.y][toPos.x];
                        pieceClone[toPos.y][toPos.x] = pieceID;
                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = true;
                        }

                        if (value > alpha) {
                            alpha = value;
                            FoundPv = true;
                            goodMove = new Move(fromPos, toPos);
                        }
                    }
                }
            }
        }
        return goodMove;
    }

    public static int PVS(int[][] piece, boolean isRedGo, int depth, int alpha, int beta, boolean isKingAlive) {
        boolean FoundPv = false;
        if (isKingAlive == false || depth <= 0) {
            return evaluate(piece);
        }

        int value = -INF;

        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if ((isRedGo == false && piece[i][j] >= 1 && piece[i][j] <= 7) || (isRedGo == true && piece[i][j] >= 8 && piece[i][j] <= 14)) {
                    List<Pos> ret = Rule.PossibleMoves(piece, j, i, piece[i][j]);
                    Pos fromPos = new Pos(j, i);
                    Iterator<Pos> it = ret.iterator();
                    while (it.hasNext()) {
                        Pos toPos = it.next();
                        int pieceID = piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x] = piece[fromPos.y][fromPos.x];
                        piece[fromPos.y][fromPos.x] = 0;

                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = false;
                        }

                        if (FoundPv) {
                            value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -alpha - 1, -alpha, isKingAlive);
                            if ((value > alpha) && (value < beta)) {
                                value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                            }
                        } else {
                            value = -PVS(piece, (isRedGo == true) ? false : true, depth - 1, -beta, -alpha, isKingAlive);
                        }

                        piece[fromPos.y][fromPos.x] = piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x] = pieceID;
                        if (pieceID == 1 || pieceID == 8) {
                            isKingAlive = true;
                        }

                        if (value >= beta) {
                            return beta;
                        }
                        if (value > alpha) {
                            alpha = value;
                            FoundPv = true;
                        }
                    }
                }
            }
        }
        return alpha;
    }

    //Alpha-Beta搜索
    /*public static Move getGoodMove(int[][] piece,boolean isRedGo,int depth){
        Move goodMove=new Move(new Pos(-1,-1),new Pos(-1,-1));
        int[][] pieceClone=new int[10][9];
        for(int i=0;i<=9;i++){
            pieceClone[i]=piece[i].clone();
        }

        isPlayerRed=isRedGo;
        int value=-INF;
        int alpha=-INF,beta=INF;
        boolean isKingAlive=true;


        for(int i=0;i<=9;i++){
            for(int j=0;j<=8;j++){
                if((isRedGo==false&&pieceClone[i][j]>=1&&pieceClone[i][j]<=7)||(isRedGo==true&&pieceClone[i][j]>=8&&pieceClone[i][j]<=14)){
                    List<Pos> ret=Rule.PossibleMoves(pieceClone,j,i,pieceClone[i][j]);
                    Pos fromPos=new Pos(j,i);
                    Iterator<Pos> it=ret.iterator();
                    while(it.hasNext()){
                        Pos toPos=it.next();

                        int tmp= pieceClone[toPos.y][toPos.x];
                        pieceClone[toPos.y][toPos.x]=pieceClone[fromPos.y][fromPos.x];
                        pieceClone[fromPos.y][fromPos.x]=0;

                        if(tmp==1||tmp==8){
                            isKingAlive=false;
                        }

                        value=-AlphaBeta(pieceClone,(isRedGo==true)?false:true,depth-1,-beta,-alpha,isKingAlive);

                        pieceClone[fromPos.y][fromPos.x]=pieceClone[toPos.y][toPos.x];
                        pieceClone[toPos.y][toPos.x]=tmp;
                        if(tmp==1||tmp==8){
                            isKingAlive=true;
                        }

                        if(value>alpha){
                            alpha=value;
                            goodMove=new Move(fromPos,toPos);
                        }
                    }
                }
            }
        }
        return goodMove;
    }
    public static int AlphaBeta(int[][] piece,boolean isRedGo,int depth,int alpha,int beta,boolean isKingAlive){
        if(isKingAlive==false||depth<=0){
            return evaluate(piece);
        }
        int value=-INF;

        for(int i=0;i<=9;i++){
            for(int j=0;j<=8;j++){
                if((isRedGo==false&&piece[i][j]>=1&&piece[i][j]<=7)||(isRedGo==true&&piece[i][j]>=8&&piece[i][j]<=14)){
                    List<Pos> ret=Rule.PossibleMoves(piece,j,i,piece[i][j]);
                    Pos fromPos=new Pos(j,i);
                    Iterator<Pos> it=ret.iterator();
                    while(it.hasNext()){
                        Pos toPos=it.next();
                        int tmp= piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x]=piece[fromPos.y][fromPos.x];
                        piece[fromPos.y][fromPos.x]=0;

                        if(tmp==1||tmp==8){
                            isKingAlive=false;
                        }

                        value=-AlphaBeta(piece,(isRedGo==true)?false:true,depth-1,-beta,-alpha,isKingAlive);

                        piece[fromPos.y][fromPos.x]=piece[toPos.y][toPos.x];
                        piece[toPos.y][toPos.x]=tmp;
                        if(tmp==1||tmp==8){
                            isKingAlive=true;
                        }

                        if(value>=beta){
                            return beta;
                        }
                        if(value>alpha){
                            alpha=value;
                        }
                    }
                }
            }
        }
        return alpha;
    }*/

    public static int getAttackNum(int[][] piece, boolean isRed) {
        int attackNum = 0;
        if (isRed == true) {
            for (int i = 0; i <= 9; i++) {
                for (int j = 0; j <= 8; j++) {
                    if (piece[i][j] >= 8 && piece[i][j] <= 14) {
                        attackNum++;
                    }
                }
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                for (int j = 0; j <= 8; j++) {
                    if (piece[i][j] >= 1 && piece[i][j] <= 7) {
                        attackNum++;
                    }
                }
            }
        }
        return attackNum;
    }

    public static int evaluate(int[][] piece) {
        int score = 0;
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 8; j++) {
                if (piece[i][j] >= 1 && piece[i][j] <= 7) {
                    score += pieceValue[piece[i][j] - 1][i][j];
                } else if (piece[i][j] >= 8 && piece[i][j] <= 14) {
                    score -= pieceValue[piece[i][j] - 1][i][j];
                }
            }
        }
        if (isPlayerRed) score *= -1;
        return score;
    }
}
