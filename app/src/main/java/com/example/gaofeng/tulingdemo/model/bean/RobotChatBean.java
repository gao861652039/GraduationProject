package com.example.gaofeng.tulingdemo.model.bean;

public class RobotChatBean {



    private String chat_text;
    /**
     * 0.机器人  1.用户
     */
    private int textType;


    public String getChat_text() {
        return chat_text;
    }

    public void setChat_text(String chat_text) {
        this.chat_text = chat_text;
    }

    public int getTextType() {
        return textType;
    }

    public void setTextType(int textType) {
        this.textType = textType;
    }
}
