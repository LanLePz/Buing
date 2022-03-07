package com.ahyapo.buing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ahyapo.buing.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class GVAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;

    public GVAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.photo_select, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.main_gridView_item_photo);
                holder.nullView = (ImageView) convertView.findViewById(R.id.null_view);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String s = list.get(position);
            if (!s.equals("a")) {
                holder.nullView.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
                Glide.with(context).load(s).dontAnimate().into(holder.imageView);

            } else {
                holder.nullView.setVisibility(View.VISIBLE);
                //在这里进行判断如果当前是待加载状态就隐藏当前控件
                holder.imageView.setVisibility(View.VISIBLE);
                //原来加上这个总是显示图片太大
                holder.imageView.setImageResource(R.mipmap.add_picture);
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView nullView;
            ImageView imageView;
        }

    }