package com.basilalasadi.iti.plateful.ui.common.presenter;

import android.util.Log;

import com.basilalasadi.iti.plateful.ui.common.TabsContract;
import com.basilalasadi.iti.plateful.util.NetworkMonitor;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.Disposable;

public class TabsPresenter implements TabsContract.Presenter {
    private static final String TAG = "TabsPresenter";
    
    private final TabsContract.View view;
    private final Disposable disposable;
    
    private boolean networkIsConnected = false;
    private TabsContract.Tab currentTab = null;
    
    public TabsPresenter(TabsContract.View view, NetworkMonitor networkMonitor) {
        this.view = view;
        
        disposable = networkMonitor.observeNetworkStatus().subscribe(
            isConnected -> {
                networkIsConnected = isConnected;
                
                if (isConnected) {
                    if (currentTab == null) {
                        currentTab = TabsContract.Tab.home;
                        
                        view.setSelectedTab(currentTab);
                        view.navigateToTab(currentTab);
                    }
                } else {
                    if (currentTab == null || (
                        !TabsContract.Tab.favorites.equals(currentTab)
                        && !TabsContract.Tab.calendar.equals(currentTab))) {
                        
                        currentTab = TabsContract.Tab.favorites;
                        
                        view.setSelectedTab(currentTab);
                        view.navigateToTab(currentTab);
                    }
                }
                
                
            },
            error -> Log.e(TAG, "Network status error: " + error.getLocalizedMessage(), error)
        );
    }
    
    @Override
    public boolean tabNavigationRequested(TabsContract.Tab tab) {
        if (networkIsConnected) {
            currentTab = tab;
            view.navigateToTab(currentTab);
            
            return true;
        } else {
            if (TabsContract.Tab.favorites.equals(tab) || TabsContract.Tab.calendar.equals(tab)) {
                currentTab = tab;
                view.navigateToTab(currentTab);
                
                return true;
            } else {
                return false;
            }
        }
    }
    
    @Override
    public void stopMonitoringNetwork() {
        disposable.dispose();
    }
    
}
