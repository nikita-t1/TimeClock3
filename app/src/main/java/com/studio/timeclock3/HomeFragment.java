package com.studio.timeclock3;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.ColorRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jaredrummler.cyanea.app.CyaneaFragment;
import com.orhanobut.logger.Logger;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.studio.timeclock3.Data.AppDatabase;
import com.studio.timeclock3.Data.WorkDay;

import net.futuredrama.jomaceld.circularpblib.BarComponent;
import net.futuredrama.jomaceld.circularpblib.CircularProgressBarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import library.minimize.com.chronometerpersist.ChronometerPersist;


public class HomeFragment extends Fragment {

    public static float PAUSE_TIME_HOURS;
    public static double PAUSE_TIME_MILLISECONDS;

    private ChronometerPersist chronometerPersistWork;

    @BindView(R.id.pbar) CircularProgressBarView progressBar;

    TimeCalculations timeCalculations;

    @BindView(R.id.startButton) Button startButton;
    @BindView(R.id.cancelButton) FloatingActionButton cancelButton;
    @BindView(R.id.pauseButton) FloatingActionButton pauseButton;

    @BindView(R.id.textViewStartTime) TextView textViewStartTime;
    @BindView(R.id.textViewEndTime) TextView textViewEndTime;

    @BindView(R.id.chronometerWork) Chronometer chronometerWork;

    @BindView(R.id.container) ConstraintLayout constraintLayout;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private TickerView tickerView;
    private int y = 0;


    public HomeFragment() {
        Logger.i("HomeFragment: Empty Constructor");
        // Required empty public constructor
    }

    public static Fragment newInstance() {

        Logger.i("HomeFragment: @newInstance");
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i("HomeFragment: @onCreate");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        Logger.i("HomeFragment: @onCreateView");

        mSharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();


        timeCalculations = new TimeCalculations(mSharedPreferences);


        chronometerPersistWork = ChronometerPersist.Companion.getInstance(chronometerWork, mSharedPreferences);

        progressBar.setNumberOfBars(1);
        progressBar.setBarsColors(new int[]{ getResources().getColor(R.color.green, null) });

        startButton.setOnClickListener(view1 -> startButtonClick());
        cancelButton.setOnClickListener(view1 -> cancelButtonClick());
        pauseButton.setOnClickListener(view1 -> pauseButtonClick());
        chronometerWork.setOnClickListener(view1 -> ssss());

        tickerView = view.findViewById(R.id.tickerView);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());
        tickerView.setTextSize(124);
        tickerView.setTypeface(Typeface.MONOSPACE);
        chronometerWork.setTextColor(Color.WHITE);

        tickerView.setText("12:34");

