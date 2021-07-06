package com.example.a77304.chessgame;

import android.content.SharedPreferences;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import AICore.AI;
import AICore.KnowledgeBase;
import AICore.Move;
import AICore.TransformTable;
import ChessMove.Rule;
import CustomDialog.CommonDialog;
import CustomDialog.RetryDialog;
import CustomDialog.SettingDialog_PvM;
import CustomView.RoundView;
import Info.ChessInfo;
import Info.InfoSet;
import Info.Pos;
import Info.SaveInfo;
import CustomView.ChessView;

import static AICore.AI.getBestMove;
import static com.example.a77304.chessgame.HomeActivity.MIN_CLICK_DELAY_TIME;
import static com.example.a77304.chessgame.HomeActivity.backMusic;
import static com.example.a77304.chessgame.HomeActivity.checkMusic;
import static com.example.a77304.chessgame.HomeActivity.clickMusic;
import static com.example.a77304.chessgame.HomeActivity.curClickTime;
import static com.example.a77304.chessgame.HomeActivity.lastClickTime;
import static com.example.a77304.chessgame.HomeActivity.playEffect;
import static com.example.a77304.chessgame.HomeActivity.playMusic;
import static com.example.a77304.chessgame.HomeActivity.selectMusic;
import static com.example.a77304.chessgame.HomeActivity.setting;
import static com.example.a77304.chessgame.HomeActivity.sharedPreferences;
import static com.example.a77304.chessgame.HomeActivity.stopMusic;
import static com.example.a77304.chessgame.HomeActivity.winMusic;


