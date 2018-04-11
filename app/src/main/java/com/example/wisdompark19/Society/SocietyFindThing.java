package com.example.wisdompark19.Society;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.wisdompark19.Adapter.SocietyFindAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyFindThing extends BaseFragment {

    private List<SocietyFindAdapter.Society_Find_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private SocietyFindAdapter mSocietyFindAdapter;
    private RecyclerView mRecyclerView;
    public static final int UPDATE_FIND = 1;

    ArrayList<String> shop_trade_image = new ArrayList<String>();
    ArrayList<String> shop_trade_content = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_find_things, container, false);

        findView(view);
        initData();
        setAdapter();
        setItemClick();
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {

        } else {
            //关闭加载框

        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        //去服务器下载数据
        shop_trade_content = new ArrayList<>();
        connectData();
    }

    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.society_find_rec);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    //异步更新SPinner
    private Handler handler_find = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_FIND:{
                    initData();
                    setAdapter();
                    setItemClick();
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void connectData(){
        new Thread(){
            public void run(){
                try{
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功，失物招领");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找信息
                        String sql_connect = "select * from shiwu where shiwu_area = '" +
                                SharePreferences.getString(getActivity(), AppConstants.USER_AREA) +
                                "' order by shiwu_id desc";
                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        while (resultSet.next()){
                            findData(resultSet.getString("shiwu_content"));
                        }
                        Message message = new Message();
                        message.what = UPDATE_FIND;
                        handler_find.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Log.d("调试", "连接失败，发布消息");
                        Toast toast = Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private void findData(String content){
        shop_trade_content.add(content);
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<shop_trade_content.size(); i++){
            SocietyFindAdapter newData = new SocietyFindAdapter(Data);
            SocietyFindAdapter.Society_Find_item society_find_item = newData.new Society_Find_item(shop_trade_content.get(i));
            Data.add(society_find_item);
        }
    }

    private void setAdapter(){
        mSocietyFindAdapter = new SocietyFindAdapter(Data);
        mRecyclerView.setAdapter(mSocietyFindAdapter);
    }

    private void setItemClick(){
        mSocietyFindAdapter.setmOnItemClickListener(new SocietyFindAdapter.OnItemClickListener() {
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
