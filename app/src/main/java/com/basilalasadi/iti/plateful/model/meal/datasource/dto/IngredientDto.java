package com.basilalasadi.iti.plateful.model.meal.datasource.dto;

import com.basilalasadi.iti.plateful.model.meal.Ingredient;

public class IngredientDto {
    private String name;
    private String measurement;
    
    public IngredientDto() {
    }
    
    public IngredientDto(Ingredient ingredient) {
        name = ingredient.getName();
        measurement = ingredient.getMeasurement();
    }
    
    public Ingredient toIngredient() {
        return new Ingredient(name, measurement);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getMeasurement() {
        return measurement;
    }
    
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
