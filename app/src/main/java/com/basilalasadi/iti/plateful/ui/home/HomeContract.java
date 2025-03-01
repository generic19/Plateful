package com.basilalasadi.iti.plateful.ui.home;

import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface HomeContract {
    interface View {
        void showLoading();
        void hideLoading();
        
        void showMessage(String message, int duration);
        
        void showFeaturedMeal(Meal meal);
        void showSuggestedCuisine(Cuisine cuisine, List<Meal> meals);
        void showDailySelection(List<Meal> meals);
        void showMealToPrepare(Meal meal);
    }
    
    interface Presenter {
        void fetchData();
        
        void clearAll();
        void backupData();
        void restoreData();
        
        void dispose();
    }
}
