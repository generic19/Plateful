package com.basilalasadi.iti.plateful.model.meal.datasource.local;

import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable putMeal(Meal meal);
    Completable putMeals(List<Meal> meals);
    
    Single<List<Meal>> searchMealByName(String query);
    Single<Meal> getMealById(String id);
    
    Single<List<Category>> getAllCategories();
    Single<List<Cuisine>> getAllCuisines();
    
    Single<List<Meal>> getMealsByCategory(Category category);
    Single<List<Meal>> getMealsByCuisine(Cuisine cuisine);
    
    Single<List<Meal>> getUserFavorites();
    Completable setUserFavorites(List<Meal> meals);
    
    Single<LinkedHashMap<Date, List<Meal>>> getCalendar();
    Completable setCalendarDate(Meal meal, Date date);
}
