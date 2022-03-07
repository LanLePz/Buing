package com.ahyapo.buing.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每一类专题内部图片信息bean类
 */
public class DisItemInfo implements Parcelable{
    private int disimgid;
    private int likenum;
    private String disimgurl;
    private String disusericon;
    private String disusername;

    public DisItemInfo(int disimgid, int likenum, String disimgurl, String disusericon, String disusername) {
        this.disimgid = disimgid;
        this.likenum = likenum;
        this.disimgurl = disimgurl;
        this.disusericon = disusericon;
        this.disusername = disusername;
    }

    protected DisItemInfo(Parcel in) {
        disimgid = in.readInt();
        likenum = in.readInt();
        disimgurl = in.readString();
        disusericon = in.readString();
        disusername = in.readString();
    }

    public static final Creator<DisItemInfo> CREATOR = new Creator<DisItemInfo>() {
        @Override
        public DisItemInfo createFromParcel(Parcel in) {
            return new DisItemInfo(in);
        }

        @Override
        public DisItemInfo[] newArray(int size) {
            return new DisItemInfo[size];
        }
    };

    @Override
    public String toString() {
        return "DisItemInfo{" +
                "disimgid=" + disimgid +
                ", likenum=" + likenum +
                ", disimgurl='" + disimgurl + '\'' +
                ", disusericon='" + disusericon + '\'' +
                ", disusername='" + disusername + '\'' +
                '}';
    }

    public int getDisimgid() {
        return disimgid;
    }

    public void setDisimgid(int disimgid) {
        this.disimgid = disimgid;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }

    public String getDisimgurl() {
        return disimgurl;
    }

    public void setDisimgurl(String disimgurl) {
        this.disimgurl = disimgurl;
    }

    public String getDisusericon() {
        return disusericon;
    }

    public void setDisusericon(String disusericon) {
        this.disusericon = disusericon;
    }

    public String getDisusername() {
        return disusername;
    }

    public void setDisusername(String disusername) {
        this.disusername = disusername;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(disimgid);
        dest.writeInt(likenum);
        dest.writeString(disimgurl);
        dest.writeString(disusericon);
        dest.writeString(disusername);
    }
}
