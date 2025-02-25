package com.basilalasadi.iti.plateful.ui.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.databinding.ItemMealLargeSubtitleBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MealsStackAdapter extends RecyclerView.Adapter<MealsStackAdapter.ViewHolder> {
    private final Context context;
    private final List<Meal> meals = new ArrayList<>();
    
    public MealsStackAdapter(Context context) {
        this.context = context;
    }
    
    public void setMeals(Collection<Meal> meals) {
        this.meals.clear();
        this.meals.addAll(meals);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealLargeSubtitleBinding binding = ItemMealLargeSubtitleBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        
        holder.binding.txtTitle.setText(meal.getTitle());
        holder.binding.txtSubtitle.setText(meal.getCuisine());
        
        Glide.with(context)
                .load(meal.getThumbnail())
                .into(holder.binding.imageView);
    }
    
    @Override
    public int getItemCount() {
        return meals.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMealLargeSubtitleBinding binding;
        
        public ViewHolder(@NonNull View view) {
            super(view);
            this.binding = ItemMealLargeSubtitleBinding.bind(view);
        }
    }
}
