package com.example.wisdompark19.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.RegistAddAdapter;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.R;
import com.mysql.jdbc.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/4/2.
 */

public class MineRegistAddActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText user_regist_add_name;
    private Spinner user_regist_add_spinner;
    private EditText user_regist_add_address;
    private Button user_regist_add_ok;
    private String user_regist_add_spinner_name;
    private String user_phone;
    private int user_sort;
    private String select;
    private RegistAddAdapter mRegistAddAdapter;
    public static final int UPDATE_SPINNER = 1;
    List<RegistAddAdapter.mSpinner>  spinners = new ArrayList<RegistAddAdapter.mSpinner>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist_add);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_regist_add");
        select = intent.getStringExtra("put_data_regist_select");
        user_phone = intent_data;
     //   user_phone = "17888836863";
        Toolbar toolbar = (Toolbar)findViewById(R.id.user_regist_add_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle("业主认证");  //标题栏名称
        back(toolbar);    //返回
        findView();
        initSpinner();
        save();
    }

    private void findView(){
        user_regist_add_ok = (Button)findViewById(R.id.user_regist_add_ok);
        user_regist_add_name = (EditText)findViewById(R.id.user_regist_add_name);
        user_regist_add_spinner = (Spinner)findViewById(R.id.user_regist_add_spinner);
        user_regist_add_address = (EditText)findViewById(R.id.user_regist_add_address);
        user_regist_add_ok.setOnClickListener(this);
    }

    //异步更新SPinner
    private Handler handler_community = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_SPINNER:{
                    mRegistAddAdapter = new RegistAddAdapter(this,spinners);
                    user_regist_add_spinner.setAdapter(mRegistAddAdapter);
                    user_regist_add_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            user_regist_add_spinner_name = spinners.get(position).getName();
                            Toast toast=Toast.makeText(MineRegistAddActivity.this, user_regist_add_spinner_name, Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

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


    //联网查询小区数据
    private void initSpinner(){
        new Thread(){
            public void run(){
                try {
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) {
                        Statement statement_community = conn.createStatement();
                        String community_sql = "select * from community";
                        ResultSet resultSet_community = statement_community.executeQuery(community_sql);
                        while (resultSet_community.next()){
                            spinners.add(new RegistAddAdapter.mSpinner(resultSet_community.getString("community_name")));
                        }

                        Message message = new Message();
                        message.what = UPDATE_SPINNER;
                        handler_community.sendMessage(message);

                        JDBCTools.releaseConnection(statement_community,conn);
                    } else {
                        Log.d("调试", "连接失败");
                        Toast toast=Toast.makeText(MineRegistAddActivity.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    //按钮点击事件，保存内容
    private void save(){
            user_regist_add_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(user_regist_add_name.getText().toString()) ||
                            TextUtils.isEmpty(user_regist_add_address.getText().toString()) ||
                            TextUtils.isEmpty(user_regist_add_spinner_name)){
                        Toast toast=Toast.makeText(MineRegistAddActivity.this, "内容不能为空", Toast.LENGTH_SHORT);
                        toast.show();
                    }else {
                        update();
                    }
                }
            });

    }

    //上传数据
    private void update(){
        new Thread(){
            public void run(){
                try {
                    Looper.prepare();
                    Connection conn_update = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn_update!= null) {
                        if(user_phone != null){
                            Statement statement_update = conn_update.createStatement();
                            String update_sql_phone = "select * from user where user_phone = '" +
                                    user_phone +
                                    "'";
                            ResultSet resultSet_update_phone = statement_update.executeQuery(update_sql_phone);
                            resultSet_update_phone.next();
                            if(resultSet_update_phone.getInt("user_sort") == 0){
                                user_sort = 0; //管理员
                            }else {
                                user_sort = 1; //业主
                            }
                            try {
                                resultSet_update_phone.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            String update_sql = "update user set user_sort = ?, user_name = ?, user_address = ?, " +
                                    "user_area = ? where user_phone = ?";
                            java.sql.PreparedStatement preparedStatement = null;
                            preparedStatement = conn_update.prepareStatement(update_sql);
                            preparedStatement.setInt(1,user_sort);
                            preparedStatement.setString(2,user_regist_add_name.getText().toString());
                            preparedStatement.setString(3,user_regist_add_address.getText().toString());
                            preparedStatement.setString(4,user_regist_add_spinner_name);
                            preparedStatement.setString(5,user_phone);
                            preparedStatement.executeUpdate();//执行更新操作
                            preparedStatement.close();
                            JDBCTools.releaseConnection(statement_update,conn_update);
                            if(select.equals("regist")){
                                Intent intent = new Intent(MineRegistAddActivity.this,MineLoginActivity.class);
                                startActivity(intent);
                            }else {
                                Toast toast=Toast.makeText(MineRegistAddActivity.this, "修改完成", Toast.LENGTH_SHORT);
                                toast.show();
                            }

                        }
                    } else {
                        Log.d("调试", "连接失败");
                        Toast toast=Toast.makeText(MineRegistAddActivity.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
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

    @Override
    public void onClick(View v) {

    }
}
