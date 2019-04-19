package com.example.ngouser;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(700);
        ImageView img = findViewById(R.id.activity_splash_brand_logo);
        TextView tv = findViewById(R.id.activity_splash_app_name);

        img.setAnimation(animation);
        tv.setAnimation(animation);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                final Intent gotoLoginScreen = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(gotoLoginScreen);
                finish();
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
