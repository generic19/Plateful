package com.basilalasadi.iti.plateful.model.authentication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.basilalasadi.iti.plateful.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Optional;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class FirebaseAuthManager {
    private static final String TAG = "FirebaseAuthManager";
    
    private static volatile FirebaseAuthManager instance;
    
    private FirebaseAuth firebaseAuth;
    private final GoogleSignInClient googleSignInClient;
    private final @NonNull Observable<Optional<FirebaseUser>> authStateObservable;
    
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
                emitter.onNext(Optional.ofNullable(auth.getCurrentUser()));
            });
        });
    }
    
    public @NonNull Observable<Optional<FirebaseUser>> getAuthStateObservable() {
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
                    
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    
                    if (currentUser == null) {
                        firebaseAuth.signInWithCredential(credential)
                            .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                            .addOnFailureListener(error -> {
                                Log.d(TAG, "signInWithGoogle: signInWithCredential: error: " + error.getLocalizedMessage(), error);
                                emitter.onError(error);
                            })
                            .addOnCanceledListener(emitter::onComplete);
                    } else if (currentUser.isAnonymous()) {
                        currentUser.linkWithCredential(credential)
                            .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                            .addOnFailureListener(error -> {
                                Log.d(TAG, "signInWithGoogle: linkWithCredential: error: " + error.getLocalizedMessage(), error);
                                emitter.onError(error);
                            })
                            .addOnCanceledListener(emitter::onComplete);
                    } else {
                        emitter.onSuccess(currentUser);
                    }
                })
                .addOnFailureListener(error -> {
                    Log.d(TAG, "signInWithGoogle: error: " + error.getLocalizedMessage(), error);
                    emitter.onError(error);
                })
                .addOnCanceledListener(emitter::onComplete);
        });
    }
    
    public Maybe<FirebaseUser> signIn(String email, String password) {
        return Maybe.create(emitter -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            
            if (currentUser == null) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                    .addOnFailureListener(error -> {
                        Log.d(TAG, "signIn: signInWithEmailAndPassword: error: " + error.getLocalizedMessage(), error);
                        emitter.onError(error);
                    })
                    .addOnCanceledListener(emitter::onComplete);
            } else if (currentUser.isAnonymous()) {
                currentUser.linkWithCredential(EmailAuthProvider.getCredential(email, password))
                    .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                    .addOnFailureListener(error -> {
                        Log.d(TAG, "signIn: linkWithCredential: error: " + error.getLocalizedMessage(), error);
                        emitter.onError(error);
                    })
                    .addOnCanceledListener(emitter::onComplete);
            } else {
                emitter.onSuccess(currentUser);
            }
        });
    }
    
    public Maybe<FirebaseUser> signUp(String email, String password) {
        return Maybe.create(emitter -> {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            
            if (currentUser == null) {
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                    .addOnFailureListener(error -> {
                        Log.d(TAG, "signUp: createUserWithEmailAndPassword: error: " + error.getLocalizedMessage(), error);
                        emitter.onError(error);
                    })
                    .addOnCanceledListener(emitter::onComplete);
            } else if (currentUser.isAnonymous()) {
                currentUser.linkWithCredential(EmailAuthProvider.getCredential(email, password))
                    .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                    .addOnFailureListener(error -> {
                        Log.d(TAG, "signUp: linkWithCredential: error: " + error.getLocalizedMessage(), error);
                        emitter.onError(error);
                    })
                    .addOnCanceledListener(emitter::onComplete);
            } else {
                emitter.onSuccess(currentUser);
            }
        });
    }
    
    public Maybe<FirebaseUser> signInAnonymously() {
        return Maybe.create(emitter -> {
            firebaseAuth.signInAnonymously()
                .addOnSuccessListener(result -> emitter.onSuccess(result.getUser()))
                .addOnFailureListener(error -> {
                    Log.d(TAG, "signInAnonymously: error: " + error.getLocalizedMessage(), error);
                    emitter.onError(error);
                })
                .addOnCanceledListener(emitter::onComplete);
        });
    }
    
    public Completable signOut() {
        return Completable.create(emitter -> {
            authStateObservable.subscribe(new Observer<>() {
                private @NonNull Disposable disposable;
                
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    this.disposable = d;
                    firebaseAuth.signOut();
                }
                
                @Override
                public void onNext(@NonNull Optional<FirebaseUser> user) {
                    if (user.isEmpty()) {
                        emitter.onComplete();
                        disposable.dispose();
                    }
                }
                
                @Override
                public void onError(@NonNull Throwable error) {
                    Log.d(TAG, "signOut: error: " + error.getLocalizedMessage(), error);
                }
                
                @Override
                public void onComplete() {
                }
            });
        });
    }
}
