package com.basilalasadi.iti.plateful.ui.favorites.view;

import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentTabFavoritesBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.ui.common.view.VerticalMealsAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.basilalasadi.iti.plateful.ui.home.view.HomeTabFragment;

public class FavoritesTabFragment extends Fragment implements VerticalMealsAdapter.Listener {
    
    private FragmentTabFavoritesBinding binding;
    private VerticalMealsAdapter verticalMealsAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabFavoritesBinding.inflate(inflater, container, false);
        
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        verticalMealsAdapter = new VerticalMealsAdapter(getContext(), this);
        verticalMealsAdapter.setItems(HomeTabFragment.EXAMPLE_MEALS);
        
        binding.recyclerMeals.setAdapter(verticalMealsAdapter);
        
        return binding.getRoot();
    }
    
    @Override
    public void onMealClicked(Meal meal) {
        Navigation.findNavController(binding.getRoot())
            .navigate(FavoritesTabFragmentDirections.actionFavoritesTabFragmentToMealDetailFragment(meal));
    }
    
    @Override
    public void onMealMenuRequested(Meal meal, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_context_meal, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            return true;
        });
        
        popupMenu.show();
    }
}