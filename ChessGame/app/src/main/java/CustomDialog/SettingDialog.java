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
 * Created by 77304 on 2021/4/13.
 */

public class SettingDialog extends Dialog implements RadioGroup.OnCheckedChangeListener {
    public Button posBtn, negBtn;
    public RadioGroup musicGroup;
    public RadioGroup effectGroup;
    public RadioButton musicTrue, musicFalse;
    public RadioButton effectTrue, effectFalse;

    public boolean isMusicPlay, isEffectPlay;

    public SettingDialog(Context context) {
        super(context, R.style.CustomDialog);
        isMusicPlay = HomeActivity.setting.isMusicPlay;
        isEffectPlay = HomeActivity.setting.isEffectPlay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
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
        musicGroup.setOnCheckedChangeListener(this);
        effectGroup.setOnCheckedChangeListener(this);
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

    public OnClickBottomListener onClickBottomListener;

    public SettingDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
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
