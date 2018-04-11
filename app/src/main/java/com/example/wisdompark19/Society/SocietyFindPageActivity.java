package com.example.wisdompark19.Society;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.ImageAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.ShowImage;
import com.mysql.jdbc.Connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.wisdompark19.Mine.MineRegistActivity.getImageAbsolutePath;

/**
 * Created by 最美人间四月天 on 2018/3/19.
 */

public class SocietyFindPageActivity extends AppCompatActivity {

    private final int camera = 1;
    private final int album = 2;
    private int mes_select;
    private List<ImageAdapter.Item_Image> ImageDatas;
    private List<String> ImagePath;
    private Button society_find_page_ok;
    private EditText society_find_page_title;
    private EditText society_find_page_content;
    private RecyclerView society_find_page_rv;
    private ImageView society_find_page_take;
    private ImageView society_find_page_add;
    private CardView society_find_page_tacv;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.society_find_page);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_find");
        mes_select = intent.getIntExtra("put_data_find_select",1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.society_find_page_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);
        findView();
    }

    private void findView(){
        society_find_page_ok = (Button)findViewById(R.id.society_find_page_ok);
        society_find_page_title = (EditText) findViewById(R.id.society_find_page_name);
        society_find_page_content = (EditText)findViewById(R.id.society_find_page_neirong);
        society_find_page_rv = (RecyclerView) findViewById(R.id.society_find_page_rv);
        society_find_page_take = (ImageView) findViewById(R.id.society_find_page_take);
        society_find_page_add = (ImageView) findViewById(R.id.society_find_page_add);
        society_find_page_tacv = (CardView) findViewById(R.id.society_find_page_tacv);

        if(mes_select == 1){
            society_find_page_ok.setVisibility(View.INVISIBLE);
            society_find_page_tacv.setVisibility(View.INVISIBLE);
            society_find_page_title.setFocusable(false);
            society_find_page_content.setFocusable(false);
        }else {
            ImageDatas = new ArrayList<>();
            ImagePath = new ArrayList<>();
        }

        society_find_page_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageDatas.size()>5){
                    Toast.makeText(SocietyFindPageActivity.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    take_photo();
                }

            }
        });
        society_find_page_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageDatas.size()>5){
                    Toast.makeText(SocietyFindPageActivity.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    select_photo();
                }
            }
        });

        society_find_page_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(society_find_page_content.getText().toString().isEmpty()){
                    Toast.makeText(SocietyFindPageActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }else {
                    if(ImagePath.size() == 0){
                        Toast.makeText(SocietyFindPageActivity.this,"请添加一张物品图片，以便认领",Toast.LENGTH_LONG).show();
                    }else {
                        if(ImagePath.size()<6){
                            for(int i = 6-ImagePath.size(); i > 0; i--){
                                ImagePath.add(null);
                            }
                        }
                        UpdateData();
                    }

                }
            }
        });
    }

    private void UpdateData(){
        new Thread(){
            public void run(){
                try{
                    Looper.prepare();//用于toast
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象

                        //上传
                        java.sql.PreparedStatement preparedStatement = null;
                        String newmessage_sql_insert = "insert into shiwu (shiwu_name,shiwu_phone,shiwu_area,shiwu_time," +
                                "shiwu_title,shiwu_content,shiwu_picture1,shiwu_picture2," +
                                "shiwu_picture3,shiwu_picture4,shiwu_picture5,shiwu_picture6) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                        preparedStatement = (java.sql.PreparedStatement)conn.prepareStatement(newmessage_sql_insert,Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, SharePreferences.getString(SocietyFindPageActivity.this, AppConstants.USER_NAME));
                        preparedStatement.setString(2, SharePreferences.getString(SocietyFindPageActivity.this, AppConstants.USER_PHONE));
                        preparedStatement.setString(3, SharePreferences.getString(SocietyFindPageActivity.this, AppConstants.USER_AREA));
                        preparedStatement.setString(4, getTime());
                        preparedStatement.setString(5, society_find_page_title.getText().toString());
                        preparedStatement.setString(6, society_find_page_content.getText().toString());
                        for(int i = 0; i < 6; i++){
                            File file = null;
                            System.out.println(ImagePath.get(i));
                            if(ImagePath.get(i) != null){
                                file = new File(ImagePath.get(i));
                                if(file.exists()){
                                    try {
                                        InputStream inputStream = new FileInputStream(file);
                                        preparedStatement.setBinaryStream(7+i,inputStream,file.length());
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    preparedStatement.setBinaryStream(7+i,null, 0);
                                }
                            }else {
                                preparedStatement.setBinaryStream(7+i,null, 0);
                            }
                        }
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                        JDBCTools.releaseConnection(stmt,conn);
                        finish();
                    }else {
                        Log.d("调试", "连接失败");
                        Toast toast = Toast.makeText(SocietyFindPageActivity.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
    }

    //获取系统时间，并进行格式转换
    private String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(new Date());
        return dateString;
    }

    //调用相机拍照
    private void take_photo(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, camera);
    }
    //调用系统相册
    private void select_photo(){
        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, album);
    }

    //从拍照或相册获取图片
    //找到图片路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case camera:
                if(data != null) {
                    ContentResolver cr = SocietyFindPageActivity.this.getContentResolver();
                    Bitmap bitmap_camera = null;
                    Uri uri_camera = data.getData();
                    Bundle extras = null;
                    try {
                        if(data.getData() != null)
                            //这个方法是根据Uri获取Bitmap图片的静态方法
                            bitmap_camera = MediaStore.Images.Media.getBitmap(cr, uri_camera);
                            //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                        else
                            extras = data.getExtras();
                        bitmap_camera = extras.getParcelable("data");

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    FileOutputStream fileOutputStream = null;
                    File file = null;
                    try {
                        // 获取 SD 卡根目录
                        String saveDir = Environment.getExternalStorageDirectory() + "/IMG";
                        // 新建目录
                        File dir = new File(saveDir);
                        if (! dir.exists()) dir.mkdir();
                        // 生成文件名
                        SimpleDateFormat t = new SimpleDateFormat("yyyyMMddssSSS");
                        String filename = "IMG_" + (t.format(new Date())) + ".jpg";
                        // 新建文件
                        file = new File(saveDir, filename);
                        // 打开文件输出流
                        fileOutputStream = new FileOutputStream(file);
                        // 生成图片文件
                        bitmap_camera.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        // 相片的完整路径
                        System.out.println( file.getPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    showImage(file.getPath());
                }
                break;
            case album:
                if (data != null) {
                    Uri uri = data.getData();
                    String imagePath;
                    imagePath = getImageAbsolutePath(this, uri);
                    showImage(imagePath);
                }
                break;

        }
    }

    /**
     * 压缩单张图片 Listener 方式
     */
    private void compressWithLs(File file) {
        Luban.get(this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setFilename("user_"+System.currentTimeMillis())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }
                    @Override
                    public void onSuccess(File file) {
                        Log.i("path", file.getAbsolutePath());
                        String image_load_path = file.getAbsolutePath();
                        System.out.println("1"+ImagePath.size());
                        ImagePath.add(image_load_path);
                        System.out.println("1"+ImagePath.size());

                        // Glide.with(RegisterActivity.this).load(file).into(image);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    /*
    * 加载图片
    * */
    private void showImage(String image_Path){
        File file = new File(image_Path);
        compressWithLs(file);
        ImageAdapter first = new ImageAdapter(ImageDatas);
        ImageAdapter.Item_Image item_image = first.new Item_Image(DealBitmap.UriToBitmap(image_Path));
        ImageDatas.add(item_image);
        update();
    }

    //显示图片
    private void update(){
        society_find_page_rv.setLayoutManager(new GridLayoutManager(this,3));
        ImageAdapter mAdapter = new ImageAdapter(ImageDatas);
        society_find_page_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SocietyFindPageActivity.this,ShowImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_select",ImagePath.get(position));
                intent.putExtras(bundle);
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
