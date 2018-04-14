package com.example.wisdompark19.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.QRCodeUtil;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;
import com.mysql.jdbc.Connection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class CodeActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_activity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_code");
        Toolbar toolbar = (Toolbar)findViewById(R.id.code_activity_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);  //标题栏名称
        back(toolbar);    //返回
        imageView = (ImageView)findViewById(R.id.code_activity_iv);
        setView();
    }

    private void setView(){
        String sex = SharePreferences.getString(CodeActivity.this, AppConstants.USER_SEX);
        String user_phone = SharePreferences.getString(CodeActivity.this, AppConstants.USER_PHONE);
      //  String user_number = user_phone;
        String user_number = "2";
        int user_sort = SharePreferences.getInt(CodeActivity.this, AppConstants.USER_SORT);
        String model = null;
        String user_name = SharePreferences.getString(CodeActivity.this, AppConstants.USER_NAME);
        String user_address = SharePreferences.getString(CodeActivity.this, AppConstants.USER_ADDRESS);
        if(user_sort == 0){
            model = "管理员";
        }else if(user_sort ==1){
            model = "业主";
        }else {
            model = "访客";
            user_name = "访客";
            user_address = "访客";
        }
        String time = getTime();
        if(sex.equals("男")){
            user_number = "0";
        }else if(sex.equals("女")){
            user_number = "1";
        }

        String setContent = user_number + " " + user_name + " " + model + " " + time + " " + user_address;
        String md5Content = md5(setContent);
        String newContent = user_number + "," + user_name + "," + model + "," + time + "," + user_address + ","
                + md5Content.substring(md5Content.length()-8);
        System.out.println(newContent);
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(newContent,640,640);
        imageView.setImageBitmap(bitmap);
    }

    //获取系统时间，并进行格式转换
    private String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(new Date());
        return dateString;
    }

    //MD5加密
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
