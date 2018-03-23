package com.example.wisdompark19.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wisdompark19.R;

/**
 * Created by 田博远 on 2018/3/23.
 */

public class SocietyTuCaoAdapter extends AlertDialog implements View.OnClickListener{
    /**
     * 上下文对象 *
     */
    Activity context;
    private EditText editText;
    private TextView textView_cancel;
    private TextView textView_sure;
    private View.OnClickListener onClickListener;

    public SocietyTuCaoAdapter(Context context) {
        super(context);
        this.context = (Activity) context;
    }

//    protected SocietyTuCaoAdapter(Context context, boolean cancelable, OnCancelListener cancelListener) {
//        super(context, cancelable, cancelListener);
//        this.context = (Activity) context;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.society_tocao_page);

        editText = (EditText) findViewById(R.id.society_tucao_page_et);
        textView_cancel = (TextView) findViewById(R.id.society_tucao_page_cancel);
        textView_sure = (TextView) findViewById(R.id.society_tucao_page_sure);


        showKeyboard();
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        //p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的1
        dialogWindow.setAttributes(p);
        textView_cancel.setOnClickListener(this.onClickListener);
        textView_sure.setOnClickListener(this.onClickListener);
        this.setCancelable(true);
    }

    public void showKeyboard() {
        if(editText!=null){
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }
    }

    @Override
    public void onClick(View v) {
        textView_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
