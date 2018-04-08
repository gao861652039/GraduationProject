package com.example.gaofeng.tulingdemo.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.model.eventmsg.UrlMsg;
import com.example.gaofeng.tulingdemo.view.MyWebView;
import com.example.gaofeng.tulingdemo.view.ui.MyProgressDialog;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsWebViewActivity extends AppCompatActivity {
    @BindView(R.id.activity_webview_webview)
    MyWebView myWebView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    private MyProgressDialog myProgressDialog;


    private UrlMsg urlMsg = new UrlMsg("", "测试", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web_view);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoadUrl(UrlMsg urlMsg) {
        if (null != urlMsg) {
            this.urlMsg = urlMsg;
            myWebView.loadUrl(urlMsg.getUrl());
            collapsing_toolbar.setTitle(urlMsg.getTitle());
        }

    }

    private void initView() {
        myProgressDialog = new MyProgressDialog(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Glide.with(this).load(R.mipmap.corlayout_back_2).into(ivImage);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                myProgressDialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                myProgressDialog.creatDialog("正在加载内容哦~", false).show();
            }
        });

        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()) {  //表示按返回键
                        myWebView.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
