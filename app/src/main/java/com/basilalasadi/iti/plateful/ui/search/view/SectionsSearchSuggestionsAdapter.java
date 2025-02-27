package com.basilalasadi.iti.plateful.ui.search.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.databinding.ItemLetterTitleSubtitleBinding;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Ingredient;
import com.basilalasadi.iti.plateful.model.meal.Section;

import java.util.ArrayList;
import java.util.Collection;

public class SectionsSearchSuggestionsAdapter extends RecyclerView.Adapter<SectionsSearchSuggestionsAdapter.ViewHolder> {
    private final ArrayList<Section> items = new ArrayList<>();
    private final Context context;
    private final Listener listener;
    
    public interface Listener {
        void onItemClicked(Section item);
    }
    
    public SectionsSearchSuggestionsAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setItems(Collection<Section> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLetterTitleSubtitleBinding binding = ItemLetterTitleSubtitleBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Section item = items.get(position);
        
        holder.binding.txtTitle.setText(item.getName());
        
        if (item instanceof Ingredient) {
            holder.binding.txtFirstLetter.setText("Ig");
            holder.binding.txtSubtitle.setText("Ingredient");
        } else if (item instanceof Cuisine) {
            holder.binding.txtFirstLetter.setText("Cs");
            holder.binding.txtSubtitle.setText("Cuisine");
        } else if (item instanceof Category) {
            holder.binding.txtFirstLetter.setText("Ct");
            holder.binding.txtSubtitle.setText("Category");
        } else {
            throw new UnsupportedOperationException();
        }
        
        holder.binding.getRoot().setOnClickListener(v -> listener.onItemClicked(item));
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLetterTitleSubtitleBinding binding;
        
        public ViewHolder(@NonNull ItemLetterTitleSubtitleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
