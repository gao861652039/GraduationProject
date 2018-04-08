package com.example.gaofeng.tulingdemo.model.eventmsg;

/**
 * Created by gaofeng on 2018/3/19.
 */

public class UrlMsg {
    private String url;
    private String title;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlMsg(String url, String title, String imageUrl) {
        this.url = url;
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
