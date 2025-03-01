package com.basilalasadi.iti.plateful.util;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CategoryDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.CuisineDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.dto.MealDto;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.db.CalendarDao;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.db.DateOnlyConverters;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.db.MealConverters;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.db.MealDao;

@Database(
    entities = {MealDto.class, CuisineDto.class, CategoryDto.class, CalendarMeal.class},
    version = 1
)
@TypeConverters({MealConverters.class, DateOnlyConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "AppDatabase";
    private static volatile AppDatabase instance;
    
    public abstract MealDao getMealDao();
    public abstract CalendarDao getCalendarDao();
    
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        DATABASE_NAME
                    ).build();
                }
            }
        }
        return instance;
    }
}
