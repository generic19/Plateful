package com.basilalasadi.iti.plateful.feature.authentication.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.basilalasadi.iti.plateful.R;


public class AuthSelectionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_auth_selection, container, false);
        
        Button btnGoogle = view.findViewById(R.id.btnGoogle);
        Button btnEmail = view.findViewById(R.id.btnSignUp);
        Button btnGuest = view.findViewById(R.id.btnGuest);
        
        btnEmail.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_authSelectionFragment_to_authSignUpFragment);
        });
        
        btnGuest.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_authSelectionFragment_to_homeTabFragment);
        });
        
        return view;
    }
}