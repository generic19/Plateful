package com.basilalasadi.iti.plateful.model.meal.datasource.local;

import android.content.Context;

import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CategoryDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CuisineDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.MealDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.db.CalendarDao;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.db.MealDao;
import com.basilalasadi.iti.plateful.util.AppDatabase;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealLocalDataSourceImpl implements MealLocalDataSource {
    private final MealDao mealDao;
    private final CalendarDao calendarDao;
    
    public MealLocalDataSourceImpl(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        
        mealDao = database.getMealDao();
        calendarDao = database.getCalendarDao();
    }
    
    public Completable clearAll() {
        return Completable.merge(List.of(
            mealDao.clearFavorites().subscribeOn(Schedulers.io()),
            calendarDao.clearCalendar().subscribeOn(Schedulers.io())
        ));
    }
    
    @Override
    public Completable putMeal(Meal meal) {
        return mealDao.putMeal(new MealDto(meal));
    }
    
    @Override
    public Completable putMeals(List<Meal> meals) {
        return mealDao.putMeals(meals.stream().map(MealDto::new).collect(Collectors.toList()));
    }
    
    @Override
    public Single<List<Meal>> searchMealByName(String query) {
        return mealDao.searchMealByName(query).map(Mappers.meal);
    }
    
    @Override
    public Single<Meal> getMealById(String id) {
        return mealDao.getMealById(id).map(MealDto::toMeal);
    }
    
    @Override
    public Single<List<Category>> getCategories() {
        return mealDao.getAllCategories().map(Mappers.category);
    }
    
    @Override
    public Single<List<Cuisine>> getCuisines() {
        return mealDao.getAllCuisines().map(Mappers.cuisine);
    }
    
    @Override
    public Completable setCategories(List<Category> categories) {
        return mealDao.setCategories(categories.stream().map(CategoryDto::new).collect(Collectors.toList()));
    }
    
    @Override
    public Completable setCuisines(List<Cuisine> cuisines) {
        return mealDao.setCuisines(cuisines.stream().map(CuisineDto::new).collect(Collectors.toList()));
    }
    
    @Override
    public Single<List<Meal>> getMealsByCategory(Category category) {
        return mealDao.getMealsByCategory(category.getName()).map(Mappers.meal);
    }
    
    @Override
    public Single<List<Meal>> getMealsByCuisine(Cuisine cuisine) {
        return mealDao.getMealsByCuisine(cuisine.getName()).map(Mappers.meal);
    }
    
    @Override
    public Single<List<Meal>> getUserFavorites() {
        return mealDao.getUserFavorites().map(Mappers.meal);
    }
    
    @Override
    public Completable setUserFavorites(List<Meal> meals) {
        return mealDao.setFavorites(
            meals.stream()
                .map(Meal::getId)
                .collect(Collectors.toList())
        );
    }
    
    @Override
    public Single<List<CalendarMeal>> getCalendar() {
        return calendarDao.getCalendar();
    }
    
    @Override
    public Completable setCalendarDate(Meal meal, Date date) {
        return calendarDao.putMeal(new CalendarMeal(meal, date));
    }
    
    @Override
    public Completable removeCalendarDate(Meal meal, Date date) {
        return calendarDao.remove(new CalendarMeal(meal, date));
    }
    
    @Override
    public Completable setCalendar(List<CalendarMeal> calendar) {
        return calendarDao.setCalendar(calendar);
    }
    
    private static class Mappers {
        private static final Function<List<MealDto>, List<Meal>> meal =
            dtos -> dtos.stream()
                .map(MealDto::toMeal)
                .collect(Collectors.toList());
        
        private static final Function<List<CategoryDto>, List<Category>> category =
            dtos -> dtos.stream()
                .map(CategoryDto::toCategory)
                .collect(Collectors.toList());
        
        private static final Function<List<CuisineDto>, List<Cuisine>> cuisine =
            dtos -> dtos.stream()
                .map(CuisineDto::toCuisine)
                .collect(Collectors.toList());
    }
}
