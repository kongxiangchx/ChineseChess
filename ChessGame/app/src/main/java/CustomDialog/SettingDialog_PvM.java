package CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.a77304.chessgame.HomeActivity;
import com.example.a77304.chessgame.R;

import static com.example.a77304.chessgame.HomeActivity.playEffect;
import static com.example.a77304.chessgame.HomeActivity.selectMusic;

/**
 * Created by 77304 on 2021/4/14.
 */

public class SettingDialog_PvM extends Dialog implements RadioGroup.OnCheckedChangeListener {
    public Button posBtn, negBtn;
    public RadioGroup musicGroup;
    public RadioGroup effectGroup;
    public RadioGroup levelGroup;
    public RadioButton musicTrue, musicFalse;
    public RadioButton effectTrue, effectFalse;
    public RadioButton level_1, level_2, level_3;

    public boolean isMusicPlay, isEffectPlay;
    public int mLevel;

    public SettingDialog_PvM(Context context) {
        super(context, R.style.CustomDialog);
        //getWindow().setWindowAnimations(R.style.DialogAnim);

        isMusicPlay = HomeActivity.setting.isMusicPlay;
        isEffectPlay = HomeActivity.setting.isEffectPlay;
        mLevel = HomeActivity.setting.mLevel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_pvm);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        if (isMusicPlay) {
            musicTrue.setChecked(true);
        } else {
            musicFalse.setChecked(true);
        }
        if (isEffectPlay) {
            effectTrue.setChecked(true);
        } else {
            effectFalse.setChecked(true);
        }
        if (mLevel == 1) {
            level_1.setChecked(true);
        } else if (mLevel == 2) {
            level_2.setChecked(true);
        } else {
            level_3.setChecked(true);
        }
        musicGroup.setOnCheckedChangeListener(this);
        effectGroup.setOnCheckedChangeListener(this);
        levelGroup.setOnCheckedChangeListener(this);
    }

    private void initView() {
        posBtn = (Button) findViewById(R.id.posBtn);
        negBtn = (Button) findViewById(R.id.negBtn);

        musicGroup = (RadioGroup) findViewById(R.id.musicGroup);
        musicTrue = (RadioButton) findViewById(R.id.musicTrue);
        musicFalse = (RadioButton) findViewById(R.id.musicFalse);

        effectGroup = (RadioGroup) findViewById(R.id.effectGroup);
        effectTrue = (RadioButton) findViewById(R.id.effectTrue);
        effectFalse = (RadioButton) findViewById(R.id.effectFalse);

        levelGroup = (RadioGroup) findViewById(R.id.levelGroup);
        level_1 = (RadioButton) findViewById(R.id.level_1);
        level_2 = (RadioButton) findViewById(R.id.level_2);
        level_3 = (RadioButton) findViewById(R.id.level_3);
    }


    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });
    }

    public SettingDialog_PvM.OnClickBottomListener onClickBottomListener;

    public SettingDialog_PvM setOnClickBottomListener(SettingDialog_PvM.OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        playEffect(selectMusic);
        RadioButton checked = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        switch (radioGroup.getId()) {
            case R.id.musicGroup:
                if (checked.getId() == R.id.musicTrue) {
                    isMusicPlay = true;
                } else {
                    isMusicPlay = false;
                }
                break;
            case R.id.effectGroup:
                if (checked.getId() == R.id.effectTrue) {
                    isEffectPlay = true;
                } else {
                    isEffectPlay = false;
                }
                break;
            case R.id.levelGroup:
                if (checked.getId() == R.id.level_1) {
                    mLevel = 1;
                } else if (checked.getId() == R.id.level_2) {
                    mLevel = 2;
                } else {
                    mLevel = 3;
                }
                break;
            default:
                break;
        }
    }

    public interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick();

        /**
         * 点击取消按钮事件
         */
        public void onNegtiveClick();
    }
}