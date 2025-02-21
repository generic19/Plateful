package com.basilalasadi.iti.plateful.feature.authentication.model.repository;


import com.basilalasadi.iti.plateful.feature.authentication.model.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface AuthenticationRepository {
    Single<User> signInWithEmail(String email, String password);
    Single<User> signInWithGoogle();
    Completable signInAsGuest();
    Completable signOut();
    Observable<User> getStatusObservable();
    User getCurrentUser();
}
