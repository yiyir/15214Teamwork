package edu.cmu.cs.cs214.hw5.framework.core;

public interface TopfoodChangeListener {
    void onDataPluginRegistered(DataPlugin plugin);
    void onDisplayPluginRegistered(DisplayPlugin plugin);
}
