package com.example.wisdompark19.Repair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 最美人间四月天 on 2018/3/13.
 */

public class RepairMakeActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView repair_time, repair_name, repair_add;
    private Spinner repair_spanner;
    private Button repair_button_ok;
    private CustomDatePicker mCustomDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_make);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data");
        Toolbar toolbar = (Toolbar)findViewById(R.id.repair_make_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle("报修");
        setSupportActionBar(toolbar);
        back(toolbar);
        findView();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.save_item, menu);
//        return true;
//    }

    private void findView(){
        repair_name = (TextView)findViewById(R.id.repair_name);
        repair_add = (TextView)findViewById(R.id.repair_add);
        repair_spanner = (Spinner)findViewById(R.id.repair_spanner);
        repair_time = (TextView)findViewById(R.id.repair_time);
        repair_time.setOnClickListener(RepairMakeActivity.this);
        initDatePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_time:
                mCustomDatePicker.show(repair_time.getText().toString());
                break;
        }
    }

    //自定义时间选择
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        repair_time.setText(now);

        mCustomDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                repair_time.setText(time);
            }
        }, "2010-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        mCustomDatePicker.showSpecificTime(true); // 显示时和分
        mCustomDatePicker.setIsLoop(true); // 允许循环滚动
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
