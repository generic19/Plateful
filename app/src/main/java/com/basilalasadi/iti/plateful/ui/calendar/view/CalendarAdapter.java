package com.basilalasadi.iti.plateful.ui.calendar.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.ItemCalendarDividerDateBinding;
import com.basilalasadi.iti.plateful.databinding.ItemMealRowBinding;
import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;
import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract.View.CalendarComponent;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract.View.DateCalendarComponent;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract.View.MealCalendarComponent;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private static final int TYPE_DATE = 0;
    private static final int TYPE_MEAL = 1;
    
    private final ArrayList<CalendarComponent> items = new ArrayList<>();
    private final Context context;
    private final Listener listener;
    
    public interface Listener {
        void onMealClicked(CalendarMeal meal);
        void onMealMenuRequested(CalendarMeal meal, View view);
    }
    
    public CalendarAdapter(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    public void setItems(Collection<CalendarComponent> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        
        switch (viewType) {
            case TYPE_DATE:
                return new DateViewHolder(ItemCalendarDividerDateBinding.inflate(inflater, parent, false));
                
            case TYPE_MEAL:
                return new MealViewHolder(ItemMealRowBinding.inflate(inflater, parent, false));
                
            default:
                throw new IllegalArgumentException("Unknown view type.");
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof DateViewHolder) {
            DateCalendarComponent component = (DateCalendarComponent) items.get(position);
            Date date = component.getDate();
            
            bindDateViewHolder((DateViewHolder) holder, date);
        } else if (holder instanceof MealViewHolder) {
            MealCalendarComponent component = (MealCalendarComponent) items.get(position);
            CalendarMeal meal = component.getMeal();

//            bindMealViewHolder((MealViewHolder) holder, meal);
        } else {
            throw new IllegalArgumentException("Unknown view holder type.");
        }
    }
    
    private void bindDateViewHolder(DateViewHolder dateHolder, Date date) {
        dateHolder.binding.txtDate.setText(
            new SimpleDateFormat("MMM d", Locale.ENGLISH).format(date)
        );
        
        dateHolder.binding.txtDay.setText(
            new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date)
        );
    }
    
    private void bindMealViewHolder(MealViewHolder holder, Meal meal) {
        holder.binding.txtTitle.setText(meal.getTitle());
        holder.binding.txtSubtitle.setText(meal.getIngredients().size() + " Ingredients");
        holder.binding.txtOverlay.setText(meal.getCategory());
        
        Glide.with(context)
            .load(meal.getThumbnail())
            .error(R.drawable.bg_welcome)
            .into(holder.binding.imageView);
        
//        holder.binding.getRoot().setOnClickListener(v -> listener.onMealClicked(meal));
//
//        holder.binding.getRoot().setOnLongClickListener(v -> {
//            listener.onMealMenuRequested(meal, v);
//            return true;
//        });
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    @Override
    public int getItemViewType(int position) {
        CalendarComponent component = items.get(position);
        
        if (component instanceof DateCalendarComponent) {
            return TYPE_DATE;
        } else if (component instanceof MealCalendarComponent) {
            return TYPE_MEAL;
        } else {
            throw new IllegalArgumentException("Unknown subtype of CalendarComponent");
        }
    }
    
    public static abstract class ViewHolder<C extends CalendarComponent> extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    
    public static class DateViewHolder extends ViewHolder<DateCalendarComponent> {
        private final ItemCalendarDividerDateBinding binding;
        
        public DateViewHolder(ItemCalendarDividerDateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    
    public static class MealViewHolder extends ViewHolder<MealCalendarComponent> {
        private final ItemMealRowBinding binding;
        
        public MealViewHolder(ItemMealRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    
}
