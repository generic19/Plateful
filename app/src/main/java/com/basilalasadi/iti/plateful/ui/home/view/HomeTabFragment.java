package com.basilalasadi.iti.plateful.ui.home.view;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.ProfileDialogFragment;
import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentTabHomeBinding;
import com.basilalasadi.iti.plateful.databinding.ViewFeaturedMealBinding;
import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Ingredient;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.MealLocalDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.MealRemoteDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.api.MealService;
import com.basilalasadi.iti.plateful.ui.common.view.MealsSmallCardAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.MealsStackAdapter;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.basilalasadi.iti.plateful.ui.home.HomeContract;
import com.basilalasadi.iti.plateful.ui.home.presenter.HomePresenter;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class HomeTabFragment extends Fragment implements HomeContract.View, ProfileDialogFragment.Listener {
    private FragmentTabHomeBinding binding;
    private HomeContract.Presenter presenter;
    
    private FirebaseAuthManager authManager;
    
    private MealsStackAdapter dailySelectionAdapter;
    private MealsSmallCardAdapter suggestedCuisineAdapter;
    private @io.reactivex.rxjava3.annotations.NonNull Disposable disposable;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTabHomeBinding.inflate(inflater, container, false);
        TabsFragment.applySystemTopPadding(binding.getRoot());
        
        authManager = FirebaseAuthManager.getInstance(getContext());
        
        FirebaseUser currentUser = authManager.getCurrentUser();
        
        int hour = LocalTime.now().getHour();
        String timeOfDay;
        
        if (hour >= 5 && hour < 12) {
            timeOfDay = "Morning";
        } else if (hour >= 12 && hour < 18) {
            timeOfDay = "Afternoon";
        } else {
            timeOfDay = "Evening";
        }
        
        String userName = currentUser.getDisplayName();
        
        if (userName != null && !userName.isEmpty()) {
            userName = userName.split(" ", 2)[0];
        } else {
            userName = "Guest";
        }
        
        binding.txtGreeting.setText(String.format("%s, %s", timeOfDay, userName));
        
        Uri photoUrl = currentUser.getPhotoUrl();
        
        
        if (photoUrl != null) {
            binding.btnProfile.setImageTintList(null);
            
            Glide.with(getContext())
                .load(photoUrl)
                .into(binding.btnProfile);
        }
        
        binding.btnProfile.setOnClickListener(v -> {
            new ProfileDialogFragment(
                this,
                new ProfileDialogFragment.UserInfo(
                    currentUser.getDisplayName(),
                    currentUser.getEmail(),
                    currentUser.getPhotoUrl()
                )
            ).show(getChildFragmentManager(), "profile");
        });
        
        return binding.getRoot();
    }
    
    private void setupSuggestedCuisine(String cuisine, List<Meal> meals) {
        binding.viewSuggestedCuisine.txtTitle.setText(cuisine);
        binding.viewSuggestedCuisine.txtSubtitle.setText(R.string.suggested_cuisine);
        
        suggestedCuisineAdapter = new MealsSmallCardAdapter(getContext(), meal -> {
            Navigation.findNavController(binding.getRoot())
                .navigate(HomeTabFragmentDirections.actionHomeTabFragmentToMealDetailFragment(meal.getId()));
        });
        
        suggestedCuisineAdapter.setMeals(meals);
        
        binding.viewSuggestedCuisine.recycler.setAdapter(suggestedCuisineAdapter);
    }
    
    private void setupDailySelection(List<Meal> meals) {
        MealsStackAdapter.setupRecycler(binding.viewDailySelection.recycler);
        
        dailySelectionAdapter = new MealsStackAdapter(getContext(), meal -> {
            Navigation.findNavController(binding.getRoot())
                .navigate(HomeTabFragmentDirections.actionHomeTabFragmentToMealDetailFragment(meal.getId()));
        });
        
        dailySelectionAdapter.setMeals(meals);
        binding.viewDailySelection.recycler.setAdapter(dailySelectionAdapter);
    }
    
    private void setupFeaturedMeal(ViewFeaturedMealBinding viewFeaturedMealBinding, String title, String subtitle, Meal meal) {
        viewFeaturedMealBinding.txtTitle.setText(title);
        viewFeaturedMealBinding.txtSubtitle.setText(subtitle);
        viewFeaturedMealBinding.txtTag.setText(meal.getCategory());
        viewFeaturedMealBinding.txtMealTitle.setText(meal.getTitle());
        viewFeaturedMealBinding.txtMealSubtitle.setText(meal.getCuisine());
        
        Glide.with(getContext())
            .load(meal.getThumbnail())
            .into(viewFeaturedMealBinding.imageView);
        
        viewFeaturedMealBinding.blurCuisine.setupWith(viewFeaturedMealBinding.cardMealImage);
        viewFeaturedMealBinding.blurCuisine.setClipToOutline(true);
        viewFeaturedMealBinding.blurCuisine.setBlurRadius(13);
        
        viewFeaturedMealBinding.cardMealImage.setOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                    .navigate(HomeTabFragmentDirections.actionHomeTabFragmentToMealDetailFragment(meal.getId()));
        });
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new HomePresenter(
            this,
            MealRepository.getInstance(
                new MealLocalDataSourceImpl(getContext()),
                new MealRemoteDataSourceImpl(MealService.create())
            ),
            FirebaseAuthManager.getInstance(getContext())
        );
        
        presenter.fetchData();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
    
    @Override
    public void onSignOutPressed() {
        disposable = authManager.signOut().subscribe(() -> {
            presenter.clearAll();
            
            NavController navController = NavHostFragment.findNavController(requireParentFragment().requireParentFragment());
            navController.navigate(R.id.action_tabsFragment_to_authSelectionFragment);
        });
    }
    
    @Override
    public void onBackupPressed() {
        presenter.backupData();
    }
    
    @Override
    public void onRestorePressed() {
        presenter.restoreData();
    }
    
    @Override
    public void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }
    
    @Override
    public void showMessage(String message, int duration) {
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
    
    @Override
    public void showFeaturedMeal(Meal meal) {
        setupFeaturedMeal(binding.viewTodaysMeal, "Today's Meal", "Picked for you today", meal);
        binding.viewTodaysMeal.getRoot().setVisibility(View.VISIBLE);
    }
    
    @Override
    public void showSuggestedCuisine(Cuisine cuisine, List<Meal> meals) {
        setupSuggestedCuisine(cuisine.getName(), meals);
        binding.viewSuggestedCuisine.getRoot().setVisibility(View.VISIBLE);
    }
    
    @Override
    public void showDailySelection(List<Meal> meals) {
        setupDailySelection(meals);
        binding.viewDailySelection.getRoot().setVisibility(View.VISIBLE);
    }
    
    @Override
    public void showMealToPrepare(Meal meal) {
        setupFeaturedMeal(binding.viewMealToPrepare, "Meal to Prepare", "From your calendar", meal);
        binding.viewMealToPrepare.getRoot().setVisibility(View.VISIBLE);
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
            )),
            false
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
            )),
            false
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
            )),
            false
        )
    );
}
