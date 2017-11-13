package edu.cmu.cs.cs214.hw5.framework.core;

import edu.cmu.cs.cs214.hw5.plugin.DataPlugin;
import edu.cmu.cs.cs214.hw5.plugin.DisplayPlugin;

public interface TopfoodChangeListener {
    void onDataPluginRegistered(DataPlugin plugin);
    void onDisplayPluginRegistered(DisplayPlugin plugin);
}
