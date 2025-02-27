package com.basilalasadi.iti.plateful.util;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapRecyclerContentLinearLayoutManager extends LinearLayoutManager {
    
    public WrapRecyclerContentLinearLayoutManager(Context context) {
        super(context, VERTICAL, false);
    }
    
    @Override
    public void onMeasure(@NonNull RecyclerView.Recycler recycler, @NonNull RecyclerView.State state, int widthSpec, int heightSpec) {
        super.onMeasure(recycler, state, widthSpec, heightSpec);
        
        int totalHeight = 0;
        int itemCount = state.getItemCount();
        
        for (int position = 0; position < itemCount; position++) {
            View view = recycler.getViewForPosition(position);
            measureChild(view, widthSpec, heightSpec);
            totalHeight += view.getMeasuredHeight();
        }
        
        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), totalHeight);
    }
}
