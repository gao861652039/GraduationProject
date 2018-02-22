package com.example.gaofeng.tulingdemo.util;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaofeng on 2017/12/15.
 */

public class NetWorkUtil {
    private static String key = "daedd0780ee042a49d1cf67342201081";
    private static String url = "http://www.tuling123.com/openapi/";
    private static NetWorkUtil netWorkUtil;
    private static Retrofit retrofit;
    private NetWorkUtil(){

    }

    public static NetWorkUtil getInstance(){
         if(netWorkUtil == null){
             netWorkUtil = new NetWorkUtil();
         }
         initRetrofit();
         return netWorkUtil;
    }

    private static void initRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit(){

        return  retrofit;
    }

    public String getKey(){
        return key;
    }
}
