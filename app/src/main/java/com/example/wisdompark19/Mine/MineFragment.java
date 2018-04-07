package com.example.wisdompark19.Mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

import java.io.ByteArrayInputStream;
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


    private CircleImageView minefragment_picture;
    private TextView minefragment_name;
    private TextView minefragment_address;
    private TextView minefragment_phone;
    private TextView minefragment_ziliao;
    private TextView minefragment_recode;
    private TextView minefragment_setting;
    private TextView minefragment_back;

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
        findView(view);
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

        String imageBase64 = SharePreferences.getString(getActivity(),AppConstants.USER_PICTURE);
        byte[] byte64 = Base64.decode(imageBase64, 0);
        ByteArrayInputStream bais = new ByteArrayInputStream(byte64);
        Bitmap user_bitmap = BitmapFactory.decodeStream(bais);
        if(user_bitmap != null){
            minefragment_picture.setImageBitmap(user_bitmap);
        }else {
            minefragment_picture.setImageResource(R.mipmap.ic_launcher_round);
        }
        minefragment_name.setText(SharePreferences.getString(getActivity(),AppConstants.USER_NAME));//姓名
        minefragment_address.setText(SharePreferences.getString(getActivity(),AppConstants.USER_ADDRESS));//地址



        minefragment_phone.setOnClickListener(this);
        minefragment_ziliao.setOnClickListener(this);
        minefragment_recode.setOnClickListener(this);
        minefragment_setting.setOnClickListener(this);
        minefragment_back.setOnClickListener(this);
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
                        //清楚缓存
                        SharePreferences.putString(getActivity(), AppConstants.USER_PHONE,null);
                        SharePreferences.putString(getActivity(), AppConstants.USER_NAME,null);
                        SharePreferences.putString(getActivity(), AppConstants.USER_ADDRESS,null);
                        SharePreferences.putString(getActivity(), AppConstants.USER_SORT,null);
                        SharePreferences.putString(getActivity(), AppConstants.USER_PICTURE,null);
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
