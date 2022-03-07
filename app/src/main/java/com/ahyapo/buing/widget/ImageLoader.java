package com.ahyapo.buing.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 用来加载图片并显示
 */
public class ImageLoader {
    private Context context;

    private int loadingImageRes;
    private int errorImageRes;

    public ImageLoader(Context context, int loadingImageRes, int errorImageRes) {
        this.context = context;
        this.loadingImageRes = loadingImageRes;
        this.errorImageRes = errorImageRes;
    }

    //用于缓存bitmap的容器对象
    private Map<String, Bitmap> cacheMap=new HashMap<String,Bitmap>();
    public void loadImage(String imagePath, ImageView imageView) {
        //根据url从一级缓存中取对应的bitmap对象
        //如果有显示（结束
        Bitmap bitmap=getFromFirstCache(imagePath);
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        //从二级缓存中查找
        bitmap=getFromSecondCache(imagePath);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            //缓存到一级缓存
            cacheMap.put(imagePath,bitmap);
            return;
        }
        //从三级缓存中查找
        //显示正在加载的图片

        loadBitmapFromThirdCache(imagePath,imageView);
    }

    //根据url从三级缓存中取对应的bitmap对象
    @SuppressLint("StaticFieldLeak")
    private void loadBitmapFromThirdCache(final String imagePath, final ImageView imageView) {
        new AsyncTask<Void,Void,Bitmap>(){

            @Override
            protected void onPreExecute() {
                imageView.setImageResource(loadingImageRes);
            }
            //联网请求得到bitmap对象
            @Override
            protected Bitmap doInBackground(Void... params) {

                Bitmap bitmap=null;
                try {
                    //得到连接
                    URL url=new URL(imagePath);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    //设置
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    //链接
                    connection.connect();
                    //发送请求读取返回的数据并封装成bitmap
                    int responseCode=connection.getResponseCode();
                    if (responseCode==200){
                        InputStream inputStream=connection.getInputStream();//图片文件流
                        //将is封装成bitmap
                        bitmap=BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        if (bitmap!=null){
                            //缓存到一级缓存
                            cacheMap.put(imagePath,bitmap);
                            //缓存到二级缓存
                            String filesPath=context.getExternalFilesDir(null).getAbsolutePath();
                            String fileName=imagePath.substring(imagePath.lastIndexOf("/"+1));//expo.jpg
                            String filePath=filesPath+"/"+fileName;
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(filePath));
                        }
                    }
                    connection.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                //显示错误图片
                if (bitmap==null){
                    imageView.setImageResource(errorImageRes);
                }else{
                    imageView.setImageBitmap(bitmap);
                }
            }
        }.execute();
    }
    //根据url从二级缓存中取对应的bitmap对象
    private Bitmap getFromSecondCache(String imagePath) {
        String filesPath=context.getExternalFilesDir(null).getAbsolutePath();
        String fileName=imagePath.substring(imagePath.lastIndexOf("/"+1));//expo.jpg
        String filePath=filesPath+"/"+fileName;
        return BitmapFactory.decodeFile(filePath);
    }

    //根据url从一级缓存中取对应的bitmap对象
    private Bitmap getFromFirstCache(String imagePath) {
        return cacheMap.get(imagePath);
    }
}
