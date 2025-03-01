package com.basilalasadi.iti.plateful.ui.favorites;

import android.util.Log;

import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;

import java.util.Date;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FavoritesPresenter implements FavoritesContract.Presenter {
    private static final String TAG = "FavoritesPresenter";
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MealRepository mealRepository;
    private final FavoritesContract.View view;
    private final FirebaseAuthManager authManager;
    
    public FavoritesPresenter(FavoritesContract.View view, MealRepository mealRepository, FirebaseAuthManager authManager) {
        this.mealRepository = mealRepository;
        this.view = view;
        this.authManager = authManager;
    }
    
    @Override
    public void fetchMeals() {
        disposables.add(
            mealRepository.getFavoriteMeals(authManager.getCurrentUser())
                .subscribe(
                    view::showMeals,
                    error -> {
                        Log.d(TAG, "fetchMeals", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                    }
                )
        );
    }
    
    @Override
    public void calendarAction(Meal meal, Date date) {
        disposables.add(
            mealRepository.setCalendarDate(meal, date).subscribe(
                () -> {},
                error -> {
                    Log.d(TAG, "calendarAction", error);
                    view.showMessage(error.getLocalizedMessage(), 5000);
                }
            )
        );
    }
    
    @Override
    public void favoritesAction(Meal meal) {
        boolean isFavorite = meal.isFavorite();
        
        if (!isFavorite) {
            disposables.add(
                mealRepository.addFavoriteMeal(meal).subscribe(
                    () -> meal.setFavorite(true),
                    error -> {
                        Log.d(TAG, "addFavoriteMeal", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                    }
                )
            );
        } else {
            disposables.add(
                mealRepository.removeFavoriteMeal(meal).subscribe(
                    () -> {
                        meal.setFavorite(false);
                        view.removeMeal(meal);
                    },
                    error -> {
                        Log.d(TAG, "removeFavoriteMeal", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                    }
                )
            );
        }
    }
    
    @Override
    public void dispose() {
        disposables.dispose();
    }
}
