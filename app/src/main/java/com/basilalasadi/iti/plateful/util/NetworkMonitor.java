package com.basilalasadi.iti.plateful.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;

public class NetworkMonitor extends ConnectivityManager.NetworkCallback {
    private final ConnectivityManager connectivityManager;

    private static volatile NetworkMonitor instance;
    
    public static NetworkMonitor getInstance(Context context) {
        if (instance == null) {
            synchronized (NetworkMonitor.class) {
                if (instance == null) {
                    instance = new NetworkMonitor(context);
                }
            }
        }
        return instance;
    }
    
    private NetworkMonitor(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    
    public Flowable<Boolean> observeNetworkStatus() {
        return
            Flowable.<Boolean>create(
                emitter -> {
                    emitter.onNext(getNetworkStatusNow());
                    
                    NetworkRequest request = new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build();
                    
                    ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(@NonNull Network network) {
                            emitter.onNext(true);
                        }
                        
                        @Override
                        public void onLost(@NonNull Network network) {
                            emitter.onNext(false);
                        }
                    };
                    
                    connectivityManager.registerNetworkCallback(request, callback);
                    
                    emitter.setCancellable(() -> connectivityManager.unregisterNetworkCallback(callback));
                },
                BackpressureStrategy.LATEST
            )
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged();
    }
    
    private boolean getNetworkStatusNow() {
        Network network = connectivityManager.getActiveNetwork();
        
        if (network != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        
        return false;
    }
}