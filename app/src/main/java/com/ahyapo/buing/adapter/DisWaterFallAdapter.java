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
import com.ahyapo.buing.activity.DissertationActivity;
import com.ahyapo.buing.bean.DissertationInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DisWaterFallAdapter extends RecyclerView.Adapter<DisSimpleViewHolder> {
    private final Context context;
    private String GET_IMAGES_URL;
    private final ArrayList<DissertationInfo> dataList;
    public DisWaterFallAdapter(Context context, ArrayList<DissertationInfo> dataList, String url) {
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
    public DisSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dissertation,parent,false);
        DisSimpleViewHolder disSimpleViewHolder = new DisSimpleViewHolder(view);
        return disSimpleViewHolder;
    }

    /**
     * 要给条目控件展示的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DisSimpleViewHolder holder, int position) {
        //设置当前控件的高度
        ViewGroup.LayoutParams layoutParams = holder.dis_item.getLayoutParams();
        //重新设置布局参数
        holder.dis_item.setLayoutParams(layoutParams);
        holder.dis_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("disinfo", dataList.get(position));
                Intent intent = new Intent(context, DissertationActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        //加载图片
        Glide.with(context).load(GET_IMAGES_URL+"/static/images"+dataList.get(position)
                .getImgurl())
                .dontAnimate()
                .into(holder.dis_img);
        holder.dis_title.setText(dataList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
