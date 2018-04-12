package com.example.wisdompark19.ViewHelper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.R;
import com.mysql.jdbc.Connection;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


/**
 * Created by 最美人间四月天 on 2017/5/18.
 */

public class ShowImage extends AppCompatActivity {
    public static final int UPDATE_SHOW = 1;
    private DragImageView img;
    private Bitmap bitmap;
    private InputStream inputStream;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        full(true);
        setContentView(R.layout.show_image);
        RelativeLayout back = (RelativeLayout)this.findViewById(R.id.back_act);
        img = (DragImageView)this.findViewById(R.id.large_image);
        Bundle bundle = this.getIntent().getExtras();
        String image_select_name = bundle.getString("image_select_name");
        int image_select_id = bundle.getInt("image_select_id");
        int image_selece_new = bundle.getInt("image_select_new");

        if(image_select_id == 0){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;//加载到内存中
            Bitmap bm = BitmapFactory.decodeFile(image_select_name,options);
            img.setImageBitmap(bm);
        }else if(image_select_id == 1){
            getPicture(image_selece_new,image_select_name);
        }else {
            Toast.makeText(this, "图片加载出现错误",Toast.LENGTH_SHORT).show();
            ShowImage.this.finish();
        }

//       Bitmap bitmap = DealBitmap.StringToBitmap(image_select);
//        img.setImageBitmap(bitmap);
//        img.setImageBitmap(StringToBitmap(Image_select));
//        Toast toast = Toast.makeText(this, "点击图片即可返回",Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM, 0, 0);
//        toast.show();

        back.setOnClickListener(new View.OnClickListener() { // 点击返回
            public void onClick(View paramView) {
                System.out.println("点击图片返回");
                full(false);
                ShowImage.this.finish();
            }
        });

    }

    private void getPicture(final int intent_data_id, final String picture_name){
        new Thread(){
            public void run(){
                try{
                    Looper.prepare();
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功,消息界面");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找信息
                        String sql_connect = "select * from newmessage where newmessage_id = '" +
                                intent_data_id +
                                "'";
                        ResultSet resultSet = stmt.executeQuery(sql_connect);
                        resultSet.next();
                        Blob picture = resultSet.getBlob(picture_name);
                        if(picture != null){
                            inputStream = picture.getBinaryStream();
                        }
                        Message message = new Message();
                        message.what = UPDATE_SHOW;
                        handler_show.sendMessage(message);
                        resultSet.close();
                        JDBCTools.releaseConnection(stmt,conn);
                    }else {
                        Log.d("调试", "连接失败,消息界面");
                        Toast toast = Toast.makeText(ShowImage.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    //异步更新
    private Handler handler_show = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case UPDATE_SHOW:{
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;//加载到内存中
                    Bitmap mBitmap = BitmapFactory.decodeStream(inputStream,null,options);
                    img.setImageBitmap(mBitmap.copy(Bitmap.Config.ARGB_8888, true));
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    //显示图片时隐藏状态栏
    private void full(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
