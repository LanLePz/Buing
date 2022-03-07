package com.ahyapo.buing.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.ahyapo.buing.R;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.ImagesInfoItem;
import com.ahyapo.buing.bean.IndexImages;
import com.ahyapo.buing.bean.MineUserInfo;
import com.ahyapo.buing.glides.GlideApp;
import com.ahyapo.buing.glides.ProgressInterceptor;
import com.ahyapo.buing.glides.ProgressListener;
import com.ahyapo.buing.http.OkHttpHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

import static com.ahyapo.buing.activity.DisItemInfoActivity.toFileSize;

public class IndexInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout index_info_card;
    private ImageView info_img;
    private CircleImageView user_icon;
    private TextView user_name;
    private TextView img_describe,user_wallet;
    private TextView img_context,download_img;
    private ImageView back_index;
    private PhotoView picture;
    private String IP_URL,DIALOG_IMG,ADD_IMAGES_ACTION_URL;
    private ProgressBar download_progress;
    private TextView need_space,download_status,download_percent,cancel_download;
    ImagesInfoItem imagesInfoItem;
    private String GET_MINE_USER_URL;
    private String token,user_tel;
    private FormBody.Builder getImageBuilder;
    private LinearLayout download_btn;
    private ArrayList<ImagesInfoItem> imagesInfoItems = new ArrayList<>();
    //下载资源
    SimpleTarget<Bitmap> simpleTarge;
    private Dialog download_dialog;
    IndexImages indexImages;
    private MineUserInfo mineUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_info);
        initView();
        initData();

    }

    private void initData() {
        //请求数据
        final PublicData app = (PublicData)getApplication();
        IP_URL = app.getURL();
        //获取传过来的图片数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        indexImages = (IndexImages) bundle.getParcelable("imginfo");
        Log.e("imgId:",indexImages.toString());
        DIALOG_IMG = indexImages.getImgurl();
        Glide.with(com.ahyapo.buing.activity.IndexInfoActivity.this).load(app.getURL()+"/static/images"+indexImages.getImgurl()).dontAnimate().into(info_img);
        Glide.with(com.ahyapo.buing.activity.IndexInfoActivity.this).load(app.getURL()+"/static/icons"+indexImages.getUsericon()).dontAnimate().into(user_icon);
        user_name.setText(indexImages.getUsername());
        img_describe.setText(indexImages.getImgdescribe());
        img_context.setText(indexImages.getImgcontext());
        addBrowseImageActionNum(indexImages.getImgid());
        requestGetMineUserData();
    }

    private void addBrowseImageActionNum(int imgId) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        String user_tel = sharedPreferences.getString("tel",null);
        FormBody.Builder addActionBuilder = new FormBody.Builder();
        addActionBuilder.add("tel",user_tel);
        addActionBuilder.add("token",token);
        addActionBuilder.add("img_id", String.valueOf(imgId));
        ADD_IMAGES_ACTION_URL = IP_URL+"/addBrowseImageActionNum/";
        OkHttpHelper.postRequest(ADD_IMAGES_ACTION_URL, addActionBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure ","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("onResponse()",""+response.body().string());
            }
        });
    }
    private void requestGetMineUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        user_tel = sharedPreferences.getString("tel",null);
        getImageBuilder = new FormBody.Builder();
        getImageBuilder.add("tel",user_tel);
        getImageBuilder.add("token",token);
        GET_MINE_USER_URL = IP_URL + "/getMineUserInfo/";
        OkHttpHelper.postRequest(GET_MINE_USER_URL, getImageBuilder,new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure ","失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                System.out.println(responseData);
                try {
                    JSONArray imgjson = new JSONArray(responseData);
                    for(int i = 0;i<imgjson.length();i++){
                        JSONObject jsonObject = imgjson.getJSONObject(i);
                        int user_wallet = jsonObject.getInt("user_wallet");
                        int user_follows = jsonObject.getInt("follows");
                        int user_fans = jsonObject.getInt("fans");
                        String user_name = jsonObject.getString("user_name");
                        String user_icon = jsonObject.getString("user_icon");
                        String user_sign = jsonObject.getString("user_sign");
                        int product = jsonObject.getInt("product");
                        mineUserInfo = new MineUserInfo(user_icon,user_name,user_sign,user_wallet,user_follows,user_fans,product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void downLoadImageAction(int imgId) {
        SharedPreferences sharedPreferences = getSharedPreferences("login_token", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        String user_tel = sharedPreferences.getString("tel",null);
        FormBody.Builder addActionBuilder = new FormBody.Builder();
        addActionBuilder.add("tel",user_tel);
        addActionBuilder.add("token",token);
        addActionBuilder.add("img_id", String.valueOf(imgId));
        ADD_IMAGES_ACTION_URL = IP_URL+"/downLoadImageAction/";
        OkHttpHelper.postRequest(ADD_IMAGES_ACTION_URL, addActionBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure ","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("onResponse()",""+response.body().string());
            }
        });
    }
    private void initView() {
        index_info_card = findViewById(R.id.index_info_card);
        back_index = findViewById(R.id.back_index);
        info_img = findViewById(R.id.info_img);
        info_img.setOnClickListener(this);
        //info_img.isEnabled();
        user_icon = findViewById(R.id.user_icon);
        user_name = findViewById(R.id.user_name);
        img_describe = findViewById(R.id.img_describe);
        img_context = findViewById(R.id.img_context);
        Animation project_ani = AnimationUtils.loadAnimation(this,R.anim.translate_indexinfo);
        index_info_card.setAnimation(project_ani);
        back_index.setOnClickListener(this);
        download_img = findViewById(R.id.download_img);
        download_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            case R.id.info_img:
                Log.i("哈哈哈","我被点了");
                showImageDialog();
                break;
            case R.id.download_img:
                Log.i("下载","我被点了");
                showDownLoadDialog();
                try {
                    //异步任务子线程获取网络图片大小，传递给UI线程更新数据
                    accessWSAction();//获取要下载的图片的大小
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    //下载弹窗
    private void showDownLoadDialog() {
        download_dialog = new Dialog(this, R.style.Download_Dialog_Style);
        View view = View.inflate(this, R.layout.dialog_download, null);
        WindowManager.LayoutParams lp = download_dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.share_animation;
        download_dialog.setContentView(view, lp);
        // 设置点击对话框外部是否关闭对话框
        download_dialog.setCanceledOnTouchOutside(true);
        download_progress = view.findViewById(R.id.download_progress);
        //需要空间
        need_space = view.findViewById(R.id.need_space);
        //取消下载
        cancel_download = view.findViewById(R.id.cancel_download);
        cancel_download.setOnClickListener(this);
        //下载状态
        download_status = view.findViewById(R.id.download_status);
        //下载百分比
        download_percent = view.findViewById(R.id.download_percent);
        //下载
        download_btn = view.findViewById(R.id.download_btn);
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downLoadImageAction(indexImages.getImgid());
                download_progress.setVisibility(View.VISIBLE);
                String imgurl = IP_URL + "/static/images" + indexImages.getImgurl();
                ProgressInterceptor.addListener(imgurl, new ProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                download_status.setText("正在下载...");
                                download_percent.setText(progress+"% / ");
                            }
                        });
                        Log.e("onProgress:",""+progress);
                        download_progress.setProgress(progress);
                    }
                });
                simpleTarge = new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            //Bitmap转换成byte[]
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] bytes = baos.toByteArray();
                            savaFileToSD("buingimages"+Math.random()*123456+".jpeg",bytes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        download_dialog.dismiss();
                    }

                };
                GlideApp.with(IndexInfoActivity.this)
                        .asBitmap()
                        .load(imgurl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存
                        .skipMemoryCache(true)
                        .into(simpleTarge);
            }
        });
        user_wallet = view.findViewById(R.id.user_wallet);
        user_wallet.setText(""+mineUserInfo.getUser_wallet());
        download_dialog.show();
    }
    //往SD卡写入文件的方法
    public void savaFileToSD(String filename, byte[] bytes) throws Exception {
        //如果手机已插入sd卡,且app具有读写sd卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory() + File.separator + "BuingImages";//自定义图片保存的位置
            File dir1 = new File(filePath);
            if (!dir1.exists()){
                dir1.mkdirs();
            }
            filename = filePath+ "/" + filename;
            //这里就不要用openFileOutput了,那个是往手机内存中写数据的
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            //将bytes写入到输出流中
            output.close();
            //关闭输出流
            download_progress.setVisibility(View.INVISIBLE);
            download_dialog.dismiss();
            Toast.makeText(this, "图片已成功保存到"+filePath, Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "SD卡不存在或者不可读写", Toast.LENGTH_SHORT).show();
    }
    //获取要下载图片的大小
    private void accessWSAction() {
        new AsyncTask<String, Void, Object>() {

            //在doInBackground 执行完成后，onPostExecute 方法将被UI 线程调用，
            // 后台的计算结果将通过该方法传递到UI 线程，并且在界面上展示给用户.
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                //activity_main_btn1.setText("请求结果为："+result);//可以更新UI
                Log.i("主线程中size",""+result);
                //计算图片大小
                String imgsize = toFileSize((Long) result);
                need_space.setText(""+imgsize);
            }
            //该方法运行在后台线程中，因此不能在该线程中更新UI，UI线程为主线程
            protected Object doInBackground(String... params) {
                long result = getImageSize(IP_URL+"/static/images"+indexImages.getImgurl());;
//						activity_main_btn1.setText("请求结果为："+result);
                return result;
            }

        }.execute();

    }
    private long getImageSize(String imgurl){
        long size;
        String imagesize;
        URL url = null;
        try {
            url = new URL(imgurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        size = conn.getContentLength();
        Log.i("long size",""+size);
//        imagesize = toFileSize(size);
//        Log.i("子线程中imagesize",""+imagesize);
        return size;
    }
    private void showImageDialog() {
        final Dialog dialog = new Dialog(this, R.style.Comment_Dialog_Style);
        View view = View.inflate(this, R.layout.picture_show, null);
        //设置弹窗padding为0，可宽度沾满屏幕
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.share_animation;
        dialog.setContentView(view, lp);
        // 设置点击对话框外部是否关闭对话框
        dialog.setCanceledOnTouchOutside(true);
        picture=view.findViewById(R.id.picture);
        Glide.with(com.ahyapo.buing.activity.IndexInfoActivity.this).load(IP_URL+"/static/images"+DIALOG_IMG).into(picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}