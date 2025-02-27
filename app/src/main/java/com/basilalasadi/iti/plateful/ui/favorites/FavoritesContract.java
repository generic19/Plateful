package com.basilalasadi.iti.plateful.ui.favorites;

import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.List;

public interface FavoritesContract {
    interface View {
        void showMeals(List<Meal> meals);
        void removeMeal(Meal meal);
    }
    
    interface Presenter {
        void fetchMeals();
        
        void calendarAction(Meal meal);
        void favoritesAction(Meal meal);
        void shareAction(Meal meal);
    }
}
