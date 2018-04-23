package com.example.wisdompark19.Repair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Connection;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class RepairActivity extends AppCompatActivity {
    private List<RepairCheckAdapter.Repair_Check_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private RepairCheckAdapter mRepairCheckAdapter;
    private RecyclerView mRecyclerView;
    public static final int UPDATE_REP = 1;

    ArrayList<String> repair_check_content = new ArrayList<String>();
    ArrayList<String> repair_check_user = new ArrayList<String>();
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
        connectData();
    }

    private void findView(){
        Button repair_make = (Button)findViewById(R.id.repair_make);
        mRecyclerView = (RecyclerView)findViewById(R.id.repair_check_rec);
        mLayoutManager = new LinearLayoutManager(RepairActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        repair_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepairActivity.this,RepairMakeActivity.class);
                intent.putExtra("repair_check",0);
                startActivity(intent);
            }
        });
    }
    private Handler handler_rep = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_REP:{
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
                    repair_check_content = new ArrayList<>();
                    repair_check_id = new ArrayList<>();
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
                                    "' order by repair_time order by repair_id desc";
                        }else {
                            sql_connect = "select * from repair where repair_name = '" +
                                    SharePreferences.getString(RepairActivity.this, AppConstants.USER_NAME) +
                                    "' order by repair_time order by repair_id desc";
                        }

                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        while (resultSet.next()){
                            findData(resultSet.getString("repair_content"),
                                     resultSet.getInt("repair_id"),
                                     resultSet.getString("repair_phone"));
                        }
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
    private void findData(String content,int id,String user){
        repair_check_content.add(content);
        repair_check_id.add(id);
        repair_check_user.add(user);
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
                Toast toast=Toast.makeText(RepairActivity.this, repair_check_content.get(position), Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(RepairActivity.this,RepairMakeActivity.class);
                intent.putExtra("repair_check",1);
                intent.putExtra("repair_check_image",repair_check_id.get(position));
                intent.putExtra("repair_user",repair_check_user.get(position));
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
