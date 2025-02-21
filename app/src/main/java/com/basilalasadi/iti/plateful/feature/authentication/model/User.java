package com.basilalasadi.iti.plateful.feature.authentication.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public class User {
    private final String id;
    private final String email;
    private final String displayName;
    private final Uri photoUri;
    private final boolean anonymous;
    private final boolean authenticated;
    
    public User(FirebaseUser firebaseUser) {
        this.id = firebaseUser.getUid();
        this.email = firebaseUser.getEmail();
        this.displayName = firebaseUser.getDisplayName();
        this.photoUri = firebaseUser.getPhotoUrl();
        this.anonymous = firebaseUser.isAnonymous();
        this.authenticated = true;
    }
    
    public User() {
        this.id = null;
        this.email = null;
        this.displayName = null;
        this.photoUri = null;
        this.anonymous = true;
        this.authenticated = false;
    }
    
    public String getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Uri getPhotoUri() {
        return photoUri;
    }
    
    public boolean isAnonymous() {
        return anonymous;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
}
