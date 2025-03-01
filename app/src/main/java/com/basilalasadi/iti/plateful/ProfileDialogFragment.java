package com.basilalasadi.iti.plateful;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.TypedValueCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.basilalasadi.iti.plateful.databinding.FragmentProfileDialogBinding;
import com.basilalasadi.iti.plateful.model.authentication.FirebaseAuthManager;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;

public class ProfileDialogFragment extends DialogFragment {
    private FragmentProfileDialogBinding binding;
    private Disposable disposable;
    private final Listener listener;
    private final UserInfo userInfo;
    
    
    public interface Listener {
        void onSignOutPressed();
        void onBackupPressed();
        void onRestorePressed();
    }
    
    public static class UserInfo {
        private final String name;
        private final String email;
        private final Uri photo;
        
        public UserInfo(String name, String email, Uri photo) {
            this.name = name;
            this.email = email;
            this.photo = photo;
        }
    }
    
    public ProfileDialogFragment(Listener listener, UserInfo userInfo) {
        this.listener = listener;
        this.userInfo = userInfo;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        
        getDialog().getWindow().setLayout(
            (int) TypedValueCompat.dpToPx(300, metrics),
            (int) TypedValueCompat.dpToPx(380, metrics)
        );
        
        binding.txtName.setText(userInfo.name != null && userInfo.name.isEmpty() ? "Guest Account" : userInfo.name);
        binding.txtEmail.setText(userInfo.email);
        
        if (userInfo.photo != null) {
            binding.imageProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            binding.imageProfile.setImageTintList(null);
            
            Glide.with(this)
                .load(userInfo.photo)
                .into(binding.imageProfile);
        }
        
        binding.btnBackup.setOnClickListener(v -> {
            listener.onBackupPressed();
            dismiss();
        });
        binding.btnRestore.setOnClickListener(v -> {
            listener.onRestorePressed();
            dismiss();
        });
        
        binding.btnSignOut.setOnClickListener(v -> {
            binding.btnSignOut.setEnabled(false);
            listener.onSignOutPressed();
        });
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (disposable != null) {
            disposable.dispose();
        }
    }
}