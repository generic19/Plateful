package com.basilalasadi.iti.plateful.ui.search.presenter;

import android.util.Log;

import androidx.core.util.Pair;

import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.MealPreview;
import com.basilalasadi.iti.plateful.model.meal.Section;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.ui.search.SearchContract;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchPresenter implements SearchContract.Presenter {
    private static final String TAG = "SearchPresenter";
    
    private final SearchContract.View view;
    private final MealRepository mealRepository;
    
    private final List<Cuisine> cuisines = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    
    private final List<Meal> extraMealSuggestions = new ArrayList<>();
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public SearchPresenter(SearchContract.View view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }
    
    @Override
    public void prefetch() {
        disposables.addAll(
            mealRepository.getCuisines().subscribe(
                cuisines::addAll,
                error -> {
                    Log.d(TAG, "getCuisines", error);
                    view.showMessage(error.getLocalizedMessage(), 5000);
                }
            ),
            
            mealRepository.getCategories().subscribe(
                categories::addAll,
                error -> {
                    Log.d(TAG, "getCategories", error);
                    view.showMessage(error.getLocalizedMessage(), 5000);
                }
            ),
            
            mealRepository.searchByName(
                new String[]{"a", "b", "c", "e", "i", "o", "u", "m", "f", "p"}[new Random().nextInt(10)]
            ).subscribe(
                extraMealSuggestions::addAll,
                error -> {
                    Log.d(TAG, "searchByName", error);
                    view.showMessage(error.getLocalizedMessage(), 5000);
                }
            )
        );
    }
    
    @Override
    public void suggest(String input) {
        List<Section> sectionSuggestions = new ArrayList<>();
        
        cuisines.parallelStream()
            .map(section -> Pair.create(section, section.getName().toLowerCase().indexOf(input.toLowerCase())))
            .filter(pair -> pair.second != -1)
            .sorted(Comparator.comparing(p -> p.second))
            .limit(2)
            .map(pair -> pair.first)
            .forEachOrdered(sectionSuggestions::add);
        
        categories.parallelStream()
            .map(section -> Pair.create(section, section.getName().toLowerCase().indexOf(input.toLowerCase())))
            .filter(pair -> pair.second != -1)
            .sorted(Comparator.comparing(p -> p.second))
            .limit(2)
            .map(pair -> pair.first)
            .forEachOrdered(sectionSuggestions::add);
        
        disposables.add(
            mealRepository.searchByName(input)
                .subscribe(
                    meals -> {
                        view.showSuggestions(
                            meals.stream().limit(4).collect(Collectors.toList()),
                            sectionSuggestions
                        );
                    },
                    error -> {
                        Log.d(TAG, "searchByName", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                    }
                )
        );
    }
    
    @Override
    public void searchForMeal(String name) {
        disposables.add(
            mealRepository.searchByName(name)
            .subscribe(
                meals -> view.showResults(meals, extraMealSuggestions),
                error -> {
                    Log.d(TAG, "searchForMeal", error);
                    view.showMessage(error.getLocalizedMessage(), 5000);
                }
            )
        );
    }
    
    @Override
    public void searchSection(Section section) {
        if (section instanceof Category) {
            showMeals(mealRepository.getMealsByCategory((Category) section));
        } else if (section instanceof Cuisine) {
            showMeals(mealRepository.getMealsByCuisine((Cuisine) section));
        }
    }
    
    private void showMeals(Single<List<MealPreview>> mealPreviewsSingle) {
        disposables.add(
            mealPreviewsSingle.flatMap(mealPreviews ->
                Observable.fromIterable(mealPreviews)
                    .flatMap(mealPreview -> mealRepository.getById(mealPreview.getId()).toObservable())
                    .collectInto(new ArrayList<Meal>(), ArrayList::add)
            )
            .subscribe(
                meals -> view.showResults(meals, extraMealSuggestions),
                error -> {
                    Log.d(TAG, "showMeals", error);
                    view.showMessage(error.getLocalizedMessage(), 5000);
                }
            )
        );
    }
    
    @Override
    public void dispose() {
        disposables.clear();
    }
}
