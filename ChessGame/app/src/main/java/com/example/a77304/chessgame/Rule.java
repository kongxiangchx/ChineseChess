package com.example.a77304.chessgame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.example.a77304.chessgame.HomeActivity.setting;

/**
 * Created by 77304 on 2021/4/4.
 */

public class Rule {
    public static int[][] area = new int[][]{
            {1, 1, 1, 2, 2, 2, 1, 1, 1},
            {1, 1, 1, 2, 2, 2, 1, 1, 1},
            {1, 1, 1, 2, 2, 2, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1},

            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 3, 3, 3, 3, 3, 3},
            {3, 3, 3, 4, 4, 4, 3, 3, 3},
            {3, 3, 3, 4, 4, 4, 3, 3, 3},
            {3, 3, 3, 4, 4, 4, 3, 3, 3},
    };
    public static int[][] offsetX = new int[][]{
            {0, 0, 1, -1},             //将 帅
            {1, 1, -1, -1},            //士
            {2, 2, -2, -2},            //象
            {1, 1, -1, -1},            //象眼
            {1, 1, -1, -1, 2, 2, -2, -2},  //马
            {0, 0, 0, 0, 1, 1, -1, -1},    //蹩马腿
            {0},                    //卒
            {-1, 0, 1},               //过河卒
            {0},                    //兵
            {-1, 0, 1},               //过河兵
            {1, 1, -1, -1, 1, 1, -1, -1},  //反向蹩马腿
    };
    public static int[][] offsetY = new int[][]{
            {1, -1, 0, 0},             //将 帅
            {1, -1, 1, -1},            //士
            {2, -2, 2, -2},            //象
            {1, -1, 1, -1},            //象眼
            {2, -2, 2, -2, 1, -1, 1, -1},  //马
            {1, -1, 1, -1, 0, 0, 0, 0},    //蹩马腿
            {1},                    //卒
            {0, 1, 0},                //过河卒
            {-1},                   //兵
            {0, -1, 0},               //过河兵
            {1, -1, 1, -1, 1, -1, 1, -1},  //反向蹩马腿
    };

    public static List<Pos> PossibleMoves(int[][] piece, int fromX, int fromY, int PieceID) {
        List<Pos> ret = new ArrayList<Pos>();
        int num;
        switch (PieceID) {
            case 1://黑将
                num = 0;
                for (int i = 0; i < offsetX[num].length; i++) {
                    int toX = fromX + offsetX[num][i];
                    int toY = fromY + offsetY[num][i];
                    if (InArea(toX, toY) == 2 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                        ret.add(new Pos(toX, toY));
                    }
                }
                Pos eatPos1 = flyKing(1, fromX, fromY, piece);
                if (eatPos1.equals(new Pos(-1, -1)) == false) {
                    ret.add(eatPos1);
                }
                break;
            case 2://黑士
                num = 1;
                for (int i = 0; i < offsetX[num].length; i++) {
                    int toX = fromX + offsetX[num][i];
                    int toY = fromY + offsetY[num][i];
                    if (InArea(toX, toY) == 2 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                        ret.add(new Pos(toX, toY));
                    }
                }
                break;
            case 3://黑象
                num = 2;
                for (int i = 0; i < offsetX[num].length; i++) {
                    int toX = fromX + offsetX[num][i];
                    int toY = fromY + offsetY[num][i];
                    int blockX = fromX + offsetX[num + 1][i];
                    int blockY = fromY + offsetY[num + 1][i];
                    if (InArea(toX, toY) >= 1 && InArea(toX, toY) <= 2 && IsSameSide(PieceID, piece[toY][toX]) == false && piece[blockY][blockX] == 0) {
                        ret.add(new Pos(toX, toY));
                    }
                }
                break;
            case 4://黑马
            case 11://红马
                num = 4;
                for (int i = 0; i < offsetX[num].length; i++) {
                    int toX = fromX + offsetX[num][i];
                    int toY = fromY + offsetY[num][i];
                    int blockX = fromX + offsetX[num + 1][i];
                    int blockY = fromY + offsetY[num + 1][i];
                    if (InArea(toX, toY) != 0 && IsSameSide(PieceID, piece[toY][toX]) == false && piece[blockY][blockX] == 0) {
                        ret.add(new Pos(toX, toY));
                    }
                }
                break;
            case 5://黑车
            case 12: //红车
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (i != fromX || j != fromY) {
                            if (CanMove(1, fromX, fromY, i, j, piece)) {
                                ret.add(new Pos(i, j));
                            }
                        }
                    }
                }
                break;
            case 6://黑炮
            case 13://红炮
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 10; j++) {
                        if (i != fromX || j != fromY) {
                            if (CanMove(2, fromX, fromY, i, j, piece)) {
                                ret.add(new Pos(i, j));
                            }
                        }
                    }
                }
                break;
            case 7://黑卒
                if (InArea(fromX, fromY) == 1) {
                    num = 6;
                    for (int i = 0; i < offsetX[num].length; i++) {
                        int toX = fromX + offsetX[num][i];
                        int toY = fromY + offsetY[num][i];
                        if (InArea(toX, toY) != 0 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                            ret.add(new Pos(toX, toY));
                        }
                    }
                } else {
                    num = 7;
                    for (int i = 0; i < offsetX[num].length; i++) {
                        int toX = fromX + offsetX[num][i];
                        int toY = fromY + offsetY[num][i];
                        if (InArea(toX, toY) != 0 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                            ret.add(new Pos(toX, toY));
                        }
                    }
                }
                break;
            case 8://红帅
                num = 0;
                for (int i = 0; i < offsetX[num].length; i++) {
                    int toX = fromX + offsetX[num][i];
                    int toY = fromY + offsetY[num][i];
                    if (InArea(toX, toY) == 4 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                        ret.add(new Pos(toX, toY));
                    }
                }
                Pos eatPos2 = flyKing(2, fromX, fromY, piece);
                if (eatPos2.equals(new Pos(-1, -1)) == false) {
                    ret.add(eatPos2);
                }
                break;
            case 9://红士
                num = 1;
                for (int i = 0; i < offsetX[num].length; i++) {
                    int toX = fromX + offsetX[num][i];
                    int toY = fromY + offsetY[num][i];
                    if (InArea(toX, toY) == 4 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                        ret.add(new Pos(toX, toY));
                    }
                }
                break;
            case 10://红象
                num = 2;
                for (int i = 0; i < offsetX[num].length; i++) {
                    int toX = fromX + offsetX[num][i];
                    int toY = fromY + offsetY[num][i];
                    int blockX = fromX + offsetX[num + 1][i];
                    int blockY = fromY + offsetY[num + 1][i];
                    if (InArea(toX, toY) >= 3 && InArea(toX, toY) <= 4 && IsSameSide(PieceID, piece[toY][toX]) == false && piece[blockY][blockX] == 0) {
                        ret.add(new Pos(toX, toY));
                    }
                }
                break;
            case 14://红兵
                if (InArea(fromX, fromY) == 3) {
                    num = 8;
                    for (int i = 0; i < offsetX[num].length; i++) {
                        int toX = fromX + offsetX[num][i];
                        int toY = fromY + offsetY[num][i];
                        if (InArea(toX, toY) != 0 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                            ret.add(new Pos(toX, toY));
                        }
                    }
                } else {
                    num = 9;
                    for (int i = 0; i < offsetX[num].length; i++) {
                        int toX = fromX + offsetX[num][i];
                        int toY = fromY + offsetY[num][i];
                        if (InArea(toX, toY) != 0 && IsSameSide(PieceID, piece[toY][toX]) == false) {
                            ret.add(new Pos(toX, toY));
                        }
                    }
                }
                break;
            default:
                break;
        }
        return ret;
    }

    public static boolean isKingDanger(int[][] piece, boolean isRedKing) {
        int num = 4;
        int op_block_num = 10;
        if (isRedKing == true) {
            int x = 0, y = 0;
            boolean flag = false;
            for (y = 7; y <= 9; y++) {
                for (x = 3; x <= 5; x++) {
                    if (piece[y][x] == 8) {
                        flag = true;
                        break;
                    }
                }
                if (flag) break;
            }

            for (int i = 0; i < offsetX[num].length; i++) {     //马
                int toX = x + offsetX[num][i];
                int toY = y + offsetY[num][i];
                int blockX = x + offsetX[op_block_num][i];
                int blockY = y + offsetY[op_block_num][i];
                if (InArea(toX, toY) != 0 && piece[toY][toX] == 4 && piece[blockY][blockX] == 0) {
                    return true;
                }
            }
            for (int i = 5; i <= 6; i++) {  //车 炮
                List<Pos> moves = PossibleMoves(piece, x, y, i + 7);
                Iterator<Pos> it = moves.iterator();
                while (it.hasNext()) {
                    Pos pos = it.next();
                    if (piece[pos.y][pos.x] == i) {
                        return true;
                    }
                }
            }
            if (flyKing(2, x, y, piece).equals(new Pos(-1, -1)) == false) { //将
                return true;
            }
            if (piece[y - 1][x] == 7 || piece[y][x - 1] == 7 || piece[y][x + 1] == 7) {
                return true;
            }
        } else {
            int x = 0, y = 0;
            boolean flag = false;
            for (y = 0; y <= 2; y++) {
                for (x = 3; x <= 5; x++) {
                    if (piece[y][x] == 1) {
                        flag = true;
                        break;
                    }
                }
                if (flag) break;
            }

            for (int i = 0; i < offsetX[num].length; i++) {     //马
                int toX = x + offsetX[num][i];
                int toY = y + offsetY[num][i];
                int blockX = x + offsetX[op_block_num][i];
                int blockY = y + offsetY[op_block_num][i];
                if (InArea(toX, toY) != 0 && piece[toY][toX] == 11 && piece[blockY][blockX] == 0) {
                    return true;
                }
            }
            for (int i = 12; i <= 13; i++) {  //车 炮
                List<Pos> moves = PossibleMoves(piece, x, y, i - 7);
                Iterator<Pos> it = moves.iterator();
                while (it.hasNext()) {
                    Pos pos = it.next();
                    if (piece[pos.y][pos.x] == i) {
                        return true;
                    }
                }
            }
            if (flyKing(1, x, y, piece).equals(new Pos(-1, -1)) == false) { //将
                return true;
            }
            if (piece[y + 1][x] == 14 || piece[y][x - 1] == 14 || piece[y][x + 1] == 14) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDead(int[][] piece, boolean isRedKing) {
        if (isRedKing == true) {
            for (int i = 0; i <= 9; i++) {
                for (int j = 0; j <= 8; j++) {
                    if (piece[i][j] >= 8 && piece[i][j] <= 14) {
                        List<Pos> moves = PossibleMoves(piece, j, i, piece[i][j]);
                        Iterator<Pos> it = moves.iterator();
                        while (it.hasNext()) {
                            Pos pos = it.next();
                            int tmp = piece[pos.y][pos.x];
                            piece[pos.y][pos.x] = piece[i][j];
                            piece[i][j] = 0;
                            if (isKingDanger(piece, true) == false) {
                                piece[i][j] = piece[pos.y][pos.x];
                                piece[pos.y][pos.x] = tmp;
                                return false;
                            }
                            piece[i][j] = piece[pos.y][pos.x];
                            piece[pos.y][pos.x] = tmp;
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                for (int j = 0; j <= 8; j++) {
                    if (piece[i][j] >= 1 && piece[i][j] <= 7) {
                        List<Pos> moves = PossibleMoves(piece, j, i, piece[i][j]);
                        Iterator<Pos> it = moves.iterator();
                        while (it.hasNext()) {
                            Pos pos = it.next();
                            int tmp = piece[pos.y][pos.x];
                            piece[pos.y][pos.x] = piece[i][j];
                            piece[i][j] = 0;
                            if (isKingDanger(piece, false) == false) {
                                piece[i][j] = piece[pos.y][pos.x];
                                piece[pos.y][pos.x] = tmp;
                                return false;
                            }
                            piece[i][j] = piece[pos.y][pos.x];
                            piece[pos.y][pos.x] = tmp;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static int InArea(int x, int y) { //0 棋盘外 1 黑盘 2 黑十字 3 红盘 4 红十字
        if (x < 0 || x > 8 || y < 0 || y > 9) {
            return 0;
        }
        return area[y][x];
    }

    public static boolean IsSameSide(int fromID, int toID) {
        if (toID == 0) {
            return false;
        }
        if ((fromID <= 7 && toID <= 7) || (fromID >= 8 && toID >= 8)) {
            return true;
        } else {
            return false;
        }
    }

    public static Pos flyKing(int id, int fromX, int fromY, int[][] piece) {
        int cnt = 0;
        boolean flag = false;
        int i;
        if (id == 1) {  //将
            for (i = fromY + 1; i <= 9; i++) {
                if (piece[i][fromX] > 0 && piece[i][fromX] != 8) {
                    cnt++;
                } else if (piece[i][fromX] == 8) {
                    flag = true;
                    break;
                }
            }
        } else {       //帅
            for (i = fromY - 1; i >= 0; i--) {
                if (piece[i][fromX] > 0 && piece[i][fromX] != 1) {
                    cnt++;
                } else if (piece[i][fromX] == 1) {
                    flag = true;
                    break;
                }
            }
        }
        if (cnt == 0 && flag == true) {
            return new Pos(fromX, i);
        } else {
            return new Pos(-1, -1);
        }
    }

    public static boolean CanMove(int id, int fromX, int fromY, int toX, int toY, int[][] piece) {
        if ((fromX != toX && fromY != toY) || IsSameSide(piece[fromY][fromX], piece[toY][toX]) == true) {
            return false;
        }
        if (id == 1) {  //车
            int start, finish;
            if (fromX == toX) {
                if (fromY < toY) {
                    start = fromY + 1;
                    finish = toY;
                } else {
                    start = toY + 1;
                    finish = fromY;
                }
                for (int i = start; i < finish; i++) {
                    if (piece[i][fromX] != 0) {
                        return false;
                    }
                }
            } else {
                if (fromX < toX) {
                    start = fromX + 1;
                    finish = toX;
                } else {
                    start = toX + 1;
                    finish = fromX;
                }
                for (int i = start; i < finish; i++) {
                    if (piece[fromY][i] != 0) {
                        return false;
                    }
                }
            }
        } else {   //炮
            if (piece[toY][toX] == 0) {
                int start, finish;
                if (fromX == toX) {
                    if (fromY < toY) {
                        start = fromY + 1;
                        finish = toY;
                    } else {
                        start = toY + 1;
                        finish = fromY;
                    }
                    for (int i = start; i < finish; i++) {
                        if (piece[i][fromX] != 0) {
                            return false;
                        }
                    }
                } else {
                    if (fromX < toX) {
                        start = fromX + 1;
                        finish = toX;
                    } else {
                        start = toX + 1;
                        finish = fromX;
                    }
                    for (int i = start; i < finish; i++) {
                        if (piece[fromY][i] != 0) {
                            return false;
                        }
                    }
                }
            } else {
                int start, finish;
                int count = 0;
                if (fromX == toX) {
                    if (fromY < toY) {
                        start = fromY + 1;
                        finish = toY;

                    } else {
                        start = toY + 1;
                        finish = fromY;
                    }
                    for (int i = start; i < finish; i++) {
                        if (piece[i][fromX] != 0) {
                            count++;
                        }
                    }
                } else {
                    if (fromX < toX) {
                        start = fromX + 1;
                        finish = toX;
                    } else {
                        start = toX + 1;
                        finish = fromX;
                    }
                    for (int i = start; i < finish; i++) {
                        if (piece[fromY][i] != 0) {
                            count++;
                        }
                    }
                }
                if (count != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Pos reversePos(Pos pos, boolean IsReverse) {
        return (IsReverse == false) ? pos : new Pos(8 - pos.x, 9 - pos.y);
    }
}
