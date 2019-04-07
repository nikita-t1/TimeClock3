package com.studio.timeclock3.Intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import com.orhanobut.logger.Logger;
import com.studio.timeclock3.R;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import io.github.dreierf.materialintroscreen.SlideFragment;

public class IntroSlideWorkingTime extends SlideFragment {

    private TimePicker timePicker;
    public Button setButton;
    private boolean isButtonClicked = false;

    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_slide_time_picker, container, false);

        mSharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();


        timePicker = (TimePicker) view.findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);

        timePicker.setScaleX(1.3f);
        timePicker.setScaleY(1.3f);

        setButton = (Button) view.findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Je nachdem wie die Firma rundet, Abweichung von 30sec möglich
                int hour, minute;
                hour = timePicker.getHour();
                minute = timePicker.getMinute();

                isButtonClicked = true;

                Long WORKING_TIME_MIN = (long)hour * 60 + minute;
                Logger.i("WORKING_TIME_MIN:" + WORKING_TIME_MIN);

                mEditor.putLong("WORKING_TIME_MIN", WORKING_TIME_MIN).apply();
            }
        });

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.green;
    }

    @Override
    public boolean canMoveFurther() {
        return isButtonClicked;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorAccent;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return "Please enter your Working Time";
    }

}
