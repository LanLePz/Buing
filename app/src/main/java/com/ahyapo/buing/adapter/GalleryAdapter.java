package com.ahyapo.buing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.bean.FollowUser;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater mInflater;
        private ArrayList<FollowUser> mDatas;
        private String GET_IMAGES_URL;
        public GalleryAdapter(Context context, ArrayList<FollowUser> datats,String url)
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
            View view = mInflater.inflate(R.layout.item_recommend_user,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.user_icon = view.findViewById(R.id.user_icon);
            viewHolder.user_name = view.findViewById(R.id.user_name);
            return viewHolder;
        }
        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i)
        {
            Glide.with(context).load(GET_IMAGES_URL+"/static/icons"+mDatas.get(i).getUsericon()).dontAnimate().into(viewHolder.user_icon);
            viewHolder.user_name.setText(mDatas.get(i).getUsername());
        }
    }