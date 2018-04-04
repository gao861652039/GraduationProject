package com.example.gaofeng.tulingdemo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.model.bean.RobotChatBean;

import java.util.List;

public class RobotChatAdapter extends RecyclerView.Adapter<RobotChatAdapter.RobotChatView> {
    private Context context;
    private List<RobotChatBean> list;


    public RobotChatAdapter(Context context, List<RobotChatBean> list) {
        this.context = context;
        this.list = list;
    }

    public void addChat(List<RobotChatBean> robotChatBean) {
        if (null != robotChatBean) {
            this.list = robotChatBean;
            notifyDataSetChanged();
        }
    }


    @Override
    public RobotChatView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_item, parent, false);
        RobotChatView robotChatView = new RobotChatView(view);
        return robotChatView;
    }

    @Override
    public void onBindViewHolder(RobotChatView holder, int position) {
        RobotChatBean robotChatBean = list.get(position);
        if (robotChatBean.getTextType() == 0) {

            holder.chat_item_tv.setText(robotChatBean.getChat_text());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.chat_item_card_view.setLayoutParams(layoutParams);

        } else if (robotChatBean.getTextType() == 1) {
            holder.chat_item_tv.setText(robotChatBean.getChat_text());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.chat_item_card_view.setLayoutParams(layoutParams);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class RobotChatView extends RecyclerView.ViewHolder {
        private TextView chat_item_tv;
        private CardView chat_item_card_view;

        public RobotChatView(View itemView) {
            super(itemView);
            chat_item_tv = itemView.findViewById(R.id.chat_item_tv);
            chat_item_card_view = itemView.findViewById(R.id.chat_item_card_view);
        }
    }
}
