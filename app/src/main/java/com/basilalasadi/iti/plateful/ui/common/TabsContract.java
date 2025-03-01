package com.basilalasadi.iti.plateful.ui.common;

public interface TabsContract {
    interface View {
        void navigateToTab(Tab tab);
        void setSelectedTab(Tab tab);
    }
    
    interface Presenter {
        boolean tabNavigationRequested(Tab tab);
        void stopMonitoringNetwork();
    }
    
    enum Tab {
        home,
        search,
        explore,
        favorites,
        calendar
    }
}
