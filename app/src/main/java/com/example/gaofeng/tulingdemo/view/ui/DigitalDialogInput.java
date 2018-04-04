package com.example.gaofeng.tulingdemo.view.ui;



import com.example.gaofeng.tulingdemo.control.MyRecognizer;
import com.example.gaofeng.tulingdemo.recognization.ChainRecogListener;

import java.util.Map;

/**
 * Created by gaofeng on 2018/4/4
 */

public class DigitalDialogInput {
    private MyRecognizer myRecognizer;

    private ChainRecogListener listener;

    private int code;

    private Map<String, Object> startParams;

    public DigitalDialogInput(MyRecognizer myRecognizer, ChainRecogListener listener, Map<String, Object> startParams) {
        this.myRecognizer = myRecognizer;
        this.listener = listener;
        this.startParams = startParams;
    }

    public MyRecognizer getMyRecognizer() {
        return myRecognizer;
    }

    public ChainRecogListener getListener() {
        return listener;
    }

    public Map<String, Object> getStartParams() {
        return startParams;
    }
}
