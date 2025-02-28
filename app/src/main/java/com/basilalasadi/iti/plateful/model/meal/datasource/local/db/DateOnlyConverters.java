package com.basilalasadi.iti.plateful.model.meal.datasource.local.db;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateOnlyConverters {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    
    @TypeConverter
    public static String fromDate(Date date) {
        return formatter.format(date);
    }
    
    @TypeConverter
    public static Date toDate(String dateString) throws ParseException {
        return formatter.parse(dateString);
    }
}
