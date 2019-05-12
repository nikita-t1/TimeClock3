package com.studio.timeclock3.intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.orhanobut.logger.Logger;
import com.studio.timeclock3.R;

import androidx.annotation.Nullable;
import io.github.dreierf.materialintroscreen.SlideFragment;


public class IntroSlidePauseTime extends SlideFragment {

    private TimePicker timePicker;
    public Button setButton;
    private boolean isButtonClicked = false;

    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_slide_time_picker, container, false);
//        view.setScaleX(1.3f);
//        view.setScaleY(1.3f);

        mSharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();


        timePicker = (TimePicker) view.findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);

        TextView mainText = (TextView) view.findViewById(R.id.timePickerMainText);
        mainText.setText("Pause Time");
        TextView descriptionText = (TextView) view.findViewById(R.id.timePickerMainDescription);
        descriptionText.setText("Please enter your daily pause hours");

        setButton = (Button) view.findViewById(R.id.setButton);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Je nachdem wie die Firma rundet, Abweichung von 30sec möglich
                int hour, minute;
                hour = timePicker.getHour();
                minute = timePicker.getMinute();

                isButtonClicked = true;

                Long PAUSE_TIME_MIN = (long)hour * 60 + minute;
                Logger.i("PAUSE_TIME_MIN:" + PAUSE_TIME_MIN);

                mEditor.putLong("PAUSE_TIME_MIN", PAUSE_TIME_MIN).apply();
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
        return "Please enter your Pause Time";
    }

}
