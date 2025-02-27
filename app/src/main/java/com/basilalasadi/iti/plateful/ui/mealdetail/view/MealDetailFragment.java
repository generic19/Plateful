package com.basilalasadi.iti.plateful.ui.mealdetail.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentMealDetailBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.bumptech.glide.Glide;

public class MealDetailFragment extends Fragment {
    private Meal meal;
    private IngredientsChecklistAdapter ingredientsChecklistAdapter;
    private com.basilalasadi.iti.plateful.databinding.FragmentMealDetailBinding binding;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        meal = MealDetailFragmentArgs.fromBundle(getArguments()).getMeal();
        
        binding = FragmentMealDetailBinding.inflate(inflater, container, false);
        
        TabsFragment.applySystemTopPadding(binding.materialToolbar);
        
        binding.materialToolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                .navigateUp();
        });
        
        binding.txtTitle.setText(meal.getTitle());
        binding.txtSubtitle.setText(meal.getCategory() + " meal from the " + meal.getCuisine() + " cuisine");
        
        Glide.with(container)
            .load(meal.getThumbnail())
            .error(R.drawable.bg_welcome)
            .into(binding.imageMeal);
            
        binding.txtCategory.setText(meal.getCategory());
        binding.txtCuisine.setText(meal.getCuisine());
        
        ingredientsChecklistAdapter = new IngredientsChecklistAdapter(getContext());
        ingredientsChecklistAdapter.setIngredients(meal.getIngredients());
        
        binding.recyclerIngredients.setAdapter(ingredientsChecklistAdapter);
        
        binding.webVideo.getSettings().setJavaScriptEnabled(true);
        binding.webVideo.setWebViewClient(new WebViewClient());
        binding.webVideo.loadUrl(meal.getYoutubeEmbedUrl().toString());
        
        binding.txtInstructions.setText(meal.getInstructions());
        
        return binding.getRoot();
    }
}