package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TopfoodFrameworkImpl implements TopfoodFramework {
    private final List<Restaurant> restaurants;
    private final String city;
    private TopfoodChangeListener listener;
    private DataPlugin currentDataPlugin;
    private DisplayPlugin currentDisplayPlugin;


    public TopfoodFrameworkImpl() {
        restaurants = new ArrayList<>();
    }

    public setRestaurants(DataPlugin dataPlugin) {

    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
    public void setTopfoodChangeListener(TopfoodChangeListener listener) {
        this.listener = listener;
    }
    public void registerDataPlugin(DataPlugin plugin){

    }
    public void registerDisplayPlugin(DisplayPlugin plugin){

    }
    private void notifyDataPluginRegistered(DataPlugin plugin){

    }
    private void notifyDisplayPluginRegistered(DisplayPlugin plugin){

    }
    public List<Restaurant> getTopRestaurants(Comparator<Restaurant> comparator){

    }
    public Map<String, Integer> getTopCuisines(){

    }
    public
}
