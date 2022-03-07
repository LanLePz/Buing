package com.ahyapo.buing.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.bean.CommentInfo;
import com.ahyapo.buing.bean.MessageInfo;
import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater mInflater;
        private ArrayList<CommentInfo> mDatas;
        private String GET_IMAGES_URL;
        public CommentAdapter(Context context, ArrayList<CommentInfo> datats, String url)
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
            TextView com_context;
            TextView com_time;
            TextView com_like_num;
            ShineButton btn_like;
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
            View view = mInflater.inflate(R.layout.item_comment,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.user_icon = view.findViewById(R.id.com_user_icon);
            viewHolder.user_name = view.findViewById(R.id.com_user_name);
            viewHolder.com_context = view.findViewById(R.id.com_context);
            viewHolder.com_time = view.findViewById(R.id.com_time);
            viewHolder.com_like_num = view.findViewById(R.id.com_like_num);
            viewHolder.btn_like = view.findViewById(R.id.btn_like);
            viewHolder.btn_like.init((Activity) context);
            viewHolder.btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("评论区点赞状态：",""+viewHolder.btn_like.isChecked());
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
            Glide.with(context).load(GET_IMAGES_URL+"/static/icons"+mDatas.get(i).getComusericon()).dontAnimate().into(viewHolder.user_icon);
            viewHolder.user_name.setText(mDatas.get(i).getComusername());
            viewHolder.com_context.setText(mDatas.get(i).getComcontext());
            viewHolder.com_time.setText(mDatas.get(i).getComtime());
            viewHolder.com_like_num.setText(""+mDatas.get(i).getComlikenum());
        }
    }