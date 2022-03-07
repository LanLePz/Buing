package com.ahyapo.buing.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FindClass implements Parcelable {
    private int id;
    private String img;
    private String name;

    public FindClass(int id, String img, String name) {
        this.id = id;
        this.img = img;
        this.name = name;
    }

    protected FindClass(Parcel in) {
        id = in.readInt();
        img = in.readString();
        name = in.readString();
    }

    public static final Creator<FindClass> CREATOR = new Creator<FindClass>() {
        @Override
        public FindClass createFromParcel(Parcel in) {
            return new FindClass(in);
        }

        @Override
        public FindClass[] newArray(int size) {
            return new FindClass[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FindClass{" +
                "id=" + id +
                ", img='" + img + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(img);
        dest.writeString(name);
    }
}
