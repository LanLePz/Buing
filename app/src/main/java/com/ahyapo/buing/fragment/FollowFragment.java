package com.ahyapo.buing.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ahyapo.buing.R;
import com.ahyapo.buing.activity.PublishActivity;
import com.ahyapo.buing.adapter.DynamicAdapter;
import com.ahyapo.buing.adapter.GalleryAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.FollowUser;
import com.ahyapo.buing.bean.FollowUserDynamic;
import com.ahyapo.buing.bean.IndexImages;
import com.ahyapo.buing.http.OkHttpHelper;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

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

public class FollowFragment extends Fragment {
    private String GET_FOLLOWS_INFO_URL;
    private String IP_URL ;
    private Activity mActivity;
    private RecyclerView recom_recyclerView;
    private RecyclerView dynamic_recycleView;
    private GalleryAdapter recom_mAdapter;
    private DynamicAdapter dyamic_mAdapter;
    private ArrayList<FollowUser> followUsers = new ArrayList<>();
    private ArrayList<IndexImages> imagesArrayList = new ArrayList<>();
    private SmartRefreshLayout mMainRefresh;
    private boolean isCreated=false;
    private int page_num = 1;
    private ImageView publish;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        Log.e("FollowFragment:","onAttach()");

    }
  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_follow, container, false);
      //推荐用户列表
      recom_recyclerView = view.findViewById(R.id.recom_recyclerView);
      requestFollowUsersData();
      //关注用户动态列表
      dynamic_recycleView = view.findViewById(R.id.dynamic_recycleView);
      requestFollowUserDynamicsData();
      //下拉刷新
      mMainRefresh = view.findViewById(R.id.refreshLayout);
      //开启自动加载功能（非必须）
      mMainRefresh.setEnableAutoLoadMore(true);
      mMainRefresh.setOnRefreshListener(new OnRefreshListener() {
          @Override
          public void onRefresh(@NonNull final RefreshLayout refreshLayout) {



              refreshLayout.getLayout().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      //refreshData();
                      //adapter.notifyDataSetChanged();
                      followUsers.clear();
                      imagesArrayList.clear();
                      requestFollowUsersData();
                      requestFollowUserDynamicsData();
                      refreshLayout.finishRefresh();
                      refreshLayout.resetNoMoreData();//setNoMoreData(false);
                  }
              }, 2000);
          }
      });


      mMainRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
          @Override
          public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
              refreshLayout.getLayout().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      requestFollowUserDynamicsData();
                      refreshLayout.finishLoadMore();
                  }
              }, 2000);


          }
      });
      publish = view.findViewById(R.id.publish);
      publish.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(mActivity, PublishActivity.class);
              mActivity.startActivity(intent);
          }
      });
      return view;
    }
    private void requestFollowUsersData(){
        //请求数据
        final PublicData app = (PublicData)getActivity().getApplication();
        IP_URL = app.getURL();
        GET_FOLLOWS_INFO_URL = app.getURL()+"/getRecommendUsers/";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        String user_tel = sharedPreferences.getString("tel",null);
        Log.e("TOKEN ",token);
        FormBody.Builder getUsersBuilder = new FormBody.Builder();
        getUsersBuilder.add("tel",user_tel);
        getUsersBuilder.add("token",token);
        OkHttpHelper.postRequest(GET_FOLLOWS_INFO_URL, getUsersBuilder, new Callback() {
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
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        FollowUser followUser = new FollowUser(user_id,user_name,user_icon);
                        followUsers.add(followUser);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        recom_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,RecyclerView.HORIZONTAL){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        });
                        //设置适配器
                        recom_mAdapter = new GalleryAdapter(mActivity, followUsers,IP_URL);
                        recom_mAdapter.notifyDataSetChanged();
                        recom_recyclerView.setAdapter(recom_mAdapter);
                    }
                });
            }
        });
    }
    private void requestFollowUserDynamicsData(){
        //请求数据
        final PublicData app = (PublicData)getActivity().getApplication();
        IP_URL = app.getURL();
        GET_FOLLOWS_INFO_URL = app.getURL()+"/getDynamicInfo/";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        String user_tel = sharedPreferences.getString("tel",null);
        Log.e("TOKEN ",token);
        FormBody.Builder getUsersBuilder = new FormBody.Builder();
        getUsersBuilder.add("tel",user_tel);
        getUsersBuilder.add("token",token);
        getUsersBuilder.add("page_num", String.valueOf(page_num));
        OkHttpHelper.postRequest(GET_FOLLOWS_INFO_URL, getUsersBuilder, new Callback() {
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
                        if (i<imgjson.length()-1){
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
                        }else{
                            int pagenum = jsonObject.getInt("page_num");
                            page_num = pagenum;
                        }


                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        dynamic_recycleView.setLayoutManager(new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        });
                        //设置适配器
                        dyamic_mAdapter = new DynamicAdapter(mActivity, imagesArrayList,IP_URL);
                        dyamic_mAdapter.notifyDataSetChanged();
                        dynamic_recycleView.setAdapter(dyamic_mAdapter);
                    }
                });
            }
        });
    }
}