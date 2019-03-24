package com.studio.timeclock3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

public class ExperimentalFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.splash_seekbar) BubbleSeekBar splash_seekbar;

    OnFragmentChangeListener fragmentChangeListener;

    public interface OnFragmentChangeListener{

        public void OnFragmentChange(String fragment);
    }

    public ExperimentalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomizationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExperimentalFragment newInstance(String param1, String param2) {
        ExperimentalFragment fragment = new ExperimentalFragment();
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
        View view =  inflater.inflate(R.layout.fragment_experimental, container, false);
        ButterKnife.bind(this, view);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Toolbar mToolbar= (Toolbar) getActivity().findViewById(R.id.action_bar);
        TextView toolbarText= (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolbarText.setText("Experimental");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentChangeListener.OnFragmentChange("Settings");
            }
        });

        splash_seekbar.setProgress(preferences.getInt("splash_seekbar", 800));
        splash_seekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                preferences.edit().putInt("splash_seekbar", progress).apply();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        if (context instanceof OnFragmentChangeListener) {
            fragmentChangeListener = (OnFragmentChangeListener) activity;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}
