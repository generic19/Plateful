package com.basilalasadi.iti.plateful.common.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.basilalasadi.iti.plateful.R;

import eightbitlab.com.blurview.BlurView;


public class FeaturedMealComponentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_component_featured_meal, container, false);
        
        float radius = 12f;
        
        ViewGroup materialCardView = view.findViewById(R.id.materialCardView);
        BlurView blurView = view.findViewById(R.id.blurView);
        
        blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        blurView.setClipToOutline(true);
        
        blurView.setupWith(materialCardView).setBlurRadius(radius);
        
        return view;
    }
}