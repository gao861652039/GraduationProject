package com.example.gaofeng.tulingdemo.wakeup;
import com.example.gaofeng.tulingdemo.util.Logger;

/**
 * Created by gaofeng on 2018/4/4
 */

public class SimpleWakeupListener implements IWakeupListener {

    private static final String TAG = "SimpleWakeupListener";

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        Logger.info(TAG, "唤醒成功，唤醒词：" + word);
    }

    @Override
    public void onStop() {
        Logger.info(TAG, "唤醒词识别结束：");
    }

    @Override
    public void onError(int errorCode, String errorMessge, WakeUpResult result) {
        Logger.info(TAG, "唤醒错误：" + errorCode + ";错误消息：" + errorMessge + "; 原始返回" + result.getOrigalJson());
    }

    @Override
    public void onASrAudio(byte[] data, int offset, int length) {
        Logger.error(TAG, "audio data： " + data.length);
    }

}
