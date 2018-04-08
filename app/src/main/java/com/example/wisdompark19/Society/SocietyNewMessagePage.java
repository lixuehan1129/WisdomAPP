package com.example.wisdompark19.Society;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.Adapter.ImageAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.Main.CodeActivity;
import com.example.wisdompark19.Mine.MineRegistActivity;
import com.example.wisdompark19.R;
import com.example.wisdompark19.ViewHelper.ShowImage;
import com.mysql.jdbc.Connection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

/**
 * Created by 最美人间四月天 on 2018/3/16.
 */

public class SocietyNewMessagePage extends AppCompatActivity {

    private final int camera = 1;
    private final int album = 2;
    private int mes_select;
    private List<ImageAdapter.Item_Image> ImageDatas;
    private List<String> ImagePath;
    private Button society_new_message_page_ok;
    private EditText society_new_message_page_title;
    private EditText society_new_message_page_content;
    private RecyclerView society_new_message_page_rv;
    private ImageView society_new_message_page_take;
    private ImageView society_new_message_page_add;
    private CardView society_new_message_page_cav;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.society_new_message_page);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_mes");
        mes_select = intent.getIntExtra("put_data_mes_select",1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.society_new_message_page_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        setSupportActionBar(toolbar);
        back(toolbar);
        findView();
    }

    private void findView(){
        society_new_message_page_ok = (Button)findViewById(R.id.society_new_message_page_ok);
        society_new_message_page_title = (EditText) findViewById(R.id.society_new_message_page_title);
        society_new_message_page_content = (EditText)findViewById(R.id.society_new_message_page_content);
        society_new_message_page_rv = (RecyclerView) findViewById(R.id.society_new_message_page_rv);
        society_new_message_page_take = (ImageView) findViewById(R.id.society_new_message_page_take);
        society_new_message_page_add = (ImageView) findViewById(R.id.society_new_message_page_add);
        society_new_message_page_cav = (CardView) findViewById(R.id.society_new_message_page_tacv);

        if(mes_select == 1){
            society_new_message_page_ok.setVisibility(View.INVISIBLE);
            society_new_message_page_cav.setVisibility(View.INVISIBLE);
            society_new_message_page_title.setFocusable(false);
            society_new_message_page_content.setFocusable(false);
        }else {
            ImageDatas = new ArrayList<>();
            ImagePath = new ArrayList<>();
        }

        society_new_message_page_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageDatas.size()>5){
                    Toast.makeText(SocietyNewMessagePage.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    take_photo();
                }

            }
        });
        society_new_message_page_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageDatas.size()>5){
                    Toast.makeText(SocietyNewMessagePage.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    select_photo();
                }
            }
        });

        society_new_message_page_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(society_new_message_page_content.getText().toString().isEmpty()){
                    Toast.makeText(SocietyNewMessagePage.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }else {
                    if(ImagePath.size()<6){
                        for(int i = 6-ImagePath.size(); i > 0; i--){
                            ImagePath.add(null);
                        }
                    }
                    UpdateData();
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
                        String newmessage_sql_insert = "insert into newmessage (newmessage_name,newmessage_phone,newmessage_area,newmessage_time," +
                                "newmessage_title,newmessage_content,newmessage_picture1,newmessage_picture2," +
                                "newmessage_picture3,newmessage_picture4,newmessage_picture5,newmessage_picture6) values(?,?,?,?,?,?,?,?,?,?,?,?)";
                        preparedStatement = (java.sql.PreparedStatement)conn.prepareStatement(newmessage_sql_insert,Statement.RETURN_GENERATED_KEYS);
                        preparedStatement.setString(1, SharePreferences.getString(SocietyNewMessagePage.this, AppConstants.USER_NAME));
                        preparedStatement.setString(2, SharePreferences.getString(SocietyNewMessagePage.this, AppConstants.USER_PHONE));
                        preparedStatement.setString(3, SharePreferences.getString(SocietyNewMessagePage.this, AppConstants.USER_AREA));
                        preparedStatement.setString(4, getTime());
                        preparedStatement.setString(5, society_new_message_page_title.getText().toString());
                        preparedStatement.setString(6, society_new_message_page_content.getText().toString());
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
                        Toast toast = Toast.makeText(SocietyNewMessagePage.this, "请检查网络", Toast.LENGTH_SHORT);
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
                    ContentResolver cr = SocietyNewMessagePage.this.getContentResolver();
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
        ImageAdapter.Item_Image item_image = first.new Item_Image(image_Path);
        ImageDatas.add(item_image);
        update();
    }

    private void update(){
        society_new_message_page_rv.setLayoutManager(new GridLayoutManager(this,3));
        ImageAdapter mAdapter = new ImageAdapter(ImageDatas);
        society_new_message_page_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SocietyNewMessagePage.this,ShowImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_select",ImageDatas.get(position).getItem_image());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private String BitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50,baos);
        String imageBase64 = new String (Base64.encode(baos.toByteArray(), 0));
        return imageBase64;
    }

    private Bitmap StringToBitmap(String s){
        String imageBase64 = s;
        byte[] byte64 = Base64.decode(imageBase64, 0);
        ByteArrayInputStream bais = new ByteArrayInputStream(byte64);
        Bitmap bitmap = BitmapFactory.decodeStream(bais);
        return bitmap;
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

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
