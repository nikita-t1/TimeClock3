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

import com.studio.timeclock3.R;

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


                Log.i("TAG", "Ausgewählt Metrisch--> Stunden: " + hour + "Minuten: " + minute);
                Log.i(". ", " .\n");
                Log.i("", "Mnuten als Float (Typecasting): "+ ((int) (minute*1.666666666666667)));
                Log.i("", "Mnuten als Float (Math.round()): "+ Math.round(minute*1.666666666666667));
                Log.i(". ", " .\n");

                long decimalMinutes = Math.round(minute*1.666666666666667);
                String workingTimeInDecimalString = hour + "." + decimalMinutes;
                Float workingTimeInDecimalFoat = Float.valueOf(workingTimeInDecimalString);

                isButtonClicked = true;

                mEditor.putFloat("WORKING_TIME_HOURS", workingTimeInDecimalFoat);
                mEditor.apply();

                mEditor.putInt("WORKING_MIN_INT_NO-HOUR", minute);
                mEditor.apply();
                Log.i("TAG", String.valueOf(workingTimeInDecimalFoat));

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
