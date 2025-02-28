package com.basilalasadi.iti.plateful.model.authentication;

import android.content.Context;
import android.content.Intent;

import com.basilalasadi.iti.plateful.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public class FirebaseAuthManager {
    private static volatile FirebaseAuthManager instance;
    
    private final FirebaseAuth firebaseAuth;
    private final GoogleSignInClient googleSignInClient;
    private final @NonNull Observable<FirebaseUser> authStateObservable;
    
    public static FirebaseAuthManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FirebaseAuthManager.class) {
                if (instance == null) {
                    instance = new FirebaseAuthManager(context);
                }
            }
        }
        return instance;
    }
    
    private FirebaseAuthManager(Context context) {
        firebaseAuth = FirebaseAuth.getInstance();
        
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.google_cloud_web_client_id))
            .requestEmail()
            .requestProfile()
            .build();
        
        googleSignInClient = GoogleSignIn.getClient(context, options);
        
        authStateObservable = Observable.create(emitter -> {
            firebaseAuth.addAuthStateListener(auth -> {
                emitter.onNext(auth.getCurrentUser());
            });
        });
    }
    
    public @NonNull Observable<FirebaseUser> getAuthStateObservable() {
        return authStateObservable;
    }
    
    public FirebaseUser getCurrentUser() {
        return firebaseAuth.getCurrentUser();
    }
    
    public Intent getSignInWithGoogleIntent() {
        return googleSignInClient.getSignInIntent();
    }
    
    public Maybe<FirebaseUser> signInWithGoogle(Intent data) {
        return Maybe.create(emitter -> {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(account -> {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    
                    firebaseAuth.signInWithCredential(credential)
                        .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                        .addOnFailureListener(emitter::onError)
                        .addOnCanceledListener(emitter::onComplete);
                })
                .addOnFailureListener(emitter::onError)
                .addOnCanceledListener(emitter::onComplete);
        });
    }
    
    public Maybe<FirebaseUser> signIn(String email, String password) {
        return Maybe.create(emitter -> {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                .addOnFailureListener(emitter::onError)
                .addOnCanceledListener(emitter::onComplete);
        });
    }
    
    public Maybe<FirebaseUser> signUp(String email, String password) {
        return Maybe.create(emitter -> {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                .addOnFailureListener(emitter::onError)
                .addOnCanceledListener(emitter::onComplete);
        });
    }
    
    public Maybe<FirebaseUser> signInAnonymously() {
        return Maybe.create(emitter -> {
            firebaseAuth.signInAnonymously()
                .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                .addOnFailureListener(emitter::onError)
                .addOnCanceledListener(emitter::onComplete);
        });
    }
    
    public void signOut() {
        firebaseAuth.signOut();
    }
}
