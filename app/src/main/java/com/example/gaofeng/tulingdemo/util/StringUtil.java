package com.example.gaofeng.tulingdemo.util;

/**
 * Created by gaofeng on 2018/3/19.
 */

public class StringUtil {

    public static boolean isEmpty(String str){
          if(null == str || "".equals(str)){
              return true;
          }else{
              return false;
          }
    }
}
