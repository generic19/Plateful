package com.basilalasadi.iti.plateful.ui.authentication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.basilalasadi.iti.plateful.R;

public class AuthSignUpFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_sign_up, container, false);
        
        EditText editEmail = view.findViewById(R.id.editEmail);
        EditText editPassword = view.findViewById(R.id.editPassword);
        Button btnSignUp = view.findViewById(R.id.btnSignUp);
        Button btnSignIn = view.findViewById(R.id.btnSignIn);
        
        btnSignIn.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_authSignUpFragment_to_authSignInFragment);
        });
        
        return view;
    }
}