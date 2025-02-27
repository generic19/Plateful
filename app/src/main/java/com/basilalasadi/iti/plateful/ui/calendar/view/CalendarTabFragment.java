package com.basilalasadi.iti.plateful.ui.calendar.view;

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
import com.basilalasadi.iti.plateful.model.calendar.CalendarMeal;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract.View.DateCalendarComponent;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract.View.MealCalendarComponent;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.basilalasadi.iti.plateful.ui.favorites.view.FavoritesTabFragmentDirections;
import com.basilalasadi.iti.plateful.ui.home.view.HomeTabFragment;

import java.util.Date;
import java.util.List;


public class CalendarTabFragment extends Fragment implements CalendarAdapter.Listener {
    
    private FragmentTabCalendarBinding binding;
    private CalendarAdapter calendarAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        calendarAdapter = new CalendarAdapter(getContext(), this);
        
        calendarAdapter.setItems(List.of(
            new DateCalendarComponent(new Date(2025, 2, 18)),
            new MealCalendarComponent(new CalendarMeal(HomeTabFragment.EXAMPLE_MEALS.get(0), 0)),
            new DateCalendarComponent(new Date(2025, 2, 19)),
            new MealCalendarComponent(new CalendarMeal(HomeTabFragment.EXAMPLE_MEALS.get(1), 0)),
            new MealCalendarComponent(new CalendarMeal(HomeTabFragment.EXAMPLE_MEALS.get(2), 0)),
            new DateCalendarComponent(new Date(2025, 2, 20)),
            new MealCalendarComponent(new CalendarMeal(HomeTabFragment.EXAMPLE_MEALS.get(0), 0)),
            new DateCalendarComponent(new Date(2025, 2, 21)),
            new MealCalendarComponent(new CalendarMeal(HomeTabFragment.EXAMPLE_MEALS.get(1), 0))
        ));
        
        binding.recyclerCalendar.setAdapter(calendarAdapter);
    }
    
    @Override
    public void onMealClicked(CalendarMeal meal) {
        Navigation.findNavController(binding.getRoot())
            .navigate(CalendarTabFragmentDirections.actionCalendarTabFragmentToMealDetailFragment(meal));
    }
    
    @Override
    public void onMealMenuRequested(CalendarMeal meal, View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_context_meal, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            return true;
        });
        
        popupMenu.show();
    }
}