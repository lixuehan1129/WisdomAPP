package com.example.wisdompark19.Society;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.SocietyComplaintItemAdapter;
import com.example.wisdompark19.Adapter.SocietyMemberAdapter;
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

public class SocietyMemberCheck extends BaseFragment {

    private List<SocietyMemberAdapter.Item_member> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private SocietyMemberAdapter mSocietyMemberAdapter;
    private RecyclerView mRecyclerView;
    public static final int UPDATE_MEM = 1;

    ArrayList<Bitmap> member_image = new ArrayList<>();
    ArrayList<String> member_name = new ArrayList<String>();
    ArrayList<String> member_phone = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_member_check, container, false);

        findView(view);
//        findData();
//        initData();
//        setAdapter();
//        setItemClick();
        return view;
    }
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            //更新界面数据，如果数据还在下载中，就显示加载框
            System.out.println("第一次出现1111");
        } else {
            //关闭加载框

        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        //去服务器下载数据
        connectData();
        System.out.println("第一次出现");
    }

    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.society_member_rec);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private Handler handler_mem = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_MEM:{
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
                    member_image = new ArrayList<>();
                    member_name = new ArrayList<>();
                    member_phone = new ArrayList<>();
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功,成员列表");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找信息
                        String sql_connect = "select * from user where user_area = '" +
                                SharePreferences.getString(getActivity(), AppConstants.USER_AREA) +
                                "' order by user_sort";
                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        while (resultSet.next()){
                            Bitmap user_picture = null;
                            Blob blob = resultSet.getBlob("user_picture");
                            if(blob != null){
                                user_picture = DealBitmap.InputToBitmap(blob.getBinaryStream());
                            }
                            findData(user_picture,resultSet.getString("user_name"),
                                    resultSet.getString("user_phone"));
                        }
                        Message message = new Message();
                        message.what = UPDATE_MEM;
                        handler_mem.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Log.d("调试", "连接失败,成员列表");
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

    private void findData(Bitmap bitmap, String name, String phone){
        member_image.add(bitmap);
        member_name.add(name);
        member_phone.add(phone);
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<member_name.size(); i++){
            SocietyMemberAdapter newData = new SocietyMemberAdapter(Data);
            SocietyMemberAdapter.Item_member item_member = newData.new Item_member(
                    member_name.get(i),member_image.get(i),member_phone.get(i)
            );
            Data.add(item_member);
        }
    }

    private void setAdapter(){
        mSocietyMemberAdapter = new SocietyMemberAdapter(Data);
        mRecyclerView.setAdapter(mSocietyMemberAdapter);
    }

    private void setItemClick(){
        mSocietyMemberAdapter.setOnItemClickListener(new SocietyMemberAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(getActivity(), member_name.get(position), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
