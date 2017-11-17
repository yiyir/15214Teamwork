package edu.cmu.cs.cs214.hw5.framework.core;

public interface TopfoodChangeListener {
    void onDataPluginRegistered(DataPlugin plugin);

    void onDisplayPluginRegistered(DisplayPlugin plugin);

    void onDataPluginChanged(DataPlugin plugin, String city);

    void onDisplayPluginChanged(DisplayPlugin plugin);

    void onSortChanged(int index);

    void onFilterChanged(String day, String time);
}
