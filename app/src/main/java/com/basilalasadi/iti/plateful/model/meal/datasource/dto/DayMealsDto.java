package com.basilalasadi.iti.plateful.model.meal.datasource.dto;

import java.util.Date;
import java.util.List;

public class DayMealsDto {
    private Date date;
    private List<MealDto> meals;
    
    public DayMealsDto() {
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public List<MealDto> getMeals() {
        return meals;
    }
    
    public void setMeals(List<MealDto> meals) {
        this.meals = meals;
    }
}
