package com.basilalasadi.iti.plateful.ui.explore.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.R;
import com.basilalasadi.iti.plateful.databinding.FragmentSectionsBinding;
import com.basilalasadi.iti.plateful.model.meal.Section;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.disposables.Disposable;

public class SectionsFragment<S extends Section> extends Fragment implements FilterableSectionsAdapter.Listener<S> {
    private static final String TAG = "SectionsFragment";
    private FragmentSectionsBinding binding;
    
    private FilterableSectionsAdapter<S> sectionsAdapter;
    private final List<S> sections;
    
    private @NonNull ObservableEmitter<String> textChangeEmitter;
    private @NonNull Disposable disposable;
    
    public SectionsFragment(List<S> sections) {
        this.sections = sections;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSectionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        sectionsAdapter = new FilterableSectionsAdapter<>(getContext(), sections, this);
        binding.recycler.setAdapter(sectionsAdapter);
        
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        
        disposable = Observable.<String>create(emitter -> this.textChangeEmitter = emitter)
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                sectionsAdapter::filter,
                error -> Log.e(TAG, error.getLocalizedMessage())
            );
        
        binding.editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            
            @Override
            public void afterTextChanged(Editable s) {
                if (textChangeEmitter != null) {
                    textChangeEmitter.onNext(s.toString());
                }
            }
        });
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (disposable != null) {
            disposable.dispose();
        }
    }
    
    @Override
    public void onSectionClicked(S section) {
    
    }
}