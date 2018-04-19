package com.example.wisdompark19.Main;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.wisdompark19.AutoProject.DealBitmap;
import com.example.wisdompark19.AutoProject.ForbidClickListener;
import com.example.wisdompark19.R;
import com.example.wisdompark19.Society.SocietyNewMessagePage;
import com.example.wisdompark19.ViewHelper.ShowImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.wisdompark19.AutoProject.AbsolutePath.getImageAbsolutePath;

/**
 * Created by ROBOSOFT on 2018/4/19.
 */

public class ShopAddActivity extends AppCompatActivity {
    private final int camera = 1;
    private final int album = 2;
    private List<ImageAdapter.Item_Image> ImageDatas;
    private List<String> ImagePath;
    private List<String> ImageData;
    private Button shop_add_ok;
    private EditText shop_add_title;
    private EditText shop_add_content;
    private EditText shop_add_price;
    private TextView shop_add_time;
    private RecyclerView shop_add_rv;
    private ImageView shop_add_take;
    private ImageView shop_add_add;
    private CardView shop_add_cav;
    private String title;
    private String content;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_add);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Toolbar toolbar = (Toolbar)findViewById(R.id.shop_add_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle("发布商品信息");
        back(toolbar);
        findView();
    }

    private void findView(){
        shop_add_ok = (Button)findViewById(R.id.shop_add_ok);
        shop_add_title = (EditText) findViewById(R.id.shop_add_title);
        shop_add_content = (EditText)findViewById(R.id.shop_add_content);
        shop_add_price = (EditText)findViewById(R.id.shop_add_price);
        shop_add_rv = (RecyclerView) findViewById(R.id.shop_add_rv);
        shop_add_take = (ImageView) findViewById(R.id.shop_add_take);
        shop_add_add = (ImageView) findViewById(R.id.shop_add_add);
        shop_add_cav = (CardView) findViewById(R.id.shop_add_tacv);

        ImageDatas = new ArrayList<>();
        ImagePath = new ArrayList<>();
        ImageData = new ArrayList<>();

        shop_add_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageDatas.size()>5){
                    Toast.makeText(ShopAddActivity.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    take_photo();
                }

            }
        });
        shop_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ImageDatas.size()>5){
                    Toast.makeText(ShopAddActivity.this,"最多添加6张",Toast.LENGTH_LONG).show();
                }else {
                    select_photo();
                }
            }
        });

        shop_add_ok.setOnClickListener(new ForbidClickListener() {

            @Override
            public void forbidClick(View v) {
                if(shop_add_content.getText().toString().isEmpty() || shop_add_title.getText().toString().isEmpty() ||
                        shop_add_price.getText().toString().isEmpty()){
                    Toast.makeText(ShopAddActivity.this,"内容不能为空",Toast.LENGTH_LONG).show();
                }else {
                    if(ImagePath.size()<6){
                        for(int i = 6-ImagePath.size(); i > 0; i--){
                            ImagePath.add(null);
                        }
                    }
                 //   UpdateData();
                }
            }
        });
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
                    ContentResolver cr = ShopAddActivity.this.getContentResolver();
                    showImage(DealBitmap.getBitmap(cr,data));
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
        shop_add_rv.setLayoutManager(new GridLayoutManager(this,3));
        ImageAdapter mAdapter = new ImageAdapter(ImageDatas);
        shop_add_rv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ShopAddActivity.this,ShowImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("image_select_name",ImageData.get(position));
                bundle.putInt("image_select_id",0);
                bundle.putInt("image_select_new",0);
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
