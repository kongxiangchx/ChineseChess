package com.example.a77304.chessgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;

/**
 * Created by 77304 on 2021/4/5.
 */

public class ChessView extends SurfaceView implements SurfaceHolder.Callback {
    public TutorialThread thread;

    public Bitmap ChessBoard;
    public Bitmap B_box, R_box, Pot;
    public Bitmap[] RP = new Bitmap[7];
    public Bitmap[] BP = new Bitmap[7];

    public Rect cSrcRect, cDesRect;

    public int Board_width, Board_height;

    public ChessInfo chessInfo;

    public ChessView(Context context, ChessInfo chessInfo) {
        super(context);
        this.chessInfo = chessInfo;
        getHolder().addCallback(this);
        init();
    }

    public void init() {
        ChessBoard = BitmapFactory.decodeResource(getResources(), R.drawable.chessboard);

        B_box = BitmapFactory.decodeResource(getResources(), R.drawable.b_box);
        R_box = BitmapFactory.decodeResource(getResources(), R.drawable.r_box);
        Pot = BitmapFactory.decodeResource(getResources(), R.drawable.pot);

        RP[0] = BitmapFactory.decodeResource(getResources(), R.drawable.r_shuai);
        RP[1] = BitmapFactory.decodeResource(getResources(), R.drawable.r_shi);
        RP[2] = BitmapFactory.decodeResource(getResources(), R.drawable.r_xiang);
        RP[3] = BitmapFactory.decodeResource(getResources(), R.drawable.r_ma);
        RP[4] = BitmapFactory.decodeResource(getResources(), R.drawable.r_ju);
        RP[5] = BitmapFactory.decodeResource(getResources(), R.drawable.r_pao);
        RP[6] = BitmapFactory.decodeResource(getResources(), R.drawable.r_bing);

        BP[0] = BitmapFactory.decodeResource(getResources(), R.drawable.b_jiang);
        BP[1] = BitmapFactory.decodeResource(getResources(), R.drawable.b_shi);
        BP[2] = BitmapFactory.decodeResource(getResources(), R.drawable.b_xiang);
        BP[3] = BitmapFactory.decodeResource(getResources(), R.drawable.b_ma);
        BP[4] = BitmapFactory.decodeResource(getResources(), R.drawable.b_ju);
        BP[5] = BitmapFactory.decodeResource(getResources(), R.drawable.b_pao);
        BP[6] = BitmapFactory.decodeResource(getResources(), R.drawable.b_zu);
    }

    public void Draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(ChessBoard, cSrcRect, cDesRect, null);
        Rect pSrcRect, pDesRect;

        for (int i = 0; i < chessInfo.piece.length; i++) {
            for (int j = 0; j < chessInfo.piece[i].length; j++) {
                if (chessInfo.piece[i][j] > 0) {
                    pDesRect = new Rect(Scale(j * 85 + 3), Scale(i * 85 + 41), Scale(j * 85 + 83), Scale(i * 85 + 121));
                    if (chessInfo.piece[i][j] <= 7) {
                        int num = chessInfo.piece[i][j] - 1;
                        pSrcRect = new Rect(0, 0, BP[num].getWidth(), BP[num].getHeight());
                        canvas.drawBitmap(BP[num], pSrcRect, pDesRect, null);
                    }
                    if (chessInfo.piece[i][j] >= 8) {
                        int num = chessInfo.piece[i][j] - 8;
                        pSrcRect = new Rect(0, 0, RP[num].getWidth(), RP[num].getHeight());
                        canvas.drawBitmap(RP[num], pSrcRect, pDesRect, null);
                    }
                }
            }
        }

