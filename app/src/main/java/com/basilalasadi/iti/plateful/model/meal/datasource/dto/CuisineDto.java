package com.basilalasadi.iti.plateful.model.meal.datasource.dto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.basilalasadi.iti.plateful.model.meal.Cuisine;
import com.basilalasadi.iti.plateful.model.meal.Ingredient;

@Entity(tableName = "CuisineDto")
public class CuisineDto {
    @PrimaryKey
    private String name;
    
    public CuisineDto() {
    }
    
    public CuisineDto(Cuisine cuisine) {
        this.name = cuisine.getName();
    }
    
    public CuisineDto(String name) {
        this.name = name;
    }
    
    public Cuisine toCuisine() {
        return new Cuisine(name);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
