package com.ahyapo.buing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ahyapo.buing.R;
import com.ahyapo.buing.application.PublicData;
import com.ahyapo.buing.bean.LoginUserInfo;
import com.ahyapo.buing.http.OkHttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String POST_GET_CODE_URL,POST_LOGIN_USER_URL;
    private LinearLayout login_edit,login_other,login_title;
    private Button login_btn,get_verify;
    private EditText tel_number,verify_code;
    final PublicData publicData = (PublicData) getApplication();
    LoginUserInfo loginUserInfo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final PublicData app = (PublicData)getApplication();
        Log.i("URL",app.getURL());
        POST_GET_CODE_URL = app.getURL()+"/getcode/";
        POST_LOGIN_USER_URL = app.getURL()+"/login/";
        initView();
    }

    private void initView() {
        login_edit = findViewById(R.id.login_edit);
        login_other = findViewById(R.id.login_other);
        login_btn = findViewById(R.id.login_btn);
        tel_number = findViewById(R.id.tel_number);
        login_title = findViewById(R.id.login_title);
        verify_code = findViewById(R.id.verify_code);
        get_verify = findViewById(R.id.get_verify);
        Animation title_ani = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.login_scale_max);
        Animation other_ani = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.login_translate_bottom);
        login_title.startAnimation(title_ani);
        login_edit.startAnimation(other_ani);
        login_other.startAnimation(other_ani);
        login_btn.startAnimation(other_ani);
        get_verify.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_verify:
                FormBody.Builder getVerifyBuilder = new FormBody.Builder();
                getVerifyBuilder.add("tel",""+tel_number.getText());
                OkHttpHelper.postRequest(POST_GET_CODE_URL, getVerifyBuilder, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("onFailure ","失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("onResponse()",""+response.body().string());
                    }
                });
                break;
            case R.id.login_btn:
                /*Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();*/
                FormBody.Builder toLoginBuilder = new FormBody.Builder();
                toLoginBuilder.add("tel",""+tel_number.getText());
                toLoginBuilder.add("verifyCode",""+verify_code.getText());
                OkHttpHelper.postRequest(POST_LOGIN_USER_URL, toLoginBuilder, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("login ","失败"+verify_code.getText());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.e("login ",responseData);

                        try {
                            JSONArray imgjson = new JSONArray(responseData);
                            for(int i = 0;i<imgjson.length();i++){
                                JSONObject jsonObject = imgjson.getJSONObject(i);
                                String msg = jsonObject.getString("msg");
                                String token = jsonObject.getString("token");
                                String user_tel = jsonObject.getString("user_tel");
                                loginUserInfo = new LoginUserInfo(user_tel,msg,token);
                                Log.e("Message",loginUserInfo.getMsg());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("MessageL",loginUserInfo.getMsg());

                        if(loginUserInfo.getMsg().equals("success")){
                            SharedPreferences login_sharedata = getSharedPreferences("login_token", Context.MODE_PRIVATE);
                            login_sharedata.edit().putString("tel",""+loginUserInfo.getUsertel())
                                    .putString("token",""+loginUserInfo.getToken())
                                    .commit();
                            //publicData.setSharedPreferences(sharedPreferences);
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this, "手机号/验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
                break;
        }
    }

}