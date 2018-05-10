package com.example.wisdompark19.Main;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.base.bj.paysdk.domain.TrPayResult;
import com.base.bj.paysdk.listener.PayResultListener;
import com.base.bj.paysdk.utils.TrPay;
import com.example.wisdompark19.Adapter.PayItemAdapter;
import com.example.wisdompark19.AutoProject.AppConstants;
import com.example.wisdompark19.AutoProject.SharePreferences;
import com.example.wisdompark19.AutoProject.TimeChange;
import com.example.wisdompark19.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 最美人间四月天 on 2018/1/18.
 */

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    private List<PayItemAdapter.Pay_item> Data;
    private RecyclerView.LayoutManager mLayoutManager;
    private PayItemAdapter mPayItemAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayout pay_water;
    private LinearLayout pay_electric;
    private LinearLayout pay_gas;
    private LinearLayout pay_property;
    private CardView pay_card;
    private TextView pay_name;
    private EditText pay_count;
    private TextView pay_cancel, pay_ok;
    private String name = null;
    private Dialog dialog;
    private TextView pay_weixin, pay_zhifubao, pay_dismiss;

    ArrayList<String> count_name = new ArrayList<String>(); // 上下滚动消息栏内容
    ArrayList<String> count_fee = new ArrayList<String>();
    ArrayList<String> count_time = new ArrayList<String>();
    ArrayList<String> count_pay = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorBlue)); //设置顶部系统栏颜色
        Intent intent = getIntent();
        String intent_data = intent.getStringExtra("put_data_pay");
        Toolbar toolbar = (Toolbar)findViewById(R.id.pay_mainTool); //标题栏
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setTitle(intent_data);
        back(toolbar);
        findView();

        initRollData();
        initData();

    }

    private void findView(){
        pay_water = (LinearLayout) findViewById(R.id.pay_water);
        pay_electric = (LinearLayout) findViewById(R.id.pay_electric);
        pay_gas = (LinearLayout) findViewById(R.id.pay_gas);
        pay_property = (LinearLayout) findViewById(R.id.pay_property);
        pay_card = (CardView) findViewById(R.id.pay_card);
        pay_name = (TextView) findViewById(R.id.pay_name);
        pay_count = (EditText) findViewById(R.id.pay_count);
        pay_cancel = (TextView) findViewById(R.id.pay_cancel);
        pay_ok = (TextView) findViewById(R.id.pay_ok);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_pay_item);
        pay_card.setVisibility(View.INVISIBLE);

        ButtonClick();
    }

    private void ButtonClick(){
        pay_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_name.setText("水费");
                pay_card.setVisibility(View.VISIBLE);
                name = "水费";
            }
        });
        pay_electric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_name.setText("电费");
                pay_card.setVisibility(View.VISIBLE);
                name = "电费";
            }
        });
        pay_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_name.setText("天然气");
                pay_card.setVisibility(View.VISIBLE);
                name = "天然气";
            }
        });
        pay_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_name.setText("物业");
                pay_card.setVisibility(View.VISIBLE);
                name = "物业费";
            }
        });

        pay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_card.setVisibility(View.INVISIBLE);
            }
        });

        pay_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name != null){
                   if(pay_count.getText().toString().trim().isEmpty()) {
                       Toast.makeText(PayActivity.this,"请输入费用",Toast.LENGTH_LONG).show();
                   }else {
                       showMyDialog();
                   }
                }
            }
        });
    }

    private void initRollData(){
//        count_name.add("物业费");
//        count_fee.add("100");
//        count_time.add("2018年4月22日");
//        count_pay.add("200");
    }

    private void initData(){
        Data = new ArrayList<>();
        for(int i=0; i<count_name.size(); i++){
            PayItemAdapter newData = new PayItemAdapter(Data);
            PayItemAdapter.Pay_item pay_item = newData.new Pay_item(count_name.get(i),
                    count_fee.get(i),count_time.get(i),count_pay.get(i));
            Data.add(pay_item);
        }

        mLayoutManager = new LinearLayoutManager(PayActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mPayItemAdapter = new PayItemAdapter(Data);
        mRecyclerView.setAdapter(mPayItemAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pay_weixin:{
//                Toast.makeText(this,"微信",Toast.LENGTH_SHORT).show();
                weiXin();
                dialog.dismiss();
                break;
            }
            case R.id.pay_zhifubao:{
//                Toast.makeText(this,"支付宝",Toast.LENGTH_SHORT).show();
                zhiFuBao();
                dialog.dismiss();
                break;
            }
            case R.id.pay_dismiss:{
//                Toast.makeText(this,"取消",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
            }
        }
    }

    private void weiXin(){
        String userid = SharePreferences.getString(this,AppConstants.USER_PHONE);
        String tradename = pay_name.getText().toString().trim();
        String outtradeno = userid + TimeChange.getBigTime();
        long amount = changeY2F(pay_count.getText().toString().trim());
        String backparams = "微信" + userid + tradename + outtradeno + pay_count.getText().toString().trim();
        String notifyurl = null;

        /**
         * 3.发起微信支付
         * @param tradename   商品名称
         * @param outtradeno   商户系统订单号(商户系统内唯一)
         * @param amount        商品价格（单位：分。如1.5元传150）
         * @param backparams 商户系统回调参数
         * @param notifyurl       商户系统回调地址
         * @param userid          商户系统用户ID(如：trpay@52yszd.com，商户系统内唯一)
         */
        TrPay.getInstance(PayActivity.this).callWxPay(tradename, outtradeno, amount, backparams, notifyurl, userid, new PayResultListener() {
            /**
             * 支付完成回调
             * @param context        上下文
             * @param outtradeno   商户系统订单号
             * @param resultCode   支付状态(RESULT_CODE_SUCC：支付成功、RESULT_CODE_FAIL：支付失败)
             * @param resultString  支付结果
             * @param payType      支付类型（1：支付宝 2：微信 3：银联）
             * @param amount       支付金额
             * @param tradename   商品名称
             */
            @Override
            public void onPayFinish(Context context, String outtradeno, int resultCode, String resultString, int payType, Long                                                           amount, String tradename) {
                if (resultCode == TrPayResult.RESULT_CODE_SUCC.getId()) {
                    //支付成功逻辑处理
                    Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                } else if (resultCode == TrPayResult.RESULT_CODE_FAIL.getId()) {
                    //支付失败逻辑处理
                    Toast.makeText(PayActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void zhiFuBao(){
        String userid = SharePreferences.getString(this,AppConstants.USER_PHONE);
        String tradename = pay_name.getText().toString().trim();
        String outtradeno = userid + TimeChange.getBigTime();
        long amount = changeY2F(pay_count.getText().toString().trim());
        String backparams = "支付宝" + userid + tradename + outtradeno + pay_count.getText().toString().trim();
        String notifyurl = null;

        /**
         * 3.发起微信支付
         * @param tradename   商品名称
         * @param outtradeno   商户系统订单号(商户系统内唯一)
         * @param amount        商品价格（单位：分。如1.5元传150）
         * @param backparams 商户系统回调参数
         * @param notifyurl       商户系统回调地址
         * @param userid          商户系统用户ID(如：trpay@52yszd.com，商户系统内唯一)
         */
        TrPay.getInstance(PayActivity.this).callAlipay(tradename, outtradeno, amount, backparams, notifyurl, userid, new PayResultListener() {
            /**
             * 支付完成回调
             * @param context        上下文
             * @param outtradeno   商户系统订单号
             * @param resultCode   支付状态(RESULT_CODE_SUCC：支付成功、RESULT_CODE_FAIL：支付失败)
             * @param resultString  支付结果
             * @param payType      支付类型（1：支付宝 2：微信 3：银联）
             * @param amount       支付金额
             * @param tradename   商品名称
             */
            @Override
            public void onPayFinish(Context context, String outtradeno, int resultCode, String resultString, int payType, Long                                                           amount, String tradename) {
                if (resultCode == TrPayResult.RESULT_CODE_SUCC.getId()) {
                    //支付成功逻辑处理
                    Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                } else if (resultCode == TrPayResult.RESULT_CODE_FAIL.getId()) {
                    //支付失败逻辑处理
                    Toast.makeText(PayActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void showMyDialog(){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.pay_activity_bottom, null);
        //初始化控件
        pay_weixin  = (TextView) inflate.findViewById(R.id.pay_weixin);
        pay_zhifubao = (TextView) inflate.findViewById(R.id.pay_zhifubao);
        pay_dismiss = (TextView) inflate.findViewById(R.id.pay_dismiss);
        pay_weixin.setOnClickListener(this);
        pay_zhifubao.setOnClickListener(this);
        pay_dismiss.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.ActionSheetDialogStyle); // 添加动画
        //获取当前Activity所在的窗体
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = -20; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
//      lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
//      lp.alpha = 9f; // 透明度
        inflate.measure(0, 0);
        lp.height = inflate.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();//显示对话框
    }

    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static long changeY2F(String amount){
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if(index == -1){
            amLong = Long.valueOf(currency+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");
        }
        return amLong;
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
