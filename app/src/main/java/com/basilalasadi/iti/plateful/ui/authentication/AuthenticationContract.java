package com.basilalasadi.iti.plateful.ui.authentication;

import android.content.Intent;

public interface AuthenticationContract {
    interface View {
        void navigateToHome();
        void showMessage(String errorMessage, int duration);
    }
    
    interface SelectionPresenter {
        void continueWithGoogle(Intent data);
        void continueAsGuest();
        void dispose();
    }
    
    interface SignInPresenter {
        void signIn(String email, String password);
        void dispose();
    }
    
    interface SignUpPresenter {
        void signUp(String email, String password);
        void dispose();
    }
}
