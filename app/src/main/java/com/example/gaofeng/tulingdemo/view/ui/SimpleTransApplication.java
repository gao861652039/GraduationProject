package com.example.gaofeng.tulingdemo.view.ui;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by gaofeng on 2018/4/4
 */

public class SimpleTransApplication extends Application {
    private DigitalDialogInput digitalDialogInput;


    public DigitalDialogInput getDigitalDialogInput() {
        return digitalDialogInput;
    }

    public void setDigitalDialogInput(DigitalDialogInput digitalDialogInput) {
        this.digitalDialogInput = digitalDialogInput;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }
}
