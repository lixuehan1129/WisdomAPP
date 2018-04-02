package com.example.wisdompark19.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.wisdompark19.R;

/**
 * Created by 最美人间四月天 on 2018/4/2.
 */

public class MineRegistAddActivity extends AppCompatActivity {
    private EditText user_regist_add_name;
    private Spinner user_regist_add_spinner;
    private EditText user_regist_add_address;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist_add);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_regist_add");
        Toolbar toolbar = (Toolbar)findViewById(R.id.user_regist_add_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle("业主认证");  //标题栏名称
        back(toolbar);    //返回
        findView();
    }

    private void findView(){
        user_regist_add_name = (EditText)findViewById(R.id.user_regist_add_name);
        user_regist_add_spinner = (Spinner)findViewById(R.id.user_regist_add_spinner);
        user_regist_add_address = (EditText)findViewById(R.id.user_regist_add_address);
    }


    //返回注销事件
    private void back(Toolbar toolbar){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
