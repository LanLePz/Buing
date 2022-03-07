package com.ahyapo.buing.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 专题类型bean类
 */
public class DissertationInfo implements Parcelable {
    private int disid;
    private String imgurl;
    private String title;
    private String context;
    private String username;
    private String usericon;

    public DissertationInfo(int disid, String imgurl, String title, String context, String username, String usericon) {
        this.disid = disid;
        this.imgurl = imgurl;
        this.title = title;
        this.context = context;
        this.username = username;
        this.usericon = usericon;
    }

    protected DissertationInfo(Parcel in) {
        disid = in.readInt();
        imgurl = in.readString();
        title = in.readString();
        context = in.readString();
        username = in.readString();
        usericon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(disid);
        dest.writeString(imgurl);
        dest.writeString(title);
        dest.writeString(context);
        dest.writeString(username);
        dest.writeString(usericon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DissertationInfo> CREATOR = new Creator<DissertationInfo>() {
        @Override
        public DissertationInfo createFromParcel(Parcel in) {
            return new DissertationInfo(in);
        }

        @Override
        public DissertationInfo[] newArray(int size) {
            return new DissertationInfo[size];
        }
    };

    @Override
    public String toString() {
        return "DissertationInfo{" +
                "disid=" + disid +
                ", imgurl='" + imgurl + '\'' +
                ", title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", username='" + username + '\'' +
                ", usericon='" + usericon + '\'' +
                '}';
    }

    public int getDisid() {
        return disid;
    }

    public void setDisid(int disid) {
        this.disid = disid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
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
