package com.basilalasadi.iti.plateful.model.calendar;

import android.net.Uri;

import com.basilalasadi.iti.plateful.model.meal.Ingredient;
import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.ArrayList;
import java.util.List;

public class CalendarMeal extends Meal {
    private final long calendarItemId;
    
    public CalendarMeal(
        String id,
        String title,
        String category,
        String cuisine,
        String instructions,
        String youtubeVideoId,
        Uri source,
        Uri thumbnail,
        ArrayList<String> tags,
        ArrayList<Ingredient> ingredients,
        long calendarItemId
    ) {
        super(id, title, category, cuisine, instructions, youtubeVideoId, source, thumbnail, tags, ingredients);
        this.calendarItemId = calendarItemId;
    }
    
    public CalendarMeal(Meal meal, long calendarItemId) {
        super(meal);
        this.calendarItemId = calendarItemId;
    }
    
    public long getCalendarItemId() {
        return calendarItemId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        CalendarMeal that = (CalendarMeal) o;
        return calendarItemId == that.calendarItemId;
    }
    
    @Override
    public int hashCode() {
        return Long.hashCode(calendarItemId);
    }
}
