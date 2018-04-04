package com.example.gaofeng.tulingdemo.view;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.example.gaofeng.tulingdemo.util.MyLog;

import java.lang.reflect.Field;
import java.util.Map;


/**
 * gaofeng
 */
public class MyWebView extends WebView {
    public static final String TAG = MyWebView.class.getSimpleName();
    private Context mContext;

    /**
     * 用户点击返回时认为当前页面加载完成。 复写stopLoading方法，设置webviewclient的超时状态位为false。
     */
    public void stopLoading() {
        super.stopLoading();
    }

    public MyWebView(Context context) {
        super(context);
        this.mContext = context;
        Init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        Init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        Init();

    }

    /**
     * 初始化网页设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void Init() {
        this.setVerticalScrollbarOverlay(true);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setRenderPriority(RenderPriority.HIGH); // 提高渲染的优先级
        this.setFocusable(true);
        this.getSettings().setBuiltInZoomControls(true);// 显示缩放按钮设置
        this.getSettings().setSupportZoom(true); // //可以缩放设置

        /**
         * xianing 加载网页上的视频插件
         * */
        this.getSettings().setPluginState(PluginState.ON_DEMAND);//显示网页上的插件
        this.setWebChromeClient(new WebChromeClient());
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        // 现在部分手机上会出现点击搜索框页面放大的情况，
        this.getSettings().setDefaultZoom(getZoomDensity(mContext));
        this.getSettings().setDefaultZoom(getZoomDensity(mContext));
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        //去掉缩放按钮  
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            // Use the API 11+ calls to disable the controls  
            this.getSettings().setBuiltInZoomControls(true);
            this.getSettings().setDisplayZoomControls(false);
        } else {
            // Use the reflection magic to make it work on earlier APIs  
            this.setZoomControlGone(this);
        }

    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private boolean bNoMotionEvent = true;

    public void setNOMotionEvent(boolean motion) {
        bNoMotionEvent = motion;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (!bNoMotionEvent && evt.getAction() == MotionEvent.ACTION_MOVE) {
            // 遮罩层，不允许拖动：
            return false;
        }
        if (isClickable()) {
            switch (evt.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        }

        return super.onTouchEvent(evt) || isClickable();

    }

    public WebSettings.ZoomDensity getZoomDensity(Context context) {
        int screenDensity = context.getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        return zoomDensity;
    }

    /**
     * android 隐藏webview的放大缩小控件
     *
     * @param view
     */
    public static void setZoomControlGone(View view) {
        Class classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
