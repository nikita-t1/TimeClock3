package com.studio.timeclock3;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import net.futuredrama.jomaceld.circularpblib.BarComponent;
import net.futuredrama.jomaceld.circularpblib.CircularProgressBarView;

import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import library.minimize.com.chronometerpersist.ChronometerPersist;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static float WORKING_TIME_HOURS;
    public static double WORKING_TIME_MILLISECONDS; //Stunden *3,6e+6 um auf Millisekunden zu kommen
    public static double WORKING_TIME_1PERCENT_MILLISECONDS;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Button startButton;
    public Button pauseButton;
    public Button cancelButton;
    public Chronometer chronometerPause;
    public Chronometer chronometerWork;

    public CircularProgressBarView progressBar;
    public float progressBarPercent = 0;
    public int stoppedMilliseconds;
    public long chronometerTime;
    public ChronometerPersist chronometerPersistWork;
    public ChronometerPersist chronometerPersistPause;
    private TextView textViewStartTime;
    private TextView textViewRemainingTime;


    public boolean isStartPressed;
    public boolean isPausePressed;

    public SharedPreferences mSharedPreferences;
    public SharedPreferences.Editor mEditor;


    public HomeFragment() {
        Logger.e("HOME FRAGMENT");
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment newInstance(String param1, String param2) {
        Logger.e("newInstance");
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.e("onCreate");

            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Logger.e("onCreateView");

        mSharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        startButton = (Button) view.findViewById(R.id.startButton);
        pauseButton = (Button) view.findViewById(R.id.pauseButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        textViewStartTime = (TextView) view.findViewById(R.id.textViewStartTime);
        textViewRemainingTime = (TextView) view.findViewById(R.id.textViewEndTime);

        progressBar = (CircularProgressBarView) view.findViewById(R.id.pbar);

        chronometerWork = (Chronometer) view.findViewById(R.id.chronometerWork);
        chronometerPause = (Chronometer) view.findViewById(R.id.chronometerPause);

        chronometerPersistWork = ChronometerPersist.getInstance(chronometerWork, mSharedPreferences);
        chronometerPersistPause = ChronometerPersist.getInstance(chronometerPause, mSharedPreferences);

        progressBar.setNumberOfBars(1);
        progressBar.setBarsColors(new int[]{ getResources().getColor(R.color.green, null) });

        textViewStartTime.setText(mSharedPreferences.getString("timey", "_____"));
        textViewRemainingTime.setText("_____");

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButtonClick();
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPausePressed = !isPausePressed;
                pauseButtonClick();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelButtonClick();
            }
        });
        return view;
    }

    private void startButtonClick() {

        /**
         * 0 == Start Screen (nur StartButton)
         * 1 == Tracking Screen (Start- und PauseButton)
         * 2 == Saving Screen (Cancel, Save, Resume)
         */

        if (0 == mSharedPreferences.getInt("isStartPressed", 0)) {

            //Geht zu Tracking Screen

            Logger.i("WORKING_TIME_HOURS: " + WORKING_TIME_HOURS);
            Logger.i("WORKING_TIME_MILLISECONDS: " + WORKING_TIME_MILLISECONDS);
            Logger.i("WORKING_TIME_1PERCENT_MILLISECONDS: "+ WORKING_TIME_1PERCENT_MILLISECONDS);

            Toasty.warning(getActivity(), "Working Time: " +  Math.round(WORKING_TIME_HOURS*0.6*100) + "min (" + WORKING_TIME_HOURS + ")", Toast.LENGTH_LONG , true).show();

            mEditor.putInt("isStartPressed", 1);
            mEditor.apply();
            Toasty.info(getActivity(), "1", Toast.LENGTH_SHORT, true).show();

            chronometerPersistWork.startChronometer();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String timey = sdf.format(new Date());
            textViewStartTime.setText(timey);
            mEditor.putString("timey",timey);
            mEditor.apply();

            progressBarUpdateThread();

            layoutToTrackingScreen(1200);

        } else if (1 == mSharedPreferences.getInt("isStartPressed", 0)) {

            //Geht zu Saving Screen

            mEditor.putInt("isStartPressed",2);
            mEditor.apply();

            if (!mSharedPreferences.getBoolean("isPausePressed", false)) {
                chronometerPersistWork.pauseChronometer();
            } else {
                chronometerPersistPause.pauseChronometer();
            }

            pauseButton.setText("RESUME");
            layoutToSavingScreen(1200);

            Toasty.warning(getActivity(), "2", Toast.LENGTH_LONG, true).show();

        } else if (2== mSharedPreferences.getInt("isStartPressed", 0)) {

            //For Goto Start Screen, see -> cancelButtonClick()

            Toasty.info(getActivity(), "Speichern oder so", Toast.LENGTH_LONG , true).show();


            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 1500ms
                    cancelButtonClick();
                }
            }, 1500);

        } else {

            Toasty.error(getActivity(), "BIG BUTTON ERROR", Toast.LENGTH_LONG, true).show();
        }

    }

    private void pauseButtonClick() {

        if (2 != mSharedPreferences.getInt("isStartPressed", 0)) {
            //Normale Pause Button Funktion

            if (!mSharedPreferences.getBoolean("isPausePressed", false)) {

                mEditor.putBoolean("isPausePressed", true);
                mEditor.apply();
                chronometerPause.animate().translationY(0f).alpha(1f).setInterpolator(new LinearInterpolator()).setDuration(1200);

                chronometerPersistWork.pauseChronometer();
                chronometerPersistPause.startChronometer();

                pauseButton.setText("Resume");

            } else {

                mEditor.putBoolean("isPausePressed", false);
                mEditor.apply();

                chronometerPersistWork.startChronometer();
                chronometerPersistPause.pauseChronometer();


                progressBarUpdateThread();

                pauseButton.setText("PAUSE");

            }

        } else {
            //Pause Button im Saving Screen
            chronometerPersistWork.startChronometer();
            layoutToTrackingScreen(1200);
            mEditor.putInt("isStartPressed",1);
            mEditor.apply();
        }
    }

    private void cancelButtonClick() {

        chronometerPause.animate().translationY(-40f).alpha(0f).setInterpolator(new LinearInterpolator()).setDuration(1200);

        mEditor.putInt("isStartPressed",0);
        mEditor.apply();
        mEditor.putBoolean("isPausePressed",false);
        mEditor.apply();
        Toasty.info(getActivity(), "0", Toast.LENGTH_SHORT, true).show();

        textViewStartTime.setText("_____");
        textViewRemainingTime.setText("_____");
        mEditor.putString("timey","_____");
        mEditor.apply();

        chronometerPersistWork.stopChronometer();
        chronometerPersistPause.stopChronometer();


        isPausePressed = false;

        progressBar.setProgressWithAnimation(0);
        progressBarPercent = 0;

        layoutToStartScreen(1200);
    }

    private void progressBarUpdateThread() {
        final Handler mHandler = new Handler();
        Thread progressThread = new Thread(new Runnable() {
            public void run() {
                while (progressBarPercent <= 100 && !mSharedPreferences.getBoolean("isPausePressed", false) && 1 == mSharedPreferences.getInt("isStartPressed", 0)) {
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {

                            Logger.i("Abgelaufene Zeit: " + Long.toString(SystemClock.elapsedRealtime() - chronometerWork.getBase()) + "ms\t\t" + (Long.toString((SystemClock.elapsedRealtime() - chronometerWork.getBase())/1000 % 60))+"s");
                            chronometerTime = SystemClock.elapsedRealtime() - chronometerWork.getBase();

                            progressBar.setProgressWithAnimation((float) (chronometerTime/WORKING_TIME_1PERCENT_MILLISECONDS));

                            progressBarPercent = getProgressBarValue();

                            Logger.i(String.valueOf(Math.round(WORKING_TIME_MILLISECONDS)));
                            Logger.i(String.valueOf(chronometerTime));
                            Logger.i(String.valueOf(Math.round((Math.round(WORKING_TIME_MILLISECONDS)-chronometerTime)/1000)));

                            long min = (Math.round((Math.round(WORKING_TIME_MILLISECONDS)-chronometerTime)/1000/60));
                            Logger.e("MIN" + String.valueOf(min));
                            long sec = (Math.round((Math.round(WORKING_TIME_MILLISECONDS)-chronometerTime)/1000% 60));
                            Logger.e(String.valueOf(sec));
                            //TODO
                            textViewRemainingTime.setText(String.format(min + "min"));
                        }
                    });
                    //progressBarPercent++;
                    Logger.e("progressBarPercent: " + String.valueOf(progressBarPercent));
                    //progressBarPercent = getProgressBarValue();
                    //android.os.SystemClock.sleep(1000);
                    android.os.SystemClock.sleep((long) WORKING_TIME_1PERCENT_MILLISECONDS); // Thread.sleep() doesn't guarantee 1000 msec sleep, it can be interrupted before
                }   //UIThreadHandler("ProgressBar Update Completed");
            }
        });

        progressThread.start();
    }

    private void layoutToStartScreen(int duration) {
        startButton.setText("Start");
        pauseButton.setText("PAUSE");

        startButton.getBackground().setTint(getResources().getColor(R.color.green, null));
        startButton.animate().translationX(0f).setInterpolator(new OvershootInterpolator()).setDuration(duration).start();

        pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(duration).start();
        pauseButton.animate().translationX(-400f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();

        cancelButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(duration).start();
        cancelButton.animate().translationX(400f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
    }

    private void layoutToSavingScreen(int duration) {
        startButton.animate().translationX(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
        startButton.setText("Save");
        startButton.getBackground().setTint(getResources().getColor(R.color.amber, null));

        pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(1f).scaleY(1f).setDuration(duration).start();
        pauseButton.animate().translationX(0f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();

        cancelButton.animate().setInterpolator(new LinearInterpolator()).scaleX(1f).scaleY(1f).setDuration(duration).start();
        cancelButton.animate().translationX(0f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
    }

    private void layoutToTrackingScreen(int duration) {
        startButton.animate().translationX(-150f).setInterpolator(new OvershootInterpolator()).setDuration(duration).start();
        startButton.setText("STOP");
        startButton.getBackground().setTint(getResources().getColor(R.color.red, null));

        pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(1f).scaleY(1f).setDuration(duration).start();
        pauseButton.animate().translationX(-50f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();

        cancelButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(duration).start();
        cancelButton.animate().translationX(150f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(duration).start();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        Logger.e("onAttach");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //long chronometerBase = chronometer.getBase();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
    }

    @Override
    public void onResume() {
        Logger.e("onResume");

        WORKING_TIME_HOURS = mSharedPreferences.getFloat("WORKING_TIME_HOURS",0.1f);
        WORKING_TIME_MILLISECONDS = WORKING_TIME_HOURS *3.6e+6;
        Logger.i("WORKING_TIME_HOURS: " + WORKING_TIME_HOURS);
        Logger.i("WORKING_TIME_MILLISECONDS: " + WORKING_TIME_MILLISECONDS);
        WORKING_TIME_1PERCENT_MILLISECONDS = WORKING_TIME_MILLISECONDS/100;
        Logger.i("WORKING_TIME_1PERCENT_MILLISECONDS: "+ WORKING_TIME_1PERCENT_MILLISECONDS);

        //ATTENTION  !!!
        // Beispiel:
        //Arbeitszeit = 5Std 7min = 5.12
        //Arbeitszeit in Millisekunden = 1.8431999588012695E7
        //Millisekunden zurück in Std min Google = 5.1199998855590820313
        // 5.1199998855590820313 < 5.12
        //Arbeitszeit in Millisekunden 1% = 184319.99588012695
        //Millisekunden 1% zurück in Std min Google = 5.11666667
        //Abweichung von 4min


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
        chronometerPersistPause.resumeState();

    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.e("onPause");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
