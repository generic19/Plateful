package com.basilalasadi.iti.plateful.ui.favorites;

import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.Date;
import java.util.List;

public interface FavoritesContract {
    interface View {
        void showMeals(List<Meal> meals);
        void removeMeal(Meal meal);
        
        void showMessage(String message, int duration);
    }
    
    interface Presenter {
        void fetchMeals();
        
        void calendarAction(Meal meal, Date date);
        void favoritesAction(Meal meal);
        
        void dispose();
    }
}
