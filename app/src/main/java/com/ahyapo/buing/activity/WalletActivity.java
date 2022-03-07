package com.ahyapo.buing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahyapo.buing.R;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back_index;
    private TextView money_num;
    private LinearLayout btn_exchange,btn_recharge,btn_welfare,btn_buycard;
    private int money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        initData();
    }
    private void initView() {
        back_index = findViewById(R.id.back_index);
        back_index.setOnClickListener(this);
        btn_exchange = findViewById(R.id.btn_exchange);
        btn_recharge = findViewById(R.id.btn_recharge);
        btn_recharge.setOnClickListener(this);
        btn_welfare = findViewById(R.id.btn_welfare);
        btn_buycard = findViewById(R.id.btn_buycard);
        money_num = findViewById(R.id.money_num);
    }
    private void initData() {
        //获取传过来的数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        money = bundle.getInt("user_wallet");
        money_num.setText(""+money);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_recharge:
                Bundle bundle = new Bundle();
                bundle.putInt("user_wallet",money);
                Intent pintent = new Intent(this, PayActivity.class);
                pintent.putExtras(bundle);
                startActivity(pintent);
                break;
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
        }
    }
}