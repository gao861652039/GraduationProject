package com.example.gaofeng.tulingdemo.view.ui;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by fujiayi on 2017/10/18.
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
