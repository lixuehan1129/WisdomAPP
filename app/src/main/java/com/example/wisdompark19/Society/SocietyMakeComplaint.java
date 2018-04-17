package com.example.wisdompark19.Society;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.SocietyComplaintItemAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/23.
 */

public class SocietyMakeComplaint extends BaseFragment {

    private List<SocietyComplaintItemAdapter.Society_Com_Item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private SocietyComplaintItemAdapter mSocietyComplaintItemAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final int UPDATE_COM = 1;

    ArrayList<String> society_com_content = new ArrayList<String>();
    ArrayList<Bitmap> society_com_image = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_make_complaint, container, false);

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
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        connectData();
    }

    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.society_complaint_rec);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.society_complaint_sr);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connectData();
            }
        });
    }

    //异步更新SPinner
    private Handler handler_find = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_COM:{
                    initData();
                    setAdapter();
                    setItemClick();
                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
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
                    society_com_image = new ArrayList<>();
                    society_com_content = new ArrayList<>();
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功，吐槽界面");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找信息
                        String sql_connect = "select * from tucao where tucao_area = '" +
                                SharePreferences.getString(getActivity(), AppConstants.USER_AREA) +
                                "' order by tucao_id desc";
                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        List<String> content_name = new ArrayList<>();
                        while (resultSet.next()){
                            content_name.add(resultSet.getString("tucao_phone"));
                            findData(resultSet.getString("tucao_content"));
                        }
                        resultSet.close();
                        for(int i = 0; i<content_name.size(); i++){
                            String sql_content_name = "select * from user where user_phone = '" +
                                    content_name.get(i) +
                                    "'";
                            ResultSet resultSet_content_name = stmt.executeQuery(sql_content_name);
                            resultSet_content_name.next();
                            Bitmap picture_path = null;
                            System.out.println(content_name.get(i));
                            Blob content_picture = resultSet_content_name.getBlob("user_picture");
                            if(content_picture != null){
                                InputStream inputStream = content_picture.getBinaryStream();
                                picture_path = DealBitmap.InputToBitmap(inputStream);
                            }
                            society_com_image.add(picture_path); //发布者头像
                            resultSet_content_name.close();
                        }
                        Message message = new Message();
                        message.what = UPDATE_COM;
                        handler_find.sendMessage(message);
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Log.d("调试", "连接失败，吐槽界面");
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
        society_com_content.add(content);
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<society_com_content.size(); i++){
            SocietyComplaintItemAdapter newData = new SocietyComplaintItemAdapter(Data);
            SocietyComplaintItemAdapter.Society_Com_Item society_com_item = newData.new Society_Com_Item(
                    society_com_content.get(i),society_com_image.get(i)
            );
            Data.add(society_com_item);
        }
    }

    private void setAdapter(){
        mSocietyComplaintItemAdapter = new SocietyComplaintItemAdapter(Data);
        mRecyclerView.setAdapter(mSocietyComplaintItemAdapter);
    }

    private void setItemClick(){
        mSocietyComplaintItemAdapter.setmOnItemClickListener(new SocietyComplaintItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(getActivity(), society_com_content.get(position), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
