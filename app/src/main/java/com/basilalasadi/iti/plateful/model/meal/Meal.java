package com.basilalasadi.iti.plateful.model.meal;

import android.net.Uri;

import org.apache.commons.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Meal {
    final String id;
    final String title;
    final String category;
    final String cuisine;
    final String instructions;
    final String youtubeVideoId;
    final Uri source;
    final Uri thumbnail;
    final List<String> tags;
    final List<Ingredient> ingredients;
    
    public Meal(String id, String title, String category, String cuisine, String instructions, String youtubeVideoId, Uri source, Uri thumbnail, List<String> tags, List<Ingredient> ingredients) {
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
    }
    
    public Meal(JSONObject mealObject) throws JSONException {
        this.id = mealObject.getString("idMeal");
        this.title = mealObject.getString("strMeal");
        this.category = mealObject.getString("strCategory");
        this.cuisine = mealObject.getString("strArea");
        
        String instructions = mealObject.getString("strInstructions");
        this.instructions = instructions.replaceAll("(?:\r?\n)+", "\n\n");
        
        this.thumbnail = Uri.parse(mealObject.getString("strMealThumb"));
        
        if (mealObject.has("strSource")) {
            this.source = Uri.parse(mealObject.getString("strSource"));
        } else {
            this.source = null;
        }
        
        String tags = mealObject.getString("strTags");
        this.tags = Arrays.asList(tags.split(","));
        
        if (mealObject.has("strYoutube")) {
            String youtube = mealObject.getString("strYoutube");
            this.youtubeVideoId = Uri.parse(youtube).getQueryParameter("v");
        } else {
            this.youtubeVideoId = null;
        }
        
        this.ingredients = new ArrayList<>();
        
        for (int i = 1; i <= 20; i++) {
            String ingredientName = mealObject.optString("strIngredient" + i, "");
            String measurement = mealObject.optString("strMeasurement" + i, "");
            
            if (ingredientName.isEmpty() || measurement.isEmpty()) {
                break;
            }
            
            this.ingredients.add(new Ingredient(
                    WordUtils.capitalize(ingredientName),
                    WordUtils.capitalize(measurement)
            ));
        }
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getCuisine() {
        return cuisine;
    }
    
    public String getInstructions() {
        return instructions;
    }
    
    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }
    
    public Uri getYoutubeEmbedUrl() {
        if (youtubeVideoId != null) {
            return Uri.parse("https://youtube.com/embed/" + youtubeVideoId);
        } else {
            return null;
        }
    }
    
    public Uri getSource() {
        return source;
    }
    
    public Uri getThumbnail() {
        return thumbnail;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}
