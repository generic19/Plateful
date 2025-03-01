package com.basilalasadi.iti.plateful.ui.home.presenter;

import android.util.Log;
import android.util.Pair;

import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.ui.home.HomeContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenter implements HomeContract.Presenter {
    private static final String TAG = "HomePresenter";
    
    private final HomeContract.View view;
    private final MealRepository mealRepository;
    private final FirebaseAuthManager authManager;
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public HomePresenter(HomeContract.View view, MealRepository mealRepository, FirebaseAuthManager authManager) {
        this.view = view;
        this.mealRepository = mealRepository;
        this.authManager = authManager;
    }
    
    @Override
    public void fetchData() {
        AtomicInteger counter = new AtomicInteger(4);
        
        view.showLoading();
        
        disposables.addAll(
            mealRepository.getRandomMeal().subscribe(
                meal -> {
                    view.showFeaturedMeal(meal);
                    
                    if (counter.decrementAndGet() == 0) {
                        view.hideLoading();
                    }
                },
                error -> {
                    Log.d(TAG, "fetchData: getRandomMeal", error);
                    view.showMessage(error.getLocalizedMessage(), 5000);
                    
                    if (counter.decrementAndGet() == 0) {
                        view.hideLoading();
                    }
                }
            ),
            mealRepository.getCuisines()
                .map(cuisines -> cuisines.get(new Random().nextInt(cuisines.size())))
                .flatMap(cuisine -> Single.zip(
                    Single.just(cuisine),
                    mealRepository.getMealsByCuisine(cuisine)
                        .flatMap(
                            previews -> Observable.fromIterable(previews)
                                .take(5)
                                .flatMap(preview -> mealRepository.getById(preview.getId()).toObservable())
                                .collectInto(new ArrayList<Meal>(), ArrayList::add)
                        ),
                    Pair::create
                ))
                .subscribe(
                    pair -> {
                        view.showSuggestedCuisine(pair.first, pair.second);
                        
                        if (counter.decrementAndGet() == 0) {
                            view.hideLoading();
                        }
                    },
                    error -> {
                        Log.d(TAG, "fetchData: getCuisines", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                        
                        if (counter.decrementAndGet() == 0) {
                            view.hideLoading();
                        }
                    }
                ),
            mealRepository.searchByName(
                new String[]{"a", "b", "c", "e", "i", "o", "u", "m", "f", "p"}[new Random().nextInt(10)]
            ).subscribe(
                meals -> {
                    view.showDailySelection(meals);
                    
                    if (counter.decrementAndGet() == 0) {
                        view.hideLoading();
                    }
                },
                error -> {
                    view.showMessage(error.getLocalizedMessage(), 5000);
                    
                    if (counter.decrementAndGet() == 0) {
                        view.hideLoading();
                    }
                }
            ),
            mealRepository.getCalendar()
                .flatMap(calendarMeals -> Observable.fromIterable(calendarMeals)
                    .filter(calendarMeal -> calendarMeal.getDate().after(new Date()))
                    .map(Optional::of)
                    .first(Optional.empty())
                    .flatMap(calendarMeal -> {
                        if (calendarMeal.isPresent()) {
                            String mealId = calendarMeal.get().getMealId();
                            
                            return mealRepository.getById(mealId)
                                .map(Optional::of);
                        } else {
                            return Single.just(Optional.<Meal>empty());
                        }
                    })
                )
                .subscribe(
                    meal -> {
                        meal.ifPresent(view::showMealToPrepare);
                        
                        if (counter.decrementAndGet() == 0) {
                            view.hideLoading();
                        }
                    },
                    error -> {
                        Log.d(TAG, "fetchData: getCalendar", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                        
                        if (counter.decrementAndGet() == 0) {
                            view.hideLoading();
                        }
                    }
                )
        );
    }
    
    @Override
    public void clearAll() {
        mealRepository.clearAllLocal().subscribe(
            () -> {},
            error -> Log.d(TAG, "clearAll", error)
        );
    }
    
    @Override
    public void backupData() {
        mealRepository.backupUserData(authManager.getCurrentUser()).subscribe(
            () -> {},
            error -> Log.d(TAG, "backupData", error)
        );
    }
    
    @Override
    public void restoreData() {
        mealRepository.restoreUserData(authManager.getCurrentUser()).subscribe(
            () -> {},
            error -> Log.d(TAG, "restoreData", error)
        );
    }
    
    @Override
    public void dispose() {
        disposables.clear();
    }
}
