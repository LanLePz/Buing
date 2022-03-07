package com.ahyapo.buing.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.adapter.ClassItemWaterFallAdapter;
import com.ahyapo.buing.adapter.WaterFallAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.FindClass;
import com.ahyapo.buing.bean.IndexImages;
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

public class ClassItemActivity extends AppCompatActivity implements View.OnClickListener {

    private String IP_URL,GET_CLASS_ITEM__INFO_URL;//IP地址
    private FindClass findClass;
    private TextView class_title;
    private ImageView back_index;
    private RecyclerView class_item_recyl;
    private ArrayList<IndexImages>  imagesArrayList = new ArrayList<>();
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_item);
        initView();
        initData();
    }

    private void initData() {
        final PublicData app = (PublicData) getApplication();
        IP_URL = app.getURL();
        //获取传递过来的数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        findClass = bundle.getParcelable("classinfo");
        class_title.setText(findClass.getName());
        requestGetClassItemInfo(findClass.getId());
    }

    private void requestGetClassItemInfo(int id) {
        GET_CLASS_ITEM__INFO_URL = IP_URL + "/getClassItemInfo/";
        FormBody.Builder getClassItemBuilder = new FormBody.Builder();
        getClassItemBuilder.add("class_id", String.valueOf(id));
        OkHttpHelper.postRequest(GET_CLASS_ITEM__INFO_URL, getClassItemBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure ","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);
                //Log.i("请求成功",responseData);
//               Gson gson = new Gson();
//               imgInfoList = gson.fromJson(responseData,new TypeToken<IndexImages>(){}.getType());
                try {
                    JSONArray imgjson = new JSONArray(responseData);
                    for(int i = 0;i<imgjson.length();i++){
                        JSONObject jsonObject = imgjson.getJSONObject(i);
                        int user_id = jsonObject.getInt("user_id");
                        int img_id = jsonObject.getInt("img_id");
                        int like_num = jsonObject.getInt("like_num");
                        String img_url = jsonObject.getString("img_url");
                        int img_type = jsonObject.getInt("img_type");
                        String img_tags = jsonObject.getString("img_tags");
                        String img_describe = jsonObject.getString("img_describe");
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        String img_context = jsonObject.getString("img_context");
                        String img_time = jsonObject.getString("img_time");
                        //设置日期格式
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //将字符串转为date对象
                        Date date1 = sdf.parse(img_time);
                        //日历，拥于获取date具体年月日时分秒
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date1);
                        Log.e("date1",""+date1);
                        //获取当前时间
                        Date nowdate = new Date();
                        //计算两个日期间隔
                        long daysBetween=(nowdate.getTime()-date1.getTime()+1000000)/(60*60*24*1000);
                        String time;
                        //大于一天展示月日，小于一天展示时分
                        if (daysBetween>=1){
                            time = (calendar.get(Calendar.MONTH)+1)+"月"+calendar.get(Calendar.DAY_OF_MONTH)+"日";
                        }else
                            time = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                        IndexImages indexImages = new IndexImages(user_id,img_id,img_url,img_type,like_num,img_tags,img_describe,user_name,user_icon,img_context,time);
                        imagesArrayList.add(indexImages);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        class_item_recyl.setLayoutManager(new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return true;
                            }
                        });
                        System.out.println("lol:"+imagesArrayList);
                        //设置适配器
                        class_item_recyl.setAdapter(new ClassItemWaterFallAdapter(ClassItemActivity.this,imagesArrayList,IP_URL));
                    }
                });
            }
        });
    }

    private void initView() {
        class_title = findViewById(R.id.class_title);
        back_index = findViewById(R.id.back_index);
        back_index.setOnClickListener(this);
        class_item_recyl = findViewById(R.id.class_item_recyl);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
        }

    }
}