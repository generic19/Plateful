package com.basilalasadi.iti.plateful.ui.explore;

import com.basilalasadi.iti.plateful.model.meal.Section;

import java.util.List;

public interface SectionsContract {
    interface View {
        void showSections(List<Section> sections);
        void showMessage(String message, int duration);
    }
    
    interface Presenter {
        void fetchAll();
        void filter(String query);
        
        void dispose();
    }
}
