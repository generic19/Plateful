package com.basilalasadi.iti.plateful.ui.mealdetail;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MealDetailNavigationData implements Parcelable {
    private final String mealId;
    private final String title;
    private final String subtitle;
    
    public MealDetailNavigationData(@NonNull String mealId, @Nullable String title, @Nullable String subtitle) {
        this.mealId = mealId;
        this.title = title;
        this.subtitle = subtitle;
    }
    
    protected MealDetailNavigationData(Parcel in) {
        this.mealId = in.readString();
        this.title = in.readString();
        this.subtitle = in.readString();
    }
    
    public String getMealId() {
        return mealId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getSubtitle() {
        return subtitle;
    }
    
    public static final Creator<MealDetailNavigationData> CREATOR = new Creator<>() {
        @Override
        public MealDetailNavigationData createFromParcel(Parcel in) {
            return new MealDetailNavigationData(in);
        }
        
        @Override
        public MealDetailNavigationData[] newArray(int size) {
            return new MealDetailNavigationData[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.mealId);
        dest.writeString(this.title);
        dest.writeString(this.subtitle);
    }
}
