package com.ahyapo.buing.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;


public class DisSimpleViewHolder extends RecyclerView.ViewHolder {
    public final FrameLayout dis_item;
    public final ImageView dis_img;
    public final TextView dis_title;
    //itemview 展示的条目view
    public DisSimpleViewHolder(@NonNull View itemView) {
        super(itemView);
        dis_item = itemView.findViewById(R.id.dis_item);
        dis_img = itemView.findViewById(R.id.dis_img);
        dis_title = itemView.findViewById(R.id.dis_title);
    }
}
