package com.basilalasadi.iti.plateful.ui.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.Section;

import java.util.List;

public interface SearchContract {
    interface View {
        void showSuggestedSearches(List<String> recentSearches, List<String> popularSearches);
        void showSuggestions(List<String> mealSuggestions, List<Section> sections);
        void showResults(@Nullable List<Meal> results, @NonNull List<Meal> suggestedMeals);
    }
    
    interface Presenter {
        void fetchSuggestedSearches();
        void suggest(String input);
        void searchForMeal(String name);
        void searchSection(Section section);
    }
}
