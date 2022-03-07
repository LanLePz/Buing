package com.ahyapo.buing.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahyapo.buing.R;
import com.ahyapo.buing.adapter.CommentAdapter;
import com.ahyapo.buing.adapter.MessageAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.CommentInfo;
import com.ahyapo.buing.bean.DisItemInfo;
import com.ahyapo.buing.bean.MessageInfo;
import com.ahyapo.buing.bean.MineUserInfo;
import com.ahyapo.buing.glides.GlideApp;
import com.ahyapo.buing.glides.ProgressInterceptor;
import com.ahyapo.buing.glides.ProgressListener;
import com.ahyapo.buing.http.OkHttpHelper;
import com.ahyapo.buing.widget.SDFileHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class DisItemInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView dis_item_img,download,dis_comment,btn_close,back_index;
    private String IP_URL,POST_GET_IMG_COMMENT_URL,ADD_IMAGES_ACTION_URL; //请求数据所用URL
    private DisItemInfo disItemInfo;  //单个专题信息
    private ProgressBar download_progress;
    private TextView need_space,download_status,download_percent,com_num,cancel_download,user_wallet;
    private LinearLayout download_btn;
    private Dialog download_dialog,com_dialog; //定义下载弹窗及评论弹窗
    //评论列表相关参数
    private RecyclerView comment_list; //评论列表
    private CommentAdapter commentAdapter; //评论列表适配器
    private int count = 0; //计算评论数量
    private ArrayList<CommentInfo> commentInfos = new ArrayList<>(); //评论数据list
    //点赞
    private ShineButton btHeart;
    //下载资源
    SimpleTarget<Bitmap> simpleTarge;
    private MineUserInfo mineUserInfo;
    private String GET_MINE_USER_URL;
    private String token,user_tel;
    private FormBody.Builder getImageBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_item_info);
        initView();//初始化view
        initData();//初始化数据

    }
    private void initView() {
        dis_item_img = findViewById(R.id.dis_item_img);
        download = findViewById(R.id.download);
        download.setOnClickListener(this);
        dis_comment = findViewById(R.id.dis_comment);
        dis_comment.setOnClickListener(this);
        //点赞效果
        btHeart = (ShineButton) findViewById(R.id.btn_like);
        btHeart.init(this);
        btHeart.setOnClickListener(this);
        back_index = findViewById(R.id.back_index);
        back_index.setOnClickListener(this);

    }
    private void initData() {
        //请求数据
        final PublicData app = (PublicData)getApplication();
        IP_URL = app.getURL();
        //获取传过来的数据
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        disItemInfo = bundle.getParcelable("disinfo");
        Glide.with(this).load(app.getURL()+"/static/images"+disItemInfo.getDisimgurl()).dontAnimate().into(dis_item_img);
        //请求评论数据
        requestCommentInfoData();
        addBrowseImageActionNum(disItemInfo.getDisimgid());
        requestGetMineUserData();
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.download: //点击下载事件
                showDownLoadDialog();
                try {
                    //异步任务子线程获取网络图片大小，传递给UI线程更新数据
                    accessWSAction();//获取要下载的图片的大小
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.dis_comment: //点击评论
                Log.e("评论","dis_comment");
                showCommentDialog();//展示评论弹窗
                break;
            case R.id.btn_close: //关闭弹窗
                com_dialog.dismiss();
                break;
            case R.id.btn_like: //点赞
                Log.e("点赞状态",""+btHeart.isChecked());
                break;
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            case R.id.cancel_download:
                Log.e("取消下载","ok");
                simpleTarge.onStop();
                Glide.with(this).clear(simpleTarge);
                break;
        }
    }
    //评论弹窗
    private void showCommentDialog() {
        //初始化弹窗
        com_dialog = new Dialog(this,R.style.Comment_Dialog_Style);
        //设置弹窗布局
        View com_view = View.inflate(this,R.layout.dialog_comment,null);
        //设置弹窗padding为0，可宽度沾满屏幕
        Window window = com_dialog.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        //初始化弹窗大小、位置、弹出关闭动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.share_animation;
        //展示弹窗
        com_dialog.show();
        //添加弹窗view到主View
        com_dialog.setContentView(com_view, lp);
        // 设置点击对话框外部是否关闭对话框
        com_dialog.setCanceledOnTouchOutside(true);
        //绑定view
        comment_list = com_view.findViewById(R.id.comment_list);
        btn_close = com_view.findViewById(R.id.btn_close);
        com_num =com_view.findViewById(R.id.com_num);
        btn_close.setOnClickListener(this);
        //开启UI线程更新UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //设置布局管理
                comment_list.setLayoutManager(new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL){
                    @Override
                    public boolean canScrollVertically() {
                        return true;
                    }
                });
                //设置适配器
                commentAdapter = new CommentAdapter(DisItemInfoActivity.this, commentInfos,IP_URL);
                comment_list.setAdapter(commentAdapter);
                com_num.setText(""+count);
            }
        });

    }
    //请求评论数据
    private void requestCommentInfoData() {
        //数据请求接口URL
        POST_GET_IMG_COMMENT_URL = IP_URL+"/getCommentInfoByImgId/";
        //POST请求请求体
        FormBody.Builder imgIdBuilder = new FormBody.Builder();
        imgIdBuilder.add("com_img_id", String.valueOf(disItemInfo.getDisimgid()));
        OkHttpHelper.postRequest(POST_GET_IMG_COMMENT_URL, imgIdBuilder, new Callback() {
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
                        int comment_id = jsonObject.getInt("comment_id");
                        String com_user_name = jsonObject.getString("user_name");
                        String com_user_icon = jsonObject.getString("user_icon");
                        String com_context = jsonObject.getString("com_context");
                        String com_time = jsonObject.getString("com_time");
                        //设置日期格式
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //将字符串转为date对象
                        Date date1 = sdf.parse(com_time);
                        //日历，拥于获取date具体年月日时分秒
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date1);
                        Log.e("date1",""+date1);
                        //获取当前时间
                        Date nowdate = new Date();
                        //计算两个日期间隔
                        long daysBetween=(nowdate.getTime()-date1.getTime()+1000000)/(60*60*24*1000);
                        String time;
                        //大于一天展示月日，小于一天展示时分
                        if (daysBetween>=1){
                            time = (calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                        }else
                            time = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                        int com_like_num = jsonObject.getInt("com_like_num");
                        CommentInfo commentInfo = new CommentInfo(comment_id,com_user_name,com_user_icon,time,com_context,com_like_num);
                        count++; //记录评论个数
                        //添加数据到评论list
                        commentInfos.add(commentInfo);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });
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
                long result = getImageSize(IP_URL+"/static/images"+disItemInfo.getDisimgurl());;
//						activity_main_btn1.setText("请求结果为："+result);
                return result;
            }

        }.execute();

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
                downLoadImageAction(disItemInfo.getDisimgid());
                download_progress.setVisibility(View.VISIBLE);
                String imgurl = IP_URL + "/static/images" + disItemInfo.getDisimgurl();
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
                GlideApp.with(DisItemInfoActivity.this)
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
    /**
     * 获取指定文件大小 　　
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        Log.i("file.exists()：",""+file.exists());
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
    /**
     * 转换文件大小
     */
    public static String toFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
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



}