package com.basilalasadi.iti.plateful.ui.splash.presenter;

import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.ui.splash.SplashContact;
import com.google.firebase.auth.FirebaseUser;

public class SplashPresenter implements SplashContact.Presenter {
    private final SplashContact.View view;
    private final FirebaseAuthManager authManager;
    
    public SplashPresenter(SplashContact.View view, FirebaseAuthManager authManager) {
        this.view = view;
        this.authManager = authManager;
    }
   
    @Override
    public void animationFinished() {
        FirebaseUser currentUser = authManager.getCurrentUser();
        
        if (currentUser == null || currentUser.isAnonymous()) {
            view.navigateToAuthentication();
        } else {
            view.navigateToHome();
        }
    }
}
