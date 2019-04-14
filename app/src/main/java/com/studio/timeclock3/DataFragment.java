package com.studio.timeclock3;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.orhanobut.logger.Logger;
import com.studio.timeclock3.Data.AppDatabase;
import com.studio.timeclock3.Data.DatabaseInitializer;
import com.yarolegovich.mp.MaterialRightIconPreference;


public class DataFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.addBtn) MaterialRightIconPreference addButton;
    @BindView(R.id.initBtn) MaterialRightIconPreference initButton;
    @BindView(R.id.deleteBtn) MaterialRightIconPreference deleteBtn;


    OnFragmentChangeListener fragmentChangeListener;

    public interface OnFragmentChangeListener{

        public void OnFragmentChange(String fragment);
    }

    public DataFragment() {
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
    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
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
        View view =  inflater.inflate(R.layout.fragment_data, container, false);
        ButterKnife.bind(this, view);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Toolbar mToolbar= (Toolbar) getActivity().findViewById(R.id.action_bar);
        TextView toolbarText= (TextView) getActivity().findViewById(R.id.toolbar_title);
        toolbarText.setText("Data");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentChangeListener.OnFragmentChange("Settings");
            }
        });

        return view;
    }

    @OnClick(R.id.deleteBtn)
    public void deleteBtnClick(){
        Toasty.error(getContext(), "Datenbank gelöscht", Toast.LENGTH_LONG , true).show();
        AppDatabase.getAppDatabase(getContext()).workDayDao().deleteAll();
    }

    @OnClick(R.id.initBtn)
    public void initBtnClick(){
        Toasty.info(getContext(), "Datenbank initialisiert", Toast.LENGTH_SHORT , true).show();
        DatabaseInitializer.populateSync(AppDatabase.getAppDatabase(getContext()));
    }

    @OnClick(R.id.addBtn)
    public void addBtnClick(){
        Toasty.success(getContext(), "Funktion wird bald hinzugefügt", Toast.LENGTH_SHORT , true).show();
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
    public void onStop() {
        super.onStop();
        AppDatabase.destroyInstance();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}