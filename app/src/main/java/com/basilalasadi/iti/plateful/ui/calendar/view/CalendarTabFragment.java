package com.basilalasadi.iti.plateful.ui.calendar.view;

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
import com.basilalasadi.iti.plateful.databinding.FragmentTabCalendarBinding;
import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.MealLocalDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.MealRemoteDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.api.MealService;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract;
import com.basilalasadi.iti.plateful.ui.calendar.presenter.CalendarPresenter;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.basilalasadi.iti.plateful.ui.favorites.view.FavoritesTabFragmentDirections;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CalendarTabFragment extends Fragment implements CalendarAdapter.Listener, CalendarContract.View {
    private FragmentTabCalendarBinding binding;
    private CalendarAdapter calendarAdapter;
    private CalendarContract.Presenter presenter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        presenter = new CalendarPresenter(this, MealRepository.getInstance(
            new MealLocalDataSourceImpl(getContext()),
            new MealRemoteDataSourceImpl(MealService.create())
        ));
        
        calendarAdapter = new CalendarAdapter(getContext(), this);
        binding.recyclerCalendar.setAdapter(calendarAdapter);
        
        presenter.fetchCalendar();
    }
    
    @Override
    public void onMealClicked(Meal meal) {
        Navigation.findNavController(binding.getRoot())
            .navigate(CalendarTabFragmentDirections.actionCalendarTabFragmentToMealDetailFragment(meal.getId()));
    }
    
    @Override
    public void showCalendar(List<CalendarContract.CalendarComponent> calendar) {
        calendarAdapter.setItems(calendar);
    }
    
    @Override
    public void showMessage(String message, int duration) {
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
    
    @Override
    public void onMealMenuRequested(Meal meal, Date date, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_context_meal, popupMenu.getMenu());
        
        if (meal.isFavorite()) {
            popupMenu.getMenu().findItem(R.id.menu_item_add_to_favorites).setTitle("Remove from Favorites");
        }
        
        popupMenu.getMenu().findItem(R.id.menu_item_add_to_calendar).setTitle("Remove from Calendar");
        
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            
            if (id == R.id.menu_item_add_to_calendar) {
                presenter.removeCalendarMeal(date, meal);
            } else if (id == R.id.menu_item_add_to_favorites) {
                presenter.favoriteAction(meal);
            }
            
            return true;
        });
        
        popupMenu.show();
    }
}