package com.example.gaofeng.tulingdemo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //获取打电话的号码

    public static String getPhoneNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


}
