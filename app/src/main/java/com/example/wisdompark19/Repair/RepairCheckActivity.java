package com.example.wisdompark19.Repair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.RepairCheckAdapter;
import com.example.wisdompark19.R;
import com.example.wisdompark19.Society.SocietyNewMessagePage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/3/13.
 */

public class RepairCheckActivity extends AppCompatActivity {

    private List<RepairCheckAdapter.Repair_Check_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private RepairCheckAdapter mRepairCheckAdapter;
    private RecyclerView mRecyclerView;

    ArrayList<String> repair_check_content = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_check);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data");
        Toolbar toolbar = (Toolbar)findViewById(R.id.repair_check_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
//        toolbar.setTitle(intent_data);
        back(toolbar);
        findView();
        findData();
        initData();
        setAdapter();
        setItemClick();
    }


    private void findView(){
        mRecyclerView = (RecyclerView)findViewById(R.id.repair_check_rec);
        mLayoutManager = new LinearLayoutManager(RepairCheckActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void findData(){
        String content;
        for(int i=0; i<15; i++){
            content = "点击查看报修内容及进度"+i;
            repair_check_content.add(content);
        }
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<15; i++){
            RepairCheckAdapter newData = new RepairCheckAdapter(Data);
            RepairCheckAdapter.Repair_Check_item repair_check_item = newData.new Repair_Check_item(
                    repair_check_content.get(i),"进度:完成","评论：XXXXXX"
            );
            Data.add(repair_check_item);
        }
    }

    private void setAdapter(){
        mRepairCheckAdapter = new RepairCheckAdapter(Data);
        mRecyclerView.setAdapter(mRepairCheckAdapter);
    }

    private void setItemClick(){
        mRepairCheckAdapter.setmOnItemClickListener(new RepairCheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(RepairCheckActivity.this, repair_check_content.get(position), Toast.LENGTH_SHORT);
                toast.show();
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
