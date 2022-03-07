package com.ahyapo.buing.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.ahyapo.buing.R;
import com.ahyapo.buing.adapter.DisWaterFallAdapter;
import com.ahyapo.buing.adapter.WaterFallAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.DissertationInfo;
import com.ahyapo.buing.bean.IndexImages;
import com.ahyapo.buing.http.OkHttpHelper;
import com.ahyapo.buing.widget.GlideImageLoader;
import com.lw.banner.Banner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class IndexFragment extends Fragment {


    private String GET_IMAGES_INDEX_URL,GET_DIS_INFO_URL;
    private String IP_URL ;
    private RecyclerView recycler_view,dis_recyclerview;
    private Activity mActivity;
    private Banner banner;
    private Integer[] imgRes = {
            R.mipmap.banner1,
            R.mipmap.banner2,
            R.mipmap.banner3,
    };
    private ArrayList<IndexImages> imgInfoList = new ArrayList<>();
    private ArrayList<DissertationInfo> dissertationInfos = new ArrayList<>();
    private SwipeRefreshLayout mMainRefresh;

    @Override
    public void onAttach(@NonNull Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_index, container, false);

       recycler_view = view.findViewById(R.id.recycler_view);
       dis_recyclerview = view.findViewById(R.id.dis_recyclerview);
       requestDissertationData();
       requestData();
       banner = view.findViewById(R.id.banner);
       banner.setImages(Arrays.asList(imgRes))
               .setImageLoader(new GlideImageLoader()) //设置Glide图片加载器
               .addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                   @Override
                   public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                   }
                   @Override
                   public void onPageSelected(int position) {

                   }
                   @Override
                   public void onPageScrollStateChanged(int state) {

                   }
               }) //设置banner改变监听
               .setOnItemClickListener(new Banner.OnItemClickListener() {
                   @Override
                   public void callBack(int position) {
                   }
               }) //设置banner点击监听
               .isAutoPlay(true) // 设置自动循环轮播
               .init();

        //下拉刷新
        mMainRefresh = view.findViewById(R.id.main_refresh);
        mMainRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDynamic();
            }
        });
       return view;
    }
    private void refreshDynamic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgInfoList.clear();
                        dissertationInfos.clear();
                        requestDissertationData();
                        requestData();
                        mMainRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    private void requestDissertationData() {
        //请求数据
        final PublicData app = (PublicData)getActivity().getApplication();
        IP_URL = app.getURL();
        GET_DIS_INFO_URL = app.getURL()+"/getDissertationInfo/";
        OkHttpHelper.getRequest(GET_DIS_INFO_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("失败",""+e);
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
                        int id  = jsonObject.getInt("dissertation_id");
                        String img_url = jsonObject.getString("img_url");
                        String title = jsonObject.getString("title");
                        String context = jsonObject.getString("context");
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        DissertationInfo dissertationInfo = new DissertationInfo(id,img_url,title,context,user_name,user_icon);
                        dissertationInfos.add(dissertationInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        dis_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,RecyclerView.HORIZONTAL){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        });
                        System.out.println("lol:"+dissertationInfos);
                        //设置适配器
                        dis_recyclerview.setAdapter(new DisWaterFallAdapter(mActivity,dissertationInfos,IP_URL));
                    }
                });
            }
        });
    }

    private void requestData(){
        //请求数据
        final PublicData app = (PublicData)getActivity().getApplication();
        IP_URL = app.getURL();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        String user_tel = sharedPreferences.getString("tel",null);
        FormBody.Builder getImageBuilder = new FormBody.Builder();
        getImageBuilder.add("tel",user_tel);
        getImageBuilder.add("token",token);
        GET_IMAGES_INDEX_URL = app.getURL()+"/getImagesIndex/";
        OkHttpHelper.postRequest(GET_IMAGES_INDEX_URL, getImageBuilder, new Callback() {
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
                        imgInfoList.add(indexImages);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        recycler_view.setLayoutManager(new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        });
                        System.out.println("lol:"+imgInfoList);
                        //设置适配器
                        recycler_view.setAdapter(new WaterFallAdapter(mActivity,imgInfoList,IP_URL));
                    }
                });
            }
        });
    }
}