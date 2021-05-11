package com.example.a77304.chessgame;

import android.content.SharedPreferences;
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

import ChessMove.Rule;
import CustomDialog.CommonDialog;
import CustomDialog.SettingDialog;
import CustomView.ChessView;
import CustomView.RoundView;
import Info.ChessInfo;
import Info.InfoSet;
import Info.Pos;
import Info.SaveInfo;

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


public class PvPActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
    private RelativeLayout relativeLayout;
    private ChessInfo chessInfo;
    private InfoSet infoSet;
    private ChessView chessView;
    private RoundView roundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        if (SaveInfo.fileIsExists("ChessInfo_pvp.bin")) {
            try {
                chessInfo = SaveInfo.DeserializeChessInfo("ChessInfo_pvp.bin");
            } catch (Exception e) {
                Log.e("chen", e.toString());
            }
        } else {
            chessInfo = new ChessInfo();
        }

        if (SaveInfo.fileIsExists("InfoSet_pvp.bin")) {
            try {
                infoSet = SaveInfo.DeserializeInfoSet("InfoSet_pvp.bin");
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
                    int i = chessInfo.Select[0], j = chessInfo.Select[1];
                    if (i >= 0 && i <= 8 && j >= 0 && j <= 9) {
                        if (chessInfo.IsRedGo == true) {
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
                                        Toast toast = Toast.makeText(PvPActivity.this, "帅被将军", Toast.LENGTH_SHORT);
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
                                            Toast toast = Toast.makeText(PvPActivity.this, "将军", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        } else if (key == 2) {
                                            playEffect(winMusic);
                                            chessInfo.status = 2;
                                            Toast toast = Toast.makeText(PvPActivity.this, "黑方失败", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                        if (chessInfo.peaceRound >= 60 || chessInfo.scene.size() >= 12 || (chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0)) {
                                            chessInfo.status = 2;
                                            Toast toast = Toast.makeText(PvPActivity.this, "和棋", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
                                    }
                                }
                            }
                        } else {
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
                                        Toast toast = Toast.makeText(PvPActivity.this, "将被将军", Toast.LENGTH_SHORT);
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
                                            Toast toast = Toast.makeText(PvPActivity.this, "将军", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        } else if (key == 2) {
                                            playEffect(winMusic);
                                            chessInfo.status = 2;
                                            Toast toast = Toast.makeText(PvPActivity.this, "红方失败", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                        if (chessInfo.peaceRound >= 60 || chessInfo.scene.size() >= 12 || (chessInfo.attackNum_B == 0 && chessInfo.attackNum_R == 0)) {
                                            chessInfo.status = 2;
                                            Toast toast = Toast.makeText(PvPActivity.this, "和棋", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }
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
                final CommonDialog retryDialog = new CommonDialog(this, "新局", "确认要开始一个新局吗");
                retryDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {

                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        try {
                            chessInfo.setInfo(new ChessInfo());
                            infoSet.newInfo();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        retryDialog.dismiss();
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
                if (!infoSet.preInfo.empty()) {
                    ChessInfo tmp = infoSet.preInfo.pop();
                    try {
                        chessInfo.setInfo(tmp);
                        infoSet.curInfo.setInfo(tmp);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.btn_setting:
                final SettingDialog settingDialog = new SettingDialog(this);
                settingDialog.setOnClickBottomListener(new SettingDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean flag = false;
                        if (setting.isMusicPlay != settingDialog.isMusicPlay) {
                            setting.isMusicPlay = settingDialog.isMusicPlay;
                            if (setting.isMusicPlay) {
                                playMusic(backMusic);
                            } else {
                                stopMusic(backMusic);
                            }
                            editor.putBoolean("isMusicPlay", settingDialog.isMusicPlay);
                            flag = true;
                        }
                        if (setting.isEffectPlay != settingDialog.isEffectPlay) {
                            setting.isEffectPlay = settingDialog.isEffectPlay;
                            editor.putBoolean("isEffectPlay", settingDialog.isEffectPlay);
                            flag = true;
                        }
                        if (flag) {
                            editor.commit();
                        }
                        settingDialog.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
                        playEffect(selectMusic);
                        settingDialog.dismiss();
                    }
                });
                settingDialog.show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        curClickTime = lastClickTime;

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
            SaveInfo.SerializeChessInfo(chessInfo, "ChessInfo_pvp.bin");
            SaveInfo.SerializeInfoSet(infoSet, "InfoSet_pvp.bin");
        } catch (Exception e) {
            Log.e("chen", e.toString());
        }
        super.onStop();
    }

    @Override
    protected void onStart() {
        playMusic(backMusic);
        super.onStart();
    }
}
