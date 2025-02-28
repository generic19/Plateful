package com.basilalasadi.iti.plateful.model.meal.datasource;

import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.MealPreview;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.MealLocalDataSource;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.MealRemoteDataSource;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRepository {
    private static volatile MealRepository instance;
    
    private final MealLocalDataSource localDataSource;
    private final MealRemoteDataSource remoteDataSource;
    
    public static MealRepository getInstance(MealLocalDataSource localDataSource, MealRemoteDataSource remoteDataSource) {
        if (instance == null) {
            synchronized (MealRepository.class) {
                if (instance == null) {
                    instance = new MealRepository(localDataSource, remoteDataSource);
                }
            }
        }
        return instance;
    }
    
    private MealRepository(MealLocalDataSource localDataSource, MealRemoteDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }
    
    public Single<Meal> getRandomMeal() {
        return remoteDataSource.getRandomMeal()
            .retry(3)
            .doAfterSuccess(meal -> localDataSource.putMeal(meal).subscribeOn(Schedulers.io()).subscribe())
            .compose(scheduleSingle());
    }
    
    public Observable<List<Meal>> searchByName(String query) {
        return Observable.concat(List.of(
            localDataSource.searchMealByName(query)
                    .onErrorComplete()
                    .toObservable(),
            
            remoteDataSource.searchMealByName(query)
                .retry(3)
                .doAfterSuccess(meals -> localDataSource.putMeals(meals).subscribeOn(Schedulers.io()).subscribe())
                .toObservable()
        ))
        .compose(scheduleObservable());
    }
    
    public Single<Meal> getById(String mealId) {
        return localDataSource.getMealById(mealId)
            .compose(scheduleSingle())
            .onErrorResumeWith(
                remoteDataSource.getMealById(mealId)
                    .retry(3)
                    .doAfterSuccess(meal -> localDataSource.putMeal(meal).subscribeOn(Schedulers.io()).subscribe())
            )
            .compose(scheduleSingle());
    }
    
    public Single<List<Category>> getCategories() {
        return localDataSource.getCategories()
            .flatMap(categories -> {
                if (categories.isEmpty()) {
                    return remoteDataSource.getAllCategories()
                        .retry(3)
                        .doAfterSuccess(c -> localDataSource.setCategories(c).subscribeOn(Schedulers.io()).subscribe())
                        .compose(scheduleSingle());
                } else {
                    return Single.just(categories).compose(scheduleSingle());
                }
            })
            .compose(scheduleSingle());
    }
    
    public Single<List<Cuisine>> getCuisines() {
        return localDataSource.getCuisines()
            .flatMap(cuisines -> {
                if (cuisines.isEmpty()) {
                    return remoteDataSource.getAllCuisines()
                        .retry(3)
                        .doAfterSuccess(c -> localDataSource.setCuisines(c).subscribeOn(Schedulers.io()).subscribe())
                        .compose(scheduleSingle());
                } else {
                    return Single.just(cuisines).compose(scheduleSingle());
                }
            })
            .compose(scheduleSingle());
    }
    
    public Single<List<MealPreview>> getMealsByCategory(Category category) {
        return localDataSource.getMealsByCategory(category)
            .flatMap(meals -> {
                if (meals.isEmpty()) {
                    return remoteDataSource.getMealsByCategory(category)
                        .doAfterSuccess(mealPreviews -> {
                            mealPreviews.forEach(mealPreview -> {
                                remoteDataSource.getMealById(mealPreview.getId())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                            });
                        })
                        .compose(scheduleSingle());
                } else {
                    List<MealPreview> mealPreviews = meals.stream()
                        .map(MealPreview::new)
                        .collect(Collectors.toList());
                    
                    return Single.just(mealPreviews)
                        .compose(scheduleSingle());
                }
            })
            .compose(scheduleSingle());
    }
    
    public Single<List<MealPreview>> getMealsByCuisine(Cuisine cuisine) {
        return localDataSource.getMealsByCuisine(cuisine)
            .flatMap(meals -> {
                if (meals.isEmpty()) {
                    return remoteDataSource.getMealsByCuisine(cuisine)
                        .doAfterSuccess(mealPreviews -> {
                            mealPreviews.forEach(mealPreview -> {
                                remoteDataSource.getMealById(mealPreview.getId())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe();
                            });
                        })
                        .compose(scheduleSingle());
                } else {
                    List<MealPreview> mealPreviews = meals.stream()
                        .map(MealPreview::new)
                        .collect(Collectors.toList());
                    
                    return Single.just(mealPreviews)
                        .compose(scheduleSingle());
                }
            })
            .compose(scheduleSingle());
    }
    
    public Single<List<Meal>> getFavoriteMeals(FirebaseUser user) {
        return localDataSource.getUserFavorites()
            .flatMap(meals -> {
                if (meals.isEmpty()) {
                    return remoteDataSource.getUserMeals(user)
                        .doAfterSuccess(localDataSource::putMeals)
                        .compose(scheduleSingle());
                } else {
                    return Single.just(meals);
                }
            })
            .compose(scheduleSingle());
    }
    
    public Completable addFavoriteMeal(Meal meal) {
        meal.setFavorite(true);
        return localDataSource.putMeal(meal).compose(scheduleCompletable());
    }
    
    public Completable removeFavoriteMeal(Meal meal) {
        meal.setFavorite(false);
        return localDataSource.putMeal(meal).compose(scheduleCompletable());
    }
    
    public Completable setCalendarDate(Meal meal, Date date) {
        return localDataSource.setCalendarDate(meal, date);
    }
    
    public Completable removeCalendarDate(Meal meal, Date date) {
        return localDataSource.removeCalendarDate(meal, date);
    }
    
    public Completable backupUserData(FirebaseUser user) {
        return Completable.merge(List.of(
            localDataSource.getUserFavorites()
                .flatMap(meals -> remoteDataSource.setUserMeals(user, meals)
                    .toSingleDefault(0)
                )
                .ignoreElement()
                .compose(scheduleCompletable()),
            
            localDataSource.getCalendar()
                .flatMap(calendarMeals -> remoteDataSource.setUserCalendar(user, calendarMeals)
                    .toSingleDefault(0)
                )
                .ignoreElement()
                .compose(scheduleCompletable())
        ));
    }
    
    public Completable restoreUserData(FirebaseUser user) {
        return Completable.merge(List.of(
            remoteDataSource.getUserMeals(user)
                .map(meals -> meals.stream()
                    .filter(Meal::isFavorite)
                    .collect(Collectors.toList())
                )
                .flatMap(meals -> localDataSource.setUserFavorites(meals)
                    .toSingleDefault(0)
                )
                .ignoreElement()
                .compose(scheduleCompletable()),
            
            remoteDataSource.getUserCalendar(user)
                .flatMap(calendarMeals -> localDataSource.setCalendar(calendarMeals)
                    .toSingleDefault(0)
                )
                .ignoreElement()
                .compose(scheduleCompletable())
        ));
    }
    
    private static <T> SingleTransformer<T, T> scheduleSingle() {
        return upstream -> upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    
    private static CompletableTransformer scheduleCompletable() {
        return upstream -> upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
    
    private static <T> ObservableTransformer<T, T> scheduleObservable() {
        return upstream -> upstream
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}
