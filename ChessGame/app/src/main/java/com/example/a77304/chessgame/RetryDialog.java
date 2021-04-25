package com.example.a77304.chessgame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by 77304 on 2021/4/19.
 */

public class RetryDialog extends Dialog implements RadioGroup.OnCheckedChangeListener {
    public Button posBtn,negBtn;
    public RadioGroup holdGroup;
    public RadioButton holdRed,holdBlack;

    public boolean isPlayerRed;

    public RetryDialog(Context context) {
        super(context,R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_retry);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        isPlayerRed=true;
        holdRed.setChecked(true);
        holdGroup.setOnCheckedChangeListener(this);
    }

    private void initView() {
        posBtn = (Button) findViewById(R.id.posBtn);
        negBtn = (Button) findViewById(R.id.negBtn);

        holdGroup=(RadioGroup)findViewById(R.id.holdGroup);
        holdRed=(RadioButton)findViewById(R.id.holdRed);
        holdBlack=(RadioButton)findViewById(R.id.holdBlack);
    }


    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onClickBottomListener!= null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });
    }

    public RetryDialog.OnClickBottomListener onClickBottomListener;
    public RetryDialog setOnClickBottomListener(RetryDialog.OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        RadioButton checked=(RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
        switch(radioGroup.getId()){
            case R.id.holdGroup:
                if(checked.getId()==R.id.holdRed){
                    isPlayerRed=true;
                }
                else{
                    isPlayerRed=false;
                }
                break;
            default:
                break;
        }
    }

    public interface OnClickBottomListener{
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
