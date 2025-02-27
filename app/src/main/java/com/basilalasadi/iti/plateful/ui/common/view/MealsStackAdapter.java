package com.basilalasadi.iti.plateful.ui.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.databinding.ItemMealLargeSubtitleBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.util.com.github.darkionavey.stacklayoutmanager.ReverseStackInterpolator;
import com.basilalasadi.iti.plateful.util.com.github.darkionavey.stacklayoutmanager.StackLayoutManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kotlin.Unit;

public class MealsStackAdapter extends RecyclerView.Adapter<MealsStackAdapter.ViewHolder> {
    private final Context context;
    private final Listener listener;
    private final List<Meal> meals = new ArrayList<>();
    
    public interface Listener {
        void onMealClicked(Meal meal);
    }
    
    public MealsStackAdapter(Context context, Listener listener) {
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
        ItemMealLargeSubtitleBinding binding = ItemMealLargeSubtitleBinding.inflate(LayoutInflater.from(context), parent, false);
        
        binding.blurCaption.setupWith(binding.cardMealImage);
        binding.blurCaption.setClipToOutline(true);
        binding.blurCaption.setBlurRadius(20);
        
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        
        holder.binding.txtTitle.setText(meal.getTitle());
        holder.binding.txtSubtitle.setText(meal.getCuisine());
        
        Glide.with(context)
                .load(meal.getThumbnail())
                .into(holder.binding.imageView);
        
        holder.binding.cardMealImage.setOnClickListener(v -> listener.onMealClicked(meal));
    }
    
    @Override
    public int getItemCount() {
        return meals.size();
    }
    
    public static void setupRecycler(RecyclerView recycler) {
        StackLayoutManager manager = new StackLayoutManager();
        
        manager.setScrollMultiplier(0.6f);
        manager.setLayoutInterpolator(new ReverseStackInterpolator());
        
        manager.setViewTransformer((x, view, stackLayoutManager) -> {
            view.setElevation(20f);
            view.setTranslationZ((1 - x) * 2);
            
            return Unit.INSTANCE;
        });
        
        recycler.setLayoutManager(manager);
        
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                
                if (newState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    manager.snap();
                }
            }
        });
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMealLargeSubtitleBinding binding;
        
        public ViewHolder(@NonNull ItemMealLargeSubtitleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
