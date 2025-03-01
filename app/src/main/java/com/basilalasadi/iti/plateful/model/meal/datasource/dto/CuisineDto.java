package com.basilalasadi.iti.plateful.model.meal.datasource.dto;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.basilalasadi.iti.plateful.model.meal.Cuisine;

@Entity(tableName = "CuisineDto")
public class CuisineDto {
    @PrimaryKey @NonNull private String name;
    
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
