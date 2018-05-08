package com.example.wisdompark19.Main;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisdompark19.R;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class PeripheralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peripheral_activity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_waishe");
        Toolbar toolbar = (Toolbar)findViewById(R.id.per_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);
        findView();
    }

    private void findView(){
        TextView textView = (TextView)findViewById(R.id.test_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if(adapter == null){
                    Toast.makeText(PeripheralActivity.this,"设备不支持蓝牙",Toast.LENGTH_LONG).show();
                }else {
                    if (!adapter.isEnabled()) {
                        adapter.enable();
                    }else {
                        adapter.disable();
                    }
                    Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600); //3600为蓝牙设备可见时间 startActivity(enable);
                }
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
