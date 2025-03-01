package com.basilalasadi.iti.plateful.ui.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.MealPreview;
import com.basilalasadi.iti.plateful.model.meal.Section;

import java.util.List;

public interface SearchContract {
    interface View {
        void showSuggestions(List<Meal> mealSuggestions, List<Section> sections);
        void showResults(@Nullable List<Meal> results, List<Meal> suggestedMeals);
        void showMeal(Meal meal);
        
        void showMessage(String message, int duration);
    }
    
    interface Presenter {
        void prefetch();
        void suggest(String input);
        
        void searchForMeal(String name);
        void searchSection(Section section);
        
        void dispose();
    }
}
