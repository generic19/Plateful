package com.basilalasadi.iti.plateful.ui.search.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.databinding.ItemTextBodyLargeBinding;

import java.util.ArrayList;
import java.util.Collection;

public class TextSearchSuggestionsAdapter extends RecyclerView.Adapter<TextSearchSuggestionsAdapter.ViewHolder> {
    private final ArrayList<String> items = new ArrayList<>();
    private final Context context;
    private final Listener listener;
    
    public interface Listener {
        void onItemClicked(String item);
    }
    
    public TextSearchSuggestionsAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setItems(Collection<String> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTextBodyLargeBinding binding = ItemTextBodyLargeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        
        holder.binding.txtTitle.setText(item);
        holder.binding.getRoot().setOnClickListener(v -> listener.onItemClicked(item));
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTextBodyLargeBinding binding;
        
        public ViewHolder(@NonNull ItemTextBodyLargeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
