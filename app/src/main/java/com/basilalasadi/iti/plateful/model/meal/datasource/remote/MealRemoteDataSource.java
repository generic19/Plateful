package com.basilalasadi.iti.plateful.model.meal.datasource.remote;

import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Ingredient;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.MealPreview;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealRemoteDataSource {
    Single<List<Meal>> searchMealByName(String query);
    Single<Meal> getMealById(String id);
    Single<Meal> getRandomMeal();
    
    Single<List<Category>> getAllCategories();
    Single<List<Cuisine>> getAllCuisines();
    Single<List<Ingredient>> getAllIngredients();
    
    Single<List<MealPreview>> getMealsByCategory(Category category);
    Single<List<MealPreview>> getMealsByCuisine(Cuisine cuisine);
    
    Single<List<Meal>> getUserMeals(FirebaseUser user);
    Completable setUserMeals(FirebaseUser user, List<Meal> meals);
}
