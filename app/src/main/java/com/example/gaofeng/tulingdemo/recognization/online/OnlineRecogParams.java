package com.example.gaofeng.tulingdemo.recognization.online;

import android.app.Activity;

import com.baidu.speech.asr.SpeechConstant;
import com.example.gaofeng.tulingdemo.recognization.CommonRecogParams;

import java.util.Arrays;

/**
 * Created by gaofeng on 2018/4/4
 */

public class OnlineRecogParams extends CommonRecogParams {


    private static final String TAG = "OnlineRecogParams";

    public OnlineRecogParams(Activity context) {
        super(context);

        stringParams.addAll(Arrays.asList(
                "_language", // 用于生成PID参数
                "_model" // 用于生成PID参数
        ));

        intParams.addAll(Arrays.asList(SpeechConstant.PROP));

        boolParams.addAll(Arrays.asList(SpeechConstant.DISABLE_PUNCTUATION));

    }


}
