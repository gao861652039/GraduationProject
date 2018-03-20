package com.example.gaofeng.tulingdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.model.bean.DishBean;
import com.example.gaofeng.tulingdemo.model.bean.NewsBean;
import com.example.gaofeng.tulingdemo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaofeng on 2018/3/19.
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.DishesViewHolder> implements View.OnClickListener {
    private Context context;
    private List<DishBean.ListBean> newsList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = null;

    public DishesAdapter(Context context, List<DishBean.ListBean> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public DishesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_news_item, parent, false);
        DishesViewHolder newsViewHolder = new DishesViewHolder(view);
        view.setOnClickListener(this);
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(DishesViewHolder holder, int position) {
        holder.itemView.setTag(position);
        DishBean.ListBean newsBean = newsList.get(position);
        if (!StringUtil.isEmpty(newsBean.getIcon())) {
            Glide.with(context).load(newsBean.getIcon()).placeholder(R.mipmap.card_view_back).into(holder.newsImage);
        }
        holder.newsTitle.setText(newsBean.getName());
        holder.newsSource.setText(newsBean.getInfo());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    class DishesViewHolder extends RecyclerView.ViewHolder {


        private ImageView newsImage;
        private TextView newsTitle;
        private TextView newsSource;

        public DishesViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsSource = itemView.findViewById(R.id.news_source);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
