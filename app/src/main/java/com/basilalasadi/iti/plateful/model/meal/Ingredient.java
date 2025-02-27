package com.basilalasadi.iti.plateful.model.meal;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Ingredient extends Section implements Parcelable {
    final String measurement;
    
    public Ingredient(String name, String measurement) {
        super(name);
        this.measurement = measurement;
    }
    
    public String getMeasurement() {
        return measurement;
    }
    
    protected Ingredient(Parcel in) {
        super(in.readString());
        measurement = in.readString();
    }
    
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }
        
        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(measurement);
    }
}
