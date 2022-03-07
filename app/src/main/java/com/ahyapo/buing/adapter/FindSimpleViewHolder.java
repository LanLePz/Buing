package com.ahyapo.buing.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;


public class FindSimpleViewHolder extends RecyclerView.ViewHolder {
    public final FrameLayout tv_item;
    public final ImageView class_img;
    public final TextView class_name;
    //itemview 展示的条目view
    public FindSimpleViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_item = itemView.findViewById(R.id.find_item);
        class_img = itemView.findViewById(R.id.class_img);
        class_name = itemView.findViewById(R.id.class_name);
    }
}
