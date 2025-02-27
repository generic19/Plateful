package com.basilalasadi.iti.plateful.model.meal;

public abstract class Section {
    private final String name;
    
    public Section(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
