package com.example.wisdompark19;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.ViewPagerAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.Main.MainFragment;
import com.example.wisdompark19.Main.MapActivity;
import com.example.wisdompark19.Mine.MineFragment;
import com.example.wisdompark19.Society.SocietyFragment;
import com.example.wisdompark19.ViewHelper.BottomNavigationViewHelper;
import com.example.wisdompark19.ViewHelper.NoScollViewPager;
import com.mysql.jdbc.Connection;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoScollViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;
    private String user_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        user_phone = SharePreferences.getString(MainActivity.this, AppConstants.USER_PHONE);
        findView(); //初始化布局
        startFragment();//执行点击或滑动
        setQuanXian();
      //  createConnect();
    }

    private void findView(){
        viewPager = (NoScollViewPager) findViewById(R.id.viewpager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
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

//    private void createConnect(){
//        new  Thread(){
//            public  void run(){
//                try {
//                    Class.forName("com.mysql.jdbc.Driver");//动态加载类
//                    String url = "jdbc:mysql://60.205.140.219:3306/shequ";
//                    //上面语句中 60.205.140.219为你的mysql服务器地址 3306为端口号   public是你的数据库名 根据你的实际情况更改
//                    Connection conn = (Connection) DriverManager.getConnection(url, "shequ", "Zz123456");
//                   //使用 DriverManger.getConnection链接数据库  第一个参数为连接地址 第二个参数为用户名 第三个参数为连接密码  返回一个Connection对象
//                    if(conn!=null){ //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
//                        Log.d("调试","连接成功");
//                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
//                        String sql = "select * from administrators"; //要执行的sql语句
//                        ResultSet rs = stmt.executeQuery(sql); //使用executeQury方法执行sql语句 返回ResultSet对象 即查询的结果
//                        while (rs.next()) {
//                            Log.d("rs", rs.getString("administrators_name"));
//                        }
//                    }else{
//                        Log.d("调试","连接失败");
//                    }
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }

}
