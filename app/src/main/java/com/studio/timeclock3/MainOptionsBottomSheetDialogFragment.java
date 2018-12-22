package com.studio.timeclock3;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainOptionsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static MainOptionsBottomSheetDialogFragment newInstance() {
        return new MainOptionsBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view =
                inflater.inflate(R.layout.fragment_main_options, container, false);

        return view;
    }
}
