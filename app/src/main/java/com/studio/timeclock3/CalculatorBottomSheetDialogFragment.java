package com.studio.timeclock3;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalculatorBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static CalculatorBottomSheetDialogFragment newInstance() {
        return new CalculatorBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view =
                inflater.inflate(R.layout.calculator, container, false);

        return view;
    }
}
