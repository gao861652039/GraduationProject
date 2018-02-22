package com.example.gaofeng.tulingdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gaofeng.tulingdemo.R;

import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;

/**
 * Created by gaofeng on 2018/2/22.
 */

public class MyAdapter extends SimpleRecAdapter<String, MyAdapter.ViewHolder> {


    public MyAdapter(Context context) {
        super(context);

    }


    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recycle_item;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_item);
        }
    }

}
