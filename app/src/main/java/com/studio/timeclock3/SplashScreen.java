package com.studio.timeclock3;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.studio.timeclock3.Intro.IntroActivity;

import java.util.logging.Logger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.splash_image) ImageView splash_image;
    @BindView(R.id.splash_text) TextView splash_text;
    long duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);
        this.getWindow().getDecorView().setSystemUiVisibility(1);

        Context context = SplashScreen.this;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        duration = preferences.getInt("splash_seekbar", 800);


        splash_image.animate().translationY(50f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
        splash_text.animate().translationY(-50f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preferences.contains("isIntroFinished")) {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    finish(); //Removes this Activity from Backstack
                    startActivity(mainIntent);
                    Animatoo.animateSlideLeft(context);

                } else {
                    Intent introIntent = new Intent(SplashScreen.this, IntroActivity.class);
                    finish();
                    startActivity(introIntent);
                }

            }
        },duration);
    }
}
