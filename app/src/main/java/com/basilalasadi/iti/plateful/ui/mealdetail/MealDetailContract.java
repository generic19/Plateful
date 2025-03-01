package com.basilalasadi.iti.plateful.ui.mealdetail;

import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.Date;

public interface MealDetailContract {
    interface View {
        void showMeal(Meal meal);
        void showMessage(String message, int duration);
    }
    
    interface Presenter {
        void fetchMeal(String mealId);
        
        void calendarAction(Meal meal, Date date);
        void favoritesAction(Meal meal);
        
        void dispose();
    }
}
