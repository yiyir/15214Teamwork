package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The 'Topfood' framework core implementation.
 */
public class TopfoodFrameworkImpl {
    /**
     * the loaded data from the data plugin
     */
    private List<Restaurant> data;
    /**
     * the internal processed data
     */
    private List<Restaurant> processedData;
    /**
     * the framework change listener
     */
    private TopfoodChangeListener listener;
    /**
     * the current data plugin
     */
    private DataPlugin currentDataPlugin;
    /**
     * the current display plugin
     */
    private DisplayPlugin currentDisplayPlugin;

    /**
     * Sets the framework's listener to be notified about changes made to the framework's state.
     */
    public void setTopfoodChangeListener(TopfoodChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Registers a new DataPlugin with the framework.
     */
    public void registerDataPlugin(DataPlugin plugin) {
        notifyDataPluginRegistered(plugin);
    }

    /**
     * Registers a new DisplayPlugin with the framework.
     */
    public void registerDisplayPlugin(DisplayPlugin plugin) {
        notifyDisplayPluginRegistered(plugin);
    }

    /**
     * Load the data through the given plugin and city name.
     *
     * @param plugin the given plugin
     * @param city   the given city
     * @return whether there is data available for the given search
     */
    public boolean loadData(DataPlugin plugin, String city) {
        if (currentDataPlugin != plugin) {
            currentDataPlugin = plugin;
        }
        data = null;
        processedData = new ArrayList<>();
        // If no data is found...
        if (currentDataPlugin.getRestaurants(city).size() == 0) {
            currentDataPlugin = null;
            notifyDataPluginChanged(null, null);
            return false;
        }
        // Update the internal stored data of the framework.
        data = currentDataPlugin.getRestaurants(city);
        for (Restaurant res : data) {
            processedData.add(res);
        }
        notifyDataPluginChanged(plugin, city);
        return true;
    }

    /**
     * Sort the data by the values of a given key.
     *
     * @param index the index of the given key
     */
    public void sortByKey(int index) {
        if (index < 0 || index >= data.get(0).getKeys().size()) throw new IndexOutOfBoundsException();
        Comparator<Restaurant> comparator = (o1, o2) -> {
            if (o1.getValues().get(index) > o2.getValues().get(index)) return -1;
            if (o1.getValues().get(index) < o2.getValues().get(index)) return 1;
            return 0;
        };
        processedData.sort(comparator);

    }

    /**
     * Filter the data by opening hours.
     *
     * @param day  the given day of search
     * @param time the given time of the day
     */
    public void filterByHours(int day, int time) {
        //assume the input day and time are both valid
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant res : processedData) {
            if (res.isOpen(day, time)) result.add(res);
        }
        processedData = result;
    }

    /**
     * Resets the internal stored processed data for a new analysis on the same data source.
     */
    public void resetData() {
        processedData = new ArrayList<>();
        for (Restaurant res : data) {
            processedData.add(res);
        }
    }

    /**
     * Gets the keys(column names) of the data.
     *
     * @return the keys
     */
    public List<String> getKeys() {
        if (data != null) return data.get(0).getKeys();
        return null;
    }

    public DataPlugin getCurrentDataPlugin() {
        return currentDataPlugin;
    }

    public List<Restaurant> getProcessedData() {
        return processedData;
    }

    /* Notify TopfoodChangeListener methods. */
    private void notifyDataPluginRegistered(DataPlugin plugin) {
        listener.onDataPluginRegistered(plugin);
    }

    private void notifyDisplayPluginRegistered(DisplayPlugin plugin) {
        listener.onDisplayPluginRegistered(plugin);
    }

    private void notifyDataPluginChanged(DataPlugin plugin, String city) {
        listener.onDataPluginChanged(plugin, city);
    }

}
