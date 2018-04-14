package com.example.wisdompark19.Repair;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.ImageAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;
import com.example.wisdompark19.Society.SocietyNewMessagePage;
import com.example.wisdompark19.ViewHelper.CustomDatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.wisdompark19.AutoProject.AbsolutePath.getImageAbsolutePath;

/**
 * Created by 最美人间四月天 on 2018/3/13.
 */

public class RepairMakeActivity extends AppCompatActivity implements View.OnClickListener{

    private final int camera = 1;
    private final int album = 2;
    private int mes_select;
    private List<ImageAdapter.Item_Image> ImageDatas = new ArrayList<>();
    private List<String> ImagePath = new ArrayList<>();
    private List<String> ImageData = new ArrayList<>();
    private List<Bitmap> ImageGetPath = new ArrayList<>();
    private int intent_data_id = 0;

    private TextView repair_time, repair_name, repair_add;
    private CircleImageView repair_image;
    private EditText repair_edit;
    private RecyclerView repair_rv;
    private CardView repair_tacv;
    private ImageView repair_make_take, repair_make_add;
    private Spinner repair_spinner;
    private Button repair_button_ok;
    private CustomDatePicker mCustomDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_make);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data");
        Toolbar toolbar = (Toolbar)findViewById(R.id.repair_make_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle("报修");
        setSupportActionBar(toolbar);
        back(toolbar);
        findView();
    }


    private void findView(){
        repair_name = (TextView)findViewById(R.id.repair_name);
        repair_add = (TextView)findViewById(R.id.repair_add);
        repair_spinner = (Spinner)findViewById(R.id.repair_spinner);
        repair_time = (TextView)findViewById(R.id.repair_time);
        repair_image = (CircleImageView) findViewById(R.id.repair_image);
        repair_button_ok = (Button)findViewById(R.id.repair_make_ok);
        repair_edit = (EditText)findViewById(R.id.repair_make_edit);
        repair_rv = (RecyclerView)findViewById(R.id.repair_make_rv);
        repair_tacv = (CardView)findViewById(R.id.repair_make_tacv);
        repair_make_take = (ImageView)findViewById(R.id.repair_make_take);
        repair_make_add = (ImageView)findViewById(R.id.repair_make_add);
        String imageBase64 = SharePreferences.getString(RepairMakeActivity.this, AppConstants.USER_PICTURE);
        Bitmap user_bitmap = DealBitmap.StringToBitmap(imageBase64);
        repair_image.setImageBitmap(user_bitmap);
        String name = SharePreferences.getString(RepairMakeActivity.this,AppConstants.USER_NAME) +
                "(" + SharePreferences.getString(RepairMakeActivity.this,AppConstants.USER_PHONE) +
                ")";
        repair_name.setText(name);
        repair_add.setText(SharePreferences.getString(RepairMakeActivity.this,AppConstants.USER_ADDRESS));

        repair_time.setOnClickListener(RepairMakeActivity.this);
        repair_button_ok.setOnClickListener(this);
        repair_make_take.setOnClickListener(this);
        repair_make_add.setOnClickListener(this);
        initDatePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repair_time:{
                mCustomDatePicker.show(repair_time.getText().toString());
                break;
            }
            case R.id.repair_make_ok:{
                if(repair_edit.getText().toString().isEmpty()){
                    Toast.makeText(RepairMakeActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }else {
                    if(ImagePath.size()<6){
                        for(int i = 6-ImagePath.size(); i > 0; i--){
                            ImagePath.add(null);
                        }
                    }
                    UpdateData();
                }
                break;
            }
            case R.id.repair_make_add:{
                if(ImageDatas.size()>5){
                    Toast.makeText(RepairMakeActivity.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    select_photo();
                }
                break;
            }
            case R.id.repair_make_take:{
                if(ImageDatas.size()>5){
                    Toast.makeText(RepairMakeActivity.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    take_photo();
                }
                break;
            }
        }
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
                        String repair_sql_insert = "insert into repair (repair_name,repair_phone,repair_area,repair_time," +
                                "repair_leixing,repair_content,repair_picture1,repair_picture2," +
                                "repair_picture3,repair_picture4,repair_picture5,repair_picture6,repair_select_time) " +
                                "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        preparedStatement = (java.sql.PreparedStatement)conn.prepareStatement(repair_sql_insert,Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, SharePreferences.getString(RepairMakeActivity.this, AppConstants.USER_NAME));
                        preparedStatement.setString(2, SharePreferences.getString(RepairMakeActivity.this, AppConstants.USER_PHONE));
                        preparedStatement.setString(3, SharePreferences.getString(RepairMakeActivity.this, AppConstants.USER_AREA));
                        preparedStatement.setString(4, getTime());
                        preparedStatement.setString(5, repair_spinner.getSelectedItem().toString());
                        preparedStatement.setString(6, repair_edit.getText().toString());
                        preparedStatement.setString(13,repair_time.getText().toString());
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
                        Toast toast = Toast.makeText(RepairMakeActivity.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();
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

    //获取系统时间，并进行格式转换
    private String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = simpleDateFormat.format(new Date());
        return dateString;
    }

    //从拍照或相册获取图片
    //找到图片路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case camera:
                if(data != null) {
                    ContentResolver cr = RepairMakeActivity.this.getContentResolver();
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

    /*
   * 加载图片
   * */
    private void showImage(String image_Path){
        File file = new File(image_Path);
        compressWithLs(file);
        ImageAdapter first = new ImageAdapter(ImageDatas);
        ImageAdapter.Item_Image item_image = first.new Item_Image(DealBitmap.UriToBitmap(image_Path));
        ImageData.add(image_Path);
        ImageDatas.add(item_image);
        update();
    }

    private void update(){
        repair_rv.setLayoutManager(new GridLayoutManager(this,3));
        ImageAdapter mAdapter = new ImageAdapter(ImageDatas);
        repair_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(RepairMakeActivity.this,ShowImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_select_name",ImageData.get(position));
                bundle.putInt("image_select_id",0);
                bundle.putInt("image_select_new",intent_data_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
                        ImagePath.add(image_load_path);
                        // Glide.with(RegisterActivity.this).load(file).into(image);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    //自定义时间选择
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+3);
        String future = sdf.format(calendar.getTime());
        repair_time.setText(now);

        mCustomDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                repair_time.setText(time);
            }
        }, now, future); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        mCustomDatePicker.showSpecificTime(true); // 显示时和分
        mCustomDatePicker.setIsLoop(true); // 允许循环滚动
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
//            case R.id.society_new_message_page_save:
//                finish();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
