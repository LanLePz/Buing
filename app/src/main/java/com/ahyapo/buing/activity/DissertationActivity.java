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
import com.ahyapo.buing.adapter.DisInfoWaterFallAdapter;
import com.ahyapo.buing.adapter.WaterFallAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.DisItemInfo;
import com.ahyapo.buing.bean.DissertationInfo;
import com.ahyapo.buing.bean.IndexImages;
import com.ahyapo.buing.http.OkHttpHelper;
import com.bumptech.glide.Glide;
import com.itheima.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class DissertationActivity extends AppCompatActivity implements View.OnClickListener {

    private String POST_DIS_IMG_URL,IP_URL,DIS_IMG;
    private RecyclerView dis_info_recyl;
    private RoundedImageView dia_info_img;
    private ImageView back_index;
    private CircleImageView dis_user_icon;
    private TextView dis_info_title,dis_user_name,dis_info_context;
    private ArrayList<DisItemInfo> disItemInfos = new ArrayList<>();
    FormBody.Builder getVerifyBuilder = new FormBody.Builder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dissertation);
        initView();
        requestData();
    }


    private void initView() {
        dis_info_title = findViewById(R.id.dis_info_title);
        dia_info_img = findViewById(R.id.dis_info_img);
        dis_info_context = findViewById(R.id.dis_info_context);
        dis_user_icon = findViewById(R.id.dis_user_icon);
        dis_user_name = findViewById(R.id.dis_user_name);
        dis_info_recyl = findViewById(R.id.dis_info_recyl);
        back_index = findViewById(R.id.back_index);
        back_index.setOnClickListener(this);
        dis_info_context.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dis_info_context:
                break;
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
        }
    }
    private void requestData(){
        //请求数据
        final PublicData app = (PublicData)getApplication();
        IP_URL = app.getURL();
        POST_DIS_IMG_URL = app.getURL()+"/getDissertationItemInfo/";
        //获取传过来的数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        DissertationInfo dissertationInfo = bundle.getParcelable("disinfo");
        Log.e("imgId:",dissertationInfo.toString());
        DIS_IMG = dissertationInfo.getImgurl();
        Glide.with(this).load(app.getURL()+"/static/images"+dissertationInfo.getImgurl()).dontAnimate().into(dia_info_img);
        Glide.with(this).load(app.getURL()+"/static/icons"+dissertationInfo.getUsericon()).dontAnimate().into(dis_user_icon);
        dis_info_title.setText(dissertationInfo.getTitle());
        dis_user_name.setText(dissertationInfo.getUsername());
        dis_info_context.setText(dissertationInfo.getContext());
        //POST请求键值对
        getVerifyBuilder.add("dis_id",""+dissertationInfo.getDisid());
        getVerifyBuilder.toString();
        OkHttpHelper.postRequest(POST_DIS_IMG_URL, getVerifyBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure ","失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);
                try {
                    JSONArray imgjson = new JSONArray(responseData);
                    for(int i = 0;i<imgjson.length();i++){
                        JSONObject jsonObject = imgjson.getJSONObject(i);
                        int disimgid = jsonObject.getInt("img_id");
                        int likenum = jsonObject.getInt("like_num");
                        String disimgurl = jsonObject.getString("img_url");
                        String disusericon = jsonObject.getString("user_icon");
                        String disusername = jsonObject.getString("user_name");
                        DisItemInfo disItemInfo = new DisItemInfo(disimgid,likenum,disimgurl,disusericon,disusername);
                        disItemInfos.add(disItemInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        dis_info_recyl.setLayoutManager(new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        });
                        System.out.println("lol:"+disItemInfos);
                        //设置适配器
                        dis_info_recyl.setAdapter(new DisInfoWaterFallAdapter(DissertationActivity.this,disItemInfos,IP_URL));
                    }
                });
            }
        });
    }
}