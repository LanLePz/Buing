package com.ahyapo.buing.bean;

public class ImageTags {
    private int class_id;
    private String class_img;
    private String class_name;

    public ImageTags(int class_id, String class_img, String class_name) {
        this.class_id = class_id;
        this.class_img = class_img;
        this.class_name = class_name;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_img() {
        return class_img;
    }

    public void setClass_img(String class_img) {
        this.class_img = class_img;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}
