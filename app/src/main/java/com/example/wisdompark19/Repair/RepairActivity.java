package com.example.wisdompark19.Repair;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.RepairCheckAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.wisdompark19.ViewHelper.DataBaseHelper;
import com.mysql.jdbc.Connection;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class RepairActivity extends AppCompatActivity {

    private List<RepairCheckAdapter.Repair_Check_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private RepairCheckAdapter mRepairCheckAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static final int UPDATE_REP = 1;
    private DataBaseHelper dataBaseHelper;

    ArrayList<String> repair_check_content = new ArrayList<String>();
//    ArrayList<String> repair_check_user = new ArrayList<String>();
    ArrayList<Integer> repair_check_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_acitivity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_repair");
        Toolbar toolbar = (Toolbar)findViewById(R.id.repair_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);
        findView();
   //     connectData();
    }

    private void findView(){
        Button repair_make = (Button)findViewById(R.id.repair_make);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.repair_sw);
        mRecyclerView = (RecyclerView)findViewById(R.id.repair_check_rec);
        mLayoutManager = new LinearLayoutManager(RepairActivity.this);
        dataBaseHelper = new DataBaseHelper(RepairActivity.this,AppConstants.SQL_VISION);
        mRecyclerView.setLayoutManager(mLayoutManager);

        repair_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairActivity.this,RepairMakeActivity.class);
                intent.putExtra("repair_check",0);
                startActivity(intent);
            }
        });

        getData();//加载网络内容改为加载本地数据
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                connectData();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connectData();
            }
        });
    }

    private void getData(){
        repair_check_content = new ArrayList<>();
        repair_check_id = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("repair",null,"repair_area = ?",new String[]{
                SharePreferences.getString(this,AppConstants.USER_AREA)
        },null,null,"repair_id desc");
        while (cursor.moveToNext()){
            //从本地数据库读取
            String content = cursor.getString(cursor.getColumnIndex("repair_content"));
            int id = cursor.getInt(cursor.getColumnIndex("repair_id"));
            findData(content,id);
        }
        cursor.close();
        sqLiteDatabase.close();
        //执行事件
        initData();
        setAdapter();
        setItemClick();
    }

    private Handler handler_rep = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_REP:{
                    getData();
//                    initData();
//                    setAdapter();
//                    setItemClick();
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
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
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功,报修管理");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找信息
                        String sql_connect;
                        if(SharePreferences.getInt(RepairActivity.this, AppConstants.USER_SORT) == 0){
                            sql_connect = "select * from repair where repair_area = '" +
                                    SharePreferences.getString(RepairActivity.this, AppConstants.USER_AREA) +
                                    "' order by repair_time desc limit 10";
                        }else {
                            sql_connect = "select * from repair where repair_name = '" +
                                    SharePreferences.getString(RepairActivity.this, AppConstants.USER_NAME) +
                                    "' order by repair_time desc limit 10";
                        }
                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
                        while (resultSet.next()){
                            if(!repair_check_id.contains(resultSet.getInt("repair_id"))){
                                ContentValues values = new ContentValues();
                                int id = resultSet.getInt("repair_id");
                                values.put("repair_id",id);
                                values.put("repair_name",resultSet.getString("repair_name"));
                                values.put("repair_phone",resultSet.getString("repair_phone"));
                                values.put("repair_time",resultSet.getString("repair_time"));
                                values.put("repair_area",resultSet.getString("repair_area"));
                                values.put("repair_title",resultSet.getString("repair_leixing"));
                                values.put("repair_content",resultSet.getString("repair_content"));
                                Blob picture1 = resultSet.getBlob("repair_picture1");
                                if(picture1 != null){
                                    values.put("repair_picture1",DealBitmap.compressImage(picture1,"_picture1_repair"+id));
                                }else {
                                    values.put("repair_picture1", (String) null);
                                }
                                Blob picture2 = resultSet.getBlob("repair_picture2");
                                if(picture2 != null){
                                    values.put("repair_picture2",DealBitmap.compressImage(picture2,"_picture2_repair"+id));
                                }else {
                                    values.put("repair_picture2", (String) null);
                                }
                                Blob picture3 = resultSet.getBlob("repair_picture3");
                                if(picture3 != null){
                                    values.put("repair_picture3",DealBitmap.compressImage(picture3,"_picture3_repair"+id));
                                }else {
                                    values.put("repair_picture3", (String) null);
                                }
                                Blob picture4 = resultSet.getBlob("repair_picture4");
                                if(picture4 != null){
                                    values.put("repair_picture4",DealBitmap.compressImage(picture4,"_picture4_repair"+id));
                                }else {
                                    values.put("repair_picture4", (String) null);
                                }
                                Blob picture5 = resultSet.getBlob("repair_picture5");
                                if(picture5 != null){
                                    values.put("repair_picture5",DealBitmap.compressImage(picture5,"_picture5_repair"+id));
                                }else {
                                    values.put("repair_picture5", (String) null);
                                }
                                Blob picture6 = resultSet.getBlob("repair_picture6");
                                if(picture6 != null){
                                    values.put("repair_picture6",DealBitmap.compressImage(picture6,"_picture6_repair"+id));
                                }else {
                                    values.put("repair_picture6", (String) null);
                                }
                                sqLiteDatabase.insert("repair",null,values);
                            }
//                            findData(resultSet.getString("repair_content"),
//                                     resultSet.getInt("repair_id"),
//                                     resultSet.getString("repair_phone"));
                        }
                        sqLiteDatabase.close();
                        Message message = new Message();
                        message.what = UPDATE_REP;
                        handler_rep.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Log.d("调试", "连接失败,报修管理");
                        Toast toast = Toast.makeText(RepairActivity.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private void findData(String content,int id){
        repair_check_content.add(content);
        repair_check_id.add(id);
//        repair_check_user.add(user);
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<repair_check_content.size(); i++){
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
                Intent intent = new Intent(RepairActivity.this,RepairMakeActivity.class);
                intent.putExtra("repair_check",1);
                intent.putExtra("repair_check_image",repair_check_id.get(position));
//                intent.putExtra("repair_user",repair_check_user.get(position));
                startActivity(intent);
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
