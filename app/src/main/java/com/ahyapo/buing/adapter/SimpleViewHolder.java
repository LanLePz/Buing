package com.ahyapo.buing.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;


public class SimpleViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayout tv_item;
    public final ImageView index_image;
    public final TextView img_decribe;
    public final ImageView user_icon;
    public final TextView user_name;
    //itemview 展示的条目view
    public SimpleViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_item = itemView.findViewById(R.id.tv_item);
        index_image = itemView.findViewById(R.id.index_image);
        img_decribe = itemView.findViewById(R.id.img_describe);
        user_icon = itemView.findViewById(R.id.user_icon);
        user_name = itemView.findViewById(R.id.user_name);
    }
}
