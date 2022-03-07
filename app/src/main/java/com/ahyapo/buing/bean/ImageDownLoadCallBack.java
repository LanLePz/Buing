package com.ahyapo.buing.bean;

import java.io.File;

public interface ImageDownLoadCallBack {
 
    void onDownLoadSuccess(File file);
 
    void onDownLoadFailed();
}