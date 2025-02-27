package com.basilalasadi.iti.plateful.ui.home;

import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.List;

public interface HomeContract {
    interface View {
        void showFeaturedMeal(Meal meal);
        void showSuggestedCuisine(Cuisine cuisine, List<Meal> meals);
        void showDailySelection(List<Meal> meals);
        void showMealToPrepare(Meal meal);
        void hideMealToPrepare();
    }
    
    interface Presenter {
        void fetchData();
    }
}
