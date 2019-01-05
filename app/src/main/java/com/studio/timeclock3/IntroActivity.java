package com.studio.timeclock3;


import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.fastaccess.datetimepicker.DatePickerFragmentDialog;
import com.fastaccess.datetimepicker.DateTimeBuilder;
import com.fastaccess.datetimepicker.TimePickerFragmentDialog;
import com.fastaccess.datetimepicker.callback.TimePickerCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import androidx.annotation.Nullable;
import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import io.github.dreierf.materialintroscreen.MessageButtonBehaviour;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;

public class IntroActivity extends MaterialIntroActivity implements TimePickerCallback {

    Integer day;
    Integer min;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        addSlide(new SlideFragmentBuilder()
                .image(R.mipmap.new_icon_foreground)
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .title("Welcome to TimeClock\n")
                .description("This App will track your Working Time \n\n Unfortunately it is in Alpha State and not recommended for everyday use\n\n Please be patient\n\n New Features and Bugfixes will be provided regularly")
                .build());


        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.blue)
                        .buttonsColor(R.color.colorAccent)
                        .title("Working Time")
                        .description("Please enter your daily working hours\n\n\n\n\n\n\n\n\n\n\n")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerFragmentDialog.newInstance(true).show(getSupportFragmentManager(), "TimePickerFragmentDialog");
//                        Calendar cal = Calendar.getInstance();
//                        TimePickerDialog tpd = new TimePickerDialog(IntroActivity.this, (view1, hourOfDay, minute) -> {
//                            Toast.makeText(IntroActivity.this, String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute), Toast.LENGTH_SHORT).show();
//                        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), DateFormat.is24HourFormat(IntroActivity.this));
//                        day = cal.get(Calendar.HOUR_OF_DAY);
//                        min = cal.get(Calendar.MINUTE);
//
//                        tpd.show()
                        ;}
                }, "SET"));





//
//        addSlide(new SlideFragmentBuilder()
//                        .backgroundColor(R.color.colorPrimary)
//                        .buttonsColor(R.color.colorAccent)
//                        .title("title 3")
//                        .description("Description 3")
//                        .build(),
//                new MessageButtonBehaviour(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showMessage("We provide solutions to make you love your work");
//                    }
//                }, "Work with love"));

    }

    @Override
    public void onTimeSet(long timeOnly, long dateWithTime) {
        Log.i("TAGGGGG", String.format("Full Date: %s\nTime Only: %s", getDateAndTime(dateWithTime), getTimeOnly(timeOnly)));
    }

    public static String getDateOnly(long time) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(time);
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
