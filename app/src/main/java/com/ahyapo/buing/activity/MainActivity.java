package com.ahyapo.buing.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ahyapo.buing.R;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.IndexImages;
import com.ahyapo.buing.bean.MineUserInfo;
import com.ahyapo.buing.fragment.FindFragment;
import com.ahyapo.buing.fragment.FollowFragment;
import com.ahyapo.buing.fragment.IndexFragment;
import com.ahyapo.buing.fragment.MessageFragment;
import com.ahyapo.buing.fragment.MineFragment;
import com.ahyapo.buing.http.OkHttpHelper;
import com.ahyapo.buing.widget.FragmentTabHost;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private MineUserInfo mineUserInfo;

    private ArrayList<IndexImages> imgInfoList = new ArrayList<>();
    private String[] tabs = new String[]{"首页", "分类", "发布","关注", "我的"};
    private Class[] mFragmentClasses = new Class[]{IndexFragment.class,FindFragment.class, FollowFragment.class,MessageFragment.class,
             MineFragment.class};
    private int[] selectorImg = new int[]{R.drawable.rb_index_drawable_selector,R.drawable.rb_find_drawable_selector,
            R.drawable.rb_follow_drawable_selector,R.drawable.rb_message_drawable_selector,R.mipmap.icon_10};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        judgePermission();
        FragmentTabHost tabHost = findViewById(R.id.tab);
        // 初始化tabHost
        tabHost.setup(com.ahyapo.buing.activity.MainActivity.this, getSupportFragmentManager(), R.id.fragment_content);
        for (int i = 0; i < 4; i++) {
            tabHost.addTab(tabHost.newTabSpec(tabs[i]).setIndicator(getTabView(i)), mFragmentClasses[i], null);
        }
        tabHost.addTab(tabHost.newTabSpec(tabs[4]).setIndicator(getMineTabView(4)), mFragmentClasses[4], null);
        //requestData();
        // 设置默认tab
        tabHost.setCurrentTab(0);

    }

    /**
     * tab的view对象
     *
     * @param index 索引
     * @return view对象
     */
    private View getTabView(int index) {
        View inflate = LayoutInflater.from(com.ahyapo.buing.activity.MainActivity.this).inflate(R.layout.item_tab, null);
        ImageView tabImage = inflate.findViewById(R.id.tab_image);
        TextView tabTitle = inflate.findViewById(R.id.tab_title);
        tabImage.setImageResource(selectorImg[index]); // 通过selector来控制图片的改变
        tabTitle.setText(tabs[index]);// 通过selector来控制文字颜色的改变
        return inflate;
    }
    private View getMineTabView(int index) {
        View inflate = LayoutInflater.from(com.ahyapo.buing.activity.MainActivity.this).inflate(R.layout.item_tab_mine, null);
        CircleImageView tabImage = inflate.findViewById(R.id.tab_image);
        TextView tabTitle = inflate.findViewById(R.id.tab_title);
        tabImage.setImageResource(selectorImg[index]); // 通过selector来控制图片的改变
        tabTitle.setText(tabs[index]);// 通过selector来控制文字颜色的改变
        return inflate;
    }
    //6.0之后要动态获取权限，重要！！！
    protected void judgePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝

            // sd卡权限
            String[] SdCardPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, SdCardPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, SdCardPermission, 100);
            }

            //手机状态权限
            String[] readPhoneStatePermission = {Manifest.permission.READ_PHONE_STATE};
            if (ContextCompat.checkSelfPermission(this, readPhoneStatePermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, readPhoneStatePermission, 200);
            }

            //定位权限
            String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, locationPermission[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, locationPermission, 300);
            }

            String[] ACCESS_COARSE_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, ACCESS_COARSE_LOCATION, 400);
            }


            String[] READ_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, READ_EXTERNAL_STORAGE, 500);
            }

            String[] WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                ActivityCompat.requestPermissions(this, WRITE_EXTERNAL_STORAGE, 600);
            }

        }else{
            //doSdCardResult();
        }
        //LocationClient.reStart();
    }
}