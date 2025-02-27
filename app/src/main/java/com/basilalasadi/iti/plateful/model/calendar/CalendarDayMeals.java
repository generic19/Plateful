package com.basilalasadi.iti.plateful.model.calendar;

import java.util.Date;
import java.util.List;

public class CalendarDayMeals implements Comparable<CalendarDayMeals> {
    private final Date date;
    private final List<CalendarMeal> meals;
    
    public CalendarDayMeals(Date date, List<CalendarMeal> meals) {
        this.date = date;
        this.meals = meals;
    }
    
    public Date getDate() {
        return date;
    }
    
    public List<CalendarMeal> getMeals() {
        return meals;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        CalendarDayMeals that = (CalendarDayMeals) o;
        return date.equals(that.date);
    }
    
    @Override
    public int hashCode() {
        return date.hashCode();
    }
    
    @Override
    public int compareTo(CalendarDayMeals o) {
        return date.compareTo(o.date);
    }
}
