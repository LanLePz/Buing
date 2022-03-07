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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ahyapo.buing.R;
import com.ahyapo.buing.adapter.FindWaterFallAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.FindClass;
import com.ahyapo.buing.http.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FindFragment extends Fragment {

    private RecyclerView recycler_view;
    private String GET_FIND_INFO_URL;
    private String IP_URL ;
    private Activity mActivity;
    private ArrayList<FindClass> findInfoList = new ArrayList<>();
    @Override
    public void onAttach(@NonNull Context context) {
        mActivity = (Activity) context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        recycler_view = view.findViewById(R.id.recycler_view);
        requestData();
        return view;
    }
    private void requestData(){
        //请求数据
        final PublicData app = (PublicData)getActivity().getApplication();
        IP_URL = app.getURL();
        GET_FIND_INFO_URL = app.getURL()+"/getFindClassInfo/";
        OkHttpHelper.getRequest(GET_FIND_INFO_URL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("失败",""+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONArray imgjson = new JSONArray(responseData);
                    for(int i = 0;i<imgjson.length();i++){
                        JSONObject jsonObject = imgjson.getJSONObject(i);
                        int find_id = jsonObject.getInt("class_id");
                        String class_img = jsonObject.getString("class_img");
                        String class_name = jsonObject.getString("class_name");
                        FindClass findClass = new FindClass(find_id,class_img,class_name);
                        findInfoList.add(findClass);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        recycler_view.setLayoutManager(new GridLayoutManager(mActivity,3){
                            @Override
                            public boolean canScrollVertically() {
                                return true;
                            }
                        });
                        System.out.println("lol:"+findInfoList);
                        //设置适配器
                        recycler_view.setAdapter(new FindWaterFallAdapter(mActivity,findInfoList,IP_URL));
                    }
                });
            }
        });
    }
}