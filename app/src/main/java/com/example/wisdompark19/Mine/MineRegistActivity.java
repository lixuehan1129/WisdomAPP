package com.example.wisdompark19.Mine;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.wisdompark19.AutoProject.JDBCTools;
import com.example.wisdompark19.R;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
//import com.mysql.jdbc.Connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by 最美人间四月天 on 2018/3/29.
 */

public class MineRegistActivity extends AppCompatActivity{

    boolean hasFocus_pre_password = false;
    boolean hasFocus_pre_password_again = false;
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    String touxiang_path = "null";

    private CircleImageView user_register_picture;
    private TextInputLayout user_regist_number_layout;
    private TextInputLayout user_regist_password_layout;
    private TextInputLayout user_regist_again_layout;
    private TextInputEditText user_regist_number;
    private TextInputEditText user_regist_password;
    private TextInputEditText user_regist_again;
    private Button user_regist_button;

    public static final int UPDATE_DIALOG = 1;

    String user_phone;
    String user_password;
    int user_sort = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_regist);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //不弹出输入法
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_regist");
        Toolbar toolbar = (Toolbar)findViewById(R.id.user_regist_mainTool); //标题栏
        toolbar.setTitle("用户注册");  //标题栏名称
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        back(toolbar);

        findView();  //定义控件
        problem_jiaodian();   //关闭输入法

        select_touxiang();  //添加头像
        initEdit();    //输入内容
        upload();
    }

    private void findView(){
        user_register_picture = (CircleImageView)findViewById(R.id.user_register_picture);
        user_regist_number_layout = (TextInputLayout)findViewById(R.id.user_regist_number_layout);
        user_regist_password_layout = (TextInputLayout)findViewById(R.id.user_regist_password_layout);
        user_regist_again_layout = (TextInputLayout)findViewById(R.id.user_regist_again_layout);
        user_regist_number = (TextInputEditText) findViewById(R.id.user_regist_number);
        user_regist_password = (TextInputEditText) findViewById(R.id.user_regist_password);
        user_regist_again = (TextInputEditText) findViewById(R.id.user_regist_again);
        user_regist_button = (Button)findViewById(R.id.user_regist_button);
    }

    private void initEdit(){

        user_regist_number_layout.setCounterEnabled(true);  //设置可以计数
        user_regist_number_layout.setCounterMaxLength(11); //计数的最大值

        user_regist_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus_pre_password==false && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password = hasFocus;
                   user_regist_password.requestFocus();
                }
            }
        });
        user_regist_again.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus_pre_password_again==false && hasFocus){
                    ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                    hasFocus_pre_password_again = hasFocus;
                    user_regist_again.requestFocus();
                }
            }
        });
        user_regist_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                user_regist_password.requestFocus();
                return false;
            }
        });
        user_regist_again.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((ScrollView)findViewById(R.id.register_scrollview)).fullScroll(ScrollView.FOCUS_DOWN);
                user_regist_again.requestFocus();
                return false;
            }
        });

        /*
        * 手机号
        * */
        user_regist_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_regist_number_layout.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String number = user_regist_number.getText().toString();
                if(number.length()<1){
                    user_regist_number_layout.setError("昵称不能为空");
                }
            }
        });

        /*
        * 密码输入监听
        * */
        user_regist_password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_regist_password_layout.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String password = user_regist_password.getText().toString();
                String password_again = user_regist_again.getText().toString();
                if(password.length()>5){
                    if(password_again.equals(password)){
                        user_regist_again_layout.setErrorEnabled(false);
                    }else {
                        if(password_again.length()>0)
                        {
                            user_regist_again_layout.setError("两次密码输入不一致");
                        }
                    }}else {
                    user_regist_password_layout.setError("密码错误(不少于6位)");
                }
            }
        });

        /*
        * 再次输入密码监听
        * */
        user_regist_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user_regist_again_layout.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                String password = user_regist_password.getText().toString();
                String password_again = user_regist_password.getText().toString();
                if(!password_again.equals(password)){
                    user_regist_password_layout.setError("两次密码输入不一致");
                }
            }
        });
    }

    /*
   * 选择头像
   * */
    private void select_touxiang() {
        user_register_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MineRegistActivity.this,"选择一张图片",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);

            }
        });

    }

    /*
   * 从相册中选择图片
   * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String imagePath;
            imagePath = getImageAbsolutePath(this,selectedImage);
            showImage(imagePath);
            File image = new File(imagePath);
            compressWithLs(image);
        }
    }


    /*
    * 加载更换头像
    * */
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        user_register_picture.setImageBitmap(bm);
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
                        touxiang_path = file.getAbsolutePath();

                        // Glide.with(RegisterActivity.this).load(file).into(image);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    //上传内容，注册监听
    private void upload(){
        user_regist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      progressDialog.dismiss();
                System.out.println(user_regist_number.getText().toString());
                if(user_regist_number.getText().toString().length() == 11  && user_regist_password.getText().toString().length()  > 5){
                    if(user_regist_password.getText().toString().equals(user_regist_again.getText().toString()) ){
                    Log.e("头像1",touxiang_path);
                    System.out.println(user_regist_number.getText().toString());
                    //首先连接数据库，查找
                    createConnect();
                } else {
                    Toast toast=Toast.makeText(MineRegistActivity.this, "两次密码不相同", Toast.LENGTH_SHORT);
                    toast.show();
                }
                }else {
                    Toast toast=Toast.makeText(MineRegistActivity.this, "手机号，密码格式不正确", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    private void createConnect(){
        new  Thread() {
            public void run() {
                try {
                    Looper.prepare();//用于toast
                    Connection conn = JDBCTools.getConnection("shequ","Zz123456");
                    if (conn != null) { //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
                        Log.d("调试", "连接成功");
                        Statement stmt = conn.createStatement(); //根据返回的Connection对象创建 Statement对象
                        //查找手机号是否已存在
                        String user_sql_phone = "select * from user where user_phone = '" +
                                user_regist_number.getText().toString() +
                                "'";
                        ResultSet resultSet_phone = stmt.executeQuery(user_sql_phone);
                        if(resultSet_phone.next()){
                            Log.d("调试", "不是空值");
                            Toast toast = Toast.makeText(MineRegistActivity.this, "用户名已存在", Toast.LENGTH_SHORT);
                            toast.show();
                        }else {
                            Log.d("调试", "结果为空，执行");
                            user_phone = user_regist_number.getText().toString();
                            //手机号判断
                            user_password = user_regist_password.getText().toString();

                            java.sql.PreparedStatement preparedStatement = null;
                            String user_sql_insert = "insert into user (user_sort,user_phone,user_password,user_picture) values(?,?,?,?)";
                            preparedStatement = (java.sql.PreparedStatement)conn.prepareStatement(user_sql_insert,Statement.RETURN_GENERATED_KEYS);
                            preparedStatement.setInt(1,2);
                            preparedStatement.setString(2,user_phone);
                            preparedStatement.setString(3,user_password);
                            Log.e("头像",touxiang_path);
                            File file = new File(touxiang_path);
                            if(file.exists()){
                                try {
                                    InputStream inputStream = new FileInputStream(file);
                                    preparedStatement.setBinaryStream(4,inputStream,file.length());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                preparedStatement.setBinaryStream(4,null, 0);
                            }
                            preparedStatement.executeUpdate();
                            preparedStatement.close();
                            if (resultSet_phone != null) {
                                try {
                                    resultSet_phone.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            JDBCTools.releaseConnection(stmt,conn);
                            showNormalDialog();
                        }

                    } else {
                        Log.d("调试", "连接失败");
                        Toast toast = Toast.makeText(MineRegistActivity.this, "请检查网络", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }.start();

    }

    private void showNormalDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MineRegistActivity.this);
        normalDialog.setMessage("注册成功，填写详细信息成为业主。");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MineRegistActivity.this,MineRegistAddActivity.class);
                        intent.putExtra("put_data_regist_add",user_phone);
                        intent.putExtra("put_data_regist_select","regist");
                        startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("跳过",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        // 显示
        normalDialog.show();
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

    /*
   * 点击空白区域 Edittext失去焦点 关闭输入法
   * */
    private void problem_jiaodian() {
        final ScrollView scrollView = (ScrollView) findViewById(R.id.register_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.clearFocus();
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

}