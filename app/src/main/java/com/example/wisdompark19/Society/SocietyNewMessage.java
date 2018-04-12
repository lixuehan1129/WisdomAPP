package com.example.wisdompark19.Society;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.NoticeItemAdapter;
import com.example.wisdompark19.Adapter.RegistAddAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.Mine.MineLoginActivity;
import com.example.wisdompark19.Mine.MineRegistAddActivity;
import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public static final int UPDATE_CONNECT = 1;


    ArrayList<String> card_message_tell = new ArrayList<>(); // 上下滚动消息栏内容
    ArrayList<String> card_message_content = new ArrayList<>();
    ArrayList<String> card_message_time = new ArrayList<>();
    ArrayList<Bitmap> card_message_image = new ArrayList<>();
    ArrayList<Integer> card_message_id = new ArrayList<>();

    @Override
    public void onStart(){
        super.onStart();
//        card_message_tell = new ArrayList<>();
//        card_message_content = new ArrayList<>();
//        card_message_time = new ArrayList<>();
//        card_message_image = new ArrayList<>();
//        card_message_id = new ArrayList<>();
//        connectData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.society_new_message, container, false);
        findView(view);

//        card_message_tell = new ArrayList<>();
//        card_message_content = new ArrayList<>();
//        card_message_time = new ArrayList<>();
//        card_message_image = new ArrayList<>();
//        card_message_id = new ArrayList<>();
//        connectData();
//        initData();
//        setAdapter();
//        setItemClick();
        return view;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            //更新界面数据，如果数据还在下载中，就显示加载框
//            initData();
//            setAdapter();
//            setItemClick();
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

    //异步更新SPinner
    private Handler handler_connect = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_CONNECT:{
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
                    card_message_tell = new ArrayList<>();
                    card_message_content = new ArrayList<>();
                    card_message_time = new ArrayList<>();
                    card_message_image = new ArrayList<>();
                    card_message_id = new ArrayList<>();
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功,新的消息");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找信息
                        String sql_connect = "select * from newmessage where newmessage_area = '" +
                                SharePreferences.getString(getActivity(), AppConstants.USER_AREA) +
                                "' order by newmessage_id desc";
                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        List<String> content_name = new ArrayList<>();
                        while (resultSet.next()){
                            content_name.add(resultSet.getString("newmessage_phone"));
                            initRollData(resultSet.getString("newmessage_title"),
                                    resultSet.getString("newmessage_content"),
                                    resultSet.getString("newmessage_time"),
                                    resultSet.getInt("newmessage_id"));
                        }
                        for(int i = 0; i<content_name.size(); i++){
                            String sql_content_name = "select * from user where user_phone = '" +
                                    content_name.get(i) +
                                    "'";
                            ResultSet resultSet_content_name = stmt.executeQuery(sql_content_name);
                            resultSet_content_name.next();
                            Bitmap picture_path = null;
                            Blob content_picture = resultSet_content_name.getBlob("user_picture");
                            if(content_picture != null){
                                InputStream inputStream = content_picture.getBinaryStream();
                                picture_path = DealBitmap.InputToBitmap(inputStream);
                            }
                            card_message_image.add(picture_path); //发布者头像
                            resultSet_content_name.close();
                        }
                        Message message = new Message();
                        message.what = UPDATE_CONNECT;
                        handler_connect.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Log.d("调试", "连接失败,新的消息");
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

    private void initRollData(String title, String content, String time, int id){
        if(title.isEmpty()){
            card_message_tell.add(content);
        }else {
            card_message_tell.add(title);
        }
        card_message_content.add(content);
        card_message_time.add(StringToString(time));
        card_message_id.add(id);
    }
    //时间格式转换
    private String StringToString(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private void findView(View view){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.rv_notice_item);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<card_message_content.size(); i++){
            NoticeItemAdapter newData = new NoticeItemAdapter(Data);
            NoticeItemAdapter.Notice_item notice_item = newData.new Notice_item(card_message_tell.get(i),
                    card_message_content.get(i),card_message_time.get(i),card_message_image.get(i),
                    card_message_id.get(i));
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
                intent.putExtra("put_data_mes_id",card_message_id.get(position));
                intent.putExtra("put_data_mes_select",1);
                intent.putExtra("put_data_mes_title",card_message_tell.get(position));
                intent.putExtra("put_data_mes_content",card_message_content.get(position));
                intent.putExtra("put_data_mes_time",card_message_time.get(position));
                startActivity(intent);
            }
        });
    }
}
