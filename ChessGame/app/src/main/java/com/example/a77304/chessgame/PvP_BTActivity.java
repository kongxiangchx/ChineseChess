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


public class PvP_BTActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
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
                    int i = chessInfo.Select[0], j = chessInfo.Select[1];
                    if (i >= 0 && j >= 0) {
                        if (chessInfo.IsRedGo == true) {
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
                                        Toast.makeText(PvP_BTActivity.this, "帅被将军", Toast.LENGTH_SHORT).show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = false;
                                        chessInfo.curPos = new Pos(i, j);
                                        if (setting.isEffectPlay) {
                                            clickMusic.start();
                                        }
                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
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
                                            Toast.makeText(PvP_BTActivity.this, "将军", Toast.LENGTH_SHORT).show();
                                        } else if (key == 2) {
                                            if (setting.isEffectPlay) {
                                                winMusic.start();
                                            }
                                            chessInfo.status = 2;
                                            Toast.makeText(PvP_BTActivity.this, "黑方失败", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(PvP_BTActivity.this, "将被将军", Toast.LENGTH_SHORT).show();
                                    } else {
                                        chessInfo.IsChecked = false;
                                        chessInfo.IsRedGo = true;
                                        chessInfo.curPos = new Pos(i, j);
                                        if (setting.isEffectPlay) {
                                            clickMusic.start();
                                        }
                                        try {
                                            infoSet.pushInfo(chessInfo);
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
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
                                            Toast.makeText(PvP_BTActivity.this, "将军", Toast.LENGTH_SHORT).show();
                                        } else if (key == 2) {
                                            if (setting.isEffectPlay) {
                                                winMusic.start();
                                            }
                                            chessInfo.status = 2;
                                            Toast.makeText(PvP_BTActivity.this, "红方失败", Toast.LENGTH_SHORT).show();
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
                try {
                    chessInfo.setInfo(new ChessInfo());
                    infoSet.newInfo();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
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
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean flag = false;
                        if (setting.isMusicPlay != settingDialog.isMusicPlay) {
                            setting.isMusicPlay = settingDialog.isMusicPlay;
                            if (setting.isMusicPlay) {
                                backMusic.start();
                            } else {
                                backMusic.pause();
                                backMusic.seekTo(0);
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

    @Override
    protected void onPause() {
        backMusic.pause();
        backMusic.seekTo(0);
        super.onPause();
    }

    @Override
    protected void onStart() {
        if (setting.isMusicPlay) {
            backMusic.start();
        }
        super.onStart();
    }
}