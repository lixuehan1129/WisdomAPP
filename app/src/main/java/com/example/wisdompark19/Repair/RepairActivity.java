package com.example.wisdompark19.Repair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wisdompark19.R;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class RepairActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_acitivity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_repair");
        Toolbar toolbar = (Toolbar)findViewById(R.id.mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);
        findView();
    }

    private void findView(){
        Button repair_make = (Button)findViewById(R.id.repair_make);
        Button repair_check = (Button)findViewById(R.id.repair_check);

        repair_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairActivity.this,RepairMakeActivity.class);
                intent.putExtra("put_data","我要报修");
                startActivity(intent);
            }
        });

        repair_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairActivity.this,RepairCheckActivity.class);
                intent.putExtra("put_data","报修查询");
                startActivity(intent);
            }
        });

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
