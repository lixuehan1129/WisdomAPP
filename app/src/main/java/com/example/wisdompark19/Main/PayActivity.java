package com.example.wisdompark19.Main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.wisdompark19.Adapter.PayItemAdapter;
import com.example.wisdompark19.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class PayActivity extends AppCompatActivity{

    private List<PayItemAdapter.Pay_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private PayItemAdapter mPayItemAdapter;
    private RecyclerView mRecyclerView;

    ArrayList<String> card_message_tell = new ArrayList<String>(); // 上下滚动消息栏内容
    ArrayList<String> card_message_content = new ArrayList<String>();
    ArrayList<String> card_message_time = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data");
        Toolbar toolbar = (Toolbar)findViewById(R.id.mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);

        initRollData();
        initData();
        findView();
    }

    private void findView(){
        ImageButton pay_water = (ImageButton)findViewById(R.id.pay_water);
        ImageButton pay_electric = (ImageButton)findViewById(R.id.pay_electric);
        ImageButton pay_gas = (ImageButton)findViewById(R.id.pay_gas);
        ImageButton pay_property = (ImageButton)findViewById(R.id.pay_property);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_pay_item);
        mLayoutManager = new LinearLayoutManager(PayActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPayItemAdapter = new PayItemAdapter(Data);
        mRecyclerView.setAdapter(mPayItemAdapter);
    }

    private void initRollData(){
        card_message_tell.add("水费");
        card_message_tell.add("电费");
        card_message_tell.add("物业费");
        card_message_tell.add("天然气");

        card_message_content.add("50");
        card_message_content.add("100");
        card_message_content.add("100");
        card_message_content.add("60");

        card_message_time.add("1月3日");
        card_message_time.add("1月3日");
        card_message_time.add("1月3日");
        card_message_time.add("1月3日");

        card_message_tell.add("水费");
        card_message_tell.add("电费");
        card_message_tell.add("物业费");
        card_message_tell.add("天然气");

        card_message_content.add("50");
        card_message_content.add("100");
        card_message_content.add("100");
        card_message_content.add("60");

        card_message_time.add("2017年12月3日");
        card_message_time.add("2017年12月3日");
        card_message_time.add("2017年12月3日");
        card_message_time.add("2017年12月3日");
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<8; i++){
            PayItemAdapter newData = new PayItemAdapter(Data);
            PayItemAdapter.Pay_item pay_item = newData.new Pay_item(card_message_tell.get(i),
                    card_message_content.get(i),card_message_time.get(i));
            Data.add(pay_item);
            System.out.println(card_message_content.get(i));
        }
    }

//    private void setAdapter(){
//
//        System.out.println("11");
//    }


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
