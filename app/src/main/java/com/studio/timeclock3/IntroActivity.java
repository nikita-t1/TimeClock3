package com.studio.timeclock3;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;

import androidx.annotation.Nullable;
import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity{

    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;
    public boolean isWorkingTimeSet = false;
    public boolean isPauseSet = false;
    public int amountPause= 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);     //Hides Navigation Bar


        mSharedPreferences = getSharedPreferences("", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();


        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);


        addSlide(new SlideFragmentBuilder()
                .image(R.mipmap.new_icon_foreground)
                .backgroundColor(R.color.green)
                .buttonsColor(R.color.colorAccent)
                .title("Welcome to TimeClock\n")
                .description("This App will track your Working Time \n\n Unfortunately it is in Alpha State and not recommended for everyday use\n\n Please be patient\n\n New Features and Bugfixes will be provided regularly")
                .build());


        addSlide(new IntroSlideWorkingTime());

        addSlide(new IntroSlidePauseTime());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.green)
                        .buttonsColor(R.color.colorAccent)
                        .neededPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .title("Storage Permission")
                        .description("This App needs the storage permission to save your working time")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("PORN");
                    }
                }, "Grant Permission"));


        // Storage Permission

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.white)
                        .buttonsColor(R.color.blue)
                        .title("To be continued...\n\n\n\n\n\n\n\n\n\n\n")
                        .build());
    }
}
