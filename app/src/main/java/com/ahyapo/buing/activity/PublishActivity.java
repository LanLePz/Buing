package com.ahyapo.buing.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ahyapo.buing.R;
import com.ahyapo.buing.adapter.CommentAdapter;
import com.ahyapo.buing.adapter.GVAdapter;
import com.ahyapo.buing.adapter.TagAdapter;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.CommentInfo;
import com.ahyapo.buing.bean.ImageTags;
import com.ahyapo.buing.http.OkHttpH;
import com.ahyapo.buing.http.OkHttpHelper;
import com.ahyapo.buing.view.CustomDialog;
import com.ahyapo.buing.view.TagListView;
import com.ahyapo.buing.widget.GlideLoader;
import com.ahyapo.buing.widget.Tag;
import com.bumptech.glide.Glide;
import com.lcw.library.imagepicker.ImagePicker;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int IMG_COUNT = 10;
    private static final String IMG_ADD_TAG = "a";
    private EditText edit_img_name,edit_img_context;
    private TextView edit_text_num_name,edit_text_num_context,cancel,publish,class_tags;
    private ToggleButton up_down;
    private RecyclerView tag_container;
    private String IP_URL;
    private TagAdapter tagAdapter;
    private GVAdapter gvAdapter;
    private List<String> img_list = new ArrayList<>();
    private ImageView picture,loading;
    private LinearLayout null_view;
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private ArrayList<String> mImagePaths;
    private ArrayList<ImageTags> imageTagsArrayList = new ArrayList<>();
    private String tel,token;
    private final MediaType mediaType=MediaType.parse("image/png");
    private File f;
    private CustomDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        //请求数据
        final PublicData app = (PublicData)getApplication();
        IP_URL = app.getURL();
        SharedPreferences sharedPreferences = this.getSharedPreferences("login_token", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token",null);
        tel = sharedPreferences.getString("tel",null);
        initView();
        initData();
    }
    private void initData() {
        edit_img_name.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int num = s.length();
                edit_text_num_name.setText(num+"/50");
            }
        });
        edit_img_context.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                int num = s.length();
                edit_text_num_context.setText(num+"/140");
            }
        });
        up_down.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    requestGetImageTags();
                    tag_container.setVisibility(View.VISIBLE);
                }else{
                    tag_container.setVisibility(View.GONE);
                }
            }
        });

    }
    private void refreshAdapter() {
        if (img_list == null) {
            img_list = new ArrayList<>();
        }
        if (gvAdapter == null) {
            gvAdapter = new GVAdapter(img_list,this);
        }
        gvAdapter.notifyDataSetChanged();
    }
    private void initView() {
        edit_img_name = findViewById(R.id.edit_img_name);
        edit_img_context = findViewById(R.id.edit_img_context);
        edit_text_num_name = findViewById(R.id.edit_text_num_name);
        edit_text_num_context = findViewById(R.id.edit_text_num_context);
        cancel = findViewById(R.id.cancel);
        publish = findViewById(R.id.publish);
        cancel.setOnClickListener(this);
        publish.setOnClickListener(this);

        up_down = findViewById(R.id.up_down);
        tag_container = findViewById(R.id.tag_container);
        class_tags = findViewById(R.id.class_tags);
        picture = findViewById(R.id.picture);
        null_view = findViewById(R.id.null_view);
        null_view.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            case  R.id.null_view:
                ImagePicker.getInstance()
                        .setTitle("标题")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .filterGif(false)//设置是否过滤gif图片
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setImagePaths(mImagePaths)//设置历史选择记录
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(PublishActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
                break;
            case R.id.publish:
                if (mImagePaths == null ||String.valueOf(class_tags.getText()).equals("")||String.valueOf(edit_img_name.getText()).equals("")||String.valueOf(edit_img_context.getText()).equals("")){
                    Toast.makeText(PublishActivity.this, "完善信息才能上传哦~",Toast.LENGTH_SHORT).show();
                }else{
                    dialog = new CustomDialog(PublishActivity.this);
                    dialog.show();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            requestUploadImage();
                        }
                    },10);
                }
                break;
        }
    }

    private void requestUploadImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(mImagePaths.get(0));
        File file = convertBitmapToFile(bitmap);
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)//tel,token,img_tags,img_describe,img_context;
                .addFormDataPart("file", file.getName()+".jpg", RequestBody.create(MediaType.parse("image/png"), file))
                .addFormDataPart("tel",tel)
                .addFormDataPart("token",token)
                .addFormDataPart("img_tags", String.valueOf(class_tags.getText()))
                .addFormDataPart("img_describe", String.valueOf(edit_img_name.getText()))
                .addFormDataPart("img_context", String.valueOf(edit_img_context.getText()))
                .build();
        OkHttpH.postUpLoadRequest(IP_URL + "/uploadImageByUser/", requestBody, new com.squareup.okhttp.Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("onFailure ","失败");
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                String responseData = response.body().string();
                Log.e("上传成功 ",responseData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("success")){
                            dialog.dismiss();
                            Toast.makeText(PublishActivity.this, "上传成功！",Toast.LENGTH_SHORT).show();
                            PublishActivity.this.finish();
                            overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                        }else{
                            dialog.dismiss();
                            Toast.makeText(PublishActivity.this, "上传失败，请重新上传！",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            picture.setVisibility(View.VISIBLE);
            null_view.setVisibility(View.GONE);
            Glide.with(this).load(mImagePaths.get(0)).dontAnimate().into(picture);
        }
    }
    private void requestGetImageTags() {
        OkHttpHelper.getRequest(IP_URL + "/getImageTags/", new Callback() {
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
                        int class_id = jsonObject.getInt("class_id");
                        String class_img = jsonObject.getString("class_img");
                        String class_name = jsonObject.getString("class_name");
                        ImageTags imageTags = new ImageTags(class_id,class_img,class_name);
                        imageTagsArrayList.add(imageTags);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置布局管理
                        tag_container.setLayoutManager(new GridLayoutManager(PublishActivity.this, 5){
                            @Override
                            public boolean canScrollVertically() {
                                return true;
                            }
                        });
                        //设置适配器
                        tagAdapter = new TagAdapter(PublishActivity.this, imageTagsArrayList,class_tags);
                        tag_container.setAdapter(tagAdapter);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null && !this.isFinishing()) {
            dialog.dismiss();
        }
    }

    private File convertBitmapToFile(Bitmap bitmap) {
        try {
            Random random = new Random();
            // create a file to write bitmap data
            f = new File(PublishActivity.this.getCacheDir(), "buing"+123456*random.nextInt(10000));
            f.createNewFile();
            // convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            // write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {

        }
        return f;
    }
}