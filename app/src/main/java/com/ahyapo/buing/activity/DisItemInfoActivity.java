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
    private String IP_URL,POST_GET_IMG_COMMENT_URL,ADD_IMAGES_ACTION_URL; //??????????????????URL
    private DisItemInfo disItemInfo;  //??????????????????
    private ProgressBar download_progress;
    private TextView need_space,download_status,download_percent,com_num,cancel_download,user_wallet;
    private LinearLayout download_btn;
    private Dialog download_dialog,com_dialog; //?????????????????????????????????
    //????????????????????????
    private RecyclerView comment_list; //????????????
    private CommentAdapter commentAdapter; //?????????????????????
    private int count = 0; //??????????????????
    private ArrayList<CommentInfo> commentInfos = new ArrayList<>(); //????????????list
    //??????
    private ShineButton btHeart;
    //????????????
    SimpleTarget<Bitmap> simpleTarge;
    private MineUserInfo mineUserInfo;
    private String GET_MINE_USER_URL;
    private String token,user_tel;
    private FormBody.Builder getImageBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dis_item_info);
        initView();//?????????view
        initData();//???????????????

    }
    private void initView() {
        dis_item_img = findViewById(R.id.dis_item_img);
        download = findViewById(R.id.download);
        download.setOnClickListener(this);
        dis_comment = findViewById(R.id.dis_comment);
        dis_comment.setOnClickListener(this);
        //????????????
        btHeart = (ShineButton) findViewById(R.id.btn_like);
        btHeart.init(this);
        btHeart.setOnClickListener(this);
        back_index = findViewById(R.id.back_index);
        back_index.setOnClickListener(this);

    }
    private void initData() {
        //????????????
        final PublicData app = (PublicData)getApplication();
        IP_URL = app.getURL();
        //????????????????????????
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        disItemInfo = bundle.getParcelable("disinfo");
        Glide.with(this).load(app.getURL()+"/static/images"+disItemInfo.getDisimgurl()).dontAnimate().into(dis_item_img);
        //??????????????????
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
                Log.e("onFailure ","??????");
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
                Log.e("onFailure ","??????");
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
                Log.e("onFailure ","??????");
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
            case R.id.download: //??????????????????
                showDownLoadDialog();
                try {
                    //?????????????????????????????????????????????????????????UI??????????????????
                    accessWSAction();//?????????????????????????????????
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.dis_comment: //????????????
                Log.e("??????","dis_comment");
                showCommentDialog();//??????????????????
                break;
            case R.id.btn_close: //????????????
                com_dialog.dismiss();
                break;
            case R.id.btn_like: //??????
                Log.e("????????????",""+btHeart.isChecked());
                break;
            case R.id.back_index:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            case R.id.cancel_download:
                Log.e("????????????","ok");
                simpleTarge.onStop();
                Glide.with(this).clear(simpleTarge);
                break;
        }
    }
    //????????????
    private void showCommentDialog() {
        //???????????????
        com_dialog = new Dialog(this,R.style.Comment_Dialog_Style);
        //??????????????????
        View com_view = View.inflate(this,R.layout.dialog_comment,null);
        //????????????padding???0????????????????????????
        Window window = com_dialog.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        //???????????????????????????????????????????????????
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.share_animation;
        //????????????
        com_dialog.show();
        //????????????view??????View
        com_dialog.setContentView(com_view, lp);
        // ????????????????????????????????????????????????
        com_dialog.setCanceledOnTouchOutside(true);
        //??????view
        comment_list = com_view.findViewById(R.id.comment_list);
        btn_close = com_view.findViewById(R.id.btn_close);
        com_num =com_view.findViewById(R.id.com_num);
        btn_close.setOnClickListener(this);
        //??????UI????????????UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //??????????????????
                comment_list.setLayoutManager(new StaggeredGridLayoutManager(1,RecyclerView.VERTICAL){
                    @Override
                    public boolean canScrollVertically() {
                        return true;
                    }
                });
                //???????????????
                commentAdapter = new CommentAdapter(DisItemInfoActivity.this, commentInfos,IP_URL);
                comment_list.setAdapter(commentAdapter);
                com_num.setText(""+count);
            }
        });

    }
    //??????????????????
    private void requestCommentInfoData() {
        //??????????????????URL
        POST_GET_IMG_COMMENT_URL = IP_URL+"/getCommentInfoByImgId/";
        //POST???????????????
        FormBody.Builder imgIdBuilder = new FormBody.Builder();
        imgIdBuilder.add("com_img_id", String.valueOf(disItemInfo.getDisimgid()));
        OkHttpHelper.postRequest(POST_GET_IMG_COMMENT_URL, imgIdBuilder, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure ","??????");
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
                        //??????????????????
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //??????????????????date??????
                        Date date1 = sdf.parse(com_time);
                        //?????????????????????date????????????????????????
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date1);
                        Log.e("date1",""+date1);
                        //??????????????????
                        Date nowdate = new Date();
                        //????????????????????????
                        long daysBetween=(nowdate.getTime()-date1.getTime()+1000000)/(60*60*24*1000);
                        String time;
                        //???????????????????????????????????????????????????
                        if (daysBetween>=1){
                            time = (calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
                        }else
                            time = calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);
                        int com_like_num = jsonObject.getInt("com_like_num");
                        CommentInfo commentInfo = new CommentInfo(comment_id,com_user_name,com_user_icon,time,com_context,com_like_num);
                        count++; //??????????????????
                        //?????????????????????list
                        commentInfos.add(commentInfo);
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //??????????????????????????????
    private void accessWSAction() {
        new AsyncTask<String, Void, Object>() {

            //???doInBackground ??????????????????onPostExecute ????????????UI ???????????????
            // ????????????????????????????????????????????????UI ??????????????????????????????????????????.
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                //activity_main_btn1.setText("??????????????????"+result);//????????????UI
                Log.i("????????????size",""+result);
                //??????????????????
                String imgsize = toFileSize((Long) result);
                need_space.setText(""+imgsize);
            }
            //?????????????????????????????????????????????????????????????????????UI???UI??????????????????
            protected Object doInBackground(String... params) {
                long result = getImageSize(IP_URL+"/static/images"+disItemInfo.getDisimgurl());;
//						activity_main_btn1.setText("??????????????????"+result);
                return result;
            }

        }.execute();

    }
    //????????????
    private void showDownLoadDialog() {
        download_dialog = new Dialog(this, R.style.Download_Dialog_Style);
        View view = View.inflate(this, R.layout.dialog_download, null);
        WindowManager.LayoutParams lp = download_dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.share_animation;
        download_dialog.setContentView(view, lp);
        // ????????????????????????????????????????????????
        download_dialog.setCanceledOnTouchOutside(true);
        download_progress = view.findViewById(R.id.download_progress);
        //????????????
        need_space = view.findViewById(R.id.need_space);
        //????????????
        cancel_download = view.findViewById(R.id.cancel_download);
        cancel_download.setOnClickListener(this);
        //????????????
        download_status = view.findViewById(R.id.download_status);
        //???????????????
        download_percent = view.findViewById(R.id.download_percent);
        //??????
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
                                download_status.setText("????????????...");
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
                            //Bitmap?????????byte[]
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
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//???????????????
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
//        Log.i("????????????imagesize",""+imagesize);
        return size;
    }
    /**
     * ???????????????????????? ??????
     */
    public static long getFileSize(File file) throws Exception {
        long size = 0;
        Log.i("file.exists()???",""+file.exists());
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            Log.e("??????????????????", "???????????????!");
        }
        return size;
    }
    /**
     * ??????????????????
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
    //???SD????????????????????????
    public void savaFileToSD(String filename, byte[] bytes) throws Exception {
        //?????????????????????sd???,???app????????????sd????????????
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory() + File.separator + "BuingImages";//??????????????????????????????
            File dir1 = new File(filePath);
            if (!dir1.exists()){
                dir1.mkdirs();
            }
            filename = filePath+ "/" + filename;
            //??????????????????openFileOutput???,???????????????????????????????????????
            FileOutputStream output = new FileOutputStream(filename);
            output.write(bytes);
            //???bytes?????????????????????
            output.close();
            //???????????????
            download_progress.setVisibility(View.INVISIBLE);
            download_dialog.dismiss();
            Toast.makeText(this, "????????????????????????"+filePath, Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "SD??????????????????????????????", Toast.LENGTH_SHORT).show();
    }



}