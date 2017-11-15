package edu.cmu.cs.cs214.hw5.framework.core;


import java.util.List;

public interface DataPlugin {
    List<Restaurant> getRestaurants(String city);

    @Override
    String toString();
}
