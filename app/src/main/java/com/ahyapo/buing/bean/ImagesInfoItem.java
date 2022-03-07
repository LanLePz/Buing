package com.ahyapo.buing.bean;

public class ImagesInfoItem {
    private int imgid;
    private String imgurl;
    private String usericon;
    private String username;
    private String imgdescribe;
    private String imgcontext;

    @Override
    public String toString() {
        return "ImagesInfoItem{" +
                "imgid=" + imgid +
                ", imgurl='" + imgurl + '\'' +
                ", usericon='" + usericon + '\'' +
                ", username='" + username + '\'' +
                ", imgdescribe='" + imgdescribe + '\'' +
                ", imgcontext='" + imgcontext + '\'' +
                '}';
    }

    public ImagesInfoItem(int imgid, String imgurl, String usericon, String username, String imgdescribe, String imgcontext) {
        this.imgid = imgid;
        this.imgurl = imgurl;
        this.usericon = usericon;
        this.username = username;
        this.imgdescribe = imgdescribe;
        this.imgcontext = imgcontext;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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

    public String getImgdescribe() {
        return imgdescribe;
    }

    public void setImgdescribe(String imgdescribe) {
        this.imgdescribe = imgdescribe;
    }

    public String getImgcontext() {
        return imgcontext;
    }

    public void setImgcontext(String imgcontext) {
        this.imgcontext = imgcontext;
    }
}
