package com.example.wisdompark19.Society;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.NoticeItemAdapter;
import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyNewMessage extends BaseFragment {


    private static final String TAG = SocietyFragment.class.getSimpleName();
    private List<NoticeItemAdapter.Notice_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private NoticeItemAdapter mNoticeItemAdapter;
    private RecyclerView mRecyclerView;

    ArrayList<String> card_message_tell = new ArrayList<String>(); // 上下滚动消息栏内容
    ArrayList<String> card_message_content = new ArrayList<String>();
    ArrayList<String> card_message_time = new ArrayList<String>();

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_new_message, container, false);
        findView(view);
        initRollData();
        initData();
        setAdapter();
        setItemClick();
        return view;
    }
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            //更新界面数据，如果数据还在下载中，就显示加载框
//            notifyDataSetChanged();
//            if (mRefreshState == STATE_REFRESHING) {
//                mRefreshListener.onRefreshing();
//            }
            initRollData();
            initData();
            setAdapter();
            setItemClick();
        } else {
            //关闭加载框
//            mRefreshListener.onRefreshFinish();
        }
    }


//    @Override
//    protected void onFragmentFirstVisible() {
        //去服务器下载数据
//        mRefreshState = STATE_REFRESHING;
//        mCategoryController.loadBaseData();
 //   }

    private void initRollData(){
        card_message_tell.add("停水通知");
        card_message_tell.add("降温通知");
        card_message_tell.add("小区广场临时占用");
        card_message_tell.add("元旦联欢会计划");
        card_message_tell.add("公共设施维修通知");

        card_message_content.add("明后两天，由于管道维修，会短时停水，不影响正常生活。");
        card_message_content.add("下周北京地区会持续降温，请大家注意保暖，降温大概持续1到2个星期。");
        card_message_content.add("由于要举办活动，将在明天暂时占用广场，希望大家理解。");
        card_message_content.add("马上就要过元旦了，居委会要举办联欢会，如果31号晚上有时间，希望都来参加。");
        card_message_content.add("针对小区公共设施损坏的情况，将进行整体的维修清理，可能会产生一些噪音，希望" +
                "理解");

        card_message_time.add("昨天");
        card_message_time.add("1月19日");
        card_message_time.add("1月10日");
        card_message_time.add("2017年12月29日");
        card_message_time.add("2017年11月");
    }

    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_notice_item);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<5; i++){
            NoticeItemAdapter newData = new NoticeItemAdapter(Data);
            NoticeItemAdapter.Notice_item notice_item = newData.new Notice_item(card_message_tell.get(i),
                    card_message_content.get(i),card_message_time.get(i));
            Data.add(notice_item);
        }
    }

    private void setAdapter(){
        mNoticeItemAdapter = new NoticeItemAdapter(Data);
        mRecyclerView.setAdapter(mNoticeItemAdapter);
    }

    private void setItemClick(){
        mNoticeItemAdapter.setmOnItemClickListener(new NoticeItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(getActivity(), card_message_tell.get(position), Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getActivity(), SocietyNewMessagePage.class);
                intent.putExtra("put_data_mes",card_message_tell.get(position));
                startActivity(intent);
            }
        });
    }
}
