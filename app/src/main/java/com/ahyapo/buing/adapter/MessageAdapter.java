package com.ahyapo.buing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.bean.MessageInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        private Context context;
        private LayoutInflater mInflater;
        private ArrayList<MessageInfo> mDatas;
        private String GET_IMAGES_URL;
        public MessageAdapter(Context context, ArrayList<MessageInfo> datats, String url)
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
            TextView chat_info;
            TextView chat_time;
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
            View view = mInflater.inflate(R.layout.item_message,
                    viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.user_icon = view.findViewById(R.id.user_icon);
            viewHolder.user_name = view.findViewById(R.id.user_name);
            viewHolder.chat_info = view.findViewById(R.id.chat_info);
            viewHolder.chat_time = view.findViewById(R.id.chat_time);
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
            viewHolder.chat_info.setText(mDatas.get(i).getChatInfo());
            viewHolder.chat_time.setText(mDatas.get(i).getChattime());


        }
    }