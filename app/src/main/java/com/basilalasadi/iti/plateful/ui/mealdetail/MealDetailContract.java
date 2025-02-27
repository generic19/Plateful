package com.basilalasadi.iti.plateful.ui.mealdetail;

import com.basilalasadi.iti.plateful.model.meal.Meal;

public interface MealDetailContract {
    interface View {
        void showMeal(Meal meal, boolean inFavorites);
        void showError(String message);
        void showDisconnected();
        
        void showIncredientsChecked(boolean[] checked);
        void showIngredientChecked(int position, boolean isChecked);
    }
    
    interface Presenter {
        void fetchMeal();
        void markIngredientChecked(int position, boolean isChecked);
        
        void calendarAction();
        void favoritesAction();
        void shareAction();
    }
}
