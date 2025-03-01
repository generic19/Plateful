package com.basilalasadi.iti.plateful.ui.authentication.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.service.autofill.RegexValidator;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentAuthSignUpBinding;
import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.basilalasadi.iti.plateful.ui.authentication.AuthenticationContract;
import com.basilalasadi.iti.plateful.ui.authentication.presenter.AuthSelectionPresenter;
import com.basilalasadi.iti.plateful.ui.authentication.presenter.AuthSignUpPresenter;
import com.google.android.material.snackbar.Snackbar;

public class AuthSignUpFragment extends Fragment implements AuthenticationContract.View {
    private AuthenticationContract.SignUpPresenter presenter;
    private FragmentAuthSignUpBinding binding;
    private NavController navController;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAuthSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new AuthSignUpPresenter(this, FirebaseAuthManager.getInstance(getContext()));
        navController = Navigation.findNavController(binding.getRoot());
        
        binding.btnSignUp.setOnClickListener(v -> {
            String email = binding.editEmail.getText().toString();
            String password = binding.editPassword.getText().toString();
            
            boolean isValid = true;
            
            if (email.isBlank()) {
                binding.editEmail.setError("Email address is required.");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editEmail.setError("Invalid email address format.");
                isValid = false;
            } else {
                binding.editEmail.setError(null);
            }
            
            if (password.isBlank()) {
                binding.editPassword.setError("Password is required.");
                isValid = false;
            } else if (password.length() < 8) {
                binding.editPassword.setError("Password is too short. Must be at least 8 characters.");
                isValid = false;
            } else {
                binding.editPassword.setError(null);
            }
            
            if (isValid) {
                binding.btnSignUp.setEnabled(false);
                presenter.signUp(email, password);
            }
        });
        
        binding.btnSignIn.setOnClickListener(v -> {
            navController.navigate(AuthSignUpFragmentDirections.actionAuthSignUpFragmentToAuthSignInFragment());
        });
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }
    
    @Override
    public void navigateToHome() {
        binding.btnSignUp.setEnabled(true);
        
        navController.navigate(
            AuthSignUpFragmentDirections.actionAuthSignUpFragmentToHomeTabFragment()
        );
    }
    
    @Override
    public void showMessage(String message, int duration) {
        binding.btnSignUp.setEnabled(true);
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
}