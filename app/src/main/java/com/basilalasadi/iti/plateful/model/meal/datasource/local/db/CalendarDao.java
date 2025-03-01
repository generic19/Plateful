package com.basilalasadi.iti.plateful.model.meal.datasource.local.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable putMeal(CalendarMeal calendarMeal);
    
    @Query("delete from CalendarMeal")
    Completable clearCalendar();
    
    @Insert
    Completable putMeals(List<CalendarMeal> calendarMeals);
    
    default Completable setCalendar(List<CalendarMeal> calendarMeals) {
        return Completable.concat(List.of(
            clearCalendar(),
            putMeals(calendarMeals)
        ));
    }
    
    @Delete
    Completable remove(CalendarMeal calendarMeal);
    
    @Query("select * from CalendarMeal order by date asc")
    Single<List<CalendarMeal>> getCalendar();
}
