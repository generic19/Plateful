package com.basilalasadi.iti.plateful.model.meal;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public class MealPreview {
    private final String id;
    private final String title;
    private final Uri thumbnail;
    
    public MealPreview(String id, String title, Uri thumbnail) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
    }
    
    public MealPreview(Meal meal) {
        this.id = meal.getId();
        this.title = meal.getTitle();
        this.thumbnail = meal.getThumbnail();
    }
    
    public MealPreview(JSONObject mealObject) throws JSONException {
        this.id = mealObject.getString("idMeal");
        this.title = mealObject.getString("strMeal");
        this.thumbnail = Uri.parse(mealObject.getString("strMealThumb"));
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Uri getThumbnail() {
        return thumbnail;
    }
}
