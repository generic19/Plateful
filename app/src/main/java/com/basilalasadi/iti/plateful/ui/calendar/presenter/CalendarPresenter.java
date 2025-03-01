package com.basilalasadi.iti.plateful.ui.calendar.presenter;

import android.util.Log;
import android.util.Pair;

import com.basilalasadi.iti.plateful.model.meal.Meal;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.ui.calendar.CalendarContract;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class CalendarPresenter implements CalendarContract.Presenter {
    private static final String TAG = "CalendarPresenter";
    
    private final CalendarContract.View view;
    private final MealRepository mealRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public CalendarPresenter(CalendarContract.View view, MealRepository mealRepository) {
        this.view = view;
        this.mealRepository = mealRepository;
    }
    
    @Override
    public void fetchCalendar() {
        disposables.add(
        mealRepository.getCalendar()
            .flatMap(calendarMeals -> Observable.fromIterable(calendarMeals)
                .flatMapSingle(calendarMeal -> mealRepository.getById(calendarMeal.getMealId())
                    .zipWith(Single.just(calendarMeal.getDate()), (meal, date) -> Pair.create(meal, date))
                )
                .collectInto(new ArrayList<Pair<Meal, Date>>(), (list, pair) -> list.add(pair))
            )
            .subscribe(
                dateMeals -> {
                    List<CalendarContract.CalendarComponent> components = new ArrayList<>();
                    
                    if (!dateMeals.isEmpty()) {
                        Date currentDate = dateMeals.get(0).second;
                        components.add(new CalendarContract.DateCalendarComponent(currentDate));
                        
                        for (Pair<Meal, Date> pair : dateMeals) {
                            if (!isSameDay(currentDate, pair.second)) {
                                currentDate = dateMeals.get(0).second;
                                components.add(new CalendarContract.DateCalendarComponent(currentDate));
                            }
                            
                            components.add(new CalendarContract.MealCalendarComponent(pair.first, pair.second));
                        }
                    }
                    
                    view.showCalendar(components);
                },
                error -> {
                    Log.d(TAG, "fetchCalendar", error);
                }
            )
        );
    }
    
    public boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate1.isEqual(localDate2);
    }
    
    @Override
    public void removeCalendarMeal(Date date, Meal meal) {
        disposables.add(
            mealRepository.removeCalendarDate(meal, date).subscribe(
                () -> fetchCalendar(),
                error -> Log.d(TAG, "removeCalendarMeal", error)
            )
        );
    }
    
    @Override
    public void favoriteAction(Meal meal) {
        boolean isFavorite = meal.isFavorite();
        
        if (!isFavorite) {
            disposables.add(
                mealRepository.addFavoriteMeal(meal).subscribe(
                    () -> meal.setFavorite(true),
                    error -> {
                        Log.d(TAG, "addFavoriteMeal", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                    }
                )
            );
        } else {
            disposables.add(
                mealRepository.removeFavoriteMeal(meal).subscribe(
                    () -> {
                        meal.setFavorite(false);
                    },
                    error -> {
                        Log.d(TAG, "removeFavoriteMeal", error);
                        view.showMessage(error.getLocalizedMessage(), 5000);
                    }
                )
            );
        }
    }
    
    @Override
    public void dispose() {
        disposables.dispose();
    }
}
