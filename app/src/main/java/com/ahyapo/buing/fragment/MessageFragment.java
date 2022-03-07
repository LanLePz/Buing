package com.ahyapo.buing.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ahyapo.buing.R;
import com.ahyapo.buing.adapter.MessageAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.MessageInfo;
import com.ahyapo.buing.http.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;


public class MessageFragment extends Fragment {

    private String POST_GET_MESSAGES_URL;
    private String IP_URL ;
    private Activity mActivity;
    private RecyclerView message_list;
    private MessageAdapter messageAdapter;
    private ArrayList<MessageInfo> messageInfos = new ArrayList<>();
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
        View view =inflater.inflate(R.layout.fragment_message, container, false);
        //推荐用户列表
        message_list = view.findViewById(R.id.message_list);
        requestFollowUserDynamicsData();
        return view;
    }
    private void requestFollowUserDynamicsData(){
        //请求数据
        final PublicData app = (PublicData)getActivity().getApplication();
        IP_URL = app.getURL();
        POST_GET_MESSAGES_URL = app.getURL()+"/getMessageInfo/";
        FormBody.Builder getVerifyBuilder = new FormBody.Builder();
        getVerifyBuilder.add("user_id","1001");
        OkHttpHelper.postRequest(POST_GET_MESSAGES_URL, getVerifyBuilder, new Callback() {
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
                        int message_id = jsonObject.getInt("message_id");
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        String chat_info = jsonObject.getString("chat_info");
                        String chat_time = jsonObject.getString("chat_time");
                        MessageInfo messageInfo = new MessageInfo(message_id,user_icon,user_name,chat_info,chat_time);
                        messageInfos.add(messageInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        message_list.setLayoutManager(new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL){
                            @Override
                            public boolean canScrollVertically() {
                                return false;
                            }
                        });
                        //设置适配器
                        messageAdapter = new MessageAdapter(mActivity, messageInfos,IP_URL);
                        message_list.setAdapter(messageAdapter);
                    }
                });

            }
        });
    }
}