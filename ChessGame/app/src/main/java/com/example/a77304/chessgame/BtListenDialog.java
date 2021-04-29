package com.example.a77304.chessgame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by 77304 on 2021/4/29.
 */

public class BtListenDialog extends Dialog{
    public Button posBtn, negBtn;
    public TextView btName,btTime;
    public String name;

    public BtListenDialog(Context context,String name) {
        super(context, R.style.CustomDialog);
        this.name=name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bt_listen);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        posBtn.setEnabled(false);
        btName.setText("主机名："+name);
        timer.start();
    }

    public CountDownTimer timer = new CountDownTimer(10000+500, 1000) {
        //我们在这里去更改定时改变的东西
        @Override
        public void onTick(long millisUntilFinished) {
            btTime.setText("请对方在"+String.valueOf(millisUntilFinished/1000)+"秒内加入对局");
        }

        //120秒执行完之后，执行的方法。
        @Override
        public void onFinish() {
            onClickBottomListener.onNegtiveClick();
        }

    };

    private void initView() {
        posBtn = (Button) findViewById(R.id.posBtn);
        negBtn = (Button) findViewById(R.id.negBtn);

        btName=(TextView)findViewById(R.id.btName);
        btTime=(TextView)findViewById(R.id.btTime);
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

    public BtListenDialog.OnClickBottomListener onClickBottomListener;

    public BtListenDialog setOnClickBottomListener(BtListenDialog.OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
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
