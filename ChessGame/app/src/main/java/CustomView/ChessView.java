package CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.a77304.chessgame.R;

import java.util.Iterator;

import ChessMove.Rule;
import Info.ChessInfo;
import Info.Pos;

/**
 * Created by 77304 on 2021/4/5.
 */

public class ChessView extends SurfaceView implements SurfaceHolder.Callback {
    public TutorialThread thread;

    public Paint paint;

    public Bitmap ChessBoard;
    public Bitmap B_box, R_box, Pot;
    public Bitmap[] RP = new Bitmap[7];
    public Bitmap[] BP = new Bitmap[7];

    public Rect cSrcRect, cDesRect;

    public int Board_width, Board_height;

    public ChessInfo chessInfo;


    public String[] thinkMood=new String[]{"😀","🙂","😶","😣","😵","😭"};
    public int thinkIndex=0;
    public int thinkFlag=0;
    public String thinkContent="😀·····";

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
                Pos drawPos = Rule.reversePos(new Pos(j, i), chessInfo.IsReverse);
                if (chessInfo.piece[i][j] > 0) {
                    pDesRect = new Rect(Scale(drawPos.x * 85 + 3), Scale(drawPos.y * 85 + 41), Scale(drawPos.x * 85 + 83), Scale(drawPos.y * 85 + 121));
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

        int drawX = chessInfo.Select[0], drawY = chessInfo.Select[1];
        Pos realPos = Rule.reversePos(new Pos(chessInfo.Select[0], chessInfo.Select[1]), chessInfo.IsReverse);
        int realX = realPos.x, realY = realPos.y;
        if (drawX >= 0 && drawY >= 0 && chessInfo.piece[realY][realX] > 0) {
            if (chessInfo.IsRedGo == true && chessInfo.piece[realY][realX] >= 8) {
                pSrcRect = new Rect(0, 0, R_box.getWidth(), R_box.getHeight());
                pDesRect = new Rect(Scale(drawX * 85 + 3), Scale(drawY * 85 + 41), Scale(drawX * 85 + 83), Scale(drawY * 85 + 121));
                canvas.drawBitmap(R_box, pSrcRect, pDesRect, null);

                Iterator<Pos> it = chessInfo.ret.iterator();
                while (it.hasNext()) {
                    Pos pos = it.next();
                    pos = Rule.reversePos(pos, chessInfo.IsReverse);
                    int x = pos.x, y = pos.y;
                    pSrcRect = new Rect(0, 0, Pot.getWidth(), Pot.getHeight());
                    pDesRect = new Rect(Scale(x * 85 + 3), Scale(y * 85 + 41), Scale(x * 85 + 83), Scale(y * 85 + 121));
                    canvas.drawBitmap(Pot, pSrcRect, pDesRect, null);
                }
            } else if (chessInfo.IsRedGo == false && chessInfo.piece[realY][realX] <= 7) {
                pSrcRect = new Rect(0, 0, B_box.getWidth(), B_box.getHeight());
                pDesRect = new Rect(Scale(drawX * 85 + 3), Scale(drawY * 85 + 41), Scale(drawX * 85 + 83), Scale(drawY * 85 + 121));
                canvas.drawBitmap(B_box, pSrcRect, pDesRect, null);

                Iterator<Pos> it = chessInfo.ret.iterator();
                while (it.hasNext()) {
                    Pos pos = it.next();
                    pos = Rule.reversePos(pos, chessInfo.IsReverse);
                    int x = pos.x, y = pos.y;
                    pSrcRect = new Rect(0, 0, Pot.getWidth(), Pot.getHeight());
                    pDesRect = new Rect(Scale(x * 85 + 3), Scale(y * 85 + 41), Scale(x * 85 + 83), Scale(y * 85 + 121));
                    canvas.drawBitmap(Pot, pSrcRect, pDesRect, null);
                }
            }
        }

        if (chessInfo.prePos.equals(new Pos(-1, -1)) == false && chessInfo.IsChecked == false) {
            int real_curX = chessInfo.curPos.x;
            int real_curY = chessInfo.curPos.y;

            Pos realPre = Rule.reversePos(chessInfo.prePos, chessInfo.IsReverse);
            Pos realCur = Rule.reversePos(chessInfo.curPos, chessInfo.IsReverse);
            int draw_preX = realPre.x;
            int draw_preY = realPre.y;
            int draw_curX = realCur.x;
            int draw_curY = realCur.y;

            Rect tmpRect;

            pDesRect = new Rect(Scale(draw_curX * 85 + 3), Scale(draw_curY * 85 + 41), Scale(draw_curX * 85 + 83), Scale(draw_curY * 85 + 121));
            tmpRect = new Rect(Scale(draw_preX * 85 + 3), Scale(draw_preY * 85 + 41), Scale(draw_preX * 85 + 83), Scale(draw_preY * 85 + 121));

            if (chessInfo.piece[real_curY][real_curX] >= 1 && chessInfo.piece[real_curY][real_curX] <= 7) {
                pSrcRect = new Rect(0, 0, B_box.getWidth(), B_box.getHeight());
                canvas.drawBitmap(B_box, pSrcRect, pDesRect, null);
                canvas.drawBitmap(B_box, pSrcRect, tmpRect, null);
            } else {
                pSrcRect = new Rect(0, 0, R_box.getWidth(), R_box.getHeight());
                canvas.drawBitmap(R_box, pSrcRect, pDesRect, null);
                canvas.drawBitmap(R_box, pSrcRect, tmpRect, null);
            }
        }

        if (chessInfo.status == 1) {
            if (chessInfo.isMachine == true) {
                if(thinkFlag==0){
                    thinkContent="";
                    for(int i=0;i<thinkIndex;i++){
                        thinkContent+='·';
                    }
                    thinkContent+=thinkMood[thinkIndex];
                    for(int i=thinkIndex+1;i<6;i++){
                        thinkContent+='·';
                    }
                    thinkIndex=(thinkIndex+1)%6;
                }
                thinkFlag=(thinkFlag+1)%5;
                canvas.drawText(thinkContent, Board_width / 2, Board_height / 2 + Scale(57)*7/20, paint);
            } else {
                thinkIndex=0;
                thinkContent="😀·····";
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

        paint = new Paint();
        paint.setTextSize(Scale(57));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
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