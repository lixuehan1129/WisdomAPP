package com.example.wisdompark19.Main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wisdompark19.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class PeripheralActivity extends AppCompatActivity {
    private Button On,Off,Visible,list;
    private BluetoothAdapter bluetoothAdapter;
    private LinearLayout linearLayout;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv,lv1;
    ArrayList<String> search;
    ArrayList<BluetoothDevice> add;

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
//        TextView textView = (TextView)findViewById(R.id.test_text);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                if(bluetoothAdapter == null){
//                    Toast.makeText(PeripheralActivity.this,"设备不支持蓝牙",Toast.LENGTH_LONG).show();
//                }else {
//                    if (!bluetoothAdapter.isEnabled()) {
//                        //通过用于弹出选择对话框开启
//                        Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        startActivityForResult(enabler, 100);
//                    }else {
//                        bluetoothAdapter.disable();
//                    }
//                    Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                    enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600); //3600为蓝牙设备可见时间 startActivity(enable);
//                }
//            }
//        });
        On = (Button)findViewById(R.id.button1);
        Off = (Button)findViewById(R.id.button2);
        Visible = (Button)findViewById(R.id.button3);
        list = (Button)findViewById(R.id.button4);
        lv = (ListView)findViewById(R.id.listView1);
        lv1 = (ListView)findViewById(R.id.listView2);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        linearLayout = (LinearLayout)findViewById(R.id.per_lin);
    }

    public void on(View view){
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(),"Turned on"
                    ,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Already on",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void list(View view){
        linearLayout.setVisibility(View.VISIBLE);
        pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            ArrayList<String> list = new ArrayList<>();
            for(BluetoothDevice bt : pairedDevices)
                list.add(bt.getName());
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this,android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
        }

        bluetoothAdapter.startDiscovery();
        search = new ArrayList<>();
        add = new ArrayList<>();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);    //绑定状态监听
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);     //搜索完成时监听
        registerReceiver(mReceiver, filter);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(add.get(position));
                if(bluetoothAdapter.isDiscovering()){
                    bluetoothAdapter.cancelDiscovery();
                    try {
                        //如果想要取消已经配对的设备，只需要将creatBond改为removeBond
                        Method method = BluetoothDevice.class.getMethod("createBond");
                        Log.e(getPackageName(), "开始配对");
                        method.invoke(add.get(position));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void off(View view){
        bluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(),"Turned off" ,
                Toast.LENGTH_LONG).show();
    }

    public void visible(View view){
        Intent getVisible = new Intent(BluetoothAdapter.
                ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName() != null){
                    if(!search.contains(device.getName())){
                        search.add(device.getName());
                        add.add(device);
                    }

                }
                if(search.size()>0){
                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (PeripheralActivity.this,android.R.layout.simple_list_item_1,search);
                    lv1.setAdapter(adapter);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d("MainActivity", "搜索结束");

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_NONE:
                        Log.e(getPackageName(), "取消配对");
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Log.e(getPackageName(), "配对中");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        Log.e(getPackageName(), "配对成功");
                        break;
                }
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
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
