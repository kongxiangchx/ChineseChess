package com.example.a77304.chessgame;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static com.example.a77304.chessgame.HomeActivity.backMusic;
import static com.example.a77304.chessgame.HomeActivity.checkMusic;
import static com.example.a77304.chessgame.HomeActivity.clickMusic;
import static com.example.a77304.chessgame.HomeActivity.selectMusic;
import static com.example.a77304.chessgame.HomeActivity.setting;
import static com.example.a77304.chessgame.HomeActivity.sharedPreferences;
import static com.example.a77304.chessgame.HomeActivity.winMusic;


public class PvMActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private RelativeLayout relativeLayout;
    private ChessInfo chessInfo;
    private InfoSet infoSet;
    private ChessView chessView;
    private RoundView roundView;

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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (chessInfo.status == 1) {
                if (x >= 0 && x <= chessView.Board_width && y >= 0 && y <= chessView.Board_height) {
                    chessInfo.Select = getPos(event);
                    Pos realPos = Rule.reversePos(new Pos(chessInfo.Select[0], chessInfo.Select[1]), chessInfo.IsReverse);
                    int i = realPos.x, j = realPos.y;

                    if (i >= 0 && j >= 0) {
                        if (chessInfo.IsRedGo == true && setting.isPlayerRed == true) {
                            if (chessInfo.IsChecked == false) {
                                if (chessInfo.piece[j][i] >= 8 && chessInfo.piece[j][i] <= 14) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.IsChecked = true;
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    if (setting.isEffectPlay) {
                                        selectMusic.start();
                                    }
                                }
                            } else {
                                if (chessInfo.piece[j][i] >= 8 && chessInfo.piece[j][i] <= 14) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    if (setting.isEffectPlay) {
                                        selectMusic.start();
                                    }
                                } else if (chessInfo.ret.contains(new Pos(i, j))) {
                                    int tmp = chessInfo.piece[j][i];
                                    chessInfo.piece[j][i] = chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x];
                                    chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = 0;
                                    if (Rule.isKingDanger(chessInfo.piece, true)) {
                                        chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = chessInfo.piece[j][i];
                                        chessInfo.piece[j][i] = tmp;
                                        Toast.makeText(PvMActivity.this, "帅被将军", Toast.LENGTH_SHORT).show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = false;
                                        chessInfo.curPos = new Pos(i, j);
                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                        if (setting.isEffectPlay) {
                                            clickMusic.start();
                                        }
                                        int key = 0;
                                        if (Rule.isKingDanger(chessInfo.piece, false)) {
                                            key = 1;
                                        }
                                        if (Rule.isDead(chessInfo.piece, false)) {
                                            key = 2;
                                        }
                                        if (key == 1) {
                                            if (setting.isEffectPlay) {
                                                checkMusic.start();
                                            }
                                            Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT).show();
                                        } else if (key == 2) {
                                            if (setting.isEffectPlay) {
                                                winMusic.start();
                                            }
                                            chessInfo.status = 2;
                                            Toast.makeText(PvMActivity.this, "黑方失败", Toast.LENGTH_SHORT).show();
                                        }
                                        AIMove(false, chessInfo.status);
                                    }
                                }
                            }
                        } else if (chessInfo.IsRedGo == false && setting.isPlayerRed == false) {
                            if (chessInfo.IsChecked == false) {
                                if (chessInfo.piece[j][i] >= 1 && chessInfo.piece[j][i] <= 7) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.IsChecked = true;
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    if (setting.isEffectPlay) {
                                        selectMusic.start();
                                    }
                                }
                            } else {
                                if (chessInfo.piece[j][i] >= 1 && chessInfo.piece[j][i] <= 7) {
                                    chessInfo.prePos = new Pos(i, j);
                                    chessInfo.ret = Rule.PossibleMoves(chessInfo.piece, i, j, chessInfo.piece[j][i]);
                                    if (setting.isEffectPlay) {
                                        selectMusic.start();
                                    }
                                } else if (chessInfo.ret.contains(new Pos(i, j))) {
                                    int tmp = chessInfo.piece[j][i];
                                    chessInfo.piece[j][i] = chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x];
                                    chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = 0;
                                    if (Rule.isKingDanger(chessInfo.piece, false)) {
                                        chessInfo.piece[chessInfo.prePos.y][chessInfo.prePos.x] = chessInfo.piece[j][i];
                                        chessInfo.piece[j][i] = tmp;
                                        Toast.makeText(PvMActivity.this, "将被将军", Toast.LENGTH_SHORT).show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = true;
                                        chessInfo.curPos = new Pos(i, j);

                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                        if (setting.isEffectPlay) {
                                            clickMusic.start();
                                        }
                                        int key = 0;
                                        if (Rule.isKingDanger(chessInfo.piece, true)) {
                                            key = 1;
                                        }
                                        if (Rule.isDead(chessInfo.piece, true)) {
                                            key = 2;
                                        }
                                        if (key == 1) {
                                            if (setting.isEffectPlay) {
                                                checkMusic.start();
                                            }
                                            Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT).show();
                                        } else if (key == 2) {
                                            if (setting.isEffectPlay) {
                                                winMusic.start();
                                            }
                                            chessInfo.status = 2;
                                            Toast.makeText(PvMActivity.this, "红方失败", Toast.LENGTH_SHORT).show();
                                        }
                                        AIMove(true, chessInfo.status);
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
        } else {
            pos[0] = pos[1] = -1;
        }
        return pos;
    }

    @Override
    public void onClick(View view) {
        if (setting.isEffectPlay) {
            selectMusic.start();
        }
        switch (view.getId()) {
            case R.id.btn_retry:
                final RetryDialog retryDialog = new RetryDialog(this);
                retryDialog.setOnClickBottomListener(new RetryDialog.OnClickBottomListener() {

                    @Override
                    public void onPositiveClick() {
                        try {
                            chessInfo.setInfo(new ChessInfo());
                            infoSet.newInfo();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (setting.isPlayerRed != retryDialog.isPlayerRed) {
                            setting.isPlayerRed = retryDialog.isPlayerRed;
                            editor.putBoolean("isPlayerRed", retryDialog.isPlayerRed);
                            editor.commit();
                        }
                        chessInfo.IsReverse = (setting.isPlayerRed == true) ? false : true;
                        if (setting.isPlayerRed == false) {
                            AIMove(true, chessInfo.status);
                        }
                        retryDialog.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
                        retryDialog.dismiss();
                    }
                });
                retryDialog.show();
                break;
            case R.id.btn_recall:
                int cnt = 0;
                while (!infoSet.preInfo.empty()) {
                    ChessInfo tmp = infoSet.preInfo.pop();
                    cnt++;
                    if (cnt >= 2) {
                        try {
                            chessInfo.setInfo(tmp);
                            infoSet.curInfo.setInfo(tmp);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                break;
            case R.id.btn_setting:
                final SettingDialog_PvM settingDialog_pvm = new SettingDialog_PvM(this);
                settingDialog_pvm.setOnClickBottomListener(new SettingDialog_PvM.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean flag = false;
                        if (setting.isMusicPlay != settingDialog_pvm.isMusicPlay) {
                            setting.isMusicPlay = settingDialog_pvm.isMusicPlay;
                            if (setting.isMusicPlay) {
                                backMusic.start();
                            } else {
                                backMusic.pause();
                                backMusic.seekTo(0);
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
                        finish();
                        backDialog.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
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
            Move move = AI.getGoodMove(chessInfo.piece, true, depth);

            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
            chessInfo.piece[fromPos.y][fromPos.x] = 0;
            chessInfo.IsChecked = false;
            chessInfo.IsRedGo = false;
            chessInfo.Select = new int[]{-1, -1};
            chessInfo.ret.clear();
            chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
            chessInfo.curPos = new Pos(toPos.x, toPos.y);

            try {
                infoSet.pushInfo(chessInfo);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if (setting.isEffectPlay) {
                clickMusic.start();
            }

            int key = 0;
            if (Rule.isKingDanger(chessInfo.piece, false)) {
                key = 1;
            }
            if (Rule.isDead(chessInfo.piece, false)) {
                key = 2;
            }
            if (key == 1) {
                if (setting.isEffectPlay) {
                    checkMusic.start();
                }
                Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT).show();
            } else if (key == 2) {
                if (setting.isEffectPlay) {
                    winMusic.start();
                }
                chessInfo.status = 2;
                Toast.makeText(PvMActivity.this, "黑方失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            long startTime = System.currentTimeMillis();
            Move move = AI.getGoodMove(chessInfo.piece, false, depth);
            long endTime = System.currentTimeMillis();    //获取结束时间
            Log.e("chen", "程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            Pos fromPos = move.fromPos;
            Pos toPos = move.toPos;
            chessInfo.piece[toPos.y][toPos.x] = chessInfo.piece[fromPos.y][fromPos.x];
            chessInfo.piece[fromPos.y][fromPos.x] = 0;
            chessInfo.IsChecked = false;
            chessInfo.IsRedGo = true;
            chessInfo.Select = new int[]{-1, -1};
            chessInfo.ret.clear();
            chessInfo.prePos = new Pos(fromPos.x, fromPos.y);
            chessInfo.curPos = new Pos(toPos.x, toPos.y);

            try {
                infoSet.pushInfo(chessInfo);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            if (setting.isEffectPlay) {
                clickMusic.start();
            }

            int key = 0;
            if (Rule.isKingDanger(chessInfo.piece, true)) {
                key = 1;
            }
            if (Rule.isDead(chessInfo.piece, true)) {
                key = 2;
            }
            if (key == 1) {
                if (setting.isEffectPlay) {
                    checkMusic.start();
                }
                Toast.makeText(PvMActivity.this, "将军", Toast.LENGTH_SHORT).show();
            } else if (key == 2) {
                if (setting.isEffectPlay) {
                    winMusic.start();
                }
                chessInfo.status = 2;
                Toast.makeText(PvMActivity.this, "红方失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        backMusic.pause();
        backMusic.seekTo(0);
        super.onPause();
    }

    @Override
    protected void onStop() {
        try {
            SaveInfo.SerializeChessInfo(chessInfo, "ChessInfo_pvm.bin");
            SaveInfo.SerializeInfoSet(infoSet, "InfoSet_pvm.bin");
        } catch (Exception e) {
            Log.e("chen", e.toString());
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        if (setting.isMusicPlay) {
            backMusic.start();
        }
        super.onStart();
    }
}
