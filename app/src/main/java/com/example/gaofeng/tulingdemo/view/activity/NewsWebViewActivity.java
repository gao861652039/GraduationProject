package com.example.gaofeng.tulingdemo.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.model.eventmsg.UrlMsg;
import com.example.gaofeng.tulingdemo.view.MyWebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsWebViewActivity extends AppCompatActivity {
    @BindView(R.id.activity_webview_webview)
    MyWebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoadUrl(UrlMsg urlMsg) {

        myWebView.loadUrl(urlMsg.getUrl());
    }


}
