package com.basilalasadi.iti.plateful.model.meal.datasource.local;

import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealLocalDataSource {
    Completable clearAll();
    
    Completable putMeal(Meal meal);
    Completable putMeals(List<Meal> meals);
    
    Single<List<Meal>> searchMealByName(String query);
    Single<Meal> getMealById(String id);
    
    Single<List<Category>> getCategories();
    Single<List<Cuisine>> getCuisines();
    
    Completable setCategories(List<Category> categories);
    Completable setCuisines(List<Cuisine> cuisines);
    
    Single<List<Meal>> getMealsByCategory(Category category);
    Single<List<Meal>> getMealsByCuisine(Cuisine cuisine);
    
    Single<List<Meal>> getUserFavorites();
    Completable setUserFavorites(List<Meal> meals);
    
    Single<List<CalendarMeal>> getCalendar();
    Completable setCalendarDate(Meal meal, Date date);
    Completable removeCalendarDate(Meal meal, Date date);
    Completable setCalendar(List<CalendarMeal> calendar);
}
