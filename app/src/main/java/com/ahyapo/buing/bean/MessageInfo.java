package com.ahyapo.buing.bean;

public class MessageInfo {
    private int messageid;
    private String usericon;
    private String username;
    private String chatInfo;
    private String chattime;

    public MessageInfo(int messageid, String usericon, String username, String chatInfo, String chattime) {
        this.messageid = messageid;
        this.usericon = usericon;
        this.username = username;
        this.chatInfo = chatInfo;
        this.chattime = chattime;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "messageid=" + messageid +
                ", usericon='" + usericon + '\'' +
                ", username='" + username + '\'' +
                ", chatInfo='" + chatInfo + '\'' +
                ", chatdate='" + chattime + '\'' +
                '}';
    }

    public int getMessageid() {
        return messageid;
    }

    public void setMessageid(int messageid) {
        this.messageid = messageid;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(String chatInfo) {
        this.chatInfo = chatInfo;
    }

    public String getChattime() {
        return chattime;
    }

    public void setChattime(String chatdate) {
        this.chattime = chatdate;
    }

}
