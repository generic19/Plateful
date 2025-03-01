package com.basilalasadi.iti.plateful.model.meal.datasource.local.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Upsert;

import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CategoryDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CuisineDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.MealDto;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface MealDao {
    @Upsert
    Completable putMeal(MealDto meal);
    
    @Upsert
    Completable putMeals(List<MealDto> meal);
    
    @Query("select * from MealDto where title like '%' || :query || '%'")
    Single<List<MealDto>> searchMealByName(String query);
    
    @Query("select * from MealDto where id = :id")
    Single<MealDto> getMealById(String id);
    
    @Query("select * from CategoryDto")
    Single<List<CategoryDto>> getAllCategories();
    
    @Query("select * from CuisineDto")
    Single<List<CuisineDto>> getAllCuisines();
    
    @Query("delete from CategoryDto")
    Completable clearCategories();
    
    @Query("delete from CuisineDto")
    Completable clearCuisines();
    
    @Insert
    Completable insertCategories(List<CategoryDto> categories);
    
    @Insert
    Completable insertCuisines(List<CuisineDto> cuisines);
    
    default Completable setCategories(List<CategoryDto> categories) {
        return Completable.concat(List.of(
            clearCategories(),
            insertCategories(categories)
        ));
    }
    
    default Completable setCuisines(List<CuisineDto> cuisines) {
        return Completable.concat(List.of(
            clearCuisines(),
            insertCuisines(cuisines)
        ));
    }
    
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
    
    default Completable setFavorites(List<String> mealIds) {
        return Completable.merge(List.of(
            setIsFavorite(mealIds),
            clearIsFavoriteFromExcluded(mealIds)
        ));
    }
}
