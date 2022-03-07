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
import com.ahyapo.buing.activity.ClassItemActivity;
import com.ahyapo.buing.bean.FindClass;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FindWaterFallAdapter extends RecyclerView.Adapter<FindSimpleViewHolder> {
    private final Context context;
    private String GET_IMAGES_URL;
    private final ArrayList<FindClass> dataList;
    public FindWaterFallAdapter(Context context, ArrayList<FindClass> dataList, String url) {
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
    public FindSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_find,parent,false);
        FindSimpleViewHolder findSimpleViewHolder = new FindSimpleViewHolder(view);
        return findSimpleViewHolder;
    }

    /**
     * 要给条目控件展示的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull FindSimpleViewHolder holder, int position) {
        //设置当前控件的高度
        ViewGroup.LayoutParams layoutParams = holder.tv_item.getLayoutParams();
        //重新设置布局参数
        holder.tv_item.setLayoutParams(layoutParams);
        holder.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("classinfo", dataList.get(position));
                Intent intent = new Intent(context, ClassItemActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        //加载图片
        Glide.with(context).load(GET_IMAGES_URL+"/static/images"+dataList.get(position).getImg()).into(holder.class_img);
        holder.class_name.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
