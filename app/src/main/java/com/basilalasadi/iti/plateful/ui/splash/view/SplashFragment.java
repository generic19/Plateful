package com.basilalasadi.iti.plateful.ui.splash.view;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.basilalasadi.iti.plateful.databinding.FragmentSplashBinding;
import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.ui.splash.SplashContact;
import com.basilalasadi.iti.plateful.ui.splash.presenter.SplashPresenter;

public class SplashFragment extends Fragment implements SplashContact.View {
    private FragmentSplashBinding binding;
    private SplashContact.Presenter presenter;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSplashBinding.inflate(inflater, container, false);
        
        presenter = new SplashPresenter(this, FirebaseAuthManager.getInstance(getContext()));
        
        setupAdditionalBottomPadding();
        setupAnimation();
        
        return binding.getRoot();
    }
    
    private void setupAdditionalBottomPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            binding.textView3.setPadding(
                    binding.textView3.getPaddingLeft(),
                    binding.textView3.getPaddingTop(),
                    binding.textView3.getPaddingRight(),
                binding.textView3.getPaddingBottom() + systemBars.bottom
            );
            
            return insets;
        });
    }
    
    private void setupAnimation() {
        binding.animationView.setAnimation(R.raw.anim_splash);
        
        binding.animationView.setMinProgress(0.6f);
        binding.animationView.setMaxProgress(0.95f);
        binding.animationView.setSpeed(-0.6f);
        
        binding.animationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                presenter.animationFinished();
            }
            
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
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
        binding.animationView.playAnimation();
    }
    
    @Override
    public void onStop() {
        super.onStop();
        binding.animationView.pauseAnimation();
    }
    
    @Override
    public void navigateToAuthentication() {
        Navigation.findNavController(binding.getRoot()).navigate(
            SplashFragmentDirections.actionSplashFragmentToAuthSelectionFragment()
        );
    }
    
    @Override
    public void navigateToHome() {
        Navigation.findNavController(binding.getRoot()).navigate(
            SplashFragmentDirections.actionSplashFragmentToHomeTabFragment()
        );
    }
}