package com.basilalasadi.iti.plateful.ui.authentication.presenter;

import android.content.Intent;

import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.ui.authentication.AuthenticationContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AuthSelectionPresenter implements AuthenticationContract.SelectionPresenter {
    private final AuthenticationContract.View view;
    private final FirebaseAuthManager authManager;
    private final CompositeDisposable disposables = new CompositeDisposable();
    
    public AuthSelectionPresenter(AuthenticationContract.View view, FirebaseAuthManager authManager) {
        this.view = view;
        this.authManager = authManager;
    }
    
    @Override
    public void continueWithGoogle(Intent data) {
        disposables.add(
            authManager.signInWithGoogle(data).subscribe(
                user -> view.navigateToHome(),
                error -> view.showMessage(error.getLocalizedMessage(), 3000)
            )
        );
    }
    
    @Override
    public void continueAsGuest() {
        disposables.add(
            authManager.signInAnonymously().subscribe(
                u -> view.navigateToHome(),
                error -> {
                    view.showMessage("Continuing in offline mode.", 5000);
                    view.navigateToHome();
                }
            )
        );
    }
    
    @Override
    public void dispose() {
        disposables.clear();
    }
}