public class PvMActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    public RelativeLayout relativeLayout;
    public ChessInfo chessInfo;
    public InfoSet infoSet;
    public ChessView chessView;
    public RoundView roundView;

    public static KnowledgeBase knowledgeBase;
    public static TransformTable transformTable;

    public Thread AIThread = new Thread(new Runnable() {
        @Override
        public void run() {
            AIMove(chessInfo.IsRedGo, chessInfo.status);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvm);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        if (SaveInfo.fileIsExists("ChessInfo_pvm.bin")) {
            try {
                chessInfo = SaveInfo.DeserializeChessInfo("ChessInfo_pvm.bin");
            } catch (Exception e) {
                Log.e("chen", e.toString());
            }
        } else {
            chessInfo = new ChessInfo();
        }

        if (SaveInfo.fileIsExists("InfoSet_pvm.bin")) {
            try {
                infoSet = SaveInfo.DeserializeInfoSet("InfoSet_pvm.bin");
            } catch (Exception e) {
                Log.e("chen", e.toString());
            }
        } else {
            infoSet = new InfoSet();
        }

        if (SaveInfo.fileIsExists("TransformTable.bin")) {
            try {
                transformTable = SaveInfo.DeserializeTransformTable("TransformTable.bin");
            } catch (Exception e) {
                Log.e("chen", e.toString());
            }
        } else {
            transformTable = new TransformTable();
        }

        if (SaveInfo.fileIsExists("KnowledgeBase.bin")) {
            try {
                knowledgeBase = SaveInfo.DeserializeKnowledgeBase("KnowledgeBase.bin");
            } catch (Exception e) {
                Log.e("chen", e.toString());
            }
        } else {
            knowledgeBase = new KnowledgeBase();
        }

        roundView = new RoundView(this, chessInfo);
        relativeLayout.addView(roundView);

        RelativeLayout.LayoutParams paramsRound = (RelativeLayout.LayoutParams) roundView.getLayoutParams();
        paramsRound.addRule(RelativeLayout.CENTER_IN_PARENT);
        paramsRound.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        paramsRound.setMargins(30, 30, 30, 30);
        roundView.setLayoutParams(paramsRound);
        roundView.setId(R.id.roundView);

        chessView = new ChessView(this, chessInfo);
        chessView.setOnTouchListener(this);
        relativeLayout.addView(chessView);

        RelativeLayout.LayoutParams paramsChess = (RelativeLayout.LayoutParams) chessView.getLayoutParams();
        paramsChess.addRule(RelativeLayout.BELOW, R.id.roundView);
        chessView.setLayoutParams(paramsChess);
        chessView.setId(R.id.chessView);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout buttonGroup = (RelativeLayout) inflater.inflate(R.layout.button_group, relativeLayout, false);
        relativeLayout.addView(buttonGroup);

        RelativeLayout.LayoutParams paramsV = (RelativeLayout.LayoutParams) buttonGroup.getLayoutParams();
        paramsV.addRule(RelativeLayout.BELOW, R.id.chessView);
        buttonGroup.setLayoutParams(paramsV);

        for (int i = 0; i < buttonGroup.getChildCount(); i++) {
            Button btn = (Button) buttonGroup.getChildAt(i);
            btn.setOnClickListener(this);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return false;
        }
        curClickTime = lastClickTime;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (chessInfo.status == 1) {
                if (x >= 0 && x <= chessView.Board_width && y >= 0 && y <= chessView.Board_height) {
                    chessInfo.Select = getPos(event);
                    Pos realPos = Rule.reversePos(new Pos(chessInfo.Select[0], chessInfo.Select[1]), chessInfo.IsReverse);
                    int i = realPos.x, j = realPos.y;

                    if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {
                        if (chessInfo.IsRedGo == true && setting.isPlayerRed == true) {
                            if (chessInfo.IsChecked == false) {
                                if (chessInfo.piece[j][i] >= 8 && chessInfo.piece[j][i] <= 14) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.IsChecked = true;
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    playEffect(selectMusic);
                                }
                            } else {
                                if (chessInfo.piece[j][i] >= 8 && chessInfo.piece[j][i] <= 14) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    playEffect(selectMusic);
                                } else if (chessInfo.ret.contains(new Pos(i, j))) {
                                    int tmp = chessInfo.piece[j][i];
                                    chessInfo.piece[j][i] = chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x];
                                    chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = 0;
                                    if (Rule.isKingDanger(chessInfo.piece, true)) {
                                        chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = chessInfo.piece[j][i];
                                        chessInfo.piece[j][i] = tmp;
                                        Toast toast = Toast.makeText(PvMActivity.this, "帅被将军", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = false;
                                        chessInfo.curPos = new Pos(i, j);

                                        chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[j][i], tmp);

                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                        playEffect(clickMusic);
                                        int key = 0;
                                        if (Rule.isKingDanger(chessInfo.piece, false)) {
                                            key = 1;
                                        }
                                        if (Rule.isDead(chessInfo.piece, false)) {
                                            key = 2;
                                        }
                                        if (key == 1) {
                                            playEffect(checkMusic);
                                            Toast toast = Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        } else if (key == 2) {
                                            playEffect(winMusic);
                                            chessInfo.status = 2;
                                            Toast toast = Toast.makeText(PvMActivity.this, "红方获得胜利", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                        if (chessInfo.status == 1) {
                                            if (chessInfo.peaceRound >= 60) {
                                                chessInfo.status = 2;
                                                Toast toast = Toast.makeText(PvMActivity.this, "双方60回合内未吃子，此乃和棋", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            } else if (chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0) {
                                                chessInfo.status = 2;
                                                Toast toast = Toast.makeText(PvMActivity.this, "双方都无攻击性棋子，此乃和棋", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            } else if (infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck) >= 4) {
                                                chessInfo.status = 2;
                                                Toast toast = Toast.makeText(PvMActivity.this, "重复局面出现4次，此乃和棋", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            }
                                        }

                                        AIThread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(400);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                chessInfo.isMachine = true;

                                                AIMove(chessInfo.IsRedGo, chessInfo.status);
                                            }
                                        });
                                        AIThread.start();
                                    }
                                }
                            }
                        } else if (chessInfo.IsRedGo == false && setting.isPlayerRed == false) {
                            if (chessInfo.IsChecked == false) {
                                if (chessInfo.piece[j][i] >= 1 && chessInfo.piece[j][i] <= 7) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.IsChecked = true;
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    playEffect(selectMusic);
                                }
                            } else {
                                if (chessInfo.piece[j][i] >= 1 && chessInfo.piece[j][i] <= 7) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    playEffect(selectMusic);
                                } else if (chessInfo.ret.contains(new Pos(i, j))) {
                                    int tmp = chessInfo.piece[j][i];
                                    chessInfo.piece[j][i] = chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x];
                                    chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = 0;
                                    if (Rule.isKingDanger(chessInfo.piece, false)) {
                                        chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = chessInfo.piece[j][i];
                                        chessInfo.piece[j][i] = tmp;
                                        Toast toast = Toast.makeText(PvMActivity.this, "将被将军", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = true;
                                        chessInfo.curPos = new Pos(i, j);

                                        chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[j][i], tmp);

                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                        playEffect(clickMusic);
                                        int key = 0;
                                        if (Rule.isKingDanger(chessInfo.piece, true)) {
                                            key = 1;
                                        }
                                        if (Rule.isDead(chessInfo.piece, true)) {
                                            key = 2;
                                        }
                                        if (key == 1) {
                                            playEffect(checkMusic);
                                            Toast toast = Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            try {
                                                Thread.sleep(1000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        } else if (key == 2) {
                                            playEffect(winMusic);
                                            chessInfo.status = 2;
                                            Toast toast = Toast.makeText(PvMActivity.this, "黑方获得胜利", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                        if (chessInfo.status == 1) {
                                            if (chessInfo.peaceRound >= 60) {
                                                chessInfo.status = 2;
                                                Toast toast = Toast.makeText(PvMActivity.this, "双方60回合内未吃子，此乃和棋", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            } else if (chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0) {
                                                chessInfo.status = 2;
                                                Toast toast = Toast.makeText(PvMActivity.this, "双方都无攻击性棋子，此乃和棋", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            } else if (infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck) >= 4) {
                                                chessInfo.status = 2;
                                                Toast toast = Toast.makeText(PvMActivity.this, "重复局面出现4次，此乃和棋", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                toast.show();
                                            }
                                        }

                                        AIThread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Thread.sleep(400);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                chessInfo.isMachine = true;

                                                AIMove(chessInfo.IsRedGo, chessInfo.status);
                                            }
                                        });
                                        AIThread.start();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int[] getPos(MotionEvent e) {
        int[] pos = new int[2];
        double x = e.getX();
        double y = e.getY();
        int[] dis = new int[]{
                chessView.Scale(3), chessView.Scale(41), chessView.Scale(80), chessView.Scale(85)
        };
        x = x - dis[0];
        y = y - dis[1];
        if (x % dis[3] <= dis[2] && y % dis[3] <= dis[2]) {
            pos[0] = (int) Math.floor(x / dis[3]);
            pos[1] = (int) Math.floor(y / dis[3]);
            if (pos[0] >= 9 || pos[1] >= 10) {
                pos[0] = pos[1] = -1;
            }
        } else {
            pos[0] = pos[1] = -1;
        }
        return pos;
    }

    @Override
    public void onClick(View view) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        curClickTime = lastClickTime;

        playEffect(selectMusic);
        switch (view.getId()) {
            case R.id.btn_retry:
                if (chessInfo.isMachine == true && chessInfo.status == 1) {
                    Toast toast = Toast.makeText(PvMActivity.this, "请等待电脑思考", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                final RetryDialog retryDialog = new RetryDialog(this);
                retryDialog.setOnClickBottomListener(new RetryDialog.OnClickBottomListener() {

                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        try {
                            chessInfo.setInfo(new ChessInfo());
                            infoSet.newInfo();
                            transformTable.transformInfos.clear();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (setting.isPlayerRed != retryDialog.isPlayerRed) {
                            setting.isPlayerRed = retryDialog.isPlayerRed;
                            editor.putBoolean("isPlayerRed", retryDialog.isPlayerRed);
                            editor.commit();
                        }
                        retryDialog.dismiss();
                        chessInfo.IsReverse = (setting.isPlayerRed == true) ? false : true;
                        if (chessInfo.IsReverse == true) {
                            try {
                                infoSet.curInfo.setInfo(chessInfo);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (setting.isPlayerRed == false) {
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            AIFirstGo();
                        }
                    }

                    @Override
                    public void onNegtiveClick() {
                        playEffect(selectMusic);
                        retryDialog.dismiss();
                    }
                });
                retryDialog.show();
                break;
            case R.id.btn_recall:
                if (chessInfo.isMachine == true && chessInfo.status == 1) {
                    Toast toast = Toast.makeText(PvMActivity.this, "请等待电脑思考", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                }
                int cnt = 0;
                int total = 2;
                if (chessInfo.status == 2 && chessInfo.isMachine == true) {
                    total = 1;
                }
                if (infoSet.preInfo.size() < total) {
                    break;
                }
                while (!infoSet.preInfo.empty()) {
                    ChessInfo tmp = infoSet.preInfo.pop();
                    cnt++;
                    try {
                        infoSet.recallZobristInfo(chessInfo.ZobristKeyCheck);
                        chessInfo.setInfo(tmp);
                        infoSet.curInfo.setInfo(tmp);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    if (cnt >= total) {
                        break;
                    }
                }
                break;
            case R.id.btn_setting:
                final SettingDialog_PvM settingDialog_pvm = new SettingDialog_PvM(this);
                settingDialog_pvm.setOnClickBottomListener(new SettingDialog_PvM.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean flag = false;
                        if (setting.isMusicPlay != settingDialog_pvm.isMusicPlay) {
                            setting.isMusicPlay = settingDialog_pvm.isMusicPlay;
                            if (setting.isMusicPlay) {
                                playMusic(backMusic);
                            } else {
                                stopMusic(backMusic);
                            }
                            editor.putBoolean("isMusicPlay", settingDialog_pvm.isMusicPlay);
                            flag = true;
                        }
                        if (setting.isEffectPlay != settingDialog_pvm.isEffectPlay) {
                            setting.isEffectPlay = settingDialog_pvm.isEffectPlay;
                            editor.putBoolean("isEffectPlay", settingDialog_pvm.isEffectPlay);
                            flag = true;
                        }
                        if (setting.mLevel != settingDialog_pvm.mLevel) {
                            setting.mLevel = settingDialog_pvm.mLevel;
                            editor.putInt("mLevel", settingDialog_pvm.mLevel);
                            flag = true;
                        }
                        if (flag) {
                            editor.commit();
                        }
                        settingDialog_pvm.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
                        playEffect(selectMusic);
                        settingDialog_pvm.dismiss();
                    }
                });
                settingDialog_pvm.show();
                break;
            case R.id.btn_back:
                final CommonDialog backDialog = new CommonDialog(this, "返回", "确认要返回吗");
                backDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {

                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        backDialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onNegtiveClick() {
                        playEffect(selectMusic);
                        backDialog.dismiss();
                    }
                });
                backDialog.show();
                break;
            default:
                break;
        }
    }

    public void AIMove(boolean isRed, int status) {
        if (status != 1) return;
        int depth = setting.mLevel * 2;
        if (isRed == true) {
            Move move = knowledgeBase.readBestMoves(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth);
            if (move == null) {
                long t1 = System.currentTimeMillis();
                move = getBestMove(chessInfo.piece, true, depth, chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, infoSet.ZobristInfo);
                long t2 = System.currentTimeMillis();
                Log.i("AI思考", "AI思考的时间：" + String.valueOf(t2 - t1) + "ms");
                knowledgeBase.saveBestMove(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth, move);
            }

            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            int tmp = chessInfo.piece[toPos.y][toPos.x];
            chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
            chessInfo.piece[fromPos.y][fromPos.x] = 0;
            chessInfo.IsChecked = false;
            chessInfo.IsRedGo = false;
            chessInfo.Select = new int[]{-1, -1};
            chessInfo.ret.clear();
            chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
            chessInfo.curPos = new Pos(toPos.x, toPos.y);

            chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[toPos.y][toPos.x], tmp);
            chessInfo.isMachine = false;

            try {
                infoSet.pushInfo(chessInfo);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            playEffect(clickMusic);

            int key = 0;
            if (Rule.isKingDanger(chessInfo.piece, false)) {
                key = 1;
            }
            if (Rule.isDead(chessInfo.piece, false)) {
                key = 2;
            }
            if (key == 1) {
                playEffect(checkMusic);
                Looper.prepare();
                Toast toast = Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            } else if (key == 2) {
                playEffect(winMusic);
                chessInfo.status = 2;
                Looper.prepare();
                Toast toast = Toast.makeText(PvMActivity.this, "红方获得胜利", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            }

            if (chessInfo.status == 1) {
                if (chessInfo.peaceRound >= 60) {
                    chessInfo.status = 2;
                    Looper.prepare();
                    Toast toast = Toast.makeText(PvMActivity.this, "双方60回合内未吃子，此乃和棋", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                } else if (chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0) {
                    chessInfo.status = 2;
                    Looper.prepare();
                    Toast toast = Toast.makeText(PvMActivity.this, "双方都无攻击性棋子，此乃和棋", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                } else if (infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck) >= 4) {
                    chessInfo.status = 2;
                    Looper.prepare();
                    Toast toast = Toast.makeText(PvMActivity.this, "重复局面出现4次，此乃和棋", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                }
            }
        } else {
            Move move = knowledgeBase.readBestMoves(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth);
            if (move == null) {
                long t1 = System.currentTimeMillis();
                move = getBestMove(chessInfo.piece, false, depth, chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, infoSet.ZobristInfo);
                long t2 = System.currentTimeMillis();
                Log.i("AI思考", "AI思考的时间：" + String.valueOf(t2 - t1) + "ms");
                knowledgeBase.saveBestMove(chessInfo.ZobristKey, chessInfo.ZobristKeyCheck, depth, move);
            }

            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            int tmp = chessInfo.piece[toPos.y][toPos.x];
            chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
            chessInfo.piece[fromPos.y][fromPos.x] = 0;
            chessInfo.IsChecked = false;
            chessInfo.IsRedGo = true;
            chessInfo.Select = new int[]{-1, -1};
            chessInfo.ret.clear();
            chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
            chessInfo.curPos = new Pos(toPos.x, toPos.y);

            chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[toPos.y][toPos.x], tmp);
            chessInfo.isMachine = false;

            try {
                infoSet.pushInfo(chessInfo);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            playEffect(clickMusic);

            int key = 0;
            if (Rule.isKingDanger(chessInfo.piece, true)) {
                key = 1;
            }
            if (Rule.isDead(chessInfo.piece, true)) {
                key = 2;
            }
            if (key == 1) {
                playEffect(checkMusic);
                Looper.prepare();
                Toast toast = Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            } else if (key == 2) {
                playEffect(winMusic);
                chessInfo.status = 2;
                Looper.prepare();
                Toast toast = Toast.makeText(PvMActivity.this, "黑方获得胜利", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            }

            if (chessInfo.status == 1) {
                if (chessInfo.peaceRound >= 60) {
                    chessInfo.status = 2;
                    Looper.prepare();
                    Toast toast = Toast.makeText(PvMActivity.this, "双方60回合内未吃子，此乃和棋", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                } else if (chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0) {
                    chessInfo.status = 2;
                    Looper.prepare();
                    Toast toast = Toast.makeText(PvMActivity.this, "双方都无攻击性棋子，此乃和棋", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                } else if (infoSet.ZobristInfo.get(chessInfo.ZobristKeyCheck) >= 4) {
                    chessInfo.status = 2;
                    Looper.prepare();
                    Toast toast = Toast.makeText(PvMActivity.this, "重复局面出现4次，此乃和棋", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Looper.loop();
                }
            }
        }
    }

    public void AIFirstGo() {
        Move[] firstMoves = new Move[8];
        firstMoves[0] = new Move(new Pos(1, 9), new Pos(2, 7));     //走马
        firstMoves[1] = new Move(new Pos(7, 9), new Pos(6, 7));     //走马
        firstMoves[2] = new Move(new Pos(2, 9), new Pos(4, 7));     //走相
        firstMoves[3] = new Move(new Pos(6, 9), new Pos(4, 7));     //走相
        firstMoves[4] = new Move(new Pos(1, 7), new Pos(4, 7));     //走炮
        firstMoves[5] = new Move(new Pos(7, 7), new Pos(4, 7));     //走炮
        firstMoves[6] = new Move(new Pos(2, 6), new Pos(2, 5));     //走兵
        firstMoves[7] = new Move(new Pos(6, 6), new Pos(6, 5));     //走兵

        int num = (int) (Math.random() * firstMoves.length);
        //Log.e("chen",String.valueOf(num));
        Move firstMove = firstMoves[num];
        Pos fromPos = firstMove.fromPos;
        Pos toPos = firstMove.toPos;
        int tmp = chessInfo.piece[toPos.y][toPos.x];
        chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
        chessInfo.piece[fromPos.y][fromPos.x] = 0;
        chessInfo.IsChecked = false;
        chessInfo.IsRedGo = false;
        chessInfo.Select = new int[]{-1, -1};
        chessInfo.ret.clear();
        chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
        chessInfo.curPos = new Pos(toPos.x, toPos.y);

        chessInfo.updateAllInfo(chessInfo.prePos, chessInfo.curPos, chessInfo.piece[toPos.y][toPos.x], tmp);

        try {
            infoSet.pushInfo(chessInfo);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        playEffect(clickMusic);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final CommonDialog backDialog = new CommonDialog(this, "返回", "确认要返回吗");
            backDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {

                @Override
                public void onPositiveClick() {
                    playEffect(selectMusic);
                    backDialog.dismiss();
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    playEffect(selectMusic);
                    backDialog.dismiss();
                }
            });
            backDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        stopMusic(backMusic);
        super.onPause();
    }

    @Override
    protected void onStop() {
        try {
            SaveInfo.SerializeChessInfo(chessInfo, "ChessInfo_pvm.bin");
            SaveInfo.SerializeInfoSet(infoSet, "InfoSet_pvm.bin");
            SaveInfo.SerializeKnowledgeBase(knowledgeBase, "KnowledgeBase.bin");
            SaveInfo.SerializeTransformTable(transformTable, "TransformTable.bin");
        } catch (Exception e) {
            Log.e("chen", e.toString());
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        playMusic(backMusic);
        if (chessInfo.isMachine) {
            if (AIThread.isAlive() == false) {
                AIThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AIMove(chessInfo.IsRedGo, chessInfo.status);
                    }
                });
                AIThread.start();
            }
        }
        super.onStart();
    }

}
