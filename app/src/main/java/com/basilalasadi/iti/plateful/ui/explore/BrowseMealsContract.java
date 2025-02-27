package com.basilalasadi.iti.plateful.ui.explore;

import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.List;

public interface BrowseMealsContract {
    interface View {
        void showMeals(List<Meal> meals);
        void showError(String message);
        void showDisconnected();
        
        void openMenu(int position, boolean inFavorites);
    }
    
    interface Presenter {
        void fetchMeals();
        
        void menuRequested(Meal meal, int position);
    }
}
