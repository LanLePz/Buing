package com.ahyapo.buing.bean;

public class MineUserInfo {
    private String user_icon;
    private String user_name;
    private String user_sign;
    private int user_wallet;
    private int user_follows;
    private int user_fans;
    private int product_num;

    @Override
    public String toString() {
        return "MineUserInfo{" +
                "user_icon='" + user_icon + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_sign='" + user_sign + '\'' +
                ", user_wallet=" + user_wallet +
                ", user_follows=" + user_follows +
                ", user_fans=" + user_fans +
                ", product_num=" + product_num +
                '}';
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_sign() {
        return user_sign;
    }

    public void setUser_sign(String user_sign) {
        this.user_sign = user_sign;
    }

    public int getUser_wallet() {
        return user_wallet;
    }

    public void setUser_wallet(int user_wallet) {
        this.user_wallet = user_wallet;
    }

    public int getUser_follows() {
        return user_follows;
    }

    public void setUser_follows(int user_follows) {
        this.user_follows = user_follows;
    }

    public int getUser_fans() {
        return user_fans;
    }

    public void setUser_fans(int user_fans) {
        this.user_fans = user_fans;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
    }

    public MineUserInfo(String user_icon, String user_name, String user_sign, int user_wallet, int user_follows, int user_fans, int product_num) {
        this.user_icon = user_icon;
        this.user_name = user_name;
        this.user_sign = user_sign;
        this.user_wallet = user_wallet;
        this.user_follows = user_follows;
        this.user_fans = user_fans;
        this.product_num = product_num;
    }
}
