package com.ahyapo.buing.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class UserFollows implements Parcelable {
    private String icon;
    private String name;
    private String product;
    private String fans;

    protected UserFollows(Parcel in) {
        icon = in.readString();
        name = in.readString();
        product = in.readString();
        fans = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(name);
        dest.writeString(product);
        dest.writeString(fans);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserFollows> CREATOR = new Creator<UserFollows>() {
        @Override
        public UserFollows createFromParcel(Parcel in) {
            return new UserFollows(in);
        }

        @Override
        public UserFollows[] newArray(int size) {
            return new UserFollows[size];
        }
    };

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getFans() {
        return fans;
    }

    public void setFans(String fans) {
        this.fans = fans;
    }

    public UserFollows(String icon, String name, String product, String fans) {
        this.icon = icon;
        this.name = name;
        this.product = product;
        this.fans = fans;
    }
}
