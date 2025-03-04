package com.basilalasadi.iti.plateful.model.meal.datasource.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.MealPreview;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.api.MealService;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.MealDto;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealRemoteDataSourceImpl implements MealRemoteDataSource {
    private final MealService mealService;
    
    public MealRemoteDataSourceImpl(MealService mealService) {
        this.mealService = mealService;
    }
    
    @Override
    public Single<List<Meal>> searchMealByName(String query) {
        return mealService.searchMealByName(query)
            .map(MealRemoteDataSourceImpl::parseMealsList);
    }
    
    private static List<Meal> parseMealsList(String jsonString) throws JSONException {
        List<Meal> meals = new ArrayList<>();
        JSONObject root = new JSONObject(jsonString);
        
        JSONArray jsonArray = root.optJSONArray("meals");
        
        if (jsonArray == null) {
            return meals;
        }
        
        for (int i = 0; i < jsonArray.length(); i++) {
            meals.add(new Meal(jsonArray.getJSONObject(i)));
        }
        
        return meals;
    }
    
    @Override
    public Single<Meal> getMealById(String id) {
        return mealService.getMealById(id)
            .map(MealRemoteDataSourceImpl::parseMealsList)
            .map(list -> list.get(0));
    }
    
    @Override
    public Single<Meal> getRandomMeal() {
        return mealService.getRandomMeal()
            .map(MealRemoteDataSourceImpl::parseMealsList)
            .map(list -> list.get(0));
    }
    
    @Override
    public Single<List<Category>> getAllCategories() {
        return mealService.getAllCategories()
            .map(jsonString -> parseDeepStringList(jsonString, "strCategory"))
            .map(strings -> strings.stream()
                .map(Category::new)
                .collect(Collectors.toList())
            );
    }
    
    @NonNull
    private static List<String> parseDeepStringList(String jsonString, String innerKey) throws JSONException {
        List<String> list = new ArrayList<>();
        
        JSONArray jsonArray = new JSONObject(jsonString)
            .getJSONArray("meals");
        
        for (int i = 0; i < jsonArray.length(); i++) {
            String jsonObject = jsonArray.getJSONObject(i)
                .getString(innerKey);
            
            list.add(jsonObject);
        }
        
        return list;
    }
    
    @Override
    public Single<List<Cuisine>> getAllCuisines() {
        return mealService.getAllCuisines()
            .map(jsonString -> parseDeepStringList(jsonString, "strArea"))
            .map(strings -> strings.stream()
                .map(Cuisine::new)
                .collect(Collectors.toList())
            );
    }
    
    @Override
    public Single<List<MealPreview>> getMealsByCategory(Category category) {
        return mealService.getMealsByCategory(category.getName())
            .map(MealRemoteDataSourceImpl::parseMealPreviewsList);
    }
    
    private static List<MealPreview> parseMealPreviewsList(String jsonString) throws JSONException {
        List<MealPreview> meals = new ArrayList<>();
        
        JSONArray jsonArray = new JSONObject(jsonString)
            .getJSONArray("meals");
        
        for (int i = 0; i < jsonArray.length(); i++) {
            meals.add(new MealPreview(jsonArray.getJSONObject(i)));
        }
        
        return meals;
    }
    
    @Override
    public Single<List<MealPreview>> getMealsByCuisine(Cuisine cuisine) {
        return mealService.getMealsByCuisine(cuisine.getName())
            .map(MealRemoteDataSourceImpl::parseMealPreviewsList);
    }
    
    @Override
    public Single<List<Meal>> getUserMeals(@NotNull FirebaseUser user) throws IllegalArgumentException {
        return Single.create(emitter -> {
            FirebaseFirestore.getInstance()
                .collection("users/" + user.getUid() + "/meals")
                .get()
                .addOnSuccessListener(s -> {
                    List<Meal> meals = s.getDocuments()
                        .stream()
                        .map(d -> d.toObject(MealDto.class))
                        .map(MealDto::toMeal)
                        .collect(Collectors.toList());
                    
                    emitter.onSuccess(meals);
                })
                .addOnFailureListener(emitter::onError);
        });
    }
    
    @Override
    public Completable setUserMeals(FirebaseUser user, List<Meal> meals) {
        return Completable.create(emitter -> {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference mealsRef = firestore.collection("users/" + user.getUid() + "/meals");
            
            mealsRef
                .get()
                .addOnSuccessListener(s -> {
                    WriteBatch batch = firestore.batch();
                    s.getDocuments().forEach(d -> batch.delete(d.getReference()));
                    
                    batch.commit()
                        .addOnSuccessListener(v -> {
                            emitter.setDisposable(
                                Observable.fromIterable(meals)
                                    .flatMapCompletable(meal -> Completable.create(em -> {
                                        mealsRef.add(meal)
                                            .addOnSuccessListener(vv -> em.onComplete())
                                            .addOnFailureListener(em::onError);
                                    }))
                                    .subscribe(emitter::onComplete, emitter::onError)
                            );
                        })
                        .addOnFailureListener(emitter::onError);
                })
                .addOnFailureListener(emitter::onError);
        });
    }
    
    @Override
    public Single<List<CalendarMeal>> getUserCalendar(FirebaseUser user) {
        return Single.create(emitter -> {
            FirebaseFirestore.getInstance()
                .collection("users/" + user.getUid() + "/calendar")
                .get()
                .addOnSuccessListener(s -> {
                    List<CalendarMeal> calendarMeals = s.getDocuments()
                        .stream()
                        .map(d -> d.toObject(CalendarMeal.class))
                        .collect(Collectors.toList());
                    
                    emitter.onSuccess(calendarMeals);
                })
                .addOnFailureListener(emitter::onError);
        });
    }
    
    @Override
    public Completable setUserCalendar(FirebaseUser user, List<CalendarMeal> calendar) {
        return Completable.create(emitter -> {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference calendarRef = firestore.collection("users/" + user.getUid() + "/calendar");
            
            calendarRef
                .get()
                .addOnSuccessListener(s -> {
                    WriteBatch batch = firestore.batch();
                    s.getDocuments().forEach(d -> batch.delete(d.getReference()));
                    
                    batch.commit()
                        .addOnSuccessListener(v -> {
                            emitter.setDisposable(
                                Observable.fromIterable(calendar)
                                    .flatMapCompletable(meal -> Completable.create(em -> {
                                        calendarRef.add(meal)
                                            .addOnSuccessListener(vv -> em.onComplete())
                                            .addOnFailureListener(em::onError);
                                    }))
                                    .subscribe(emitter::onComplete, emitter::onError)
                            );
                        })
                        .addOnFailureListener(emitter::onError);
                })
                .addOnFailureListener(emitter::onError);
        });
    }
    
    private static class MealsList {
        private List<MealDto> meals;
        
        public MealsList() {
        }
        
        public MealsList(List<Meal> meals) {
            this.meals = meals.stream()
                .map(MealDto::new)
                .collect(Collectors.toList());
        }
    }
    
    private static class CalendarMealsList {
        private List<CalendarMeal> calendarMeals;
        
        public CalendarMealsList() {
        }
        
        public CalendarMealsList(List<CalendarMeal> calendarMeals) {
            this.calendarMeals = calendarMeals;
        }
    }
}
