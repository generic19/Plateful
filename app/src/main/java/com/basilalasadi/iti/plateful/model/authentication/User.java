package com.basilalasadi.iti.plateful.model.authentication;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public class User {
    private final String id;
    private final String email;
    private final String displayName;
    private final Uri photoUri;
    private final boolean anonymous;
    private final boolean authenticated;
    
    public User(String id, String email, String displayName, Uri photoUri, boolean anonymous, boolean authenticated) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.photoUri = photoUri;
        this.anonymous = anonymous;
        this.authenticated = authenticated;
    }
    
    public User() {
        this(null, null, null, null, false, false);

    }
    
    public User(FirebaseUser firebaseUser) {
        this(
            firebaseUser.getUid(),
            firebaseUser.getEmail(),
            firebaseUser.getDisplayName(),
            firebaseUser.getPhotoUrl(),
            firebaseUser.isAnonymous(),
            true
        );
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
