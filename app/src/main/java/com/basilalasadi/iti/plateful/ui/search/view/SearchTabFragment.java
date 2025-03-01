package com.basilalasadi.iti.plateful.ui.search.view;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.TypedValueCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.basilalasadi.iti.plateful.databinding.FragmentTabSearchBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.Section;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.MealLocalDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.MealRemoteDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.api.MealService;
import com.basilalasadi.iti.plateful.ui.common.view.MealsSmallCardAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.MealsStackAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.basilalasadi.iti.plateful.ui.search.SearchContract;
import com.basilalasadi.iti.plateful.ui.search.presenter.SearchPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SearchTabFragment extends Fragment implements SearchContract.View {
    private FragmentTabSearchBinding binding;
    private SearchContract.Presenter presenter;
    
    private MealSearchSuggestionsAdapter mealSearchSuggestionsAdapter;
    private SectionsSearchSuggestionsAdapter sectionsSearchSuggestionsAdapter;
    private MealsStackAdapter searchResultsAdapter;
    private MealsSmallCardAdapter suggestedMealsAdapter;
    
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabSearchBinding.inflate(inflater, container, false);
        
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        setupSearchBar();
        setupSearchSuggestions();
        setupSearchResults();
        setupSuggestedMeals();

        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new SearchPresenter(
            this,
            MealRepository.getInstance(
                new MealLocalDataSourceImpl(getContext()),
                new MealRemoteDataSourceImpl(MealService.create())
            )
        );
        
        presenter.prefetch();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        disposables.dispose();
        presenter.dispose();
    }
    
    private void setupSearchBar() {
        binding.editSearch.setOnFocusChangeListener((v, hasFocus) -> {
            float radius = TypedValueCompat.dpToPx(16, getResources().getDisplayMetrics());
            
            if (hasFocus) {
                binding.txtLayoutSearch.setBoxCornerRadii(radius, radius, 0, 0);
                binding.cardSearch.setCardElevation(TypedValueCompat.dpToPx(4, getResources().getDisplayMetrics()));
                
                binding.groupSearchSuggestions.setVisibility(View.VISIBLE);
            } else {
                binding.txtLayoutSearch.setBoxCornerRadii(radius, radius, radius, radius);
                binding.cardSearch.setCardElevation(0);
                
                binding.groupSearchSuggestions.setVisibility(View.GONE);
            }
        });
        
        binding.editSearch.setOnEditorActionListener((v, actionId, event) -> {
            clearSearchBarFocus();
            
            String query = binding.editSearch.getText().toString();
            if (query.length() > 2) {
                presenter.searchForMeal(query);
            }
            
            return false;
        });
        
        disposables.add(
            Observable.<String>create(emitter -> {
                binding.editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    
                    @Override
                    public void afterTextChanged(Editable s) {
                        emitter.onNext(s.toString());
                    }
                });
            })
            .filter(query -> query.length() > 1)
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                input -> presenter.suggest(input),
                error -> {
                    showMessage(error.getLocalizedMessage(), 5000);
                }
            )
        );
    }
    
    private void clearSearchBarFocus() {
        binding.editSearch.clearFocus();
        
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(binding.editSearch.getWindowToken(), 0);
    }
    
    private void setupSearchSuggestions() {
        mealSearchSuggestionsAdapter = new MealSearchSuggestionsAdapter(getContext(), (meal) -> {
            clearSearchBarFocus();
            showMeal(meal);
        });
        sectionsSearchSuggestionsAdapter = new SectionsSearchSuggestionsAdapter(getContext(), section -> {
            presenter.searchSection(section);
            clearSearchBarFocus();
        });
        
        binding.recyclerTextSuggestions.setAdapter(mealSearchSuggestionsAdapter);
        binding.recyclerCardSuggestions.setAdapter(sectionsSearchSuggestionsAdapter);
    }
    
    private void setupSearchResults() {
        MealsStackAdapter.setupRecycler(binding.recyclerSearchResults);
        
        searchResultsAdapter = new MealsStackAdapter(getContext(), this::showMeal);
        binding.recyclerSearchResults.setAdapter(searchResultsAdapter);
    }
    
    private void setupSuggestedMeals() {
        suggestedMealsAdapter = new MealsSmallCardAdapter(getContext(), this::showMeal);
        binding.recyclerSuggestedMeals.setAdapter(suggestedMealsAdapter);
    }
    
    @Override
    public void showSuggestions(List<Meal> meals, List<Section> sections) {
        mealSearchSuggestionsAdapter.setItems(meals);
        sectionsSearchSuggestionsAdapter.setItems(sections);
    }
    
    @Override
    public void showResults(List<Meal> results, List<Meal> suggestedMeals) {
        searchResultsAdapter.setMeals(results);
        suggestedMealsAdapter.setMeals(suggestedMeals);
        
        binding.groupSearchResults.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void showMeal(Meal meal) {
        Navigation.findNavController(binding.getRoot())
            .navigate(SearchTabFragmentDirections.actionSearchTabFragmentToMealDetailFragment(meal.getId()));
    }
    
    @Override
    public void showMessage(String message, int duration) {
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
}