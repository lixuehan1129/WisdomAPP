package com.example.wisdompark19.Main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.wisdompark19.Adapter.FunctionListAdapter;
import com.example.wisdompark19.R;
import com.example.wisdompark19.Repair.RepairActivity;
import com.example.wisdompark19.Society.SocietyNewMessagePage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 最美人间四月天 on 2018/1/9.
 */

public class MainFragment extends Fragment {

    private List<FunctionListAdapter.Function_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private FunctionListAdapter mFunctionListAdapter;
    private RecyclerView mRecyclerView;
    private GridView mGridView;


    private int mCurrPos;
    ArrayList<String> card_message_tell = new ArrayList<String>(); // 上下滚动消息栏内容
    ArrayList<String> card_message_content = new ArrayList<String>();
    ArrayList<String> card_message_time = new ArrayList<String>();

    private int[] mImages = {
            R.mipmap.ic_main_pay,
            R.mipmap.ic_main_map,
            R.mipmap.ic_main_cart,
            R.mipmap.ic_main_waishe,
            R.mipmap.ic_main_repair,
            R.mipmap.ic_main_code,
            R.mipmap.ic_main_more,
            0,
            0

    };
    private String[] mContent = {
            "生活缴费",
            "我的位置",
            "电商平台",
            "外设接口",
            "报修管理",
            "通行二维码",
            "更多",
            null,
            null
    };

    private ViewFlipper viewFlipper;