        int i = chessInfo.Select[0], j = chessInfo.Select[1];
        if (i >= 0 && j >= 0 && chessInfo.piece[j][i] > 0) {
            if (chessInfo.IsRedGo == true && chessInfo.piece[j][i] >= 8) {
                pSrcRect = new Rect(0, 0, R_box.getWidth(), R_box.getHeight());
                pDesRect = new Rect(Scale(i * 85 + 3), Scale(j * 85 + 41), Scale(i * 85 + 83), Scale(j * 85 + 121));
                canvas.drawBitmap(R_box, pSrcRect, pDesRect, null);

                Iterator<Pos> it = chessInfo.ret.iterator();
                while (it.hasNext()) {
                    Pos pos = it.next();
                    int x = pos.x, y = pos.y;
                    pSrcRect = new Rect(0, 0, Pot.getWidth(), Pot.getHeight());
                    pDesRect = new Rect(Scale(x * 85 + 3), Scale(y * 85 + 41), Scale(x * 85 + 83), Scale(y * 85 + 121));
                    canvas.drawBitmap(Pot, pSrcRect, pDesRect, null);
                }
            } else if (chessInfo.IsRedGo == false && chessInfo.piece[j][i] <= 7) {
                pSrcRect = new Rect(0, 0, B_box.getWidth(), B_box.getHeight());
                pDesRect = new Rect(Scale(i * 85 + 3), Scale(j * 85 + 41), Scale(i * 85 + 83), Scale(j * 85 + 121));
                canvas.drawBitmap(B_box, pSrcRect, pDesRect, null);

                Iterator<Pos> it = chessInfo.ret.iterator();
                while (it.hasNext()) {
                    Pos pos = it.next();
                    int x = pos.x, y = pos.y;
                    pSrcRect = new Rect(0, 0, Pot.getWidth(), Pot.getHeight());
                    pDesRect = new Rect(Scale(x * 85 + 3), Scale(y * 85 + 41), Scale(x * 85 + 83), Scale(y * 85 + 121));
                    canvas.drawBitmap(Pot, pSrcRect, pDesRect, null);
                }
            }
        }

        if (chessInfo.prePos.equals(new Pos(-1, -1)) == false && chessInfo.IsChecked == false) {
            int preX = chessInfo.prePos.x;
            int preY = chessInfo.prePos.y;
            int curX = chessInfo.curPos.x;
            int curY = chessInfo.curPos.y;
            Rect tmpRect;

            pDesRect = new Rect(Scale(curX * 85 + 3), Scale(curY * 85 + 41), Scale(curX * 85 + 83), Scale(curY * 85 + 121));
            tmpRect = new Rect(Scale(preX * 85 + 3), Scale(preY * 85 + 41), Scale(preX * 85 + 83), Scale(preY * 85 + 121));

            if (chessInfo.piece[curY][curX] >= 1 && chessInfo.piece[curY][curX] <= 7) {
                pSrcRect = new Rect(0, 0, B_box.getWidth(), B_box.getHeight());
                canvas.drawBitmap(B_box, pSrcRect, pDesRect, null);
                canvas.drawBitmap(B_box, pSrcRect, tmpRect, null);
            } else {
                pSrcRect = new Rect(0, 0, R_box.getWidth(), R_box.getHeight());
                canvas.drawBitmap(R_box, pSrcRect, pDesRect, null);
                canvas.drawBitmap(R_box, pSrcRect, tmpRect, null);
            }
        }
    }

    public int Scale(int x) {
        return x * Board_width / 768;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Board_width = MeasureSpec.getSize(widthMeasureSpec);
        Board_height = Board_width * 909 / 750;

        cSrcRect = new Rect(0, 0, ChessBoard.getWidth(), ChessBoard.getHeight());
        cDesRect = new Rect(0, 0, Board_width, Board_height);

        setMeasuredDimension(Board_width, Board_height);

    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.thread = new TutorialThread(getHolder());
        this.thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class TutorialThread extends Thread {//刷帧线程
        public int span = 100;//睡眠100毫秒数
        public SurfaceHolder surfaceHolder;

        public TutorialThread(SurfaceHolder surfaceHolder) {
            this.surfaceHolder = surfaceHolder;
        }

        public void run() {//重写的方法
            Canvas c;//画布
            while (true) {//循环绘制
                c = this.surfaceHolder.lockCanvas();
                try {
                    Draw(c);//绘制方法
                } catch (Exception e) {
                }
                if (c != null) this.surfaceHolder.unlockCanvasAndPost(c);
                try {
                    Thread.sleep(span);//睡眠时间，单位是毫秒
                } catch (Exception e) {
                    e.printStackTrace();//输出异常堆栈信息
                }
            }
        }
    }
}