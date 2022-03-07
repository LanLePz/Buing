package com.ahyapo.buing.bean;

public class FollowUserDynamic {
    private int userid;
    private String usericon;
    private String username;
    private String posttime;
    private String postimg;
    private String posttitle;
    private String postinfo;
    private int likenum;
    private int discuss;

    public FollowUserDynamic(int userid, String usericon, String username, String posttime, String postimg, String posttitle, String postinfo, int likenum, int discuss) {
        this.userid = userid;
        this.usericon = usericon;
        this.username = username;
        this.posttime = posttime;
        this.postimg = postimg;
        this.posttitle = posttitle;
        this.postinfo = postinfo;
        this.likenum = likenum;
        this.discuss = discuss;
    }

    @Override
    public String toString() {
        return "FollowUserDynamic{" +
                "userid=" + userid +
                ", usericon='" + usericon + '\'' +
                ", username='" + username + '\'' +
                ", posttime='" + posttime + '\'' +
                ", postimg='" + postimg + '\'' +
                ", posttitle='" + posttitle + '\'' +
                ", postinfo='" + postinfo + '\'' +
                ", likenum=" + likenum +
                ", discuss=" + discuss +
                '}';
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getPostimg() {
        return postimg;
    }

    public void setPostimg(String postimg) {
        this.postimg = postimg;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public void setPosttitle(String posttitle) {
        this.posttitle = posttitle;
    }

    public String getPostinfo() {
        return postinfo;
    }

    public void setPostinfo(String postinfo) {
        this.postinfo = postinfo;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }

    public int getDiscuss() {
        return discuss;
    }

    public void setDiscuss(int discuss) {
        this.discuss = discuss;
    }
}
