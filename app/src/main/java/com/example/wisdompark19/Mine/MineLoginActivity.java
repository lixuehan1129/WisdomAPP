package com.example.wisdompark19.Mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wisdompark19.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 最美人间四月天 on 2018/3/28.
 */

public class MineLoginActivity extends AppCompatActivity {

    private CircleImageView user_login_picture;
    private TextInputLayout user_login_name_layout;
    private TextInputLayout user_login_password_layout;
    private TextInputEditText user_login_name;
    private TextInputEditText user_login_password;
    private Button user_login_forget;
    private Button user_login_regist;
    private Button user_login_login;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_login");
        Toolbar toolbar = (Toolbar)findViewById(R.id.user_login_mainTool); //标题栏
//        toolbar.setTitle(intent_data);  //标题栏名称
        clear_focus();
        findView();
        problem_jiaodian();
    }

    private void findView(){
        user_login_picture = (CircleImageView)findViewById(R.id.user_login_picture);
        user_login_name_layout = (TextInputLayout)findViewById(R.id.user_login_name_layout);
        user_login_password_layout = (TextInputLayout)findViewById(R.id.user_login_password_layout);
        user_login_name = (TextInputEditText)findViewById(R.id.user_login_name);
        user_login_password = (TextInputEditText)findViewById(R.id.user_login_password);
        user_login_forget = (Button)findViewById(R.id.user_login_forget);
        user_login_regist = (Button)findViewById(R.id.user_login_regist);
        user_login_login = (Button)findViewById(R.id.user_login_login);
        initEditText();
        pageDo();
    }

    private void initEditText(){
        user_login_name_layout.setCounterEnabled(true);  //设置可以计数
        user_login_name_layout.setCounterMaxLength(15); //计数的最大值

        user_login_name.setText(getIntent().getStringExtra("put_extra"));

        user_login_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_login_name_layout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        user_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_login_password_layout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void pageDo(){
        user_login_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(MineLoginActivity.this, "忘记密码", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        user_login_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(MineLoginActivity.this, "注册", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(MineLoginActivity.this,MineRegistActivity.class);
                intent.putExtra("put_data_regist","注册");
                startActivity(intent);
            }
        });
        user_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(MineLoginActivity.this, "登录", Toast.LENGTH_SHORT);
                Log.e("name", String.valueOf(user_login_name.getText()));
                Log.e("name", String.valueOf(user_login_password.getText()));
                toast.show();
            }
        });
    }


    /*
    * 清除焦点
    * */
    private void clear_focus(){
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_relativeLayout);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relativeLayout.requestFocus();
                close_input();
                return false;
            }
        });
    }
    /*
   * 点击空白区域 Edittext失去焦点 关闭输入法
   * */
    private void problem_jiaodian() {
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_relativeLayout);
        relativeLayout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relativeLayout.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                return false;
            }
        });
    }

    /*
    * 关闭输入法并 清除焦点
    * */
    private void close_input(){
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.edit_relativeLayout);
        //linearLayout.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
