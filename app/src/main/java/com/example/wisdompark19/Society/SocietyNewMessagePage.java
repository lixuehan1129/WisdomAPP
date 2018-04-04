package com.example.wisdompark19.Society;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wisdompark19.R;

/**
 * Created by 最美人间四月天 on 2018/3/16.
 */

public class SocietyNewMessagePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.society_new_message_page);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_mes");
        Toolbar toolbar = (Toolbar)findViewById(R.id.new_message_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle("消息通知");
        setSupportActionBar(toolbar);
        back(toolbar);
        findView();
//        findData();
//        initData();
//        setAdapter();
//        setItemClick();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.save_item, menu);
//        return true;
//    }


    private void findView(){

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
//            case R.id.society_new_message_page_save:
//                finish();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
