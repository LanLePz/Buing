package com.ahyapo.buing.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class IndexImages implements Parcelable {
    private int userid;
    private int imgid;
    private String imgurl;
    private int imgtype;
    private int likenum;
    private String imgtags;
    private String imgdescribe;
    private String username;
    private String usericon;
    private String imgcontext;
    private String imgtime;

    protected IndexImages(Parcel in) {
        userid = in.readInt();
        imgid = in.readInt();
        imgurl = in.readString();
        imgtype = in.readInt();
        likenum = in.readInt();
        imgtags = in.readString();
        imgdescribe = in.readString();
        username = in.readString();
        usericon = in.readString();
        imgcontext = in.readString();
        imgtime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userid);
        dest.writeInt(imgid);
        dest.writeString(imgurl);
        dest.writeInt(imgtype);
        dest.writeInt(likenum);
        dest.writeString(imgtags);
        dest.writeString(imgdescribe);
        dest.writeString(username);
        dest.writeString(usericon);
        dest.writeString(imgcontext);
        dest.writeString(imgtime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IndexImages> CREATOR = new Creator<IndexImages>() {
        @Override
        public IndexImages createFromParcel(Parcel in) {
            return new IndexImages(in);
        }

        @Override
        public IndexImages[] newArray(int size) {
            return new IndexImages[size];
        }
    };

    @Override
    public String toString() {
        return "IndexImages{" +
                "userid=" + userid +
                ", imgid=" + imgid +
                ", imgurl='" + imgurl + '\'' +
                ", imgtype=" + imgtype +
                ", likenum=" + likenum +
                ", imgtags='" + imgtags + '\'' +
                ", imgdescribe='" + imgdescribe + '\'' +
                ", username='" + username + '\'' +
                ", usericon='" + usericon + '\'' +
                ", imgcontext='" + imgcontext + '\'' +
                ", imgtime='" + imgtime + '\'' +
                '}';
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
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

    public int getImgtype() {
        return imgtype;
    }

    public void setImgtype(int imgtype) {
        this.imgtype = imgtype;
    }

    public int getLikenum() {
        return likenum;
    }

    public void setLikenum(int likenum) {
        this.likenum = likenum;
    }

    public String getImgtags() {
        return imgtags;
    }

    public void setImgtags(String imgtags) {
        this.imgtags = imgtags;
    }

    public String getImgdescribe() {
        return imgdescribe;
    }

    public void setImgdescribe(String imgdescribe) {
        this.imgdescribe = imgdescribe;
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

    public String getImgcontext() {
        return imgcontext;
    }

    public void setImgcontext(String imgcontext) {
        this.imgcontext = imgcontext;
    }

    public String getImgtime() {
        return imgtime;
    }

    public void setImgtime(String imgtime) {
        this.imgtime = imgtime;
    }

    public IndexImages(int userid, int imgid, String imgurl, int imgtype, int likenum, String imgtags, String imgdescribe, String username, String usericon, String imgcontext, String imgtime) {
        this.userid = userid;
        this.imgid = imgid;
        this.imgurl = imgurl;
        this.imgtype = imgtype;
        this.likenum = likenum;
        this.imgtags = imgtags;
        this.imgdescribe = imgdescribe;
        this.username = username;
        this.usericon = usericon;
        this.imgcontext = imgcontext;
        this.imgtime = imgtime;
    }
}
