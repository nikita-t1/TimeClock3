package com.studio.timeclock3;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.google.android.material.button.MaterialButton;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;
import com.orhanobut.logger.Logger;

import net.futuredrama.jomaceld.circularpblib.BarComponent;
import net.futuredrama.jomaceld.circularpblib.CircularProgressBarView;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import library.minimize.com.chronometerpersist.ChronometerPersist;

import static android.content.Context.MODE_PRIVATE;


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
    public Chronometer chronometer;
    public Chronometer chronometerPause;
    public Chronometer chronometerHelper;

    private long pauseOffset;
    public CircularProgressBarView progressBar;
    public float progressBarPercent = 0;
    public int stoppedMilliseconds;
    public long chronometerTime;
    public ChronometerPersist chronometerPersist;

    private long startOffset;   // <-- dont overthink this on too much


    public boolean isStartPressed = false;
    public boolean isPausePressed = false;


    public HomeFragment() {
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

        Logger.i("onCreateView");

        startButton = (Button) view.findViewById(R.id.startButton);
        pauseButton = (Button) view.findViewById(R.id.pauseButton);
        chronometer = (Chronometer) view.findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometerPause = (Chronometer) view.findViewById(R.id.chronometerPause);
        chronometerPause.setFormat("Pause: %s");
        chronometerPause.setVisibility(View.GONE);

        chronometerHelper = (Chronometer) view.findViewById(R.id.chronometerHelper);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        chronometerPersist = ChronometerPersist.getInstance(chronometerHelper, sharedpreferences);

        progressBar = (CircularProgressBarView) view.findViewById(R.id.pbar);

        progressBar.setNumberOfBars(1);
        progressBar.setBarsColors(new int[]{ getResources().getColor(R.color.green, null) });

        System.out.println("YEEEEESSSS!!!!");
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test();
                isStartPressed = !isStartPressed;
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

        if (isStartPressed) {

            chronometerPersist.startChronometer();

            progressBarUpdateThread();

            Toast.makeText(getActivity(), "IF", Toast.LENGTH_SHORT).show();

            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);      //Startet Chronometer
            chronometer.start();


            ObjectAnimator startButtonAnimator = ObjectAnimator.ofFloat(startButton, "translationX", 0, -150);
            startButtonAnimator.setInterpolator(new EasingInterpolator(Ease.BACK_IN));
            //animator.setStartDelay(500);
            startButtonAnimator.setDuration(2000);
            startButtonAnimator.start();
            startButton.setText("STOP");

            startButton.getBackground().setTint(getResources().getColor(R.color.red, null));


            ObjectAnimator pauseButtonAnimator = ObjectAnimator.ofFloat(pauseButton, "translationX", -0, 270);
            pauseButtonAnimator.setInterpolator(new EasingInterpolator(Ease.BACK_IN));
            //animator.setStartDelay(500);
            pauseButtonAnimator.setDuration(2000);
            pauseButtonAnimator.start();

        } else {

            chronometerPersist.stopChronometer();

            Logger.e("error");
            Logger.w("warning");
            Logger.i("information");
            Logger.wtf("What a Terrible Failure");

            progressBar.setProgressWithAnimation(0);
            progressBarPercent = 0;

            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());     //Setzt Chronometer zurück
            pauseOffset = 0;

            chronometerPause.stop();
            chronometerPause.setBase(SystemClock.elapsedRealtime());
            startOffset = 0;

            chronometerPause.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "ELSE", Toast.LENGTH_SHORT).show();

            ObjectAnimator startButtonAnimator = ObjectAnimator.ofFloat(startButton, "translationX", -150, 0);
            startButtonAnimator.setInterpolator(new EasingInterpolator(Ease.BACK_IN));
            //animator.setStartDelay(500);
            startButtonAnimator.setDuration(2000);
            startButtonAnimator.start();
            startButton.setText("Start");
            startButton.getBackground().setTint(getResources().getColor(R.color.green, null));



            ObjectAnimator pauseButtonAnimator = ObjectAnimator.ofFloat(pauseButton, "translationX", 270, -0);
            pauseButtonAnimator.setInterpolator(new EasingInterpolator(Ease.BACK_IN));
            //animator.setStartDelay(500);
            pauseButtonAnimator.setDuration(2000);
            pauseButtonAnimator.start();

            chronometerPause.setVisibility(View.GONE);

        }

    }

    private void pauseButtonClick() {

        final Animation in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(3000);

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(3000);

        if (chronometerPause.getVisibility() == 8f) {
            chronometerPause.setVisibility(View.VISIBLE);
        }

        if (isPausePressed) {

            chronometerPersist.pauseChronometer();

            chronometerPause.setBase(SystemClock.elapsedRealtime() - startOffset);     //Startet Chronometer 2
            chronometerPause.start();

            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();    //Pausiert Chronometer

            pauseButton.setText("Resume");

        } else {

            chronometerPersist.startChronometer();

            progressBarUpdateThread();

            chronometerPause.stop();
            startOffset = SystemClock.elapsedRealtime() - chronometer.getBase();    //Pausiert Chronometer 2

            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);   //Setzt Chronometer fort
            chronometer.start();
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

    public void test() {

        if (false) {

            ValueAnimator widthAnimator = ValueAnimator.ofInt(10, 500);
            widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int animatedValue = (int) animation.getAnimatedValue();
                    startButton.getLayoutParams().width = animatedValue;
                    startButton.requestLayout();
                }
            });
        }
        if (false) {

            ObjectAnimator animator = ObjectAnimator.ofFloat(startButton, "translationX", 0, -150);
            animator.setInterpolator(new EasingInterpolator(Ease.BACK_IN));
            animator.setStartDelay(500);
            animator.setDuration(2000);
            animator.start();
        }
        if (false) {

            AnimatorSet buttonAnimator = new AnimatorSet();

            /**
             * ValueAnimator to update x position of a button
             */
            ValueAnimator buttonAnimatorX = ValueAnimator.ofFloat(500, 650);
            buttonAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    startButton.setX((float) animation.getAnimatedValue() - startButton.getLayoutParams().width / 2);
                    startButton.requestLayout();
                }
            });

            ValueAnimator buttonSizeAnimator = ValueAnimator.ofInt(5, 300);
            buttonSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    startButton.getLayoutParams().width = (int) animation.getAnimatedValue();
                    startButton.getLayoutParams().height = (int) animation.getAnimatedValue();
                    startButton.requestLayout();
                }
            });

            //startButton.setBackgroundResource(R.drawable.rounded_button_start);
            buttonAnimatorX.setDuration(1000);
            buttonAnimator.play(buttonAnimatorX).with(buttonSizeAnimator);
            buttonAnimator.start();
            //startButton.setBackgroundResource(R.drawable.rounded_button_start);

        }
    }

    public void progressBarUpdateThread() {
        final Handler mHandler = new Handler();
        Thread progressThread = new Thread(new Runnable() {
            public void run() {
                while (progressBarPercent <= 100 && !isPausePressed && isStartPressed) {
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {

                            Logger.i(Long.toString(SystemClock.elapsedRealtime() - chronometer.getBase()) + "ms\t\t" + (Long.toString((SystemClock.elapsedRealtime() - chronometer.getBase())/1000 % 60))+"s");
                            chronometerTime = SystemClock.elapsedRealtime() - chronometer.getBase();

                            progressBar.setProgressWithAnimation((float) (chronometerTime/WORKING_TIME_1PERCENT_MILLISECONDS));

                            progressBarPercent = getProgressBarValue();
                        }
                    });
                    //progressBarPercent++;
                    Logger.e(String.valueOf(progressBarPercent));
                    //progressBarPercent = getProgressBarValue();
                    //android.os.SystemClock.sleep(1000);
                    android.os.SystemClock.sleep((long) WORKING_TIME_1PERCENT_MILLISECONDS); // Thread.sleep() doesn't guarantee 1000 msec sleep, it can be interrupted before
                }   UIThreadHandler("ProgressBar Update Completed");
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
        long chronometerBase = chronometer.getBase();

        Logger.i("**********************************************");

        Logger.i(String.valueOf(chronometerBase));

        Logger.i("**********************************************");

        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        chronometerPersist.resumeState();

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
