package com.ahyapo.buing.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.activity.ClassItemInfoActivity;
import com.ahyapo.buing.activity.DynamicItemInfoActivity;
import com.ahyapo.buing.bean.FollowUserDynamic;
import com.ahyapo.buing.bean.IndexImages;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater mInflater;
        private ArrayList<IndexImages> mDatas;
        private String GET_IMAGES_URL;
        public DynamicAdapter(Context context, ArrayList<IndexImages> datats,String url)
        {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
            this.GET_IMAGES_URL = url;
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public ViewHolder(View arg0)
            {
                super(arg0);
            }
            ImageView user_icon;
            TextView user_name;
            TextView post_time;
            ImageView post_img;
            TextView post_title;
            TextView post_info;
            TextView like_num;
            TextView discuss;
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View view = mInflater.inflate(R.layout.item_dynamic,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.user_icon = view.findViewById(R.id.user_icon);
            viewHolder.user_name = view.findViewById(R.id.user_name);
            viewHolder.post_time = view.findViewById(R.id.post_time);
            viewHolder.post_img = view.findViewById(R.id.post_img);
            viewHolder.post_title = view.findViewById(R.id.post_title);
            viewHolder.post_info = view.findViewById(R.id.post_info);
            viewHolder.like_num = view.findViewById(R.id.like_num);
            viewHolder.discuss = view.findViewById(R.id.discuss_num);
            return viewHolder;
        }
        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i)
        {
            Glide.with(context).load(GET_IMAGES_URL+"/static/icons"+mDatas.get(i).getUsericon()).into(viewHolder.user_icon);
            viewHolder.user_name.setText(mDatas.get(i).getUsername());
            Glide.with(context).load(GET_IMAGES_URL+"/static/litimgs"+mDatas.get(i).getImgurl()).into(viewHolder.post_img);
            viewHolder.post_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("imginfo", mDatas.get(i));
                    Intent intent = new Intent(context, DynamicItemInfoActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            viewHolder.post_time.setText(mDatas.get(i).getImgtime());
            viewHolder.post_title.setText(mDatas.get(i).getImgdescribe());
            viewHolder.post_info.setText(mDatas.get(i).getImgcontext());
            viewHolder.like_num.setText(""+mDatas.get(i).getLikenum());
        }
    }