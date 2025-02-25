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

public class AuthSignInFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_sign_in, container, false);
        
        EditText editEmail = view.findViewById(R.id.editEmail);
        EditText editPassword = view.findViewById(R.id.editPassword);
        Button btnSignIn = view.findViewById(R.id.btnSignIn);
        Button btnJoinUs = view.findViewById(R.id.btnJoinUs);
        
        btnJoinUs.setOnClickListener(v -> {
            Navigation.findNavController(view).navigateUp();
        });
        
        return view;
    }
}