package com.basilalasadi.iti.plateful.ui.splash.view;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.basilalasadi.iti.plateful.R;

public class SplashFragment extends Fragment {
    private static final String TAG = "SplashFragment";
    
    private LottieAnimationView animationView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        
        setupAdditionalBottomPadding(view);
        setupAnimation(view);
        
        return view;
    }
    
    private void setupAdditionalBottomPadding(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            View bottomView = view.findViewById(R.id.textView3);
            
            bottomView.setPadding(
                    bottomView.getPaddingLeft(),
                    bottomView.getPaddingTop(),
                    bottomView.getPaddingRight(),
                    bottomView.getPaddingBottom() + systemBars.bottom
            );
            
            return insets;
        });
    }
    
    private void setupAnimation(View view) {
        animationView = view.findViewById(R.id.animationView);
        
        animationView.setAnimation(R.raw.anim_splash);
        
        animationView.setMinProgress(0.6f);
        animationView.setMaxProgress(0.95f);
        animationView.setSpeed(-0.33f);
        
        animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
            }
            
            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_authSelectionFragment);
            }
            
            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }
            
            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {
            }
        });
    }
    
    @Override
    public void onStart() {
        super.onStart();
        animationView.playAnimation();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        animationView.pauseAnimation();
    }
}