package com.basilalasadi.iti.plateful.ui.calendar;

import com.basilalasadi.iti.plateful.model.meal.Meal;

import java.util.Date;
import java.util.List;

public interface CalendarContract {
    interface View {
        void showCalendar(List<CalendarContract.CalendarComponent> calendar);
        void showMessage(String message, int duration);
        
    }
    
    interface Presenter {
        void fetchCalendar();
        
        void removeCalendarMeal(Date date, Meal meal);
        void favoriteAction(Meal meal);
        
        void dispose();
    }
    
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
    
    class MealCalendarComponent extends CalendarComponent {
        private final Meal meal;
        private final Date date;
        
        public MealCalendarComponent(Meal meal, Date date) {
            this.meal = meal;
            this.date = date;
        }
        
        public Meal getMeal() {
            return meal;
        }
        
        public Date getDate() {
            return date;
        }
    }
}
