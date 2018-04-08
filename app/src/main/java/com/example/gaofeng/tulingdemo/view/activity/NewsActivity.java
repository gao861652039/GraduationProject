package com.example.gaofeng.tulingdemo.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.adapter.DishesAdapter;
import com.example.gaofeng.tulingdemo.adapter.NewsAdapter;
import com.example.gaofeng.tulingdemo.model.bean.DishBean;
import com.example.gaofeng.tulingdemo.model.bean.NewsBean;
import com.example.gaofeng.tulingdemo.model.bean.UrlBean;
import com.example.gaofeng.tulingdemo.model.eventmsg.UrlMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity {
    @BindView(R.id.news_recycle)
    RecyclerView news_recycle;

    private NewsAdapter adapter;
    private DishesAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getNewsBeanEvent(final NewsBean newsBean) {
        if (null != newsBean) {

            adapter = new NewsAdapter(this, newsBean.getList());
            news_recycle.setLayoutManager(new GridLayoutManager(this, 2));
            if (null != adapter2) {
                news_recycle.swapAdapter(adapter, true);
            } else {
                news_recycle.setAdapter(adapter);
            }

            adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    String url = newsBean.getList().get(position).getDetailurl();
                    EventBus.getDefault().postSticky(new UrlMsg(url, "新闻详情", newsBean.getList().get(position).getIcon()));
                    startActivity(new Intent(NewsActivity.this, NewsWebViewActivity.class));
                }
            });
            getSupportActionBar().setTitle("新闻列表");

            EventBus.getDefault().removeStickyEvent(newsBean);
        } else {
            Toast.makeText(this, "暂无新闻", Toast.LENGTH_SHORT).show();
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getDishesEvent(final DishBean dishBean) {
        if (null != dishBean) {

            adapter2 = new DishesAdapter(this, dishBean.getList());
            news_recycle.setLayoutManager(new GridLayoutManager(this, 2));
            if (null != adapter) {
                news_recycle.swapAdapter(adapter2, true);
            } else {
                news_recycle.setAdapter(adapter2);
            }
            adapter2.setOnItemClickListener(new DishesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    String url = dishBean.getList().get(position).getDetailurl();
                    EventBus.getDefault().postSticky(new UrlMsg(url, "菜品详情", dishBean.getList().get(position).getIcon()));
                    startActivity(new Intent(NewsActivity.this, NewsWebViewActivity.class));
                }
            });
            getSupportActionBar().setTitle("菜谱列表");
            EventBus.getDefault().removeStickyEvent(dishBean);
        } else {
            Toast.makeText(this, "暂无菜谱", Toast.LENGTH_SHORT).show();
        }
    }


}
