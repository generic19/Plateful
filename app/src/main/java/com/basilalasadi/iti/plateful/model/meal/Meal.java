package com.basilalasadi.iti.plateful.model.meal;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.apache.commons.text.WordUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Meal implements Parcelable {
    private final String id;
    private final String title;
    private final String category;
    private final String cuisine;
    private final String instructions;
    private final String youtubeVideoId;
    private final Uri source;
    private final Uri thumbnail;
    private final List<String> tags;
    private final List<Ingredient> ingredients;
    
    public Meal(
        String id,
        String title,
        String category,
        String cuisine,
        String instructions,
        String youtubeVideoId,
        Uri source,
        Uri thumbnail,
        List<String> tags,
        List<Ingredient> ingredients
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
    }
    
    public Meal(Meal src) {
        this.id = src.id;
        this.title = src.title;
        this.category = src.category;
        this.cuisine = src.cuisine;
        this.instructions = src.instructions;
        this.youtubeVideoId = src.youtubeVideoId;
        this.source = src.source;
        this.thumbnail = src.thumbnail;
        this.tags = src.tags;
        this.ingredients = src.ingredients;
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
        this.tags = new ArrayList<>(Arrays.asList(tags.split(",")));
        
        if (mealObject.has("strYoutube")) {
            String youtube = mealObject.getString("strYoutube");
            this.youtubeVideoId = Uri.parse(youtube).getQueryParameter("v");
        } else {
            this.youtubeVideoId = null;
        }
        
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        
        for (int i = 1; i <= 20; i++) {
            String ingredientName = mealObject.optString("strIngredient" + i, "");
            String measurement = mealObject.optString("strMeasurement" + i, "");
            
            if (ingredientName.isEmpty() || measurement.isEmpty()) {
                break;
            }
            
            ingredients.add(new Ingredient(WordUtils.capitalize(ingredientName), WordUtils.capitalize(measurement)));
        }
        
        this.ingredients = ingredients;
    }
    
    protected Meal(Parcel in) {
        id = in.readString();
        title = in.readString();
        category = in.readString();
        cuisine = in.readString();
        instructions = in.readString();
        youtubeVideoId = in.readString();
        source = Uri.CREATOR.createFromParcel(in);
        thumbnail = Uri.CREATOR.createFromParcel(in);
        tags = in.createStringArrayList();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }
    
    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }
        
        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };
    
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
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(cuisine);
        dest.writeString(instructions);
        dest.writeString(youtubeVideoId);
        Uri.writeToParcel(dest, source);
        Uri.writeToParcel(dest, thumbnail);
        dest.writeStringList(tags);
        dest.writeTypedList(ingredients);
    }
}
