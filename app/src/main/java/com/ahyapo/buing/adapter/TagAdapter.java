package com.ahyapo.buing.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.activity.DynamicItemInfoActivity;
import com.ahyapo.buing.bean.ImageTags;
import com.ahyapo.buing.bean.IndexImages;
import com.ahyapo.buing.widget.TagMap;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater mInflater;
        private ArrayList<ImageTags> mDatas;
        private TextView tags;
        private ArrayList<String> class_tags = new ArrayList<>();
        //private TagMap<String,String> class_tags = new TagMap();



    public TagAdapter(Context context, ArrayList<ImageTags> datats, TextView tags)
            {
                this.context = context;
                mInflater = LayoutInflater.from(context);
                mDatas = datats;
                this.tags = tags;
            }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(View arg0)
            {
                super(arg0);
            }
            ToggleButton class_name;
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

    /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View view = mInflater.inflate(R.layout.tag,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.class_name = view.findViewById(R.id.class_name);

            return viewHolder;
        }
        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i)
        {
            viewHolder.class_name.setText(mDatas.get(i).getClass_name());
            viewHolder.class_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        class_tags.add(mDatas.get(i).getClass_name());
                        tags.setText(tagsToString(class_tags).toString());
                    }else{
                        class_tags.remove(mDatas.get(i).getClass_name());
                        tags.setText(tagsToString(class_tags).toString());
                    }
                }
            });
        }
    private StringBuilder tagsToString(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            String s = iterator.next();
            sb.append(s);
            if (!iterator.hasNext())
                sb.append("");
            else
                sb.append(",");
        }
        return sb;
    }
}