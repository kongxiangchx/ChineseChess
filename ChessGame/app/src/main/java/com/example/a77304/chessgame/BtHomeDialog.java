package com.example.a77304.chessgame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by 77304 on 2021/4/28.
 */

public class BtHomeDialog extends Dialog{
    public Button btn_create;
    public Button btn_join;
    public Button btn_back;


    public BtHomeDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bt_home);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
    }

    public void initView(){
        btn_create=(Button)findViewById(R.id.btn_create);
        btn_join=(Button)findViewById(R.id.btn_join);
        btn_back=(Button)findViewById(R.id.btn_back);
    }

    private void initEvent() {
        //设置创建对局按钮被点击后，向外界提供监听
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onCreateClick();
                }
            }
        });
        //设置加入对局按钮被点击后，向外界提供监听
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onJoinClick();
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

    public BtHomeDialog.OnClickBottomListener onClickBottomListener;

    public BtHomeDialog setOnClickBottomListener(BtHomeDialog.OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        /**
         * 创建对局事件
         */
        public void onCreateClick();

        /**
         * 加入对局事件
         */
        public void onJoinClick();

        /**
         * 返回事件
         */
        public void onBackClick();
    }
}
