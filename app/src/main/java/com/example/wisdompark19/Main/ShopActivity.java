package com.example.wisdompark19.Main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.ShopTradeItemAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;
import com.mysql.jdbc.Connection;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 * 社区交易界面
 */
public class ShopActivity extends AppCompatActivity {

    private List<ShopTradeItemAdapter.Shop_Trade_item> Data;
    private ShopTradeItemAdapter mShopTradeItemAdapter;
    private RecyclerView mRecyclerView;
    private Button shop_ok;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static final int UPDATE_SHOP = 1;

    ArrayList<Bitmap> shop_trade_image = new ArrayList<>(); // 上下滚动消息栏内容
    ArrayList<String> shop_trade_title = new ArrayList<>();
    ArrayList<String> shop_trade_price = new ArrayList<>();
    ArrayList<Integer> shop_trade_id = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onlineshopping_activity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_shop");
        Toolbar toolbar = (Toolbar)findViewById(R.id.shop_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);

        findView();
    }

    private void findView(){
        shop_ok = (Button) findViewById(R.id.shop_ok);
        mRecyclerView = (RecyclerView) findViewById(R.id.shop_trade_rec);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.shop_swip);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        shop_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this,ShopAddActivity.class);
                startActivity(intent);
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                connectData();
            }
        });

    }

    private void connectData(){
        new Thread(){
            public void run(){
                try{
                    shop_trade_title = new ArrayList<>();
                    shop_trade_price = new ArrayList<>();
                    shop_trade_image = new ArrayList<>();
                    shop_trade_id = new ArrayList<>();
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功,商品列表");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找信息
                        String sql_connect = "select * from shop where shop_area = '" +
                                SharePreferences.getString(ShopActivity.this, AppConstants.USER_AREA) +
                                "' order by shop_id desc";
                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        while (resultSet.next()){
                            Blob picture1 = resultSet.getBlob("shop_picture1");
                            Bitmap bitmap1 = null;
                            if(picture1 != null){
                                InputStream inputStream1 = picture1.getBinaryStream();
                                bitmap1 = DealBitmap.InputToBitmap(inputStream1);
                            }
                            findData(bitmap1,resultSet.getString("shop_title"),
                                    resultSet.getString("shop_price"),
                                    resultSet.getInt("shop_id"));
                        }

                        Message message = new Message();
                        message.what = UPDATE_SHOP;
                        handler_shop.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Log.d("调试", "连接失败,商品列表");
                        Toast toast = Toast.makeText(ShopActivity.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    private Handler handler_shop = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_SHOP:{
                    initData();
                    setAdapter();
                    mSwipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwipeRefreshLayout.setRefreshing(false);
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

    private void findData(Bitmap bitmap, String title, String price, int id){
        shop_trade_image.add(bitmap);
        shop_trade_title.add(title);
        shop_trade_price.add("￥"+price);
        shop_trade_id.add(id);
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<shop_trade_title.size(); i++){
            ShopTradeItemAdapter newData = new ShopTradeItemAdapter(Data);
            ShopTradeItemAdapter.Shop_Trade_item shop_trade_item = newData.new Shop_Trade_item(
                    shop_trade_image.get(i),shop_trade_title.get(i),shop_trade_price.get(i)
            );
            Data.add(shop_trade_item);
        }
    }

    private void setAdapter(){
        mShopTradeItemAdapter = new ShopTradeItemAdapter(Data);
        mRecyclerView.setAdapter(mShopTradeItemAdapter);
        setItemClick();
    }

    private void setItemClick(){
        mShopTradeItemAdapter.setmOnItemClickListener(new ShopTradeItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast toast=Toast.makeText(ShopActivity.this, shop_trade_title.get(position), Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(ShopActivity.this,ShopPageActivity.class);
                intent.putExtra("put_data",shop_trade_title.get(position));
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

