package com.basilalasadi.iti.plateful.ui.explore.view;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentBrowseSectionBinding;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.Section;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.MealLocalDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.MealRemoteDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.api.MealService;
import com.basilalasadi.iti.plateful.ui.common.view.VerticalMealsAdapter;
import com.basilalasadi.iti.plateful.ui.explore.BrowseSectionContract;
import com.basilalasadi.iti.plateful.ui.explore.presenter.BrowseSectionPresenter;
import com.basilalasadi.iti.plateful.ui.favorites.view.FavoritesTabFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BrowseSectionFragment extends Fragment implements BrowseSectionContract.View, VerticalMealsAdapter.Listener {
    private final Section section;
    private final Delegate delegate;
    private FragmentBrowseSectionBinding binding;
    private BrowseSectionContract.Presenter presenter;
    private VerticalMealsAdapter mealsAdapter;
    
    public interface Delegate {
        void showMeal(Meal meal);
    }
    
    public BrowseSectionFragment(Section section, Delegate delegate) {
        this.section = section;
        this.delegate = delegate;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBrowseSectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new BrowseSectionPresenter(section, this, MealRepository.getInstance(
            new MealLocalDataSourceImpl(getContext()),
            new MealRemoteDataSourceImpl(MealService.create())
        ));
        
        binding.materialToolbar.setTitle(section.getName());
        binding.materialToolbar.setSubtitle(section instanceof Category ? "Category" : "Cuisine");
        
        mealsAdapter = new VerticalMealsAdapter(getContext(), this);
        binding.recyclerMeals.setAdapter(mealsAdapter);
        
        presenter.fetchMeals();
    }
    
    @Override
    public void showMeals(List<Meal> meals) {
        mealsAdapter.setItems(meals);
    }
    
    @Override
    public void showMessage(String message, int duration) {
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
    
    @Override
    public void onMealClicked(Meal meal) {
        delegate.showMeal(meal);
    }
    
    @Override
    public void onMealMenuRequested(Meal meal, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_context_meal, popupMenu.getMenu());
        
        if (meal.isFavorite()) {
            popupMenu.getMenu().findItem(R.id.menu_item_add_to_favorites).setTitle("Remove from Favorites");
        }
        
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            
            if (id == R.id.menu_item_add_to_calendar) {
                showDatePicker(meal);
            } else if (id == R.id.menu_item_add_to_favorites) {
                if (meal.isFavorite()) {
                    presenter.removeFromFavorites(meal);
                } else {
                    presenter.addToFavorites(meal);
                }
            }
            
            return true;
        });
        
        popupMenu.show();
    }
    
    public void showDatePicker(Meal meal) {
        Calendar calendar = Calendar.getInstance();
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            getContext(),
            (view, selectedYear, selectedMonth, selectedDay) -> {
                presenter.addToCalendar(meal, new Date(selectedYear - 1900, selectedMonth - 1, selectedDay));
            },
            year, month, day
        );
        
        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 1);
        
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(nextMonth.getTimeInMillis());
        
        datePickerDialog.show();
    }
}