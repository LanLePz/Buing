package com.ahyapo.buing.bean;

public class FollowUser {
    private int userid;
    private String username;
    private String usericon;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "FollowUser{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", usericon='" + usericon + '\'' +
                '}';
    }

    public FollowUser(int userid, String username, String usericon) {
        this.userid = userid;
        this.username = username;
        this.usericon = usericon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }
}
