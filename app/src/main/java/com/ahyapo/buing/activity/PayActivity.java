package com.ahyapo.buing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ahyapo.buing.R;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.http.OkHttpHelper;
import com.ahyapo.buing.view.CustomDialog;
import com.ahyapo.buing.view.MultiLineRadioGroup;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back_index;
    private TextView money_num,balance,recharge,pay_num,confirm_pay,cancel_download,tv_loading_tx;
    private double money,recharge_num;
    private MultiLineRadioGroup radioGroup;
    private String tel,token,IP_URL;
    private Dialog dialog;
    private CustomDialog customDialog;
    private RadioButton money_btn_1,money_btn_2,money_btn_3,money_btn_4,money_btn_5,money_btn_6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        //请求数据
        final PublicData app = (PublicData)getApplication();
        IP_URL = app.getURL();
        SharedPreferences sharedPreferences = this.getSharedPreferences("login_token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        tel = sharedPreferences.getString("tel",null);
        initView();
        initData();
    }

    private void initData() {
        radioGroup.check(R.id.money_btn_1);
        //获取传过来的数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        money = bundle.getInt("user_wallet");
        balance.setText(""+money);
        recharge_num = 1.00;
        money_num.setText("1.00");
    }


    private void initView() {
        back_index = findViewById(R.id.back_index);
        back_index.setOnClickListener(this);
        radioGroup = findViewById(R.id.radioGroup);
        money_btn_1 = findViewById(R.id.money_btn_1);
        money_btn_1.setOnClickListener(this);
        money_btn_2 = findViewById(R.id.money_btn_2);
        money_btn_2.setOnClickListener(this);
        money_btn_3 = findViewById(R.id.money_btn_3);
        money_btn_3.setOnClickListener(this);
        money_btn_4 = findViewById(R.id.money_btn_4);
        money_btn_4.setOnClickListener(this);
        money_btn_5 = findViewById(R.id.money_btn_5);
        money_btn_5.setOnClickListener(this);
        money_btn_6 = findViewById(R.id.money_btn_6);
        money_btn_6.setOnClickListener(this);
        money_num = findViewById(R.id.money);
        balance = findViewById(R.id.balance);
        recharge = findViewById(R.id.recharge);
        recharge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            case R.id.money_btn_1:
                money_num.setText("1.00");
                recharge_num = 1.00;
                break;
            case R.id.money_btn_2:
                money_num.setText("3.00");
                recharge_num = 3.00;
                break;
            case R.id.money_btn_3:
                money_num.setText("10.00");
                recharge_num = 10.00;
                break;
            case R.id.money_btn_4:
                money_num.setText("30.00");
                recharge_num = 30.00;
                break;
            case R.id.money_btn_5:
                money_num.setText("50.00");
                recharge_num = 50.00;
                break;
            case R.id.money_btn_6:
                money_num.setText("100.00");
                recharge_num = 100.00;
                break;
            case R.id.recharge:
                showImageDialog();
                break;
            case R.id.confirm_pay:
                dialog.dismiss();
                customDialog = new CustomDialog(PayActivity.this);
                tv_loading_tx = customDialog.findViewById(R.id.tv_loading_tx);
                tv_loading_tx.setText("正在支付...");
                customDialog.show();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        requestRechargeForUser();
                    }
                },5000);
                break;
            case R.id.cancel_download:
                dialog.dismiss();
                break;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (customDialog != null && !this.isFinishing()) {
            customDialog.dismiss();
        }
    }
    private void requestRechargeForUser() {
        FormBody.Builder rechargeBuilder = new FormBody.Builder();
        rechargeBuilder.add("tel", tel);
        rechargeBuilder.add("token", token);
        rechargeBuilder.add("recharge_num", ""+recharge_num);
        OkHttpHelper.postRequest(IP_URL + "/toRechargeForUser/", rechargeBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("requestRechargeForUser ","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("success")){
                            customDialog.dismiss();
                            Toast.makeText(PayActivity.this, "充值成功！",Toast.LENGTH_SHORT).show();
                            PayActivity.this.finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }else{
                            customDialog.dismiss();
                            Toast.makeText(PayActivity.this, "充值失败，请重新支付！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void showImageDialog() {
        dialog = new Dialog(this, R.style.Comment_Dialog_Style);
        View view = View.inflate(this, R.layout.dialog_pay, null);
        //设置弹窗padding为0，可宽度沾满屏幕
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = 900;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.share_animation;
        dialog.setContentView(view, lp);
        // 设置点击对话框外部是否关闭对话框
        dialog.setCanceledOnTouchOutside(true);
        pay_num=view.findViewById(R.id.pay_num);
        pay_num.setText(""+recharge_num);
        confirm_pay=view.findViewById(R.id.confirm_pay);
        confirm_pay.setOnClickListener(this);
        cancel_download=view.findViewById(R.id.cancel_download);
        cancel_download.setOnClickListener(this);
        dialog.show();
    }
}