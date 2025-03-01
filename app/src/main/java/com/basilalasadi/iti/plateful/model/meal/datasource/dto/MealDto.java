package com.basilalasadi.iti.plateful.model.meal.datasource.dto;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.List;
import java.util.stream.Collectors;

@Entity(tableName = "MealDto")
public class MealDto {
    @PrimaryKey @NonNull private String id;
    private String title;
    @ColumnInfo(index = true) private String category;
    @ColumnInfo(index = true) private String cuisine;
    private String instructions;
    private String youtubeVideoId;
    private String source;
    private String thumbnail;
    private List<String> tags;
    private List<IngredientDto> ingredients;
    @ColumnInfo(index = true) private boolean isFavorite;
    
    public MealDto() {
        this("", "", "", "", "", "", "", "", null, null, false);
    }
    
    public MealDto(
        @NonNull String id,
        String title,
        String category,
        String cuisine,
        String instructions,
        String youtubeVideoId,
        String source,
        String thumbnail,
        List<String> tags,
        List<IngredientDto> ingredients,
        boolean isFavorite
    ) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.cuisine = cuisine;
        this.instructions = instructions;
        this.youtubeVideoId = youtubeVideoId;
        this.source = source;
        this.thumbnail = thumbnail;
        this.tags = tags;
        this.ingredients = ingredients;
        this.isFavorite = isFavorite;
    }
    
    @Ignore
    public MealDto(Meal meal) {
        id = meal.getId();
        title = meal.getTitle();
        category = meal.getCategory();
        cuisine = meal.getCuisine();
        instructions = meal.getInstructions();
        youtubeVideoId = meal.getYoutubeVideoId();
        source = meal.getSource().toString();
        thumbnail = meal.getThumbnail().toString();
        tags = meal.getTags();
        ingredients = meal.getIngredients()
            .stream()
            .map(IngredientDto::new)
            .collect(Collectors.toList());
        isFavorite = meal.isFavorite();
    }
    
    public Meal toMeal() {
        Meal meal = new Meal(
            id,
            title,
            category,
            cuisine,
            instructions,
            youtubeVideoId,
            Uri.parse(source),
            Uri.parse(thumbnail),
            tags,
            ingredients.stream()
                .map(ingredientDto -> ingredientDto.toIngredient())
                .collect(Collectors.toList()),
            isFavorite
        );
        
        return meal;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getCuisine() {
        return cuisine;
    }
    
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    
    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }
    
    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getThumbnail() {
        return thumbnail;
    }
    
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public List<IngredientDto> getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(List<IngredientDto> ingredients) {
        this.ingredients = ingredients;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
