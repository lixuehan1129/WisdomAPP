package com.example.wisdompark19.ViewHelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.R;

import java.io.ByteArrayInputStream;


/**
 * Created by 最美人间四月天 on 2017/5/18.
 */

public class ShowImage extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        full(true);
        setContentView(R.layout.show_image);

        Bundle bundle = this.getIntent().getExtras();
        String Image_select = bundle.getString("image_select");
        RelativeLayout back = (RelativeLayout)this.findViewById(R.id.back_act);
        DragImageView img = (DragImageView)this.findViewById(R.id.large_image);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;//加载到内存中
        Bitmap bm = BitmapFactory.decodeFile(Image_select,options);
        img.setImageBitmap(bm);

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
