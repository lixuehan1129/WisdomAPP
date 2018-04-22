package com.example.wisdompark19;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.Mine.MineLoginActivity;
import com.example.wisdompark19.Mine.MineRegistActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ROBOSOFT on 2018/4/18.
 */

public class SplashActivity extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(!SharePreferences.getString(SplashActivity.this, AppConstants.USER_PHONE).isEmpty()){
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(SplashActivity.this,MineLoginActivity.class);
            startActivity(intent);
        }
    }
}
