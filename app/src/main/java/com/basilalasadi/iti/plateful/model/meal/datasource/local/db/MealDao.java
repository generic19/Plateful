package com.basilalasadi.iti.plateful.model.meal.datasource.local.db;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Upsert;

import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CategoryDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CuisineDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.MealDto;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {
    @Upsert
    Completable putMeal(MealDto meal);
    
    @Upsert
    Completable putMeals(List<MealDto> meal);
    
    @Query("select * from MealDto where title = :query")
    Single<List<MealDto>> searchMealByName(String query);
    
    @Query("select * from MealDto where id = :id")
    Single<MealDto> getMealById(String id);
    
    @Query("select * from CategoryDto")
    Single<List<CategoryDto>> getAllCategories();
    
    @Query("select * from CuisineDto")
    Single<List<CuisineDto>> getAllCuisines();
    
    @Query("select * from MealDto where category = :categoryName")
    Single<List<MealDto>> getMealsByCategory(String categoryName);
    
    @Query("select * from MealDto where cuisine = :cuisineName")
    Single<List<MealDto>> getMealsByCuisine(String cuisineName);
    
    @Query("select * from MealDto where isFavorite = 1")
    Single<List<MealDto>> getUserFavorites();
    
    @Query("update MealDto set isFavorite = 0")
    Completable clearFavorites();
    
    @Query("update MealDto set isFavorite = 1 where id in (:mealIds)")
    Completable setIsFavorite(List<String> mealIds);
    
    @Query("update MealDto set isFavorite = 0 where id not in (:mealIds)")
    Completable clearIsFavoriteFromExcluded(List<String> mealIds);
    
    @Transaction
    default Completable setFavorites(List<String> mealIds) {
        return Completable.merge(List.of(
            setIsFavorite(mealIds),
            clearIsFavoriteFromExcluded(mealIds)
        ));
    }
    
    @Query("update MealDto set calendarDate = :date where id = :mealId")
    Completable setCalendarDate(String mealId, @Nullable Date date);
    
    @Query("select distinct calendarDate from MealDto order by calendarDate asc")
    List<Date> getCalendarDates();
    
    @Query("select * from mealDto where calendarDate = :date")
    Single<List<MealDto>> getMealsByCalendarDate(Date date);
    
    @Transaction
    default Single<LinkedHashMap<Date, List<MealDto>>> getCalendar() {
        return Observable.fromIterable(getCalendarDates())
            .flatMapSingle(this::getMealsByCalendarDate)
            .collectInto(new LinkedHashMap<>(), (map, meals) -> {
                if (!meals.isEmpty()) {
                    map.put(meals.get(0).getCalendarDate(), meals);
                }
            });
    }
}
