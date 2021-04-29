package com.example.a77304.chessgame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by 77304 on 2021/4/28.
 */

public class PvPModeDialog extends Dialog {
    public Button btn_alone;
    public Button btn_bt;
    public Button btn_back;


    public PvPModeDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pvp_mode);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
    }

    public void initView(){
        btn_alone=(Button)findViewById(R.id.btn_alone);
        btn_bt=(Button)findViewById(R.id.btn_bt);
        btn_back=(Button)findViewById(R.id.btn_back);
    }

    private void initEvent() {
        //设置单机版按钮被点击后，向外界提供监听
        btn_alone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onAloneClick();
                }
            }
        });
        //设置蓝牙版按钮被点击后，向外界提供监听
        btn_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onBtClick();
                }
            }
        });
        //设置返回按钮被点击后，向外界提供监听
        btn_back.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onBackClick();
                }
            }
        });
    }

    public PvPModeDialog.OnClickBottomListener onClickBottomListener;

    public PvPModeDialog setOnClickBottomListener(PvPModeDialog.OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        /**
         * 单机版事件
         */
        public void onAloneClick();

        /**
         * 蓝牙版事件
         */
        public void onBtClick();

        /**
         * 返回事件
         */
        public void onBackClick();
    }
}
