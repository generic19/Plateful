package com.basilalasadi.iti.plateful.model.meal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "CalendarMeal")
public class CalendarMeal {
    @PrimaryKey private String mealId;
    @PrimaryKey private Date date;
    
    public CalendarMeal() {
    }
    
    public CalendarMeal(String mealId, Date date) {
        this.mealId = mealId;
        this.date = date;
    }
    
    public CalendarMeal(Meal meal, Date date) {
        this.mealId = meal.getId();
        this.date = date;
    }
    
    public String getMealId() {
        return mealId;
    }
    
    public void setMealId(String mealId) {
        this.mealId = mealId;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
}
