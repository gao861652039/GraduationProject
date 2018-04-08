package com.example.gaofeng.tulingdemo.view.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gaofeng.tulingdemo.R;
import com.example.gaofeng.tulingdemo.util.StringUtil;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * 默认进度条对话框
 */
public class MyProgressDialog extends Dialog {

    //    private MyProgressDialog myProgressDialog;
    private Context context;

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public MyProgressDialog(Context context) {
        super(context,R.style.CustomProgressDialog);
        this.context = context;
    }

    public MyProgressDialog creatDialog(String message) {
        return creatDialog(StringUtil.isEmpty(message) ? "数据加载中..." : message, true);
    }

    /**
     * @param message
     * @return
     */
    public MyProgressDialog creatDialog(String message, boolean cancelable) {
        setContentView(R.layout.view_myprogressdialog);
        setCancelable(cancelable);
        getWindow().getAttributes().gravity = Gravity.CENTER;

        // 加载图片动画
        AVLoadingIndicatorView avi = findViewById(R.id.avi);
        avi.show();

        // 加载提示信息
        TextView tv_message = (TextView) findViewById(R.id.view_myprogressdialog_message);
        if (null == message || "".equals(message))
            tv_message.setVisibility(View.GONE);
        else
            tv_message.setText(message);
        setCanceledOnTouchOutside(false);
        return this;
    }

/*    @Override
    public void cancel() {
        super.cancel();
       dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dismiss();
    }*/
}