package com.basilalasadi.iti.plateful.ui.authentication;

public interface AuthenticationContract {
    interface View {
        void navigateToHome();
        void showError(String errorMessage);
    }
    
    interface SelectionPresenter {
        void continueWithGoogle();
        void continueAsGuest();
    }
    
    interface SignInPresenter {
        void signIn(String email, String password);
    }
    
    interface SignUpPresenter {
        void signUp(String email, String password);
    }
}
