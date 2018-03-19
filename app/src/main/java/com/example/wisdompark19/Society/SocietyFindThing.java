package com.example.wisdompark19.Society;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.ShopTradeItemAdapter;
import com.example.wisdompark19.Main.ShopActivity;
import com.example.wisdompark19.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyFindThing extends Fragment {

    private List<ShopTradeItemAdapter.Shop_Trade_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShopTradeItemAdapter mShopTradeItemAdapter;
    private RecyclerView mRecyclerView;

    ArrayList<String> shop_trade_image = new ArrayList<String>();
    ArrayList<String> shop_trade_content = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_find_things, container, false);

        findView(view);
        findData();
        initData();
        setAdapter();
        setItemClick();

        return view;
    }

    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.society_find_rec);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }

    private void findData(){
        String shop_image;
        String shop_content;
        for(int i=0; i<15; i++){
            shop_image = "图片"+i;
            shop_content = "失物招领信息"+i;
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
                Toast toast=Toast.makeText(getActivity(), shop_trade_content.get(position), Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getActivity(),SocietyFindPageActivity.class);
                intent.putExtra("put_data",shop_trade_content.get(position));
                startActivity(intent);
            }
        });
    }
}
