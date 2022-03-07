package com.ahyapo.buing.adapter;;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.activity.DisItemInfoActivity;
import com.ahyapo.buing.activity.IndexInfoActivity;
import com.ahyapo.buing.bean.DisItemInfo;
import com.ahyapo.buing.bean.IndexImages;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DisInfoWaterFallAdapter extends RecyclerView.Adapter<DisInfoSimpleViewHolder> {
    private final Context context;
    private String GET_IMAGES_URL;
    private final ArrayList<DisItemInfo> dataList;
    public DisInfoWaterFallAdapter(Context context, ArrayList<DisItemInfo> dataList, String url) {
        this.dataList = dataList;
        this.context = context;
        this.GET_IMAGES_URL = url;
    }

    /**
     * 创建viewHolder 并设置viewHolder的布局效果
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public DisInfoSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dis_simple,parent,false);
        DisInfoSimpleViewHolder simpleViewHolder = new DisInfoSimpleViewHolder(view);
        return simpleViewHolder;
    }

    /**
     * 要给条目控件展示的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DisInfoSimpleViewHolder holder, int position) {
        //设置当前控件的高度
        ViewGroup.LayoutParams layoutParams = holder.tv_item.getLayoutParams();
        //重新设置布局参数
        holder.tv_item.setLayoutParams(layoutParams);
        holder.index_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("disinfo", dataList.get(position));
                Intent intent = new Intent(context, DisItemInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        //holder.tv_item.setBackgroundColor(Color.rgb((int) (100+Math.random()*155),(int) (100+Math.random()*155),(int) (100+Math.random()*155)));
        Glide.with(context).load(GET_IMAGES_URL+"/static/litimgs"+dataList.get(position).getDisimgurl()).dontAnimate().into(holder.index_image);
        Glide.with(context).load(GET_IMAGES_URL+"/static/icons"+dataList.get(position).getDisusericon()).dontAnimate().into(holder.user_icon);
        holder.user_name.setText(dataList.get(position).getDisusername());
        holder.like_num.setText(""+dataList.get(position).getLikenum());
        //holder.index_image.setImageURI(Uri.parse("http://192.168.43.206:8000/static/images"+dataList.get(position).getImgurl()));
        //创建动画对象
        /*TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,1000,Animation.ABSOLUTE,0);
        translateAnimation.setDuration(5000);
        holder.tv_item.startAnimation(translateAnimation);*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
