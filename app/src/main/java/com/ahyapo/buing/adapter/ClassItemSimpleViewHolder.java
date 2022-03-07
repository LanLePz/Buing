package com.ahyapo.buing.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;


public class ClassItemSimpleViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayout tv_item;
    public final ImageView class_item_img;


    //itemview 展示的条目view
    public ClassItemSimpleViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_item = itemView.findViewById(R.id.tv_item);
        class_item_img = itemView.findViewById(R.id.class_item_img);
    }
}
