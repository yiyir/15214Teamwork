package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class GoogleDataPlugin implements DataPlugin {
    @Override
    public List<Restaurant> getRestaurants(String city) {
        return new ArrayList<Restaurant>();
    }

    @Override
    public String toString() {
        return "GoogleDataPlugin";
    }
}
