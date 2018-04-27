package com.example.wisdompark19;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.wisdompark19.Adapter.ViewPagerAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.Main.MainFragment;
import com.example.wisdompark19.Mine.MineFragment;
import com.example.wisdompark19.Society.SocietyFragment;
import com.example.wisdompark19.ViewHelper.BottomNavigationViewHelper;
import com.example.wisdompark19.ViewHelper.DataBaseHelper;
import com.example.wisdompark19.ViewHelper.NoScollViewPager;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.mysql.jdbc.Connection;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private NoScollViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        findView(); //初始化布局
        init();
        startFragment();//执行点击或滑动
        setQuanXian();
        createConnect();
    }

    private void findView(){
        viewPager = (NoScollViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager.setOffscreenPageLimit(2);
    }

    private void startFragment(){
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_main:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.item_society:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.item_mine:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(MainFragment.newInstance("主页"));
        viewPagerAdapter.addFragment(SocietyFragment.newInstance("社区"));
        viewPagerAdapter.addFragment(MineFragment.newInstance("我的"));
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setQuanXian(){
        //获取权限
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.VIBRATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.VIBRATE);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if(!permissionList.isEmpty()){
            String[] permissions= permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
    }
    //Android按返回键，程序进入后台运行，不关闭程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
           // moveTaskToBack(false);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void createConnect(){
        new  Thread(){
            public  void run(){
                try {
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if(conn!=null){ //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试","连接成功");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        String sql = "select * from user where user_phone = '" +
                                SharePreferences.getString(MainActivity.this,AppConstants.USER_PHONE) +
                                "'"; //要执行的sql语句
                        ResultSet rs = stmt.executeQuery(sql); //使用executeQury方法执行sql语句 返回ResultSet对象 即查询的结果
                        while (rs.next()) {
                            int user_sort = rs.getInt("user_sort");
                            SharePreferences.remove(MainActivity.this,AppConstants.USER_SORT);
                            SharePreferences.putInt(MainActivity.this,AppConstants.USER_SORT,user_sort);
                        }
                        rs.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else{
                        Log.d("调试","连接失败");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Looper.loop();
                }
            }
        }.start();
    }

    private void init(){
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

}