        return view;
    }

    private void ssss() {
        for (WorkDay e : (AppDatabase.getAppDatabase(getContext()).workDayDao().getAll())){
            Logger.i(String.valueOf(e.getUserNote()));
        }
    }


    private void startButtonClick() {

        /**
         * 0 == Start Screen (nur StartButton)
         * 1 == Tracking Screen (Start- und PauseButton)
         * 2 == Saving Screen (Cancel, Save, Resume)
         */

        if (0 == mSharedPreferences.getInt("isStartPressed", 0)) {

            //Geht zu Tracking Screen

            mEditor.putInt("isStartPressed", 1).apply();

            chronometerPersistWork.startChronometer();

            timeCalculations.setStartTime();
            textViewStartTime.setText(timeCalculations.getStartTimeAsDate());
            textViewEndTime.setText(timeCalculations.getEndTimeEstimateAsDate(false));

            progressBarUpdateThread();

            layoutToTrackingScreen(1200);

        } else if (1 == mSharedPreferences.getInt("isStartPressed", 0)) {

            //Geht zu Saving Screen

            mEditor.putInt("isStartPressed",2).apply();

            timeCalculations.setEndTime();

            if (!mSharedPreferences.getBoolean("isPausePressed", false)) {
                chronometerPersistWork.pauseChronometer();
            }

            textViewEndTime.setText(timeCalculations.getEndTimeAsDate());
            layoutToSavingScreen(1200);

        } else if (2== mSharedPreferences.getInt("isStartPressed", 0)) {

            //For Goto Start Screen, see -> cancelButtonClick()

            Toasty.info(getActivity(), "Speichern oder so", Toast.LENGTH_LONG , true).show();

            timeCalculations.calculateWorkingPeriodAsMinutes();
            Calendar calender = Calendar.getInstance();

            Logger.w(  "Kalenderwoche: " + calender.get(Calendar.WEEK_OF_YEAR) +"\n"+
                                "Wochentag: " + calender.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) +" " + (calender.get(Calendar.DAY_OF_WEEK)-1) + "\n"+
                                "Tag: " + calender.get(Calendar.DAY_OF_MONTH) + "\n"+
                                "Monat: " + calender.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +" " + (calender.get(Calendar.MONTH)+1) + "\n"+
                                "Eingestempelt: "  + timeCalculations.getStartTimeAsDate() + "\n"+
                                "Ausgestempelt: " + timeCalculations.getEndTimeAsDate() + "\n"+
                                "Pause: " + timeCalculations.getPauseTimeAsDateString() + "\n"+
                                "Stunden (Brutto): " + timeCalculations.getWorkingPeriodAsDateString(true) + "\n"+
                                "Stunden (Netto): " + timeCalculations.getWorkingPeriodAsDateString(false) + "\n"+             //TODO
                                "Überstunden: " + "\n"+                 //TODO
                                "Anwesenheit: " + "true" + "\n"+
                                "Notiz: " + "null" + "\n"+
                                "Zusatz 1: " + "null" + "\n"+
                                "Zusatz 2: " + "null" + "\n");

        } else {

            Toasty.error(getActivity(), "BIG BUTTON ERROR", Toast.LENGTH_LONG, true).show();
        }

    }

    private void pauseButtonClick() {

        //Endzeit wird nach der Pause nicht akkktuslisiert!!!
        // Add Extra Pause time To ENd time

        chronometerPersistWork.startChronometer();
        mEditor.putInt("isStartPressed", 1).apply();
        layoutToTrackingScreen(1200);
    }

    private void cancelButtonClick() {

        mEditor.putInt("isStartPressed",0).apply();
        mEditor.putBoolean("isPausePressed", false).apply();

        chronometerPersistWork.stopChronometer();

        progressBar.setProgressWithAnimation(0);

        layoutToStartScreen(1200);
    }

    private void progressBarUpdateThread() {
        timeCalculations.calculateWorkingTimeEstimateAsMinutes();
        AtomicInteger i= new AtomicInteger();

        chronometerWork.setOnChronometerTickListener(chronometer -> {
            long duration = SystemClock.elapsedRealtime() - chronometerWork.getBase();
            long workingTimeMilli = (timeCalculations.getWorkingTimeEstimateAsMinutes(false)*60000);
            long percent = duration / (workingTimeMilli/100);

//            tickerView.setText(timeCalculations.convertMinutesToDateString(workingTimeMilli/60000));
            Date date = new Date(duration - TimeUnit.HOURS.toMillis(1));
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String dateFormatted = formatter.format(date);
            tickerView.setText(dateFormatted);

            Logger.i("ProgressBar Percent: " + percent + "\t Counter: " + (i.getAndIncrement()));

            progressBar.setProgressWithAnimation(percent);
        });
    }

    private void layoutToStartScreen(int duration) {
        startButton.setText("Start");

        startButton.getBackground().setTint(getResources().getColor(R.color.green, null));
//        startButton.animate().translationX(-150f).setInterpolator(new OvershootInterpolator()).setDuration(duration).start();


//        pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(duration).start();
//        pauseButton.animate().translationX(-400f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();

        pauseButton.hide();
        cancelButton.hide();
//        cancelButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(duration).start();
//        cancelButton.animate().translationX(0f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
    }

    private void layoutToSavingScreen(int duration) {
//        startButton.animate().translationX(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
        startButton.setText("Save");
        startButton.getBackground().setTint(getResources().getColor(R.color.amber, null));

//        pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(1f).scaleY(1f).setDuration(duration).start();
//        pauseButton.animate().translationX(0f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();

        pauseButton.show();
        cancelButton.show();

//        cancelButton.animate().setInterpolator(new LinearInterpolator()).scaleX(1f).scaleY(1f).setDuration(duration).start();
//        cancelButton.animate().translationX(0f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
    }

    private void layoutToTrackingScreen(int duration) {
//        startButton.animate().translationX(-150f).setInterpolator(new OvershootInterpolator()).setDuration(duration).start();
        startButton.setText("STOP");
        startButton.getBackground().setTint(getResources().getColor(R.color.red, null));

//        pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(duration).start();
//        pauseButton.animate().translationX(-150f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();

        pauseButton.hide();
        cancelButton.hide();
//        cancelButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(duration).start();
//        cancelButton.animate().translationX(0f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
    }

    private float getProgressBarValue() {
        ArrayList<BarComponent> array = progressBar.getBarComponentsArray();
        //Log.i("+","********************************************************");
        for (BarComponent element:array) {
            //Log.i("INFO", String.valueOf(element.getValue()));
        }
        //Log.i("+","********************************************************");
        return array.get(0).getValue();
    }

    private void UIThreadHandler(final String string) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toasty.success(getActivity(), string, Toast.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        Logger.i("HomeFragment: @onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Logger.i("HomeFragment: @onDetach");
        super.onDetach();
    }

    @Override
    public void onResume() {
        Logger.i("HomeFragment: @onResume");

            //TODO
        //Dat muss doch besser gehen    !.!.!.
        super.onResume();

        if (0 == mSharedPreferences.getInt("isStartPressed", 0)) {
            layoutToStartScreen(1);
        } else if (1 == mSharedPreferences.getInt("isStartPressed", 0)) {
            layoutToTrackingScreen(1);
            progressBarUpdateThread();
        } else if (2 == mSharedPreferences.getInt("isStartPressed", 0)) {
            layoutToSavingScreen(1);
        }

        chronometerPersistWork.resumeState();

    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.i("HomeFragment: @onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Logger.i("HomeFragment: @onStop");
    }
}
