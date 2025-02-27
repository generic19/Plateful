package com.basilalasadi.iti.plateful.ui.search.view;

import static com.basilalasadi.iti.plateful.ui.home.view.HomeTabFragment.EXAMPLE_MEALS;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.util.TypedValueCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentTabSearchBinding;
import com.basilalasadi.iti.plateful.databinding.ItemSearchChipBinding;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Ingredient;
import com.basilalasadi.iti.plateful.ui.common.view.MealsSmallCardAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.MealsStackAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class SearchTabFragment extends Fragment {
    private FragmentTabSearchBinding binding;
    private TextSearchSuggestionsAdapter textSearchSuggestionsAdapter;
    private SectionsSearchSuggestionsAdapter sectionsSearchSuggestionsAdapter;
    private MealsStackAdapter searchResultsAdapter;
    private MealsSmallCardAdapter suggestedMealsAdapter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabSearchBinding.inflate(inflater, container, false);
        
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        setupSearchBar();
        setupDefaultSuggestions();
        setupSearchSuggestions();
        setupSearchResults();
        setupSuggestedMeals();
        
        textSearchSuggestionsAdapter.setItems(List.of(
            "Spicy Arabiatta Penne",
            "Spicy Arabiatta",
            "Spicy"
        ));
        
        sectionsSearchSuggestionsAdapter.setItems(List.of(
            new Cuisine("Greek"),
            new Ingredient("Chicken", "1 Pound"),
            new Category("Vegetarian")
        ));
        
        return binding.getRoot();
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
            binding.editSearch.clearFocus();
            
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            
            return false;
        });
    }
    
    private void setupDefaultSuggestions() {
        showRecentSearches(List.of("Egyptian", "Italian", "Greek"));
        showPopularSearches(List.of("Egyptian", "Italian", "Greek"));
    }
    
    private void setupSearchSuggestions() {
        textSearchSuggestionsAdapter = new TextSearchSuggestionsAdapter(getContext(), term -> {
        
        });
        
        sectionsSearchSuggestionsAdapter = new SectionsSearchSuggestionsAdapter(getContext(), section -> {
        
        });
        
        binding.recyclerTextSuggestions.setAdapter(textSearchSuggestionsAdapter);
        binding.recyclerCardSuggestions.setAdapter(sectionsSearchSuggestionsAdapter);
    }
    
    public void showRecentSearches(List<String> terms) {
        fillChipGroup(terms, binding.chipGroupRecentSearches, R.drawable.ic_search);
    }
    
    public void showPopularSearches(List<String> terms) {
        fillChipGroup(terms, binding.chipGroupPopularSearches, R.drawable.ic_trend_up);
    }
    
    private void fillChipGroup(List<String> terms, ChipGroup chipGroup, @DrawableRes int iconResource) {
        chipGroup.removeAllViews();
        
        for (String term : terms) {
            Chip chip = ItemSearchChipBinding.inflate(getLayoutInflater()).getRoot();
            
            chip.setChipIconResource(iconResource);
            chip.setText(term);
            chip.setOnClickListener(v -> searchChipClicked(term));
            
            chipGroup.addView(chip);
        }
    }
    
    private void setupSearchResults() {
        MealsStackAdapter.setupRecycler(binding.recyclerSearchResults);
        
        searchResultsAdapter = new MealsStackAdapter(getContext(), meal -> {});
        searchResultsAdapter.setMeals(EXAMPLE_MEALS);
        
        binding.recyclerSearchResults.setAdapter(searchResultsAdapter);
    }
    
    private void setupSuggestedMeals() {
        suggestedMealsAdapter = new MealsSmallCardAdapter(getContext(), meal -> {});
        suggestedMealsAdapter.setMeals(EXAMPLE_MEALS);
        
        binding.recyclerSuggestedMeals.setAdapter(suggestedMealsAdapter);
    }
    
    private void searchChipClicked(String term) {
    
    }
}