package com.basilalasadi.iti.plateful.ui.home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.databinding.FragmentTabHomeBinding;
import com.basilalasadi.iti.plateful.ui.common.view.MealsStackAdapter;
import com.basilalasadi.iti.plateful.util.StackLayoutManager;


public class HomeTabFragment extends Fragment {
    
    private MealsStackAdapter adapter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentTabHomeBinding binding = FragmentTabHomeBinding.inflate(inflater, container, false);
        
        StackLayoutManager manager = new StackLayoutManager();
        manager.setHorizontalLayout(true);
        binding.viewDailySelection.recycler.setLayoutManager(manager);
        
        adapter = new MealsStackAdapter(getContext());
        binding.viewDailySelection.recycler.setAdapter(adapter);
        
        return binding.getRoot();
    }
}
