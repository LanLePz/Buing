package com.ahyapo.buing.http;



import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpHelper {
       public static void getRequest(String url, Callback callback){
           OkHttpClient client = new OkHttpClient();
                   //.Builder().connectTimeout(1, TimeUnit.SECONDS);
           Request request = new Request.Builder()
                   .url(url)
                   .get()
                   .build();
           client.newCall(request).enqueue(callback);
       }
       public static void postRequest(String url, FormBody.Builder builder,Callback callback){
           OkHttpClient client = new OkHttpClient();
           FormBody formBody = builder.build();
           Request request = new Request.Builder()
                   .url(url)
                   .post(formBody)
                   .build();
           client.newCall(request).enqueue(callback);
       }

}
