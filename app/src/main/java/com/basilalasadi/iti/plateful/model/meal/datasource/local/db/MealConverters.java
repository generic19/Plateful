package com.basilalasadi.iti.plateful.model.meal.datasource.local.db;

import androidx.room.TypeConverter;

import com.basilalasadi.iti.plateful.model.meal.datasource.dto.IngredientDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MealConverters {
    @TypeConverter
    public static String fromStringList(List<String> list) {
        JSONArray jsonArray = new JSONArray();
        
        for (String str : list) {
            jsonArray.put(str);
        }
        
        return jsonArray.toString();
    }
    
    @TypeConverter
    public static List<String> toStringList(String json) throws JSONException {
        List<String> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        
        return list;
    }
    
    @TypeConverter
    public static String fromIngredient(IngredientDto ingredient) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("name", ingredient.getName());
        jsonObject.put("measurement", ingredient.getMeasurement());
        
        return jsonObject.toString();
    }
    
    @TypeConverter
    public static IngredientDto toIngredient(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        
        IngredientDto dto = new IngredientDto();
        
        dto.setName(jsonObject.getString("name"));
        dto.setMeasurement(jsonObject.getString("measurement"));
        
        return dto;
    }
    
    @TypeConverter
    public static String fromIngredientList(List<IngredientDto> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        
        for (IngredientDto dto : list) {
            jsonArray.put(new JSONObject(fromIngredient(dto)));
        }
        
        return jsonArray.toString();
    }
    
    @TypeConverter
    public static List<IngredientDto> toIngredientList(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        List<IngredientDto> list = new ArrayList<>();
        
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(toIngredient(jsonArray.getJSONObject(i).toString()));
        }
        
        return list;
    }
}
