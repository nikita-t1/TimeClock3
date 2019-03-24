package com.studio.timeclock3.Intro;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.studio.timeclock3.MainActivity;
import com.studio.timeclock3.R;
import com.studio.timeclock3.SplashScreen;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;
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

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);


        //Einfacher Intro Bildschirm
        addSlide(new SlideFragmentBuilder()
                .image(R.mipmap.new_icon_foreground)    //Bild
                .backgroundColor(R.color.green)         //Hintergrundfarbe
                .buttonsColor(R.color.colorAccent)      //Button Farbe
                .title("Welcome to TimeClock\n")        //Text
                .description("This App will track your Working Time \n\n " +        //Beschreibung
                        "Unfortunately it is in Alpha State and not recommended for everyday use\n\n " +
                        "Please be patient\n\n New Features and Bugfixes will be provided regularly")
                .build());                              //Konstruktions Anweisung (Muss!) [Einfach immer drin lassen]


        addSlide(new IntroSlideExample());              //Selbstgemachter Bildschirm

        addSlide(new IntroSlideWorkingTime());

        addSlide(new IntroSlidePauseTime());

        //Intro Bildschirm mit Rechte Abfrage (zB. Interner Speicher, Kamera)
        // + anpassbarer Button
        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.green)
                        .buttonsColor(R.color.colorAccent)
                        .neededPermissions(new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,   //Berechtigung wird hier angefordert,...
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}) //... muss aber zudem in der Manifest aufgeführt sein
                        .title("Storage Permission")
                        .description("This App needs the storage permission to save your working time")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {     //Erstellen des eigegen Buttons
                    @Override
                    public void onClick(View v) {
                        showMessage("PORN");                                //Was passieren soll nach dem Klick
                    }
                }, "Grant Permission"));                    //Was auf dem Button steht



        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.white)
                        .buttonsColor(R.color.blue)
                        .title("To be continued...\n\n\n\n\n\n\n\n\n\n\n")
                        .build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        mEditor.putBoolean("isIntroFinished", true).commit();
        Intent mainIntent = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(mainIntent);

    }
}
