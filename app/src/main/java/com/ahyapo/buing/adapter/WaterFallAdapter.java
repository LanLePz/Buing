package com.ahyapo.buing.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ahyapo.buing.R;
import com.ahyapo.buing.activity.IndexInfoActivity;
import com.ahyapo.buing.bean.IndexImages;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WaterFallAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
    private final Context context;
    private String GET_IMAGES_URL;
    private final ArrayList<IndexImages> dataList;
    public WaterFallAdapter(Context context, ArrayList<IndexImages> dataList,String url) {
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
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_simple,parent,false);
        SimpleViewHolder simpleViewHolder = new SimpleViewHolder(view);
        return simpleViewHolder;
    }

    /**
     * 要给条目控件展示的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        //设置当前控件的高度
        ViewGroup.LayoutParams layoutParams = holder.tv_item.getLayoutParams();
        //重新设置布局参数
        holder.tv_item.setLayoutParams(layoutParams);
        holder.index_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("imginfo", dataList.get(position));
                Intent intent = new Intent(context, IndexInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        Glide.with(context).load(GET_IMAGES_URL+"/static/litimgs"+dataList.get(position).getImgurl()).dontAnimate().into(holder.index_image);
        Glide.with(context).load(GET_IMAGES_URL+"/static/icons"+dataList.get(position).getUsericon()).dontAnimate().into(holder.user_icon);
        holder.user_name.setText(dataList.get(position).getUsername());
        holder.img_decribe.setText(dataList.get(position).getImgdescribe());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
