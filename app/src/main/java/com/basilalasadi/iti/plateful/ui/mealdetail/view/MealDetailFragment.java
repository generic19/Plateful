package com.basilalasadi.iti.plateful.ui.mealdetail.view;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentMealDetailBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.MealLocalDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.MealRemoteDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.api.MealService;
import com.basilalasadi.iti.plateful.ui.common.view.TabsFragment;
import com.basilalasadi.iti.plateful.ui.mealdetail.MealDetailContract;
import com.basilalasadi.iti.plateful.ui.mealdetail.presenter.MealDetailPresenter;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class MealDetailFragment extends Fragment implements MealDetailContract.View {
    private Meal meal;
    private IngredientsChecklistAdapter ingredientsChecklistAdapter;
    private com.basilalasadi.iti.plateful.databinding.FragmentMealDetailBinding binding;
    private MealDetailContract.Presenter presenter;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMealDetailBinding.inflate(inflater, container, false);
        
        TabsFragment.applySystemTopPadding(binding.materialToolbar);
        
        binding.materialToolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(binding.getRoot())
                .navigateUp();
        });
        
        binding.materialToolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            
            if (id == R.id.menu_item_add_to_calendar) {
                showDatePicker();
            } else if (id == R.id.menu_item_add_to_favorites) {
                presenter.favoritesAction(meal);
            }
            
            return true;
        });
        
        ingredientsChecklistAdapter = new IngredientsChecklistAdapter(getContext());
        binding.recyclerIngredients.setAdapter(ingredientsChecklistAdapter);
        
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String mealId = MealDetailFragmentArgs.fromBundle(getArguments()).getMealId();
        
        presenter = new MealDetailPresenter(this, MealRepository.getInstance(
            new MealLocalDataSourceImpl(getContext()),
            new MealRemoteDataSourceImpl(MealService.create())
        ));
        
        presenter.fetchMeal(mealId);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
    
    @Override
    public void showMeal(Meal meal) {
        this.meal = meal;
        bindMeal();
    }
    
    private void bindMeal() {
        if (meal.isFavorite()) {
            binding.materialToolbar.getMenu().findItem(R.id.menu_item_add_to_favorites).setTitle("Remove from favorites");
        } else {
            binding.materialToolbar.getMenu().findItem(R.id.menu_item_add_to_favorites).setTitle("Add to favorites");
        }
        
        binding.txtTitle.setText(meal.getTitle());
        binding.txtSubtitle.setText(String.format(getString(R.string.s_meal_from_the_s_cuisine), meal.getCategory(), meal.getCuisine()));
        
        Glide.with(getContext())
            .load(meal.getThumbnail())
            .error(R.drawable.bg_welcome)
            .into(binding.imageMeal);
        
        binding.txtCategory.setText(meal.getCategory());
        binding.txtCuisine.setText(meal.getCuisine());
        
        ingredientsChecklistAdapter.setIngredients(meal.getIngredients());
        
        if (meal.getYoutubeEmbedUrl() != null) {
            binding.webVideo.getSettings().setJavaScriptEnabled(true);
            binding.webVideo.setWebViewClient(new WebViewClient());
            binding.webVideo.loadUrl(meal.getYoutubeEmbedUrl().toString());
        } else {
            binding.cardVideo.setVisibility(View.GONE);
            binding.txtLabelRecipeVideo.setVisibility(View.GONE);
        }
        
        binding.txtInstructions.setText(meal.getInstructions());
    }
    
    @Override
    public void showMessage(String message, int duration) {
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
    
    public void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            getContext(),
            (view, selectedYear, selectedMonth, selectedDay) -> {
                presenter.calendarAction(meal, new Date(selectedYear - 1900, selectedMonth - 1, selectedDay));
            },
            year, month, day
        );
        
        Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 1);
        
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(nextMonth.getTimeInMillis());
        
        datePickerDialog.show();
    }
}