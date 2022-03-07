package com.ahyapo.buing.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ahyapo.buing.R;
import com.ahyapo.buing.widget.CircleButton;

public class WelActivity extends AppCompatActivity {

    private ImageView camera;
    private CircleButton enter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);
        //init();
        SharedPreferences sharedPreferences = getSharedPreferences("login_token", Context.MODE_PRIVATE);
        /*SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();*/
        String user_tel = sharedPreferences.getString("tel",null);
        String token = sharedPreferences.getString("token",null);
        Log.e("wel sharedPreferences",user_tel+" and "+token);
        if (user_tel==null||token==null){
            init();
        }else{
            Intent intent = new Intent(WelActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void init() {
        Animation camera_ani = AnimationUtils.loadAnimation(WelActivity.this,R.anim.wel_rotate);
        camera = findViewById(R.id.camera);
        camera.startAnimation(camera_ani);
        Animation enter_ani = AnimationUtils.loadAnimation(WelActivity.this,R.anim.wel_translate_left);
        enter = findViewById(R.id.enter);
        enter.startAnimation(enter_ani);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}