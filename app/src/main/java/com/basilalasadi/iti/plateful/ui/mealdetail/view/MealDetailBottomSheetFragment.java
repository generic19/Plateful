package com.basilalasadi.iti.plateful.ui.mealdetail.view;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MealDetailBottomSheetFragment extends BottomSheetDialogFragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_detail_bottom_sheet, container, false);
    }
}