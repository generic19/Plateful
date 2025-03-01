package com.basilalasadi.iti.plateful.ui.explore.view;

import android.content.Context;
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


public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder> {
    private List<Section> sections;
    
    private final Context context;
    private final Listener listener;
    
    public interface Listener {
        void onSectionClicked(Section section);
    }
    
    public SectionsAdapter(Context context, Listener listener) {
        this.context = context;
        this.sections = new ArrayList<>();
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLetterTitleBinding binding = ItemLetterTitleBinding.inflate(LayoutInflater.from(context), parent, false);
        
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Section section = sections.get(position);
        
        String firstLetter;
        
        if (section instanceof Category) {
            firstLetter = "Ct";
        } else if (section instanceof Cuisine) {
            firstLetter = "Cs";
        } else {
            firstLetter = "";
        }
        
        holder.binding.txtFirstLetter.setText(firstLetter);
        holder.binding.txtTitle.setText(section.getName());
        holder.binding.getRoot().setOnClickListener(v -> listener.onSectionClicked(section));
    }
    
    @Override
    public int getItemCount() {
        return sections.size();
    }
    
    public void setSections(List<Section> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemLetterTitleBinding binding;
        
        public ViewHolder(@NonNull ItemLetterTitleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

