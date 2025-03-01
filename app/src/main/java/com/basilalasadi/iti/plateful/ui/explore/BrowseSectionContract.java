package com.basilalasadi.iti.plateful.ui.explore;

import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.Date;
import java.util.List;

public interface BrowseSectionContract {
    interface View {
        void showMeals(List<Meal> meals);
        void showMessage(String message, int duration);
    }
    
    interface Presenter {
        void fetchMeals();
        
        void addToFavorites(Meal meal);
        void addToCalendar(Meal meal, Date date);
        void removeFromFavorites(Meal meal);
    }
}
