package com.example.gaofeng.tulingdemo.view.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.example.gaofeng.tulingdemo.R
import com.example.gaofeng.tulingdemo.model.eventmsg.UrlMsg
import com.example.gaofeng.tulingdemo.view.MyWebView
import com.example.gaofeng.tulingdemo.view.ui.MyProgressDialog
import com.wang.avi.AVLoadingIndicatorView

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import butterknife.BindView
import butterknife.ButterKnife

class NewsWebViewActivity : AppCompatActivity() {
    @BindView(R.id.activity_webview_webview)
    internal var myWebView: MyWebView? = null
    @BindView(R.id.toolbar)
    internal var toolbar: Toolbar? = null
    @BindView(R.id.ivImage)
    internal var ivImage: ImageView? = null
    @BindView(R.id.collapsing_toolbar)
    internal var collapsing_toolbar: CollapsingToolbarLayout? = null
    private var myProgressDialog: MyProgressDialog? = null


    private var urlMsg = UrlMsg("", "测试", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_web_view)
        ButterKnife.bind(this)
        initView()
        EventBus.getDefault().register(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun getLoadUrl(urlMsg: UrlMsg?) {
        if (null != urlMsg) {
            this.urlMsg = urlMsg
            myWebView!!.loadUrl(urlMsg.url)
            collapsing_toolbar!!.title = urlMsg.title
        }

    }

    private fun initView() {
        myProgressDialog = MyProgressDialog(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { onBackPressed() }

        Glide.with(this).load(R.mipmap.corlayout_back_2).into(ivImage!!)
        myWebView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                myProgressDialog!!.dismiss()
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                myProgressDialog!!.creatDialog("正在加载内容哦~", false).show()
            }
        }

        myWebView!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && myWebView!!.canGoBack()) {  //表示按返回键
                    myWebView!!.goBack()   //后退
                    return@OnKeyListener true    //已处理
                }
            }
            false
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}
