package com.example.a77304.chessgame;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public Button btn_pvm;
    public Button btn_pvp;
    public Button btn_help;
    public Button btn_about;
    public Button btn_setting;

    public static Setting setting;

    public static MediaPlayer backMusic;
    public static MediaPlayer selectMusic;
    public static MediaPlayer clickMusic;
    public static MediaPlayer checkMusic;
    public static MediaPlayer winMusic;

    public static SharedPreferences sharedPreferences;

    public static Zobrist zobrist;

    public static long curClickTime=0L;
    public static long lastClickTime=0L;
    public static final int MIN_CLICK_DELAY_TIME = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        PermissionUtils.isGrantExternalRW(this, 1);

        initMusic();

        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);

        zobrist=new Zobrist();

        setting = new Setting(sharedPreferences);

        btn_pvm = (Button) findViewById(R.id.btn_pvm);
        btn_pvm.setOnClickListener(this);

        btn_pvp = (Button) findViewById(R.id.btn_pvp);
        btn_pvp.setOnClickListener(this);

        btn_help = (Button) findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        btn_about = (Button) findViewById(R.id.btn_about);
        btn_about.setOnClickListener(this);

        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(this);
    }

    public void initMusic() {
        backMusic = MediaPlayer.create(this, R.raw.background);
        backMusic.setLooping(true);
        backMusic.setVolume(0.2f,0.2f);
        selectMusic = MediaPlayer.create(this, R.raw.select);
        selectMusic.setVolume(5f, 5f);
        clickMusic = MediaPlayer.create(this, R.raw.click);
        clickMusic.setVolume(5f, 5f);
        checkMusic = MediaPlayer.create(this, R.raw.checkmate);
        checkMusic.setVolume(5f, 5f);
        winMusic = MediaPlayer.create(this, R.raw.win);
        winMusic.setVolume(5f, 5f);
    }

    public static void playMusic(MediaPlayer mediaPlayer){
        if(setting.isMusicPlay){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            mediaPlayer.start();
        }
    }

    public static void stopMusic(MediaPlayer mediaPlayer){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }
    
    public static void playEffect(MediaPlayer mediaPlayer){
        if (setting.isEffectPlay) {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            mediaPlayer.start();
        }
    }

    @Override
    public void onClick(View view) {
        lastClickTime=System.currentTimeMillis();
        if(lastClickTime-curClickTime<MIN_CLICK_DELAY_TIME){
            return ;
        }
        curClickTime=lastClickTime;
        Intent intent;
        playEffect(selectMusic);
        switch (view.getId()) {
            case R.id.btn_pvm:
                intent = new Intent(HomeActivity.this, PvMActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_pvp:
                intent = new Intent(HomeActivity.this, PvPActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_help:
                final OnlyReadDialog helpDialog = new OnlyReadDialog(this, "帮助", "本款象棋软件有两种玩\n法，分别是双人博弈和\n人机博弈，具体玩法请\n参照中国象棋规则。");
                helpDialog.setOnClickBottomListener(new OnlyReadDialog.OnClickBottomListener() {

                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        helpDialog.dismiss();
                    }
                });
                helpDialog.show();
                break;
            case R.id.btn_about:
                final OnlyReadDialog aboutDialog = new OnlyReadDialog(this, "关于", "作者：空想chx\ngithub：kongxiangchx");
                aboutDialog.setOnClickBottomListener(new OnlyReadDialog.OnClickBottomListener() {

                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        aboutDialog.dismiss();
                    }
                });
                aboutDialog.show();
                break;
            case R.id.btn_setting:
                final SettingDialog settingDialog = new SettingDialog(this);
                settingDialog.setOnClickBottomListener(new SettingDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        playEffect(selectMusic);
                        Editor editor = sharedPreferences.edit();
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
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        lastClickTime=System.currentTimeMillis();
        if(lastClickTime-curClickTime<MIN_CLICK_DELAY_TIME){
            return true;
        }
        curClickTime=lastClickTime;

        if(keyCode==KeyEvent.KEYCODE_BACK){
            final CommonDialog backDialog = new CommonDialog(this, "退出", "确认要退出吗");
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("chen", "获取存储权限成功");

                    Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isMusicPlay", true);
                    editor.putBoolean("isEffectPlay", true);
                    editor.putBoolean("isPlayerRed", true);
                    editor.putInt("mLevel", 2);
                    editor.commit();
                    try {
                        SaveInfo.SerializeChessInfo(new ChessInfo(),"ChessInfo_pvp.bin");
                        SaveInfo.SerializeInfoSet(new InfoSet(),"InfoSet_pvp.bin");
                        SaveInfo.SerializeChessInfo(new ChessInfo(),"ChessInfo_pvm.bin");
                        SaveInfo.SerializeInfoSet(new InfoSet(),"InfoSet_pvm.bin");
                        SaveInfo.SerializeKnowledgeBase(new KnowledgeBase(),"KnowledgeBase.bin");
                        SaveInfo.SerializeTransformTable(new TransformTable(),"TransformTable.bin");
                    } catch (Exception e) {
                        Log.e("chen",e.toString());
                    }

                    playMusic(backMusic);
                } else {
                    Toast.makeText(this, "获取存储权限失败，请手动开启存储权限", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        stopMusic(backMusic);
        super.onPause();
    }

    @Override
    protected void onStart() {
        playMusic(backMusic);
        super.onStart();
    }
}
