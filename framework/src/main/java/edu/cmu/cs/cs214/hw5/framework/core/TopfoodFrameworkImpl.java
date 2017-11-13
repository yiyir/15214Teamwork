package edu.cmu.cs.cs214.hw5.framework.core;

import com.sun.org.apache.regexp.internal.RE;
import edu.cmu.cs.cs214.hw5.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.plugin.DisplayPlugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * The 'Topfood' framework core implementation.
 */
public class TopfoodFrameworkImpl {
    private List<Restaurant> restaurants;
    private String city;
    private TopfoodChangeListener listener;
    private DataPlugin currentDataPlugin;
    private DisplayPlugin currentDisplayPlugin;

    public TopfoodFrameworkImpl() {
        restaurants = new ArrayList<>();
    }

    /**
     * Sets the framework's listener to be notified about changes made to the framework's state.
     */
    public void setTopfoodChangeListener(TopfoodChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Registers a new DataPlugin with the framework, read in the data and notifies the GUI about the change.
     */
    public void registerDataPlugin(DataPlugin plugin) {
        currentDataPlugin = plugin;
        readData(plugin);
        notifyDataPluginRegistered(plugin);
    }

    public void registerDisplayPlugin(DisplayPlugin plugin) {
        notifyDisplayPluginRegistered(plugin);
    }

    public List<Restaurant> getTopRestaurants(int index) {
        if (index < 0 || index >= restaurants.get(0).getKeys().size()) throw new IndexOutOfBoundsException();
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant res : restaurants) {
            result.add(res);
        }
        Comparator<Restaurant> comparator = new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                if(o1.getValues().get(index) > o2.getValues().get(index)) return -1;
                if(o1.getValues().get(index) < o2.getValues().get(index)) return 1;
                return 0;
            }
        };
        result.sort(comparator);
        return result;

    }

    public List<Restaurant> filterbyHours(int day, int time) {
        //assume the input day and time are both valid
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant res : restaurants) {
            if (res.isOpen(day, time)) result.add(res);
        }
        return result;
    }


    private void notifyDataPluginRegistered(DataPlugin plugin) {
        listener.onDataPluginRegistered(plugin);
    }

    private void notifyDisplayPluginRegistered(DisplayPlugin plugin) {
        listener.onDisplayPluginRegistered(plugin);
    }

    /**
     * Read in the data from the given DataPlugin.
     *
     * @param plugin the DataPlugin
     */
    private void readData(DataPlugin plugin) {

    }
}
