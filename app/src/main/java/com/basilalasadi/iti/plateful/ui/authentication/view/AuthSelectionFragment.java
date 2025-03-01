package com.basilalasadi.iti.plateful.ui.authentication.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.databinding.FragmentAuthSelectionBinding;
import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.ui.authentication.AuthenticationContract;
import com.basilalasadi.iti.plateful.ui.authentication.presenter.AuthSelectionPresenter;
import com.google.android.material.snackbar.Snackbar;

public class AuthSelectionFragment extends Fragment implements AuthenticationContract.View {
    private static final int REQUEST_CODE_GOOGLE = 100;
    
    private AuthenticationContract.SelectionPresenter presenter;
    private FragmentAuthSelectionBinding binding;
    private NavController navController;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAuthSelectionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new AuthSelectionPresenter(this, FirebaseAuthManager.getInstance(getContext()));
        navController = Navigation.findNavController(binding.getRoot());
        
        binding.btnGoogle.setOnClickListener(v -> {
            Intent intent = FirebaseAuthManager.getInstance(getContext()).getSignInWithGoogleIntent();
            
            startActivityForResult(intent, REQUEST_CODE_GOOGLE);
        });
        
        binding.btnSignUp.setOnClickListener(v -> {
            navController.navigate(
                AuthSelectionFragmentDirections.actionAuthSelectionFragmentToAuthSignUpFragment()
            );
        });
        
        binding.btnGuest.setOnClickListener(v -> {
            presenter.continueAsGuest();
        });
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GOOGLE && data != null) {
            presenter.continueWithGoogle(data);
        }
    }
    
    @Override
    public void navigateToHome() {
        Navigation.findNavController(binding.getRoot()).navigate(
            AuthSelectionFragmentDirections.actionAuthSelectionFragmentToHomeTabFragment()
        );
    }
    
    @Override
    public void showMessage(String message, int duration) {
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
}