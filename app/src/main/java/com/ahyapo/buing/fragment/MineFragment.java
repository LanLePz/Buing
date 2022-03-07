package com.ahyapo.buing.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ahyapo.buing.R;
import com.ahyapo.buing.activity.DynamicItemInfoActivity;
import com.ahyapo.buing.activity.LoginActivity;
import com.ahyapo.buing.activity.MainActivity;
import com.ahyapo.buing.activity.MineInfoActivity;
import com.ahyapo.buing.activity.WalletActivity;
import com.ahyapo.buing.activity.WelActivity;
import com.ahyapo.buing.adapter.MineWaterFallAdapter;
import com.ahyapo.buing.adapter.WaterFallAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.IndexImages;
import com.ahyapo.buing.bean.MineUserInfo;
import com.ahyapo.buing.http.OkHttpHelper;
import com.bumptech.glide.Glide;

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


public class MineFragment extends Fragment implements View.OnClickListener {

    private LinearLayout user_info;
    private RelativeLayout project_info;
    private RecyclerView mine_recyclerView;
    private Activity mActivity;
    private String  IP_URL,GET_IMAGES_INDEX_URL,GET_MINE_USER_URL;
    private MineUserInfo mineUserInfo;
    private ArrayList<IndexImages> imgInfoList = new ArrayList<>();
    private FormBody.Builder getImageBuilder;
    private ImageView mine_user_icon,more_action,refresh;
    private TextView mine_user_name,mine_user_sign,mine_user_wallet,mine_user_follows,mine_user_fans;
    private TextView follow_btn,fans_btn,product_num;
    private FrameLayout like_btn,discuss_btn,message_btn;
    private String token,user_tel;
    private SwipeRefreshLayout mMainRefresh;
    View view;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            view.invalidate();
        }
    };
    @Override
    public void onAttach(@NonNull Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        initData();
        return view;
    }
    private void initView() {
        user_info = view.findViewById(R.id.user_info);
        project_info = view.findViewById(R.id.project_info);
        Animation user_ani = AnimationUtils.loadAnimation(getActivity(),R.anim.translate_userinfo);
        Animation project_ani = AnimationUtils.loadAnimation(getActivity(),R.anim.translate_project);
        user_info.startAnimation(user_ani);
        project_info.startAnimation(project_ani);
        mine_recyclerView = view.findViewById(R.id.mine_recyclerView);
        mine_user_icon = view.findViewById(R.id.mine_user_icon);
        mine_user_name = view.findViewById(R.id.mine_user_name);
        mine_user_sign = view.findViewById(R.id.mine_user_sign);
        mine_user_wallet = view.findViewById(R.id.mine_user_wallet);
        mine_user_wallet.setOnClickListener(this);
        mine_user_follows = view.findViewById(R.id.mine_user_follows);
        mine_user_fans = view.findViewById(R.id.mine_user_fans);
        follow_btn = view.findViewById(R.id.follow_btn);
        fans_btn = view.findViewById(R.id.fans_btn);
        like_btn = view.findViewById(R.id.like_btn);
        discuss_btn = view.findViewById(R.id.discuss_btn);
        message_btn = view.findViewById(R.id.message_btn);
        follow_btn.setOnClickListener(this);
        fans_btn.setOnClickListener(this);
        like_btn.setOnClickListener(this);
        discuss_btn.setOnClickListener(this);
        message_btn.setOnClickListener(this);
        product_num = view.findViewById(R.id.product_num);
        more_action = view.findViewById(R.id.more_action);
        more_action.setOnClickListener(this);
        refresh = view.findViewById(R.id.refresh);
        refresh.setOnClickListener(this);
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
                        requestGetMineUserData();
                        requestGetMineImagesData();
                    }
                });
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.follow_btn:
                Bundle follow_bundle = new Bundle();
                follow_bundle.putString("type", "follow");
                follow_bundle.putString("user_tel",user_tel);
                follow_bundle.putString("token",token);
                Intent follow_intent = new Intent(mActivity, MineInfoActivity.class);
                follow_intent.putExtras(follow_bundle);
                mActivity.startActivity(follow_intent);
                break;
            case R.id.fans_btn:
                Bundle fans_bundle = new Bundle();
                fans_bundle.putString("type", "fans");
                fans_bundle.putString("user_tel",user_tel);
                fans_bundle.putString("token",token);
                Intent fans_intent = new Intent(mActivity, MineInfoActivity.class);
                fans_intent.putExtras(fans_bundle);
                mActivity.startActivity(fans_intent);
                break;
            case R.id.like_btn:
                Bundle like_bundle = new Bundle();
                like_bundle.putString("type", "like");
                like_bundle.putString("user_tel",user_tel);
                like_bundle.putString("token",token);
                Intent like_intent = new Intent(mActivity, MineInfoActivity.class);
                like_intent.putExtras(like_bundle);
                mActivity.startActivity(like_intent);
                break;
            case R.id.discuss_btn:
                Bundle discuss_bundle = new Bundle();
                discuss_bundle.putString("type", "discuss");
                discuss_bundle.putString("user_tel",user_tel);
                discuss_bundle.putString("token",token);
                Intent discuss_intent = new Intent(mActivity, MineInfoActivity.class);
                discuss_intent.putExtras(discuss_bundle);
                mActivity.startActivity(discuss_intent);
                break;
            case R.id.message_btn:
                Bundle message_bundle = new Bundle();
                message_bundle.putString("type", "message");
                message_bundle.putString("user_tel",user_tel);
                message_bundle.putString("token",token);
                Intent message_intent = new Intent(mActivity, MineInfoActivity.class);
                message_intent.putExtras(message_bundle);
                mActivity.startActivity(message_intent);
                break;
            case R.id.more_action:
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("login_token", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                mActivity.finish();
                break;
            case R.id.mine_user_wallet:
                Bundle bundle = new Bundle();
                bundle.putInt("user_wallet",mineUserInfo.getUser_wallet());
                Intent pintent = new Intent(mActivity, WalletActivity.class);
                pintent.putExtras(bundle);
                startActivity(pintent);
                break;
            case R.id.refresh:
                refreshDynamic();
                break;
        }
    }
    private void initData() {
        //请求数据
        final PublicData app = (PublicData)getActivity().getApplication();
        IP_URL = app.getURL();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        user_tel = sharedPreferences.getString("tel",null);
        getImageBuilder = new FormBody.Builder();
        getImageBuilder.add("tel",user_tel);
        getImageBuilder.add("token",token);
        requestGetMineImagesData();
        requestGetMineUserData();
    }

    private void requestGetMineUserData() {
        GET_MINE_USER_URL = IP_URL + "/getMineUserInfo/";
        OkHttpHelper.postRequest(GET_MINE_USER_URL, getImageBuilder,new Callback(){

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
                        int user_wallet = jsonObject.getInt("user_wallet");
                        int user_follows = jsonObject.getInt("follows");
                        int user_fans = jsonObject.getInt("fans");
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        String user_sign = jsonObject.getString("user_sign");
                        int product = jsonObject.getInt("product");
                        mineUserInfo = new MineUserInfo(user_icon,user_name,user_sign,user_wallet,user_follows,user_fans,product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("mineUserInfo",mineUserInfo.toString());
                        Glide.with(mActivity).load(IP_URL+"/static/icons"+mineUserInfo.getUser_icon()).dontAnimate().into(mine_user_icon);
                        mine_user_name.setText(mineUserInfo.getUser_name());
                        mine_user_sign.setText(mineUserInfo.getUser_sign());
                        mine_user_wallet.setText(""+mineUserInfo.getUser_wallet());
                        mine_user_follows.setText(""+mineUserInfo.getUser_follows());
                        mine_user_fans.setText(""+mineUserInfo.getUser_fans());
                        product_num.setText(""+mineUserInfo.getProduct_num());
                    }
                });
            }
        });
    }



    private void requestGetMineImagesData(){
        GET_IMAGES_INDEX_URL = IP_URL+"/getUserMineImages/";
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
                        mine_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        });
                        System.out.println("lol:"+imgInfoList);
                        //设置适配器
                        mine_recyclerView.setAdapter(new MineWaterFallAdapter(mActivity,imgInfoList,IP_URL));
                    }
                });
            }
        });
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden = true){
            Animation user_ani = AnimationUtils.loadAnimation(getActivity(),R.anim.translate_userinfo);
            Animation project_ani = AnimationUtils.loadAnimation(getActivity(),R.anim.translate_project);
            user_info.startAnimation(user_ani);
            project_info.startAnimation(project_ani);
            if (hidden = true){
                handler.sendEmptyMessage(0);
            }
        }
    }


}