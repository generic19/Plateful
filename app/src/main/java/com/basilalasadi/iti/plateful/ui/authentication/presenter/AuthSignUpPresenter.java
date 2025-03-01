package com.basilalasadi.iti.plateful.ui.authentication.presenter;

import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.ui.authentication.AuthenticationContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AuthSignUpPresenter implements AuthenticationContract.SignUpPresenter {
    private final AuthenticationContract.View view;
    private final FirebaseAuthManager authManager;
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public AuthSignUpPresenter(AuthenticationContract.View view, FirebaseAuthManager authManager) {
        this.view = view;
        this.authManager = authManager;
    }
    
    @Override
    public void signUp(String email, String password) {
        disposables.add(
            authManager.signUp(email, password).subscribe(
                user -> view.navigateToHome(),
                error -> view.showMessage(error.getLocalizedMessage(), 5000)
            )
        );
    }
    
    @Override
    public void dispose() {
        disposables.clear();
    }
}
