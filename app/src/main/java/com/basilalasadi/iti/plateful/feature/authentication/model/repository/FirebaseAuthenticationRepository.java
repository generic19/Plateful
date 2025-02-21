package com.basilalasadi.iti.plateful.feature.authentication.model.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import com.basilalasadi.iti.plateful.feature.authentication.model.User;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseAuthenticationRepository implements AuthenticationRepository {
    private static volatile FirebaseAuthenticationRepository instance;
    
    public static FirebaseAuthenticationRepository getInstance() {
        if (instance == null) {
            synchronized (FirebaseAuthenticationRepository.class) {
                if (instance == null) {
                    instance = new FirebaseAuthenticationRepository();
                }
            }
        }
        return instance;
    }
    
    private FirebaseAuthenticationRepository() {
    
    }
    
    
    @Override
    public Single<User> signInWithEmail(String email, String password) {
        return null;
    }
    
    @Override
    public Single<User> signInWithGoogle() {
        return null;
    }
    
    @Override
    public Completable signInAsGuest() {
        return null;
    }
    
    @Override
    public Completable signOut() {
        return null;
    }
    
    @Override
    public Observable<User> getStatusObservable() {
        return null;
    }
    
    @Override
    public User getCurrentUser() {
        return null;
    }
}
