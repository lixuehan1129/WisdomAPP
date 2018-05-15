package com.example.wisdompark19.Main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.AmountView;
import com.example.wisdompark19.ViewHelper.DataBaseHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 最美人间四月天 on 2018/3/19.
 */

public class ShopPageActivity extends AppCompatActivity {

    private SliderLayout sliderLayout;
    private ArrayList<String> shop_url = new ArrayList<>();
    private TextView shop_message;
    private TextView shop_price;
    private String name;
    private float price;
    private int size = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_trade_page);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.colorWu)); //设置顶部系统栏颜色
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Intent intent = getIntent();
        int intent_data = intent.getIntExtra("put_shop_id",0);
        Toolbar toolbar = (Toolbar)findViewById(R.id.shop_trade_page_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_black);
        back(toolbar);
        findView();
        initData(intent_data);
        problem_jiaodian();
    }

    @SuppressLint("SetTextI18n")
    private void initData(int i){
        DataBaseHelper dataBaseHelper = new DataBaseHelper(ShopPageActivity.this, AppConstants.SQL_VISION);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("shop",null,"shop_id = ?",new String[]{String.valueOf(i)},
                null,null,"shop_id desc");
        if(cursor.getCount() == 1){
            while (cursor.moveToNext()){
                String bitmap1 = cursor.getString(cursor.getColumnIndex("shop_picture1"));
                if(bitmap1 != null){
                    shop_url.add(bitmap1);
                }
                String bitmap2 = cursor.getString(cursor.getColumnIndex("shop_picture2"));
                if(bitmap2 != null){
                    shop_url.add(bitmap2);
                }
                String bitmap3 = cursor.getString(cursor.getColumnIndex("shop_picture3"));
                if(bitmap3 != null){
                    shop_url.add(bitmap3);
                }
                String bitmap4 = cursor.getString(cursor.getColumnIndex("shop_picture4"));
                if(bitmap4 != null){
                    shop_url.add(bitmap4);
                }
                String bitmap5 = cursor.getString(cursor.getColumnIndex("shop_picture5"));
                if(bitmap5 != null){
                    shop_url.add(bitmap5);
                }
                String bitmap6 = cursor.getString(cursor.getColumnIndex("shop_picture6"));
                if(bitmap6 != null){
                    shop_url.add(bitmap6);
                }
                name = cursor.getString(cursor.getColumnIndex("shop_title"));
                price = Float.parseFloat(cursor.getString(cursor.getColumnIndex("shop_price")));
                shop_message.setText(cursor.getString(cursor.getColumnIndex("shop_content")));
                shop_price.setText("￥" + cursor.getString(cursor.getColumnIndex("shop_price")));
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        rollPicture();
    }

    private void findView(){
        sliderLayout = (SliderLayout) findViewById(R.id.shop_trade_page_slider);
        shop_message = (TextView) findViewById(R.id.shop_trade_page_text);
        shop_price = (TextView) findViewById(R.id.shop_trade_page_price);
        TextView shop_pay = (TextView) findViewById(R.id.shop_trade_page_pay);
        AmountView mAmountView = (AmountView) findViewById(R.id.amount_view);
        mAmountView.setGoods_storage(50);
        mAmountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                size = amount;
            }
        });

        shop_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNormalDialog();
            }
        });
    }

    //图片滚动
    private void rollPicture(){
        for (int i=0; i<shop_url.size(); i++){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            textSliderView
                    .image(new File(shop_url.get(i)))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)  //图片缩放类型
                    .setOnSliderClickListener(onSliderClickListener); //图片点击
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("shop_picture",shop_url.get(i)); //传入参数
            sliderLayout.addSlider(textSliderView); //添加页面
        }
//        if(shop_url.size() > 1){
//            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);//滑动动画
//        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//默认指示器样式
//        sliderLayout.setCustomAnimation(new DescriptionAnimation());//设置图片描述显示动画
//            sliderLayout.setDuration(4000);//设置滚动时间，也是计时器时间
//       }
        sliderLayout.setDuration(5000);
        sliderLayout.addOnPageChangeListener(onPageChangeListener);
    }

    private BaseSliderView.OnSliderClickListener onSliderClickListener=new BaseSliderView.OnSliderClickListener() {
        @Override
        public void onSliderClick(BaseSliderView slider) {
            Toast.makeText(ShopPageActivity.this,slider.getBundle().get("shop_picture") + "",
                    Toast.LENGTH_SHORT).show();
        }
    };

    //页面改变监听
    private ViewPagerEx.OnPageChangeListener onPageChangeListener=new ViewPagerEx.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            Log.d("ansen", "Page Changed: " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ShopPageActivity.this);
        normalDialog.setMessage("商品信息:  "+ name +"\n\n"+
                                "数   量 :  "+ size +"\n\n"+
                                "总   价 :  "+ size*price);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
    /*
      * 点击空白区域 Edittext失去焦点 关闭输入法
      * */
    @SuppressLint("ClickableViewAccessibility")
    private void problem_jiaodian() {
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.sss_rela);
        relativeLayout.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                relativeLayout.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                return false;
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

    //性能优化。当页面显示时进行自动播放
    @Override
    protected void onStart() {
        super.onStart();
        sliderLayout.startAutoCycle();
    }
    //性能优化。当页面不显示时暂停自动播放
    @Override
    protected void onStop() {
        super.onStop();
        sliderLayout.stopAutoCycle();
    }
}

