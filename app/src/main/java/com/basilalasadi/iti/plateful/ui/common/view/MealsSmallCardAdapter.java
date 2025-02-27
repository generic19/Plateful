package com.basilalasadi.iti.plateful.ui.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.ItemMealSmallBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.bumptech.glide.Glide;


public class MealsSmallCardAdapter extends RecyclerView.Adapter<MealsSmallCardAdapter.ViewHolder> {
    private final Context context;
    private final Listener listener;
    
    private final List<Meal> meals = new ArrayList<>();
    
    public interface Listener {
        void onMealClicked(Meal meal);
    }
    
    public MealsSmallCardAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setMeals(Collection<Meal> meals) {
        this.meals.clear();
        this.meals.addAll(meals);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealSmallBinding binding = ItemMealSmallBinding.inflate(LayoutInflater.from(context), parent, false);
        
        binding.blurCaption.setupWith(binding.cardMealImage);
        binding.blurCaption.setClipToOutline(true);
        binding.blurCaption.setBlurRadius(13);
        
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        
        holder.binding.txtTitle.setText(meal.getTitle());
        
        holder.binding.txtSubtitle.setText(
            meal.getTags().isEmpty()
                ? meal.getCuisine()
                : String.join(", ", meal.getTags())
        );
        
        holder.binding.txtTag.setText(meal.getCategory());
        
        Glide.with(context)
            .load(meal.getThumbnail())
            .error(R.drawable.bg_welcome)
            .into(holder.binding.imageView);
        
        holder.binding.cardMealImage.setOnClickListener(v -> listener.onMealClicked(meal));
    }
    
    @Override
    public int getItemCount() {
        return meals.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMealSmallBinding binding;
        
        public ViewHolder(@NonNull ItemMealSmallBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
