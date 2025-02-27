package com.basilalasadi.iti.plateful.ui.mealdetail.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.databinding.ItemIngredientCheckableBinding;
import com.basilalasadi.iti.plateful.model.meal.Ingredient;

import java.util.ArrayList;
import java.util.Collection;

public class IngredientsChecklistAdapter extends RecyclerView.Adapter<IngredientsChecklistAdapter.ViewHolder> {
    private final ArrayList<Ingredient> ingredients = new ArrayList<>();
    private final Context context;
    
    public IngredientsChecklistAdapter(Context context) {
        this.context = context;
    }
    
    public void setIngredients(Collection<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIngredientCheckableBinding binding =
            ItemIngredientCheckableBinding.inflate(LayoutInflater.from(context), parent, false);
        
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        
        holder.binding.txtTitle.setText(ingredient.getName());
        holder.binding.txtSubtitle.setText(ingredient.getMeasurement());
    }
    
    @Override
    public int getItemCount() {
        return ingredients.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemIngredientCheckableBinding binding;
        
        public ViewHolder(@NonNull ItemIngredientCheckableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
