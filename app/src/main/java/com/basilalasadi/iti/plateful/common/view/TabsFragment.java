package com.basilalasadi.iti.plateful.common.view;

import android.os.Bundle;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TabsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    view.getPaddingLeft(),
                    systemBars.top,
                    view.getPaddingRight(),
                    view.getPaddingBottom()
            );
            
            return insets;
        });
        
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            
            if (id == R.id.nav_item_home) {
                navController.navigate(R.id.action_global_homeTabFragment);
            } else if (id == R.id.nav_item_search) {
                navController.navigate(R.id.action_global_searchTabFragment);
            } else if (id == R.id.nav_item_explore) {
                navController.navigate(R.id.action_global_exploreTabFragment);
            } else if (id == R.id.nav_item_favorites) {
                navController.navigate(R.id.action_global_favoritesTabFragment);
            } else if (id == R.id.nav_item_calendar) {
                navController.navigate(R.id.action_global_calendarTabFragment);
            }
            
            return true;
        });
        
        bottomNavigationView.setOnItemReselectedListener(item -> {});
        
        return view;
    }
}