package com.ahyapo.buing.bean;

public class LoginUserInfo {
    private String usertel;
    private String msg;
    private String token;

    public LoginUserInfo(String usertel, String msg, String token) {
        this.usertel = usertel;
        this.msg = msg;
        this.token = token;
    }

    public String getUsertel() {
        return usertel;
    }

    public void setUsertel(String usertel) {
        this.usertel = usertel;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
