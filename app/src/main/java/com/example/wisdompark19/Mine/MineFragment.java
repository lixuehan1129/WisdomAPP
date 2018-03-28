package com.example.wisdompark19.Mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.R;

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
    private TextView minefragment_regist;
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
        minefragment_regist = (TextView)view.findViewById(R.id.minefragment_regist);
        minefragment_setting = (TextView)view.findViewById(R.id.minefragment_setting);
        minefragment_back = (TextView)view.findViewById(R.id.minefragment_back);

        minefragment_phone.setOnClickListener(this);
        minefragment_ziliao.setOnClickListener(this);
        minefragment_recode.setOnClickListener(this);
        minefragment_regist.setOnClickListener(this);
        minefragment_setting.setOnClickListener(this);
        minefragment_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.minefragment_phone:{
                Toast toast=Toast.makeText(getActivity(), minefragment_phone.getText(), Toast.LENGTH_SHORT);
                toast.show();
            }
            case R.id.minefragment_ziliao:{
                Toast toast=Toast.makeText(getActivity(), minefragment_ziliao.getText(), Toast.LENGTH_SHORT);
                toast.show();
            }
            case R.id.minefragment_recode:{
                Toast toast=Toast.makeText(getActivity(), minefragment_recode.getText(), Toast.LENGTH_SHORT);
                toast.show();
            }
            case R.id.minefragment_regist:{
                Toast toast=Toast.makeText(getActivity(), minefragment_regist.getText(), Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getActivity(),MineLoginActivity.class);
                intent.putExtra("put_data_login","登录");
                startActivity(intent);
            }
            case R.id.minefragment_setting:{
                Toast toast=Toast.makeText(getActivity(), minefragment_setting.getText(), Toast.LENGTH_SHORT);
                toast.show();
            }
            case R.id.minefragment_back:{
                Toast toast=Toast.makeText(getActivity(), minefragment_back.getText(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
