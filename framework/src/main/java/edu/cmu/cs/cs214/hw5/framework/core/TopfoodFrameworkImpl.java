package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The 'Topfood' framework core implementation.
 */
public class TopfoodFrameworkImpl {
    private List<Restaurant> data;
    private List<Restaurant> processedData;
    private TopfoodChangeListener listener;
    private DataPlugin currentDataPlugin;
    private DisplayPlugin currentDisplayPlugin;

    public TopfoodFrameworkImpl() {

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
        notifyDataPluginRegistered(plugin);
    }

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
        if (currentDataPlugin != plugin) currentDataPlugin = plugin;
        // change the internal stored data of the framework
        if (currentDataPlugin.getRestaurants(city).size() == 0) return false;
        data = currentDataPlugin.getRestaurants(city);
        for (Restaurant res : data) {
            processedData.add(res);
        }
        return true;
    }

    public void sortByKey(int index) {
        if (index < 0 || index >= data.get(0).getKeys().size()) throw new IndexOutOfBoundsException();
        Comparator<Restaurant> comparator = (o1, o2) -> {
            if (o1.getValues().get(index) > o2.getValues().get(index)) return -1;
            if (o1.getValues().get(index) < o2.getValues().get(index)) return 1;
            return 0;
        };
        processedData.sort(comparator);
    }

    public void filterByHours(int day, int time, List<Restaurant> restaurants) {
        //assume the input day and time are both valid
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant res : processedData) {
            if (res.isOpen(day, time)) result.add(res);
        }
        processedData = result;
    }

    public List<String> getKeys() {
        if (data != null) return data.get(0).getKeys();
        return null;
    }

    public List<Restaurant> getProcessedData() {
        return processedData;
    }

    private void notifyDataPluginRegistered(DataPlugin plugin) {
        listener.onDataPluginRegistered(plugin);
    }

    private void notifyDisplayPluginRegistered(DisplayPlugin plugin) {
        listener.onDisplayPluginRegistered(plugin);
    }

}
