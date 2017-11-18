package edu.cmu.cs.cs214.hw5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.FrameworkImpl;
import edu.cmu.cs.cs214.hw5.framework.gui.FrameworkGui;

/**
 * Main class.
 */
public class Main {
    /**
     * Create and display framework gui.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndStartFramework);
    }

    private static void createAndStartFramework() {
        FrameworkImpl core = new FrameworkImpl();
        FrameworkGui gui = new FrameworkGui(core);
        core.addFrameworkListener(gui);

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
        List<DataPlugin> result = new ArrayList<>();
        Iterator<DataPlugin> plugins = ServiceLoader.load(DataPlugin.class).iterator();
        while (plugins.hasNext()) {
            DataPlugin plugin = plugins.next();
            result.add(plugin);
            System.out.println("Loaded plugin " + plugin.getName());
        }
        return result;
    }

    private static List<DisplayPlugin> loadDisplayPlugins() {
        List<DisplayPlugin> result = new ArrayList<>();
        Iterator<DisplayPlugin> plugins = ServiceLoader.load(DisplayPlugin.class).iterator();
        while (plugins.hasNext()) {
            DisplayPlugin plugin = plugins.next();
            result.add(plugin);
            System.out.println("Loaded plugin " + plugin.getName());
        }
        return result;
    }
}
