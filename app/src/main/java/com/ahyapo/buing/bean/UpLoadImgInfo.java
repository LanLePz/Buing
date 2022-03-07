package com.ahyapo.buing.bean;

import java.util.Date;

public class UpLoadImgInfo {
    //用户手机号、用户token、发表时间、图片标签、图片名称、图片描述
    private String tel,token,img_tags,img_describe,img_context;
    private Date img_time;

    public UpLoadImgInfo(String tel, String token, String img_tags, String img_describe, String img_context, Date img_time) {
        this.tel = tel;
        this.token = token;
        this.img_tags = img_tags;
        this.img_describe = img_describe;
        this.img_context = img_context;
        this.img_time = img_time;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImg_tags() {
        return img_tags;
    }

    public void setImg_tags(String img_tags) {
        this.img_tags = img_tags;
    }

    public String getImg_describe() {
        return img_describe;
    }

    public void setImg_describe(String img_describe) {
        this.img_describe = img_describe;
    }

    public String getImg_context() {
        return img_context;
    }

    public void setImg_context(String img_context) {
        this.img_context = img_context;
    }

    public Date getImg_time() {
        return img_time;
    }

    public void setImg_time(Date img_time) {
        this.img_time = img_time;
    }
}
