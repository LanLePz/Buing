package com.ahyapo.buing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.adapter.CommentAdapter;
import com.ahyapo.buing.adapter.FollowAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.CommentInfo;
import com.ahyapo.buing.bean.UserFollows;
import com.ahyapo.buing.http.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MineInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private ImageView back_index;
    private FormBody.Builder getInfoBuilder;
    private String type,user_tel,token;
    private String IP_URL;
    private ArrayList<UserFollows> userFollowsArrayList = new ArrayList<>();
    private RecyclerView mine_info_recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_info);
        initView();
        initData();
    }

    private void initData() {
        //请求数据
        final PublicData app = (PublicData)getApplication();
        IP_URL = app.getURL();
        //获取传过来的数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        type = bundle.getString("type");
        user_tel = bundle.getString("user_tel");
        token = bundle.getString("token");
        Log.e("user_tel:",user_tel +"token:"+ token);
        getInfoBuilder = new FormBody.Builder();
        getInfoBuilder.add("tel",user_tel);
        getInfoBuilder.add("token",token);
        if (type.equals("follow")){
            title.setText("关注");
            requestGetFollowsByUserId();
        }else if(type.equals("fans")){
            title.setText("粉丝");
            requestGetFansByUserId();
        }else if(type.equals("like")){
            title.setText("点赞");
            requestGetLikeByUserId();
        }else if(type.equals("discuss")){
            title.setText("评论");
            requestGetDiscussByUserId();
        }else if(type.equals("message")){
            title.setText("消息");
            requestGetMessageByUserId();
        }
    }

    private void requestGetFollowsByUserId() {
        OkHttpHelper.postRequest(IP_URL+"/getFollowsByUserId/", getInfoBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getFollowsByUserId ","失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);
                try {
                    JSONArray imgjson = new JSONArray(responseData);
                    for(int i = 0;i<imgjson.length();i++){
                        JSONObject jsonObject = imgjson.getJSONObject(i);
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        String product = jsonObject.getString("product");
                        String fans = jsonObject.getString("fans");
                        UserFollows userFollows = new UserFollows(user_icon,user_name,product,fans);
                        userFollowsArrayList.add(userFollows);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        mine_info_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return true;
                            }
                        });
                        //设置适配器
                        FollowAdapter followAdapter = new FollowAdapter(MineInfoActivity.this, userFollowsArrayList,IP_URL,type);
                        mine_info_recyclerView.setAdapter(followAdapter);
                    }
                });
            }
        });
    }

    private void requestGetFansByUserId() {
        OkHttpHelper.postRequest(IP_URL+"/getFansByUserId/", getInfoBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getFansByUserId ","失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);
                try {
                    JSONArray imgjson = new JSONArray(responseData);
                    for(int i = 0;i<imgjson.length();i++){
                        JSONObject jsonObject = imgjson.getJSONObject(i);
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        String product = jsonObject.getString("product");
                        String fans = jsonObject.getString("fans");
                        UserFollows userFollows = new UserFollows(user_icon,user_name,product,fans);
                        userFollowsArrayList.add(userFollows);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        mine_info_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return true;
                            }
                        });
                        //设置适配器
                        FollowAdapter followAdapter = new FollowAdapter(MineInfoActivity.this, userFollowsArrayList,IP_URL,type);
                        mine_info_recyclerView.setAdapter(followAdapter);
                    }
                });
            }
        });
    }

    private void requestGetLikeByUserId() {

    }

    private void requestGetDiscussByUserId() {

    }

    private void requestGetMessageByUserId() {

    }

    private void initView() {
        title = findViewById(R.id.title);
        back_index = findViewById(R.id.back_index);
        back_index.setOnClickListener(this);
        mine_info_recyclerView = findViewById(R.id.mine_info_recyclerView);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
        }
    }
}