    public static MainFragment newInstance(String info) {

        Bundle args = new Bundle();
        args.putString("info", info);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(args);
        return mainFragment;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mainfragment, null);
        findView(view); //界面
    //    initData();     //数据
    //    setAdapter();   //更新Adapter
    //    clickItem();    //功能按钮的点击
        initGridData();
        initRollData();
        initRollNotice();
        return view;
    }

    //初始化界面
    private void findView(View view){
        Toolbar mToolber = (Toolbar)view.findViewById(R.id.mainFragment_mainTool);
        mToolber.setTitle("主页");
     //   mRecyclerView = (RecyclerView)view.findViewById(R.id.function_list);
    //    mLayoutManager = new GridLayoutManager(getActivity(),3);
    //    mRecyclerView.setLayoutManager(mLayoutManager);
        mGridView = (GridView)view.findViewById(R.id.mainFragment_gridview);
        //滚动通知
        viewFlipper = (ViewFlipper)view.findViewById(R.id.roll_flipper);
    }

    private void initGridData(){
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < 9; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", mImages[i]);// 添加图像资源的ID
            map.put("ItemText", mContent[i]);// 按序号做ItemText
            lstImageItem.add(map);
        }
        //构建一个适配器
        SimpleAdapter simple = new SimpleAdapter(getActivity(), lstImageItem, R.layout.gridview_item,
                new String[] { "ItemImage", "ItemText" }, new int[] {R.id.gridview_item_card_image,
                R.id.gridview_item_card_name });
        mGridView.setAdapter(simple);
        //添加选择项监听事件
        mGridView.setOnItemClickListener(new GridView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){ //对应的功能点击
                    case 0:{
                        //显示toast信息
//                        Toast toast=Toast.makeText(getActivity(), "功能列表"+position, Toast.LENGTH_SHORT);
//                        toast.show();
                        Intent intent = new Intent(getActivity(),PayActivity.class);
                        intent.putExtra("put_data_pay","生活缴费");
                        startActivity(intent);
                    }break;
                    case 1:{
                        Intent intent = new Intent(getActivity(),MapActivity.class);
                        intent.putExtra("put_data_weizhi","我的位置");
                        startActivity(intent);

                    }break;
                    case 2:{
                        Intent intent = new Intent(getActivity(),ShopActivity.class);
                        intent.putExtra("put_data_shop","电商平台");
                        startActivity(intent);
                    }break;
                    case 3:{
                        Intent intent = new Intent(getActivity(),PeripheralActivity.class);
                        intent.putExtra("put_data_waishe","外设接口");
                        startActivity(intent);
                    }break;
                    case 4:{
                        Intent intent = new Intent(getActivity(),RepairActivity.class);
                        intent.putExtra("put_data_repair","报修管理");
                        startActivity(intent);
                    }break;
                    case 5:{
                        Intent intent = new Intent(getActivity(),CodeActivity.class);
                        intent.putExtra("put_data_code","通行二维码");
                        startActivity(intent);
                    }break;
                    case 6:{
//                        显示toast信息
                        Toast toast=Toast.makeText(getActivity(), "正在更新", Toast.LENGTH_SHORT);
                        toast.show();
                    }break;
                    case 7:{
                    }break;

                }
            }

        });
    }

    //添加数据，可以直接修改，关联Adapter修改功能的数量
    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<9; i++){
            FunctionListAdapter newData = new FunctionListAdapter(Data);
            FunctionListAdapter.Function_item function_item =  newData.new Function_item(String.valueOf(i));
            Data.add(function_item);
        }
    }

    //更新Adapter
    private void setAdapter(){
        mFunctionListAdapter = new FunctionListAdapter(Data);
        mRecyclerView.setAdapter(mFunctionListAdapter);
    }



    //添加点击事件
    private void clickItem(){
        mFunctionListAdapter.setmOnItemClickListener(new FunctionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){ //对应的功能点击
                    case 0:{
                        Intent intent = new Intent(getActivity(),PayActivity.class);
                        intent.putExtra("put_data","生活缴费");
                        startActivity(intent);
                    }break;
                    case 1:{
                        Intent intent = new Intent(getActivity(),MapActivity.class);
                        intent.putExtra("put_data","我的位置");
                        startActivity(intent);

                    }break;
                    case 2:{
                        Intent intent = new Intent(getActivity(),ShopActivity.class);
                        intent.putExtra("put_data","电商平台");
                        startActivity(intent);
                    }break;
                    case 3:{
                        Intent intent = new Intent(getActivity(),PeripheralActivity.class);
                        intent.putExtra("put_data","外设接口");
                        startActivity(intent);
                    }break;
                    case 4:{
                        Intent intent = new Intent(getActivity(),RepairActivity.class);
                        intent.putExtra("put_data","报修管理");
                        startActivity(intent);
                    }break;
                    case 5:{
                        Intent intent = new Intent(getActivity(),CodeActivity.class);
                        intent.putExtra("put_data","通行二维码");
                        startActivity(intent);
                    }break;
                    case 6:{
                    }break;
                    case 7:{
                    }break;

                }
            }
        });
    }

    private void initRollData(){
        // 滚动消息栏的显示内容
//        titleList.add("通知1");
//        titleList.add("通知2");
//        titleList.add("通知3");
//        titleList.add("通知4");
        card_message_tell.add("停水通知");
        card_message_tell.add("降温通知");
        card_message_tell.add("小区广场临时占用");
        card_message_tell.add("元旦联欢会计划");

        card_message_content.add("明后两天，由于管道维修，会短时停水，不影响正常生活。");
        card_message_content.add("下周北京地区会持续降温，请大家注意保暖，降温大概持续1到2个星期。");
        card_message_content.add("由于要举办活动，将在明天暂时占用广场，希望大家理解。");
        card_message_content.add("马上就要过元旦了，居委会要举办联欢会，如果31号晚上有时间，希望都来参加");

        card_message_time.add("昨天");
        card_message_time.add("1月19日");
        card_message_time.add("1月10日");
        card_message_time.add("2017年12月29日");
    }

    // 上下滚动消息栏
    private void initRollNotice() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
               if(getActivity() == null){
                   return;
               }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            moveNext();
                            Log.d("Task", "下一个");
                        }
                    });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 7000);
    }


    private void moveNext() {
        setView(this.mCurrPos, this.mCurrPos + 1);
        viewFlipper.setInAnimation(getActivity(), R.anim.in_bottomtop);
        viewFlipper.setOutAnimation(getActivity(), R.anim.out_bottomtop);
        viewFlipper.showNext();
    }

    // 将titleList 文本添加到 textView 中
    private void setView(int curr, int next) {

        View noticeView = getLayoutInflater().inflate(R.layout.notice_item, null);
        CardView cardView = (CardView)noticeView.findViewById(R.id.card_message);
        final TextView card_message_tell_tv = (TextView)cardView.findViewById(R.id.card_message_tell);
        final TextView card_message_content_tv = (TextView)cardView.findViewById(R.id.card_message_content);
        TextView card_message_time_tv = (TextView)cardView.findViewById(R.id.card_message_time);
        if ((curr < next) && (next > (card_message_content.size() - 1))) {
            next = 0;
        } else if ((curr > next) && (next < 0)) {
            next = card_message_content.size() - 1;
        }
//        notice_tv.setText(titleList.get(next));
        card_message_tell_tv.setText(card_message_tell.get(next));
        card_message_content_tv.setText(card_message_content.get(next));
        card_message_time_tv.setText(card_message_time.get(next));

        Log.e("通知", card_message_tell_tv.getText().toString());
        // 点击文本跳转到网络链接中
        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), SocietyNewMessagePage.class);
                intent.putExtra("put_data",card_message_tell_tv.getText().toString());
                startActivity(intent);
                System.out.println(card_message_content_tv.getText().toString());
                Toast.makeText(getActivity(), card_message_content_tv.getText(),Toast.LENGTH_LONG).show();
            }
        });
        if (viewFlipper.getChildCount() > 1) {
            viewFlipper.removeViewAt(0);
        }
        viewFlipper.addView(noticeView, viewFlipper.getChildCount());
        mCurrPos = next;

    }


}
