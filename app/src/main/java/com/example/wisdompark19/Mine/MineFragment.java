package com.example.wisdompark19.Mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;
import com.mysql.jdbc.Connection;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 最美人间四月天 on 2018/1/9.
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    public static final int UPDATE_USER = 1;

    private CircleImageView minefragment_picture;
    private TextView minefragment_name;
    private TextView minefragment_address;
    private TextView minefragment_phone;
    private TextView minefragment_ziliao;
    private TextView minefragment_recode;
    private TextView minefragment_setting;
    private TextView minefragment_back;
    private InputStream inputStream; //用户头像
    private String user_phone; //用户名
    private String user_address;
    private String user_name;

    public static MineFragment newInstance(String info) {
        Bundle args = new Bundle();
        args.putString("info", info);
        MineFragment mineFragment = new MineFragment();
        mineFragment.setArguments(args);
        return mineFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.minefragment, null);
        Toolbar mToolbar = (Toolbar)view.findViewById(R.id.minefragment_mainTool);
        mToolbar.setTitle("我的");
        user_phone = SharePreferences.getString(getActivity(),AppConstants.user_phone);
        findView(view);
        if(minefragment_name.getText().toString().equals("正在加载")){
            initData();
        }
        return view;
    }

    private void findView(View view){
        minefragment_picture = (CircleImageView)view.findViewById(R.id.minefragment_picture);
        minefragment_name = (TextView)view.findViewById(R.id.minefragment_name);
        minefragment_address = (TextView)view.findViewById(R.id.minefragment_address);
        minefragment_phone = (TextView)view.findViewById(R.id.minefragment_phone);
        minefragment_ziliao = (TextView)view.findViewById(R.id.minefragment_ziliao);
        minefragment_recode = (TextView)view.findViewById(R.id.minefragment_recode);
        minefragment_setting = (TextView)view.findViewById(R.id.minefragment_setting);
        minefragment_back = (TextView)view.findViewById(R.id.minefragment_back);

        minefragment_phone.setOnClickListener(this);
        minefragment_ziliao.setOnClickListener(this);
        minefragment_recode.setOnClickListener(this);
        minefragment_setting.setOnClickListener(this);
        minefragment_back.setOnClickListener(this);
    }

    //异步更新界面
    private Handler handler_user = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_USER:{
                    //图片格式转换，InputStream转换为Bitmap
                    minefragment_picture.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                    if(user_name == null){
                        minefragment_name.setText("访客");
                    }else {
                        minefragment_name.setText(user_name);
                    }
                    if(user_address == null){
                        minefragment_address.setText("访客模式，请完善信息");
                    }else {
                        minefragment_address.setText(user_address);
                    }
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void initData(){
        new Thread(){
            public void run(){
                try {
                    Log.d("调试", "开始执行");
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) {
                        Statement statement_user = conn.createStatement();
                        String user_check_sql = "select * from user where user_phone = '" +
                                user_phone +
                                "'";
                        ResultSet resultSet_user = statement_user.executeQuery(user_check_sql);
                        resultSet_user.next();
                        user_address = resultSet_user.getString("user_address");
                        user_name = resultSet_user.getString("user_name");
                        Blob user_picture = resultSet_user.getBlob("user_picture");
                        inputStream = user_picture.getBinaryStream();
                        //更新UI
                        Message message = new Message();
                        message.what = UPDATE_USER;
                        handler_user.sendMessage(message);
                        //关闭数据库连接
                        resultSet_user.close();
                        JDBCTools.releaseConnection(statement_user,conn);

                    } else {
                        Log.d("调试", "连接失败");
                        Toast toast = Toast.makeText(getActivity(), "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minefragment_phone:{
                Toast toast=Toast.makeText(getActivity(), minefragment_phone.getText(), Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            case R.id.minefragment_ziliao:{
                Toast toast=Toast.makeText(getActivity(), minefragment_ziliao.getText(), Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            case R.id.minefragment_recode:{
                Toast toast=Toast.makeText(getActivity(), minefragment_recode.getText(), Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            case R.id.minefragment_setting:{
                Toast toast=Toast.makeText(getActivity(), minefragment_setting.getText(), Toast.LENGTH_SHORT);
                toast.show();
                break;
            }
            case R.id.minefragment_back:{
                showNormalDialog();
                break;
            }
        }
    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setMessage("确定退出？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharePreferences.putString(getActivity(), AppConstants.user_phone,null);
                        Intent intent = new Intent(getActivity(),MineLoginActivity.class);
                        intent.putExtra("put_data_login","登录");
                        startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 显示
        normalDialog.show();
    }
}
