package com.ahyapo.buing.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;


public class DisInfoSimpleViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayout tv_item;
    public final ImageView index_image;
    public final ImageView user_icon;
    public final TextView user_name;
    public final TextView like_num;

    //itemview 展示的条目view
    public DisInfoSimpleViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_item = itemView.findViewById(R.id.tv_item);
        index_image = itemView.findViewById(R.id.index_image);
        user_icon = itemView.findViewById(R.id.user_icon);
        user_name = itemView.findViewById(R.id.user_name);
        like_num = itemView.findViewById(R.id.like_num);
    }
}
