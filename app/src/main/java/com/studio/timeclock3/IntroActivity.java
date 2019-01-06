package com.studio.timeclock3;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;
import android.widget.Toast;


import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.DateTimeBuilder;
import com.fastaccess.datetimepicker.TimePickerFragmentDialog;
import com.fastaccess.datetimepicker.callback.DatePickerCallback;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import adil.dev.lib.materialnumberpicker.dialog.NumberPickerDialog;
import androidx.annotation.Nullable;
import es.dmoral.toasty.Toasty;
import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity implements TimePickerCallback{

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
                .backgroundColor(R.color.amber)
                .buttonsColor(R.color.colorAccent)
                .title("Welcome to TimeClock\n")
                .description("This App will track your Working Time \n\n Unfortunately it is in Alpha State and not recommended for everyday use\n\n Please be patient\n\n New Features and Bugfixes will be provided regularly")
                .build());


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.green)
                        .buttonsColor(R.color.colorAccent)
                        .title("Working Time")
                        .description("Please enter your daily working hours\n\n\n\n\n\n\n\n\n\n\n")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerFragmentDialog.newInstance(true).show(getSupportFragmentManager(), "TimePickerFragmentDialog");
                    }

                }, "SET"));

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.blue)
                        .buttonsColor(R.color.colorAccent)
                        .description("Please enter the amount of breaks you have per day\n\n\n\n\n\n\n\n\n\n\n")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumberPickerDialog dialog = new NumberPickerDialog(IntroActivity.this, 0, 10, new NumberPickerDialog.NumberPickerCallBack() {
                            @Override
                            public void onSelectingValue(int value) {
                                Toast.makeText(IntroActivity.this, "Selected " + String.valueOf(value), Toast.LENGTH_SHORT).show();
                                amountPause = value;
                            }
                        });
                        dialog.show();
                        ;
                    }
                }, "SET"));

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.blue)
                        .buttonsColor(R.color.colorAccent)
                        .title("To be continued...\n\n\n\n\n\n\n\n\n\n\n")
                        .build());
    }

    @Override
    public void onTimeSet(long timeOnly, long dateWithTime) {
        Log.i("TAGGGGG", String.format("Full Date: %s\nTime Only: %s", getDateAndTime(dateWithTime), getTimeOnly(timeOnly)));
        mEditor.putString("workingTime", String.valueOf(timeOnly));
        mEditor.apply();

        Log.i("TAG", String.valueOf(timeOnly));
        Toast.makeText(this, getTimeOnly(timeOnly), Toast.LENGTH_LONG).show();
        SimpleDateFormat sample = new SimpleDateFormat("hh:mm");
        Log.i("INFFOOO", sample.format(timeOnly));
    }

    public static String getDateAndTime(long time) {
        SimpleDateFormat sample = new SimpleDateFormat("dd MMM yyyy, hh:mma", Locale.getDefault());
        return sample.format(new Date(time));
    }

    public static String getTimeOnly(long time) {
        SimpleDateFormat sample = new SimpleDateFormat("hh:mma", Locale.getDefault());
        return sample.format(time);
    }
}
