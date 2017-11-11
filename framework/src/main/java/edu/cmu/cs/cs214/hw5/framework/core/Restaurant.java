package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.ArrayList;
import java.util.List;


public class Restaurant {
    private String name;
    private String category;
    private List<String> keys;
    private List<Integer> values;


    public Restaurant(String name, String category) {
        this.name = name;
        this.category = category;
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }
    public void addData(String key, Integer value){
        keys.add(key);
        values.add(value);
    }

    public String getCategory() {
        return category;
    }

    public List<String> getKeys() {
        return keys;
    }

    public List<Integer> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
