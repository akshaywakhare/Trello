package com.example.track_trigger;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DashBoardActivity;
import com.example.project.R;

//import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, DashBoardActivity.class);

                startActivity(intent);
                finish();

            }
        }, 3000);
    }
}
