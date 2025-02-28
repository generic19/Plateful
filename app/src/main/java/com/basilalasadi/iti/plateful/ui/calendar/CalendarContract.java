package com.basilalasadi.iti.plateful.ui.calendar;

import com.basilalasadi.iti.plateful.model.meal.CalendarMeal;

import java.util.Date;
import java.util.List;

public interface CalendarContract {
    interface View {
        void showCalendar(List<CalendarComponent> calendar);
        void showError(String message);
        
        abstract class CalendarComponent {}
        
        class DateCalendarComponent extends CalendarComponent {
            private final Date date;
            
            public DateCalendarComponent(Date date) {
                this.date = date;
            }
            
            public Date getDate() {
                return date;
            }
        }
        
        class MealCalendarComponent extends CalendarComponent{
            private final CalendarMeal meal;
            
            public MealCalendarComponent(CalendarMeal meal) {
                this.meal = meal;
            }
            
            public CalendarMeal getMeal() {
                return meal;
            }
        }
    }
    
    interface Presenter {
        void fetchCalendar();
    }
}
