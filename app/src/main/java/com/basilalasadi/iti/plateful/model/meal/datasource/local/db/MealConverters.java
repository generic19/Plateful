package com.basilalasadi.iti.plateful.model.meal.datasource.local.db;

import android.util.Log;

import androidx.room.TypeConverter;

import com.basilalasadi.iti.plateful.model.meal.datasource.dto.IngredientDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MealConverters {
    private static final String TAG = "MealConverters";
    
    @TypeConverter
    public static String fromStringList(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        
        for (String str : list) {
            jsonArray.put(str);
        }
        
        return jsonArray.toString();
    }
    
    @TypeConverter
    public static List<String> toStringList(String json) {
        try {
            List<String> list = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(json);
            
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            
            return list;
        } catch (JSONException ex) {
            Log.e(TAG, "fromIngredientList", ex);
            return new ArrayList<>();
        }
    }
    
    @TypeConverter
    public static String fromIngredient(IngredientDto ingredient) {
        try {
            JSONObject jsonObject = new JSONObject();
            
            jsonObject.put("name", ingredient.getName());
            jsonObject.put("measurement", ingredient.getMeasurement());
            
            return jsonObject.toString();
        } catch (JSONException ex) {
            Log.e(TAG, "fromIngredientList", ex);
            return "{}";
        }
    }
    
    @TypeConverter
    public static IngredientDto toIngredient(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            
            IngredientDto dto = new IngredientDto();
            
            dto.setName(jsonObject.getString("name"));
            dto.setMeasurement(jsonObject.getString("measurement"));
            
            return dto;
        } catch (JSONException ex) {
            Log.e(TAG, "fromIngredientList", ex);
            return null;
        }
    }
    
    @TypeConverter
    public static String fromIngredientList(List<IngredientDto> list) {
        try {
            JSONArray jsonArray = new JSONArray();
            
            for (IngredientDto dto : list) {
                jsonArray.put(new JSONObject(fromIngredient(dto)));
            }
            
            return jsonArray.toString();
        } catch (JSONException ex) {
            Log.e(TAG, "fromIngredientList", ex);
            return "[]";
        }
    }
    
    @TypeConverter
    public static List<IngredientDto> toIngredientList(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            List<IngredientDto> list = new ArrayList<>();
            
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(toIngredient(jsonArray.getJSONObject(i).toString()));
            }
            
            return list;
        } catch (JSONException ex) {
            Log.e(TAG, "toIngredientList", ex);
            return new ArrayList<>();
        }
    }
}
