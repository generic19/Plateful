package com.basilalasadi.iti.plateful.model.meal.datasource.dto;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.basilalasadi.iti.plateful.model.meal.Category;

@Entity(tableName = "CategoryDto")
public class CategoryDto {
    @PrimaryKey @NonNull private String name;
    
    public CategoryDto(Category category) {
        this.name = category.getName();
    }
    
    public CategoryDto(String name) {
        this.name = name;
    }
    
    public Category toCategory() {
        return new Category(name);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
