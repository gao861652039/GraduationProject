package com.example.gaofeng.tulingdemo.util;

/**
 * Created by gaofeng on 2018/3/19.
 */

public class StringUtil {
    //空判断
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    //获取省市县信息
    public static String getAddressInfo(String str) {
        int index = str.indexOf("附近");
        return str.substring(0, index);
    }

}
