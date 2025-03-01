package com.basilalasadi.iti.plateful.ui.explore.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentTabExploreBinding;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.Section;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ExploreTabFragment extends Fragment implements SectionsFragment.Delegate, BrowseSectionFragment.Delegate {
    private FragmentTabExploreBinding binding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabExploreBinding.inflate(inflater, container, false);
        
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        getChildFragmentManager().beginTransaction()
            .replace(R.id.fragmentContainerView, new SectionsFragment(Category.class, this))
            .commit();
        
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                goToSectionsFragment(tab);
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                goToSectionsFragment(tab);
            }
        });
        
        return binding.getRoot();
    }
    
    private void goToSectionsFragment(TabLayout.Tab tab) {
        Fragment fragment;
        
        switch (tab.getPosition()) {
            case 0:
                fragment = new SectionsFragment(Category.class, ExploreTabFragment.this);
                break;

            case 1:
                fragment = new SectionsFragment(Cuisine.class, ExploreTabFragment.this);
                break;

            default:
                return;
        }
        
        getChildFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit();
    }
    
    private static final List<Category> CATEGORIES = List.of(
        new Category("Starter"),
        new Category("Breakfast"),
        new Category("Dessert"),
        new Category("Lunch"),
        new Category("Side"),
        new Category("Vegetarian"),
        new Category("Seafood")
    );
    
    private static final List<Cuisine> CUISINES = List.of(
        new Cuisine("Italian"),
        new Cuisine("Greek"),
        new Cuisine("French"),
        new Cuisine("Egyptian"),
        new Cuisine("Lebanese"),
        new Cuisine("Japanese"),
        new Cuisine("Mexican")
    );
    
    @Override
    public void onSectionClicked(Section section) {
        getChildFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainerView, new BrowseSectionFragment(section, this))
            .commit();
    }
    
    @Override
    public void showMeal(Meal meal) {
        Navigation.findNavController(binding.getRoot()).navigate(
            ExploreTabFragmentDirections.actionExploreTabFragmentToMealDetailFragment(meal.getId())
        );
    }
}