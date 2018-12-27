package com.studio.timeclock3;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import net.futuredrama.jomaceld.circularpblib.BarComponent;
import net.futuredrama.jomaceld.circularpblib.CircularProgressBarView;

import java.util.ArrayList;

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

    public static final double WORKING_TIME_HOURS = 0.00416667;
    public static final double WORKING_TIME_MILLISECONDS = WORKING_TIME_HOURS *3.6e+6; //Stunden *3,6e+6 um auf Millisekunden zu kommen
    public static final double WORKING_TIME_1PERCENT_MILLISECONDS = WORKING_TIME_MILLISECONDS/100;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Button startButton;
    public Button pauseButton;
    public Chronometer chronometerPause;
    public Chronometer chronometerWork;

    public CircularProgressBarView progressBar;
    public float progressBarPercent = 0;
    public int stoppedMilliseconds;
    public long chronometerTime;
    public ChronometerPersist chronometerPersistWork;
    public ChronometerPersist chronometerPersistPause;


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

        startButton = (Button) view.findViewById(R.id.startButton);
        pauseButton = (Button) view.findViewById(R.id.pauseButton);

        mSharedPreferences = getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        if (mSharedPreferences.getBoolean("isStartPressed", false)) {
            startButton.animate().translationX(-150f).setInterpolator(new OvershootInterpolator()).setDuration(1200).start();
            startButton.setText("STOP");
            startButton.getBackground().setTint(getResources().getColor(R.color.red, null));
            pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(1f).scaleY(1f).setDuration(1200).start();
            pauseButton.animate().translationX(290f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1200).start();

        } else {
            startButton.setText("Start");
            startButton.getBackground().setTint(getResources().getColor(R.color.green, null));
            startButton.animate().translationX(0f).setInterpolator(new OvershootInterpolator()).setDuration(1200).start();

            pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(1200).start();
            pauseButton.animate().translationX(0f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1200).start();
        }


        chronometerWork = (Chronometer) view.findViewById(R.id.chronometerWork);
        chronometerPersistWork = ChronometerPersist.getInstance(chronometerWork, mSharedPreferences);

        chronometerPause = (Chronometer) view.findViewById(R.id.chronometerPause);
        chronometerPersistPause = ChronometerPersist.getInstance(chronometerPause, mSharedPreferences);


        progressBar = (CircularProgressBarView) view.findViewById(R.id.pbar);

        progressBar.setNumberOfBars(1);
        progressBar.setBarsColors(new int[]{ getResources().getColor(R.color.green, null) });

        System.out.println("YEEEEESSSS!!!!");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test();
                //isStartPressed=true;
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
        return view;
    }

    private void startButtonClick() {

        if (!mSharedPreferences.getBoolean("isStartPressed", false)) {

            mEditor.putBoolean("isStartPressed",true);
            mEditor.apply();

            chronometerPersistWork.startChronometer();

            progressBarUpdateThread();

            startButton.animate().translationX(-150f).setInterpolator(new OvershootInterpolator()).setDuration(1200).start();
            startButton.setText("STOP");
            startButton.getBackground().setTint(getResources().getColor(R.color.red, null));

            pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(1f).scaleY(1f).setDuration(1200).start();
            pauseButton.animate().translationX(290f).alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1200).start();

        } else {

            chronometerPause.animate().translationY(-40f).alpha(0f).setInterpolator(new LinearInterpolator()).setDuration(1200);

            mEditor.putBoolean("isStartPressed",false);
            mEditor.apply();
            mEditor.putBoolean("isPausePressed",false);
            mEditor.apply();


            chronometerPersistWork.stopChronometer();
            chronometerPersistPause.stopChronometer();


            isPausePressed = false;

            progressBar.setProgressWithAnimation(0);
            progressBarPercent = 0;


            Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();

            startButton.setText("Start");
            pauseButton.setText("PAUSE");

            startButton.getBackground().setTint(getResources().getColor(R.color.green, null));
            startButton.animate().translationX(0f).setInterpolator(new OvershootInterpolator()).setDuration(1200).start();

            pauseButton.animate().setInterpolator(new LinearInterpolator()).scaleX(0f).scaleY(0f).setDuration(1200).start();
            pauseButton.animate().translationX(0f).alpha(0f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1200).start();
        }

    }

    private void pauseButtonClick() {

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(3000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(3000);


        if (!mSharedPreferences.getBoolean("isPausePressed", false)) {

            mEditor.putBoolean("isPausePressed",true);
            mEditor.apply();
            chronometerPause.animate().translationY(0f).alpha(1f).setInterpolator(new LinearInterpolator()).setDuration(1200);

            chronometerPersistWork.pauseChronometer();
            chronometerPersistPause.startChronometer();

            pauseButton.setText("Resume");

        } else {

            mEditor.putBoolean("isPausePressed",false);
            mEditor.apply();

            chronometerPersistWork.startChronometer();
            chronometerPersistPause.pauseChronometer();


            progressBarUpdateThread();

            pauseButton.setText("PAUSE");

        }
    }

    public float getProgressBarValue() {
        ArrayList<BarComponent> array = progressBar.getBarComponentsArray();
        //Log.i("+","********************************************************");
        for (BarComponent element:array) {
            //Log.i("INFO", String.valueOf(element.getValue()));
        }
        //Log.i("+","********************************************************");
        return array.get(0).getValue();
    }


    public void progressBarUpdateThread() {
        final Handler mHandler = new Handler();
        Thread progressThread = new Thread(new Runnable() {
            public void run() {
                while (progressBarPercent <= 100 && !mSharedPreferences.getBoolean("isPausePressed", false) && mSharedPreferences.getBoolean("isStartPressed", false)) {
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {

                            Logger.i(Long.toString(SystemClock.elapsedRealtime() - chronometerWork.getBase()) + "ms\t\t" + (Long.toString((SystemClock.elapsedRealtime() - chronometerWork.getBase())/1000 % 60))+"s");
                            chronometerTime = SystemClock.elapsedRealtime() - chronometerWork.getBase();

                            progressBar.setProgressWithAnimation((float) (chronometerTime/WORKING_TIME_1PERCENT_MILLISECONDS));

                            progressBarPercent = getProgressBarValue();
                        }
                    });
                    //progressBarPercent++;
                    Logger.e(String.valueOf(progressBarPercent));
                    //progressBarPercent = getProgressBarValue();
                    //android.os.SystemClock.sleep(1000);
                    android.os.SystemClock.sleep((long) WORKING_TIME_1PERCENT_MILLISECONDS); // Thread.sleep() doesn't guarantee 1000 msec sleep, it can be interrupted before
                }   //UIThreadHandler("ProgressBar Update Completed");
            }
        });

        progressThread.start();
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
        super.onResume();
        // Neue Rechnung benötigt
        //progressBar.setProgressWithAnimation((float) (SystemClock.elapsedRealtime() - chronometerWork.getBase()/WORKING_TIME_1PERCENT_MILLISECONDS));

        // TODO: Check if Fragments stays hidden when the RAM is full!!


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
