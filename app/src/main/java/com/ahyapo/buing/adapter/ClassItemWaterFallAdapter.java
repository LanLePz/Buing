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
import com.ahyapo.buing.activity.ClassItemInfoActivity;
import com.ahyapo.buing.activity.DisItemInfoActivity;
import com.ahyapo.buing.bean.DisItemInfo;
import com.ahyapo.buing.bean.IndexImages;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

;

public class ClassItemWaterFallAdapter extends RecyclerView.Adapter<ClassItemSimpleViewHolder> {
    private final Context context;
    private String GET_IMAGES_URL;
    private final ArrayList<IndexImages> dataList;
    public ClassItemWaterFallAdapter(Context context, ArrayList<IndexImages> dataList, String url) {
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
    public ClassItemSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class,parent,false);
        ClassItemSimpleViewHolder simpleViewHolder = new ClassItemSimpleViewHolder(view);
        return simpleViewHolder;
    }

    /**
     * 要给条目控件展示的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ClassItemSimpleViewHolder holder, int position) {
        //设置当前控件的高度
        ViewGroup.LayoutParams layoutParams = holder.tv_item.getLayoutParams();
        //重新设置布局参数
        holder.tv_item.setLayoutParams(layoutParams);
        holder.class_item_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("imginfo", dataList.get(position));
                Intent intent = new Intent(context, ClassItemInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        //holder.tv_item.setBackgroundColor(Color.rgb((int) (100+Math.random()*155),(int) (100+Math.random()*155),(int) (100+Math.random()*155)));
        Glide.with(context).load(GET_IMAGES_URL+"/static/litimgs"+dataList.get(position).getImgurl()).dontAnimate().into(holder.class_item_img);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
