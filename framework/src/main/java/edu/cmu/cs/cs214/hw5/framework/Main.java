package edu.cmu.cs.cs214.hw5.framework;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.TopfoodFrameworkImpl;
import edu.cmu.cs.cs214.hw5.framework.gui.TopfoodFrameworkGui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndStartFramework);
    }

    private static void createAndStartFramework() {
       TopfoodFrameworkImpl core = new TopfoodFrameworkImpl();
        TopfoodFrameworkGui gui = new TopfoodFrameworkGui(core);
        core.setTopfoodChangeListener(gui);
        List<DataPlugin> dataPlugins = loadDataPlugins();
        dataPlugins.forEach(core::registerDataPlugin);
        List<DisplayPlugin> displayPlugins = loadDisplayPlugins();
        displayPlugins.forEach(core::registerDisplayPlugin);
    }

    /**
     * Load plugins listed in META-INF/services/...
     *
     * @return List of instantiated plugins
     */
    private static List<DataPlugin> loadDataPlugins() {
        List<DataPlugin> result = new ArrayList<DataPlugin>();
        Iterator<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class).iterator();
        while (plugins.hasNext()) {
            DataPlugin plugin = plugins.next();
            result.add(plugin);
        }
        return result;
    }
    private static List<DisplayPlugin> loadDisplayPlugins() {
        List<DisplayPlugin> result = new ArrayList<DisplayPlugin>();
        Iterator<DisplayPlugin> plugins = ServiceLoader.load(DisplayPlugin.class).iterator();
        while (plugins.hasNext()) {
            DisplayPlugin plugin = plugins.next();
            result.add(plugin);
        }
        return result;
    }
}