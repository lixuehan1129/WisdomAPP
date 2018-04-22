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
import android.widget.Toast;


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

    ArrayList<String> count_name = new ArrayList<String>(); // 上下滚动消息栏内容
    ArrayList<String> count_fee = new ArrayList<String>();
    ArrayList<String> count_time = new ArrayList<String>();
    ArrayList<String> count_pay = new ArrayList<String>();

    ImageButton pay_water;
    ImageButton pay_electric;
    ImageButton pay_gas;
    ImageButton pay_property;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_pay");
        Toolbar toolbar = (Toolbar)findViewById(R.id.mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);

        initRollData();
        initData();
        findView();
        ButtonCick();
    }

    private void findView(){
        pay_water = (ImageButton)findViewById(R.id.pay_water);
        pay_electric = (ImageButton)findViewById(R.id.pay_electric);
        pay_gas = (ImageButton)findViewById(R.id.pay_gas);
        pay_property = (ImageButton)findViewById(R.id.pay_property);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_pay_item);
        mLayoutManager = new LinearLayoutManager(PayActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPayItemAdapter = new PayItemAdapter(Data);
        mRecyclerView.setAdapter(mPayItemAdapter);
    }

    private void ButtonCick(){
        pay_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PayActivity.this,"水费",Toast.LENGTH_LONG).show();
            }
        });
        pay_electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PayActivity.this,"电费",Toast.LENGTH_LONG).show();
            }
        });
        pay_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PayActivity.this,"天然气",Toast.LENGTH_LONG).show();
            }
        });
        pay_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PayActivity.this,"物业费",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initRollData(){
        count_name.add("物业费");
        count_fee.add("100");
        count_time.add("2018年4月22日");
        count_pay.add("200");
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<count_name.size(); i++){
            PayItemAdapter newData = new PayItemAdapter(Data);
            PayItemAdapter.Pay_item pay_item = newData.new Pay_item(count_name.get(i),
                    count_fee.get(i),count_time.get(i),count_pay.get(i));
            Data.add(pay_item);
        }
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
