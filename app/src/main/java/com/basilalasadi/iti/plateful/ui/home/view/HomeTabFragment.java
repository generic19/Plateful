package com.basilalasadi.iti.plateful.ui.home.view;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.databinding.FragmentTabHomeBinding;
import com.basilalasadi.iti.plateful.databinding.ViewFeaturedMealBinding;
import com.basilalasadi.iti.plateful.model.meal.Ingredient;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.ui.common.view.MealsSmallCardAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.MealsStackAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;

import java.util.ArrayList;
import java.util.List;


public class HomeTabFragment extends Fragment {
    private MealsStackAdapter dailySelectionAdapter;
    private MealsSmallCardAdapter suggestedCuisineAdapter;
    private FragmentTabHomeBinding binding;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabHomeBinding.inflate(inflater, container, false);
        
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        setupDailySelection();
        
        setupFeaturedMeal(
            binding.viewTodaysMeal,
            "Today's Meal",
            "Picked for you today",
            EXAMPLE_MEALS.get(0)
        );
        
        setupFeaturedMeal(
            binding.viewMealToPrepare,
            "Meal to Prepare",
            "Today from your calendar",
            EXAMPLE_MEALS.get(1)
        );
        
        setupSuggestedCuisine("Greek");
        
        return binding.getRoot();
    }
    
    private void setupSuggestedCuisine(String cuisine) {
        binding.viewSuggestedCuisine.txtTitle.setText(cuisine);
        binding.viewSuggestedCuisine.txtSubtitle.setText("Suggested cuisine");
        
        suggestedCuisineAdapter = new MealsSmallCardAdapter(getContext(), meal -> {
            Navigation.findNavController(binding.getRoot())
                .navigate(HomeTabFragmentDirections.actionHomeTabFragmentToMealDetailFragment(meal));
        });
        
        suggestedCuisineAdapter.setMeals(EXAMPLE_MEALS);
        
        binding.viewSuggestedCuisine.recycler.setAdapter(suggestedCuisineAdapter);
    }
    
    private void setupDailySelection() {
        MealsStackAdapter.setupRecycler(binding.viewDailySelection.recycler);
        
        dailySelectionAdapter = new MealsStackAdapter(getContext(), meal -> {
            Navigation.findNavController(binding.getRoot())
                .navigate(HomeTabFragmentDirections.actionHomeTabFragmentToMealDetailFragment(meal));
        });
        
        dailySelectionAdapter.setMeals(EXAMPLE_MEALS);
        
        binding.viewDailySelection.recycler.setAdapter(dailySelectionAdapter);
    }
    
    private void setupFeaturedMeal(ViewFeaturedMealBinding viewFeaturedMealBinding, String title, String subtitle, Meal meal) {
        viewFeaturedMealBinding.txtTitle.setText(title);
        viewFeaturedMealBinding.txtSubtitle.setText(subtitle);
        viewFeaturedMealBinding.txtTag.setText(meal.getCategory());
        viewFeaturedMealBinding.txtMealTitle.setText(meal.getTitle());
        viewFeaturedMealBinding.txtMealSubtitle.setText(meal.getCuisine());
        
        viewFeaturedMealBinding.blurCuisine.setupWith(viewFeaturedMealBinding.cardMealImage);
        viewFeaturedMealBinding.blurCuisine.setClipToOutline(true);
        viewFeaturedMealBinding.blurCuisine.setBlurRadius(13);
        
        viewFeaturedMealBinding.cardMealImage.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(HomeTabFragmentDirections.actionHomeTabFragmentToMealDetailFragment(meal));
        });
    }
    
    public static final List<Meal> EXAMPLE_MEALS = List.of(
        new Meal(
            "52771",
            "Spicy Arrabiata Penne",
            "Vegetarian",
            "Italian",
            "Preheat oven to 350° F. Spray a 9x13-inch baking pan with non-stick spray.\n\nCombine soy sauce, ½ cup water, brown sugar, ginger and garlic in a small saucepan and cover. Bring to a boil over medium heat. Remove lid and cook for one minute once boiling.\n\nMeanwhile, stir together the corn starch and 2 tablespoons of water in a separate dish until smooth. Once sauce is boiling, add mixture to the saucepan and stir to combine. Cook until the sauce starts to thicken then remove from heat.\n\nPlace the chicken breasts in the prepared pan. Pour one cup of the sauce over top of chicken. Place chicken in oven and bake 35 minutes or until cooked through. Remove from oven and shred chicken in the dish using two forks.\n\n*Meanwhile, steam or cook the vegetables according to package directions.\n\nAdd the cooked vegetables and rice to the casserole dish with the chicken. Add most of the remaining sauce, reserving a bit to drizzle over the top when serving. Gently toss everything together in the casserole dish until combined. Return to oven and cook 15 minutes. Remove from oven and let stand 5 minutes before serving. Drizzle each serving with remaining sauce. Enjoy!",
            "1IszT_guI08",
            null,
            Uri.parse("https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg"),
            new ArrayList<>(List.of("Pasta", "Curry")),
            new ArrayList<>(List.of(
                new Ingredient("penne rigate", "1 pound"),
                new Ingredient("olive oil", "1/4 cup"),
                new Ingredient("garlic", "3 cloves")
            ))
        ),
        new Meal(
            "52961",
            "Budino Di Ricotta",
            "Vegetarian",
            "Italian",
            "Bring a large pot of water to a boil.",
            "1IszT_guI08",
            null,
            Uri.parse("https://www.themealdb.com/images/media/meals/1549542877.jpg"),
            new ArrayList<>(List.of("Pasta", "Curry")),
            new ArrayList<>(List.of(
                new Ingredient("penne rigate", "1 pound"),
                new Ingredient("olive oil", "1/4 cup"),
                new Ingredient("garlic", "3 cloves")
            ))
        ),
        new Meal(
            "52796",
            "Chicken Alfredo Primavera",
            "Vegetarian",
            "Italian",
            "Bring a large pot of water to a boil.",
            "1IszT_guI08",
            null,
            Uri.parse("https://www.themealdb.com/images/media/meals/syqypv1486981727.jpg"),
            new ArrayList<>(List.of("Pasta", "Curry")),
            new ArrayList<>(List.of(
                new Ingredient("penne rigate", "1 pound"),
                new Ingredient("olive oil", "1/4 cup"),
                new Ingredient("garlic", "3 cloves")
            ))
        )
    );
}
