package com.example.gaofeng.tulingdemo.model.bean;

/**
 * Created by gaofeng on 2017/12/15.
 */

public class UrlBean {


    /**
     * code : 200000
     * text : 亲，已帮你找到相关酒店信息
     * url : http://m.elong.com/hotel/0101/nlist/#indate=2017-12-16&outdate=2017-12-17&keywords=%E4%BF%A1%E6%81%AF%E8%B7%AF
     */

    private int code;
    private String text;
    private String url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
