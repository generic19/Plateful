package com.basilalasadi.iti.plateful.model.meal.datasource.remote.api;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealService {
    static MealService create() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("www.themealdb.com/api/json/v1/1/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build();
        
        return retrofit.create(MealService.class);
    }
    
    @GET("search.php")
    Single<String> searchMealByName(@Query("s") String query);
    
    @GET("lookup.php")
    Single<String> getMealById(@Query("i") String id);
    
    @GET("random.php")
    Single<String> getRandomMeal();
    
    @GET("list.php?c=list")
    Single<String> getAllCategories();
    
    @GET("list.php?a=list")
    Single<String> getAllCuisines();
    
    @GET("list.php?i=list")
    Single<String> getAllIngredients();
    
    @GET("filter.php")
    Single<String> getMealsByCategory(@Query("c") String categoryName);
    
    @GET("filter.php")
    Single<String> getMealsByCuisine(@Query("a") String cuisineName);
}
