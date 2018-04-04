package com.example.gaofeng.tulingdemo.wakeup;

/**
 * Created by gaofeng on 2018/4/4
 */

public interface IWakeupListener {


    void onSuccess(String word, WakeUpResult result);

    void onStop();

    void onError(int errorCode, String errorMessge, WakeUpResult result);

    void onASrAudio(byte[] data, int offset, int length);
}
