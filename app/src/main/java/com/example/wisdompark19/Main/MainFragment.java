package com.example.wisdompark19.Main;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.AutoProject.TimeChange;
import com.example.wisdompark19.MainActivity;
import com.example.wisdompark19.R;
import com.example.wisdompark19.Repair.RepairActivity;
import com.example.wisdompark19.Society.SocietyNewMessagePage;
import com.example.wisdompark19.ViewHelper.BaseFragment;
import com.mysql.jdbc.Connection;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 最美人间四月天 on 2018/1/9.
 */

public class MainFragment extends BaseFragment {

    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver mReceiver;
    public static final int UPDATE_ROLL = 1;
    private GridView mGridView;
    private TextView textView;
    private int mCurrPos;
    ArrayList<String> card_message_tell = new ArrayList<String>(); // 上下滚动消息栏内容
    ArrayList<String> card_message_content = new ArrayList<String>();
    ArrayList<String> card_message_time = new ArrayList<String>();
    ArrayList<Integer> card_message_id = new ArrayList<>();
    ArrayList<Bitmap> card_message_image = new ArrayList<>();

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.BROAD_CON);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                //收到广播后所作的操作
                findView(getView());
                getData();
            }
        };
        broadcastManager.registerReceiver(mReceiver, intentFilter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
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
        initGridData();
        return view;
    }

    @Override
    protected void onFragmentFirstVisible() {
        //去服务器下载数据
        textView.getLayoutParams().height = textView.getLayoutParams().WRAP_CONTENT;
        getData();
    }


    //初始化界面
    private void findView(View view){
        Toolbar mToolber = (Toolbar)view.findViewById(R.id.mainFragment_mainTool);
        mToolber.setTitle("主页");
        mGridView = (GridView)view.findViewById(R.id.mainFragment_gridview);
        //滚动通知
        viewFlipper = (ViewFlipper)view.findViewById(R.id.roll_flipper);
        textView = (TextView)view.findViewById(R.id.roll_vis);
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
                        Toast toast=Toast.makeText(getActivity(), "正在更新", Toast.LENGTH_SHORT);
                        toast.show();
                    }break;
                    case 7:{
                    }break;

                }
            }

        });
    }

    private void getData(){
        new Thread(){
            public void run(){
                try {
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if(conn!=null){ //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试","连接成功,滚动消息");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        String sql = "select * from newmessage where newmessage_area = '" +
                                SharePreferences.getString(getActivity(),AppConstants.USER_AREA) +
                                "' order by newmessage_id desc limit 3";
                        ResultSet rs = stmt.executeQuery(sql); //使用executeQury方法执行sql语句 返回ResultSet对象 即查询的结果
                        List<String> content_name = new ArrayList<>();
                        while (rs.next()) {
                            content_name.add(rs.getString("newmessage_phone"));
                            initRollData(rs.getString("newmessage_title"),rs.getString("newmessage_content"),
                                    rs.getString("newmessage_time"),rs.getInt("newmessage_id"));
                        }
                        rs.close();
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
                        message.what = UPDATE_ROLL;
                        handler_roll.sendMessage(message);
                        JDBCTools.releaseConnection(stmt,conn);
                    }else{
                        Log.d("调试","连接失败，滚动消息");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private Handler handler_roll = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_ROLL:{
                    if(card_message_content.size() > 0){
                        textView.getLayoutParams().height = 0;
                        initRollNotice();
                    }
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void initRollData(String tell, String content, String time, int id ){
        // 滚动消息栏的显示内容
        card_message_tell.add(tell);
        card_message_content.add(content);
        card_message_time.add(TimeChange.StringToString(time));
        card_message_id.add(id);
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
        final CircleImageView card_message_image_tv = (CircleImageView)cardView.findViewById(R.id.card_message_image);
        final TextView card_message_tell_tv = (TextView)cardView.findViewById(R.id.card_message_tell);
        final TextView card_message_content_tv = (TextView)cardView.findViewById(R.id.card_message_content);
        final TextView card_message_time_tv = (TextView)cardView.findViewById(R.id.card_message_time);
        final TextView card_message_id_tv = (TextView)cardView.findViewById(R.id.card_message_id);
        if ((curr < next) && (next > (card_message_content.size() - 1))) {
            next = 0;
        } else if ((curr > next) && (next < 0)) {
            next = card_message_content.size() - 1;
        }
        card_message_tell_tv.setText(card_message_tell.get(next));
        card_message_content_tv.setText(card_message_content.get(next));
        card_message_time_tv.setText(card_message_time.get(next));
        card_message_image_tv.setImageBitmap(card_message_image.get(next));
        card_message_id_tv.setText(String.valueOf(card_message_id.get(next)));

        // 点击文本跳转到网络链接中
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), SocietyNewMessagePage.class);
                intent.putExtra("put_data_mes_id",Integer.valueOf(card_message_id_tv.getText().toString()));
                intent.putExtra("put_data_mes_select",1);
                intent.putExtra("put_data_mes_title",card_message_tell_tv.getText().toString());
                intent.putExtra("put_data_mes_content",card_message_content_tv.getText().toString());
                intent.putExtra("put_data_mes_time",card_message_time_tv.getText().toString());
                startActivity(intent);
            }
        });
        if (viewFlipper.getChildCount() > 1) {
            viewFlipper.removeViewAt(0);
        }
        viewFlipper.addView(noticeView, viewFlipper.getChildCount());
        mCurrPos = next;
    }
}
