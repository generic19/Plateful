package com.basilalasadi.iti.plateful.model.authentication.repository;

import com.basilalasadi.iti.plateful.model.authentication.User;

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
