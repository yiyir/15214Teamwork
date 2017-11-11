package edu.cmu.cs.cs214.hw5.plugin;

public interface DataPlugin {
    void retrieveData(String city);
    List<Restaurant> getRestaurants();
}
