package com.basilalasadi.iti.plateful.ui.splash;

public interface SplashContact {
    interface View {
        void navigateToAuthentication();
        void navigateToHome();
    }
    
    interface Presenter {
        void checkAuthentication();
        void animationFinished();
    }
}
