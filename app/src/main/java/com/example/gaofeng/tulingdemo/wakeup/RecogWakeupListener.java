package com.example.gaofeng.tulingdemo.wakeup;

import android.os.Handler;


import com.example.gaofeng.tulingdemo.recognization.IStatus;


/**
 * Created by gaofeng on 2018/4/4
 */

public class RecogWakeupListener extends SimpleWakeupListener implements IStatus {

    private static final String TAG = "RecogWakeupListener";

    private Handler handler;

    public RecogWakeupListener(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        super.onSuccess(word, result);
        handler.sendMessage(handler.obtainMessage(STATUS_WAKEUP_SUCCESS));
    }
}
