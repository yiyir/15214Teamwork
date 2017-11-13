package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.Restaurant;

import java.util.List;

public interface DataPlugin {
    List<Restaurant> getRestaurants(String city);
}
