package com.ahyapo.buing.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.bean.CommentInfo;
import com.ahyapo.buing.bean.UserFollows;
import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater mInflater;
        private ArrayList<UserFollows> mDatas;
        private String GET_IMAGES_URL;
        private boolean ischecked;
        private String type;
        public FollowAdapter(Context context, ArrayList<UserFollows> datats, String url,String type)
        {
            this.context = context;
            mInflater = LayoutInflater.from(context);
            mDatas = datats;
            this.type = type;
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
            TextView product_num;
            TextView fans_num;
            TextView cancel_follow;
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

        /**
         * 创建ViewHolder
         */
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View view = mInflater.inflate(R.layout.item_follows,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.user_icon = view.findViewById(R.id.user_icon);
            viewHolder.user_name = view.findViewById(R.id.user_name);
            viewHolder.product_num = view.findViewById(R.id.product_num);
            viewHolder.fans_num = view.findViewById(R.id.fans_num);
            viewHolder.cancel_follow = view.findViewById(R.id.cancel_follow);
            if (type.equals("follow")){
                ischecked = true;
            }else ischecked = false;
            if (!ischecked){
                Log.e("取消关注：","OK");
                viewHolder.cancel_follow.setBackground(context.getDrawable(R.drawable.edit_border_blue));
                viewHolder.cancel_follow.setTextColor(Color.WHITE);
                viewHolder.cancel_follow.setText("关注");
            }else {
                Log.e("取消关注：","OK");
                viewHolder.cancel_follow.setBackground(context.getDrawable(R.drawable.edit_border_follow));
                viewHolder.cancel_follow.setTextColor(Color.parseColor("#9E9E9E"));
                viewHolder.cancel_follow.setText("已关注");
            }
            viewHolder.cancel_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ischecked){
                        Log.e("取消关注：","OK");
                        viewHolder.cancel_follow.setBackground(context.getDrawable(R.drawable.edit_border_blue));
                        viewHolder.cancel_follow.setTextColor(Color.WHITE);
                        viewHolder.cancel_follow.setText("关注");
                        ischecked = false;
                    }else {
                        Log.e("取消关注：","OK");
                        viewHolder.cancel_follow.setBackground(context.getDrawable(R.drawable.edit_border_follow));
                        viewHolder.cancel_follow.setTextColor(Color.parseColor("#9E9E9E"));
                        viewHolder.cancel_follow.setText("已关注");
                        ischecked = true;
                    }

                }
            });
            return viewHolder;
        }
        /**
         * 设置值
         */
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i)
        {
            Glide.with(context).load(GET_IMAGES_URL+"/static/icons"+mDatas.get(i).getIcon()).dontAnimate().into(viewHolder.user_icon);
            viewHolder.user_name.setText(mDatas.get(i).getName());
            viewHolder.product_num.setText(mDatas.get(i).getProduct());
            viewHolder.fans_num.setText(mDatas.get(i).getFans());
        }
    }