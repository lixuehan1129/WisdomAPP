package com.example.wisdompark19.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.wisdompark19.R;

/**
 * Created by 最美人间四月天 on 2018/3/28.
 */

public class MineLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_login");
        Toolbar toolbar = (Toolbar)findViewById(R.id.user_login_mainTool); //标题栏
//        toolbar.setTitle(intent_data);  //标题栏名称
        findView();
    }

    private void findView(){

    }
}
