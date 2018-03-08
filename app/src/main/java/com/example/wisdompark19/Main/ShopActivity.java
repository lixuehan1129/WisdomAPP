package com.example.wisdompark19.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.ShopTradeItemAdapter;
import com.example.wisdompark19.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 * 社区交易界面
 */

public class ShopActivity extends AppCompatActivity {

    private List<ShopTradeItemAdapter.Shop_Trade_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShopTradeItemAdapter mShopTradeItemAdapter;
    private RecyclerView mRecyclerView;

    ArrayList<String> shop_trade_image = new ArrayList<String>(); // 上下滚动消息栏内容
    ArrayList<String> shop_trade_content = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onlineshopping_activity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data");
        Toolbar toolbar = (Toolbar)findViewById(R.id.mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);

        findView();
        findData();
        initData();
        setAdapter();
        setItemClick();
    }

    private void findView(){
        mRecyclerView = (RecyclerView)findViewById(R.id.shop_trade_rec);
        mLayoutManager = new LinearLayoutManager(ShopActivity.this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    private void findData(){
        String shop_image;
        String shop_content;
        for(int i=0; i<15; i++){
            shop_image = "图片"+i;
            shop_content = "商品描述"+i;
            shop_trade_image.add(shop_image);
            shop_trade_content.add(shop_content);
        }
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<15; i++){
            ShopTradeItemAdapter newData = new ShopTradeItemAdapter(Data);
            ShopTradeItemAdapter.Shop_Trade_item shop_trade_item = newData.new Shop_Trade_item(
                    shop_trade_image.get(i),shop_trade_content.get(i)
            );
            Data.add(shop_trade_item);
        }
    }

    private void setAdapter(){
        mShopTradeItemAdapter = new ShopTradeItemAdapter(Data);
        mRecyclerView.setAdapter(mShopTradeItemAdapter);
    }

    private void setItemClick(){
        mShopTradeItemAdapter.setmOnItemClickListener(new ShopTradeItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(ShopActivity.this, shop_trade_content.get(position), Toast.LENGTH_SHORT);
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
