package com.basilalasadi.iti.plateful.ui.mealdetail.presenter;

import android.util.Log;

import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.ui.mealdetail.MealDetailContract;

import java.util.Date;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MealDetailPresenter implements MealDetailContract.Presenter {
    private static final String TAG = "MealDetailPresenter";
    
    private final MealDetailContract.View view;
    private final MealRepository mealRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public MealDetailPresenter(MealDetailContract.View view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }
    
    @Override
    public void fetchMeal(String mealId) {
        disposables.add(
        mealRepository.getById(mealId)
            .subscribe(
                view::showMeal,
                error -> {
                    Log.d(TAG, "fetchMeal", error);
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
                error -> view.showMessage(error.getLocalizedMessage(), 5000)
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
        disposables.clear();
    }
}
