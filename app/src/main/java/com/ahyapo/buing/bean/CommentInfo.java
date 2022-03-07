package com.ahyapo.buing.bean;

public class CommentInfo {
    private int commentid;
    private String comusername;
    private String comusericon;
    private String comtime;
    private String comcontext;
    private int comlikenum;

    public CommentInfo(int commentid, String comusername, String comusericon, String comtime, String comcontext, int comlikenum) {
        this.commentid = commentid;
        this.comusername = comusername;
        this.comusericon = comusericon;
        this.comtime = comtime;
        this.comcontext = comcontext;
        this.comlikenum = comlikenum;
    }

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public String getComusername() {
        return comusername;
    }

    public void setComusername(String comusername) {
        this.comusername = comusername;
    }

    public String getComusericon() {
        return comusericon;
    }

    public void setComusericon(String comusericon) {
        this.comusericon = comusericon;
    }

    public String getComtime() {
        return comtime;
    }

    public void setComtime(String comtime) {
        this.comtime = comtime;
    }

    public String getComcontext() {
        return comcontext;
    }

    public void setComcontext(String comcontext) {
        this.comcontext = comcontext;
    }

    public int getComlikenum() {
        return comlikenum;
    }

    public void setComlikenum(int comlikenum) {
        this.comlikenum = comlikenum;
    }
}
