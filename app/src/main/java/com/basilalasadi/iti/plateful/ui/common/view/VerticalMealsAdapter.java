package com.basilalasadi.iti.plateful.ui.common.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.ItemMealLargeButtonBinding;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;

public class VerticalMealsAdapter extends RecyclerView.Adapter<VerticalMealsAdapter.ViewHolder> {
    private final ArrayList<Meal> items = new ArrayList<>();
    private final Context context;
    private final Listener listener;
    
    public interface Listener {
        void onMealClicked(Meal meal);
        void onMealMenuRequested(Meal meal, View view);
    }
    
    public VerticalMealsAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setItems(Collection<Meal> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    
    public void removeItem(Meal item) {
        int index = items.indexOf(item);
        
        if (index != -1) {
            items.remove(index);
            notifyItemRemoved(index);
        }
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMealLargeButtonBinding binding = ItemMealLargeButtonBinding.inflate(LayoutInflater.from(context), parent, false);
        
        binding.blurCaption.setupWith(binding.cardMealImage);
        binding.blurCaption.setClipToOutline(true);
        binding.blurCaption.setBlurRadius(20);
        
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = items.get(position);
        
        Glide.with(context)
            .load(meal.getThumbnail())
            .error(R.drawable.bg_welcome)
            .into(holder.binding.imageView);
        
        holder.binding.txtTitle.setText(meal.getTitle());
        
        holder.binding.cardMealImage.setOnClickListener(v -> listener.onMealClicked(meal));
        holder.binding.btnMore.setOnClickListener(v -> listener.onMealMenuRequested(meal, v));
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMealLargeButtonBinding binding;
        
        public ViewHolder(@NonNull ItemMealLargeButtonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

