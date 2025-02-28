package com.basilalasadi.iti.plateful.model.meal.datasource.local;

import android.content.Context;

import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CategoryDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CuisineDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.MealDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.db.MealDao;
import com.basilalasadi.iti.plateful.util.AppDatabase;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;

public class MealLocalDataSourceImpl implements MealLocalDataSource {
    private final MealDao dao;
    
    public MealLocalDataSourceImpl(Context context) {
        dao = AppDatabase.getInstance(context).getMealDao();
    }
    
    @Override
    public Completable putMeal(Meal meal) {
        return dao.putMeal(new MealDto(meal));
    }
    
    @Override
    public Completable putMeals(List<Meal> meals) {
        return dao.putMeals(meals.stream().map(MealDto::new).collect(Collectors.toList()));
    }
    
    @Override
    public Single<List<Meal>> searchMealByName(String query) {
        return dao.searchMealByName(query).map(Mappers.meal);
    }
    
    @Override
    public Single<Meal> getMealById(String id) {
        return dao.getMealById(id).map(MealDto::toMeal);
    }
    
    @Override
    public Single<List<Category>> getAllCategories() {
        return dao.getAllCategories().map(Mappers.category);
    }
    
    @Override
    public Single<List<Cuisine>> getAllCuisines() {
        return dao.getAllCuisines().map(Mappers.cuisine);
    }
    
    @Override
    public Single<List<Meal>> getMealsByCategory(Category category) {
        return dao.getMealsByCategory(category.getName()).map(Mappers.meal);
    }
    
    @Override
    public Single<List<Meal>> getMealsByCuisine(Cuisine cuisine) {
        return dao.getMealsByCuisine(cuisine.getName()).map(Mappers.meal);
    }
    
    @Override
    public Single<List<Meal>> getUserFavorites() {
        return dao.getUserFavorites().map(Mappers.meal);
    }
    
    @Override
    public Completable setUserFavorites(List<Meal> meals) {
        return dao.setFavorites(
            meals.stream()
                .map(Meal::getId)
                .collect(Collectors.toList())
        );
    }
    
    @Override
    public Single<LinkedHashMap<Date, List<Meal>>> getCalendar() {
        return dao.getCalendar()
            .map(map -> map.entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                            .stream()
                            .map(MealDto::toMeal)
                            .collect(Collectors.toList()),
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                    )
                )
            );
    }
    
    @Override
    public Completable setCalendarDate(Meal meal, Date date) {
        return dao.setCalendarDate(meal.getId(), date);
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
