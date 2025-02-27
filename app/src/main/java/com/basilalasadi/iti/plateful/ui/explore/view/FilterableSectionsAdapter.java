package com.basilalasadi.iti.plateful.ui.explore.view;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.databinding.ItemLetterTitleBinding;
import com.basilalasadi.iti.plateful.model.meal.Category;
import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Section;

import java.util.ArrayList;
import java.util.List;


public class FilterableSectionsAdapter<S extends Section> extends RecyclerView.Adapter<FilterableSectionsAdapter.ViewHolder> {
    private final List<S> allSections;
    private final ArrayList<S> visibleSections;
    
    private final Context context;
    private final Listener<S> listener;
    
    private final String firstLetter;
    
    public interface Listener<Sc extends Section> {
        void onSectionClicked(Sc section);
    }
    
    public FilterableSectionsAdapter(Context context, List<S> sections, Listener<S> listener) {
        this.context = context;
        
        this.allSections = sections;
        this.visibleSections = new ArrayList<>(sections);
        this.listener = listener;
        
        if (sections.isEmpty()) {
            firstLetter = "";
        } else if (sections.get(0) instanceof Category) {
            firstLetter = "Ct";
        } else if (sections.get(0) instanceof Cuisine) {
            firstLetter = "Cs";
        } else {
            firstLetter = "";
        }
    }
    
    public void filter(String query) {
        if (query.isBlank()) {
            clearFilter();
        } else {
            final String queryLowerCase = query.toLowerCase();
            visibleSections.clear();
            
            allSections.stream()
                .map(
                    s -> new Pair<>(
                        s,
                        s.getName()
                            .toLowerCase()
                            .indexOf(queryLowerCase)
                    )
                )
                .filter(p -> p.second != -1)
                .sorted(
                    (p1, p2) -> p1.second.equals(p2.second)
                        ? p1.first.getName().compareToIgnoreCase(p2.first.getName())
                        : p1.second.compareTo(p2.second)
                )
                .map(p -> p.first)
                .forEach(visibleSections::add);
            
            notifyDataSetChanged();
        }
    }
    
    public void clearFilter() {
        visibleSections.clear();
        visibleSections.addAll(allSections);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLetterTitleBinding binding = ItemLetterTitleBinding.inflate(LayoutInflater.from(context), parent, false);
        binding.txtFirstLetter.setText(firstLetter);
        
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        S section = visibleSections.get(position);
        
        holder.binding.txtTitle.setText(section.getName());
        holder.binding.getRoot().setOnClickListener(v -> listener.onSectionClicked(section));
    }
    
    @Override
    public int getItemCount() {
        return visibleSections.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLetterTitleBinding binding;
        
        public ViewHolder(@NonNull ItemLetterTitleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

