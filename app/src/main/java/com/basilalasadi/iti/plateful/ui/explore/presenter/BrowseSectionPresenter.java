package com.basilalasadi.iti.plateful.ui.explore.presenter;

import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.Section;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.ui.explore.BrowseSectionContract;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class BrowseSectionPresenter implements BrowseSectionContract.Presenter {
    private final Section section;
    private final BrowseSectionContract.View view;
    private final MealRepository mealRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public BrowseSectionPresenter(Section section, BrowseSectionContract.View view, MealRepository mealRepository) {
        this.section = section;
        this.view = view;
        this.mealRepository = mealRepository;
    }
    
    @Override
    public void fetchMeals() {
        if (section instanceof Category) {
            disposables.add(
                mealRepository.getMealsByCategory((Category) section)
                    .flatMapObservable(Observable::fromIterable)
                    .flatMap(mealPreview -> mealRepository.getById(mealPreview.getId()).toObservable())
                    .collectInto(new ArrayList<Meal>(), ArrayList::add)
                    .subscribe(
                        view::showMeals,
                        error -> view.showMessage(error.getLocalizedMessage(), 5000)
                    )
            );
        } else if (section instanceof Cuisine) {
            disposables.add(
                mealRepository.getMealsByCuisine((Cuisine) section)
                    .flatMapObservable(Observable::fromIterable)
                    .flatMap(mealPreview -> mealRepository.getById(mealPreview.getId()).toObservable())
                    .collectInto(new ArrayList<Meal>(), ArrayList::add)
                    .subscribe(
                        view::showMeals,
                        error -> view.showMessage(error.getLocalizedMessage(), 5000)
                    )
            );
        }
    }
    
    @Override
    public void addToFavorites(Meal meal) {
        disposables.add(
            mealRepository.addFavoriteMeal(meal).subscribe(
                () -> meal.setFavorite(true),
                error -> view.showMessage(error.getLocalizedMessage(), 5000)
            )
        );
    }
    
    @Override
    public void removeFromFavorites(Meal meal) {
        disposables.add(
            mealRepository.removeFavoriteMeal(meal).subscribe(
                () -> meal.setFavorite(false),
                error -> view.showMessage(error.getLocalizedMessage(), 5000)
            )
        );
    }
    
    @Override
    public void addToCalendar(Meal meal, Date date) {
        disposables.add(
            mealRepository.setCalendarDate(meal, date).subscribe(
                () -> {},
                error -> view.showMessage(error.getLocalizedMessage(), 5000)
            )
        );
    }
}
