package com.basilalasadi.iti.plateful.ui.explore.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basilalasadi.iti.plateful.databinding.FragmentSectionsBinding;
import com.basilalasadi.iti.plateful.model.meal.Section;
import com.basilalasadi.iti.plateful.model.meal.datasource.MealRepository;
import com.basilalasadi.iti.plateful.model.meal.datasource.local.MealLocalDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.MealRemoteDataSourceImpl;
import com.basilalasadi.iti.plateful.model.meal.datasource.remote.api.MealService;
import com.basilalasadi.iti.plateful.ui.explore.SectionsContract;
import com.basilalasadi.iti.plateful.ui.explore.presenter.SectionsPresenter;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.disposables.Disposable;

public class SectionsFragment extends Fragment implements SectionsAdapter.Listener, SectionsContract.View {
    private static final String TAG = "SectionsFragment";
    private final Class<? extends Section> type;
    private final Delegate delegate;
    private FragmentSectionsBinding binding;
    
    private SectionsAdapter sectionsAdapter;
    
    private @NonNull ObservableEmitter<String> textChangeEmitter;
    private @NonNull Disposable disposable;
    private SectionsContract.Presenter presenter;
    
    public interface Delegate {
        void onSectionClicked(Section section);
    }
    
    public SectionsFragment(Class<? extends Section> type, Delegate delegate) {
        this.type = type;
        this.delegate = delegate;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSectionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new SectionsPresenter(type, this, MealRepository.getInstance(
            new MealLocalDataSourceImpl(getContext()),
            new MealRemoteDataSourceImpl(MealService.create())
        ));
        
        presenter.fetchAll();
        
        sectionsAdapter = new SectionsAdapter(getContext(), this);
        binding.recycler.setAdapter(sectionsAdapter);
        
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        
        disposable = Observable.<String>create(emitter -> {
            binding.editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                
                @Override
                public void afterTextChanged(Editable s) {
                    emitter.onNext(s.toString());
                }
            });
        })
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                presenter::filter,
                error -> Log.e(TAG, error.getLocalizedMessage())
            );
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (disposable != null) {
            disposable.dispose();
        }
    }
    
    @Override
    public void onSectionClicked(Section section) {
        delegate.onSectionClicked(section);
    }
    
    @Override
    public void showSections(List<Section> sections) {
        sectionsAdapter.setSections(sections);
    }
    
    @Override
    public void showMessage(String message, int duration) {
        Snackbar.make(binding.getRoot(), message, duration).show();
    }
}