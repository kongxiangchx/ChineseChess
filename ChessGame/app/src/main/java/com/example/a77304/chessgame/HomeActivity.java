package com.example.a77304.chessgame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initMusic();

        sharedPreferences=getSharedPreferences("setting",MODE_PRIVATE);
        boolean isFirstRun=sharedPreferences.getBoolean("isFirstRun", true);
        Editor editor=sharedPreferences.edit();
        if(isFirstRun){
            editor.putBoolean("isFirstRun", false);
            editor.putBoolean("isMusicPlay",true);
            editor.putBoolean("isEffectPlay",true);
            editor.putBoolean("isPlayerRed",true);
            editor.putInt("mLevel",2);
            editor.commit();
            /*try {
                SaveInfo.SerializeChessInfo(new ChessInfo(),"ChessInfo_pvp.bin");
                SaveInfo.SerializeInfoSet(new InfoSet(),"InfoSet_pvp.bin");
                SaveInfo.SerializeChessInfo(new ChessInfo(),"ChessInfo_pvm.bin");
                SaveInfo.SerializeInfoSet(new InfoSet(),"InfoSet_pvm.bin");
            } catch (Exception e) {
                Log.e("chen",e.toString());
            }*/
        }

        PermissionUtils.isGrantExternalRW(this, 1);

        setting=new Setting(sharedPreferences);

        btn_pvm=(Button)findViewById(R.id.btn_pvm);
        btn_pvm.setOnClickListener(this);

        btn_pvp=(Button)findViewById(R.id.btn_pvp);
        btn_pvp.setOnClickListener(this);

        btn_help=(Button)findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        btn_about=(Button)findViewById(R.id.btn_about);
        btn_about.setOnClickListener(this);

        btn_setting=(Button)findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(this);
    }

    void initMusic(){
        backMusic=MediaPlayer.create(this,R.raw.background);
        backMusic.setLooping(true);
        selectMusic=MediaPlayer.create(this,R.raw.select);
        selectMusic.setVolume(2f,2f);
        clickMusic=MediaPlayer.create(this,R.raw.click);
        clickMusic.setVolume(2f,2f);
        checkMusic=MediaPlayer.create(this,R.raw.checkmate);
        checkMusic.setVolume(2f,2f);
        winMusic=MediaPlayer.create(this,R.raw.win);
        winMusic.setVolume(2f,2f);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(setting.isEffectPlay){
            selectMusic.start();
        }
        switch (view.getId()){
            case R.id.btn_pvm:
                intent=new Intent(HomeActivity.this,PvMActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_pvp:
                intent=new Intent(HomeActivity.this,PvPActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_help:
                final CommonDialog helpDialog=new CommonDialog(this,"帮助","帮助里面什么都没有");
                helpDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener(){

                    @Override
                    public void onPositiveClick() {
                        helpDialog.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
                        helpDialog.dismiss();
                    }
                });
                helpDialog.show();
                break;
            case R.id.btn_about:
                final CommonDialog aboutDialog=new CommonDialog(this,"关于","关于里面什么都没有");
                aboutDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener(){

                    @Override
                    public void onPositiveClick() {
                        aboutDialog.dismiss();
                    }

                    @Override
                    public void onNegtiveClick() {
                        aboutDialog.dismiss();
                    }
                });
                aboutDialog.show();
                break;
            case R.id.btn_setting:
                final SettingDialog settingDialog=new SettingDialog(this);
                settingDialog.setOnClickBottomListener(new SettingDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        Editor editor=sharedPreferences.edit();
                        boolean flag=false;
                        if(setting.isMusicPlay!=settingDialog.isMusicPlay){
                            setting.isMusicPlay=settingDialog.isMusicPlay;
                            if(setting.isMusicPlay){
                                backMusic.start();
                            }
                            else {
                                backMusic.pause();
                                backMusic.seekTo(0);
                            }
                            editor.putBoolean("isMusicPlay",settingDialog.isMusicPlay);
                            flag=true;
                        }
                        if(setting.isEffectPlay!=settingDialog.isEffectPlay){
                            setting.isEffectPlay=settingDialog.isEffectPlay;
                            editor.putBoolean("isEffectPlay",settingDialog.isEffectPlay);
                            flag=true;
                        }
                        if(flag){
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
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("chen","获取存储权限成功");
                } else {
                    Toast.makeText(this,"获取存储权限失败，请手动开启存储权限",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        backMusic.pause();
        backMusic.seekTo(0);
        super.onPause();
    }

    @Override
    protected void onStart() {
        if(setting.isMusicPlay){
            backMusic.start();
        }
        super.onStart();
    }
}
