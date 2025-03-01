package com.basilalasadi.iti.plateful.ui.explore.presenter;

import android.util.Log;
import android.util.Pair;

import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Section;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.ui.explore.SectionsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SectionsPresenter implements SectionsContract.Presenter {
    private static final String TAG = "SectionsPresenter";
    
    private final Class<? extends Section> sectionsType;
    private final SectionsContract.View view;
    private final MealRepository mealRepository;
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final List<Section> sections = new ArrayList<>();
    
    public SectionsPresenter(Class<? extends Section> sectionsType, SectionsContract.View view, MealRepository mealRepository) {
        this.sectionsType = sectionsType;
        this.view = view;
        this.mealRepository = mealRepository;
    }
    
    @Override
    public void fetchAll() {
        if (sectionsType.equals(Category.class)) {
            disposables.add(
                mealRepository.getCategories()
                    .subscribe(
                        sec -> {
                            sections.addAll(sec);
                            view.showSections(sections);
                        },
                        error -> {
                            Log.d(TAG, "getCategories", error);
                            view.showMessage(error.getLocalizedMessage(), 5000);
                        }
                    )
            );
        } else if (sectionsType.equals(Cuisine.class)) {
            disposables.add(
                mealRepository.getCuisines()
                    .subscribe(
                        sec -> {
                            sections.addAll(sec);
                            view.showSections(sections);
                        },
                        error -> {
                            Log.d(TAG, "getCategories", error);
                            view.showMessage(error.getLocalizedMessage(), 5000);
                        }
                    )
            );
        }
    }
    
    @Override
    public void filter(String query) {
        String queryLowerCase = query.toLowerCase();
        
        List<Section> result = sections.parallelStream()
            .map(
                s -> new Pair<>(
                    s,
                    s.getName()
                        .toLowerCase()
                        .indexOf(queryLowerCase)
                )
            )
            .filter(p -> p.second != -1)
            .sorted(
                (p1, p2) -> p1.second.equals(p2.second)
                    ? p1.first.getName().compareToIgnoreCase(p2.first.getName())
                    : p1.second.compareTo(p2.second)
            )
            .map(p -> p.first)
            .collect(Collectors.toList());
        
        view.showSections(result);
    }
    
    @Override
    public void dispose() {
        disposables.dispose();
    }
}
