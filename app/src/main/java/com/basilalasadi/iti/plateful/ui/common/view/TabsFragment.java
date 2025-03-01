package com.basilalasadi.iti.plateful.ui.common.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.basilalasadi.iti.plateful.databinding.FragmentTabsBinding;
import com.basilalasadi.iti.plateful.ui.common.TabsContract;
import com.basilalasadi.iti.plateful.ui.common.presenter.TabsPresenter;
import com.basilalasadi.iti.plateful.util.NetworkMonitor;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TabsFragment extends Fragment implements TabsContract.View {
    
    private FragmentTabsBinding binding;
    private NavController navController;
    private TabsPresenter presenter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabsBinding.inflate(inflater, container, false);
        
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        
        navController = navHostFragment.getNavController();
        
//        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        presenter = new TabsPresenter(this, NetworkMonitor.getInstance(getContext()));
        
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            
            if (id == R.id.nav_item_home) {
                return presenter.tabNavigationRequested(TabsContract.Tab.home);
            } else if (id == R.id.nav_item_search) {
                return presenter.tabNavigationRequested(TabsContract.Tab.search);
            } else if (id == R.id.nav_item_explore) {
                return presenter.tabNavigationRequested(TabsContract.Tab.explore);
            } else if (id == R.id.nav_item_favorites) {
                return presenter.tabNavigationRequested(TabsContract.Tab.favorites);
            } else if (id == R.id.nav_item_calendar) {
                return presenter.tabNavigationRequested(TabsContract.Tab.calendar);
            } else {
                return false;
            }
        });
        
        binding.bottomNavigationView.setOnItemReselectedListener(item -> {});
    }
    
    @Override
    public void navigateToTab(TabsContract.Tab tab) {
        switch (tab) {
            case home:
                navController.navigate(R.id.action_global_homeTabFragment);
                break;
            case search:
                navController.navigate(R.id.action_global_searchTabFragment);
                break;
            case explore:
                navController.navigate(R.id.action_global_exploreTabFragment);
                break;
            case favorites:
                navController.navigate(R.id.action_global_favoritesTabFragment);
                break;
            case calendar:
                navController.navigate(R.id.action_global_calendarTabFragment);
                break;
        }
    }
    
    @Override
    public void setSelectedTab(TabsContract.Tab tab) {
        switch (tab) {
            case home:
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_item_home);
                break;
            case search:
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_item_search);
                break;
            case explore:
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_item_explore);
                break;
            case favorites:
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_item_favorites);
                break;
            case calendar:
                binding.bottomNavigationView.setSelectedItemId(R.id.nav_item_calendar);
                break;
        }
    }
    
    public static void applySystemTopPadding(View view) {
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
    }
}