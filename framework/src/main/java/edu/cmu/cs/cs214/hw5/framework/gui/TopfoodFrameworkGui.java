package edu.cmu.cs.cs214.hw5.framework.gui;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.TopfoodChangeListener;
import edu.cmu.cs.cs214.hw5.framework.core.TopfoodFrameworkImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

public class TopfoodFrameworkGui implements TopfoodChangeListener {
    private static final String DEFAULT_TITLE = "Topfood Framework";

    // Menu-related titles.
    private static final String MENU_TITLE = "Data";
    private static final String MENU_LOAD_DATA = "Load Data";
    private static final String MENU_FUNCTIONS = "Functions";
    private static final String MENU_DISPLAY_DATA = "Display Data";
    private static final String MENU_EXIT = "Exit";
    private static final String FUNCTION_FILTER = "Filter Data...";
    private static final String FUNCTION_SORT = "Sort Data";

    // Dialog titles and messages.
    private static final String SET_CITY_TITLE = "Set city of interest";
    private static final String SET_CITY_MSG = "Enter city name:";
    private static final String ERROR_LOAD_DATA_TITLE = "Error!";
    private static final String ERROR_LOAD_DATA_MSG = "No data is available for the given search, please re-enter the city name.";
    private static final String SET_TIME_TITLE = "Filter by opening hours";
    private static final String SET_TIME_MSG = "Select time:";

    // Menu-related stuff.
    private final JMenu loadDataMenu;
    private final JMenu displayDataMenu;
    private final ButtonGroup dataPluginGroup = new ButtonGroup();
    private final ButtonGroup displayPluginGroup = new ButtonGroup();


    // The parent JFrame window.
    private final JFrame frame;
    private TopfoodFrameworkImpl core;
    private DataPlugin currentDataPlugin;
    private DisplayPlugin currentDisplayPlugin;

    public TopfoodFrameworkGui(TopfoodFrameworkImpl core) {
        this.core = core;
        frame = new JFrame(DEFAULT_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));

        // Set-up the menu bar.
        JMenuBar menuBar = new JMenuBar();
        JMenu dataMenu = new JMenu(MENU_TITLE);
        dataMenu.setMnemonic(KeyEvent.VK_F);

        // Add an 'Load Data' menu item.
        loadDataMenu = new JMenu(MENU_LOAD_DATA);
        loadDataMenu.setMnemonic(KeyEvent.VK_N);
        dataMenu.add(loadDataMenu);

        // Add a 'Functions' menu item.
        JMenu functionsMenu = new JMenu(MENU_FUNCTIONS);
        functionsMenu.setMnemonic(KeyEvent.VK_N);
        dataMenu.add(functionsMenu);

        // Add a 'Filter Data' menu item to the 'Functions' menu.
        JCheckBoxMenuItem filterFunction = new JCheckBoxMenuItem(FUNCTION_FILTER);
        filterFunction.setSelected(false);
        filterFunction.addActionListener(e -> {
            if (filterFunction.isSelected()) {
                String[] hours = new String[48];
                for (int i = 0; i < 48; i+=2) {
                    hours[i] =i/2+":"+"00";
                    hours[i+1]=i/2+":"+"30";
                }
                JOptionPane.showInputDialog(frame,
                        SET_TIME_MSG, SET_TIME_TITLE,
                        JOptionPane.PLAIN_MESSAGE, null,
                        hours, hours[0]);
            } else {
                filterFunction.setSelected(false);
            }
        });
        functionsMenu.add(filterFunction);

        // Add a 'Sort Data' menu item to the 'Functions' menu.
        JMenu sortMenu = new JMenu(FUNCTION_SORT);
        sortMenu.setMnemonic(KeyEvent.VK_N);
        functionsMenu.add(sortMenu);
        // add buttons here
        if (core.getKeys() != null) {
            for (String key : core.getKeys()) {
                JCheckBoxMenuItem sortMenuItem = new JCheckBoxMenuItem(key);
                sortMenuItem.setSelected(false);
                dataPluginGroup.add(sortMenuItem);
                sortMenu.add(sortMenuItem);
            }
        }

        // Add a 'Display Data' menu item.
        displayDataMenu = new JMenu(MENU_DISPLAY_DATA);
        displayDataMenu.setMnemonic(KeyEvent.VK_R);
        dataMenu.add(displayDataMenu);


        // Add a separator between 'Display Data' and 'Exit' menu items.
        dataMenu.addSeparator();

        // Add an 'Exit' menu item.
        JMenuItem exitMenuItem = new JMenuItem(MENU_EXIT);
        exitMenuItem.setMnemonic(KeyEvent.VK_X);
        exitMenuItem.addActionListener(event -> System.exit(0));
        dataMenu.add(exitMenuItem);

        menuBar.add(dataMenu);
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onDataPluginRegistered(final DataPlugin plugin) {
        JRadioButtonMenuItem dataMenuItem = new JRadioButtonMenuItem(plugin.toString());
        dataMenuItem.setSelected(false);
        dataMenuItem.addActionListener(event -> {
            String city = (String) JOptionPane.showInputDialog(frame, SET_CITY_MSG,
                    SET_CITY_TITLE, JOptionPane.PLAIN_MESSAGE, null, null, "");
            if (city != null) {
                boolean success = core.loadData(plugin, city);
                if (!success) {
                    showErrorDialog(frame, ERROR_LOAD_DATA_TITLE, ERROR_LOAD_DATA_MSG);
                    dataPluginGroup.clearSelection();
                }
            }

        });
        dataPluginGroup.add(dataMenuItem);
        loadDataMenu.add(dataMenuItem);
    }

    @Override
    public void onDisplayPluginRegistered(DisplayPlugin plugin) {
        JRadioButtonMenuItem displayMenuItem = new JRadioButtonMenuItem(plugin.toString());
        displayMenuItem.setSelected(false);
        displayMenuItem.addActionListener(event -> {
            // cannot display when function is not selected or data is not loaded
//            if (core.getPlayers().isEmpty()) {
//                // Can't start a game with no players.
//                showErrorDialog(frame, ERROR_NO_PLAYERS_TITLE, ERROR_NO_PLAYERS_MSG);
//                gameGroup.clearSelection();
//            } else {
//                core.startNewGame(plugin);
//            }
        });
        displayPluginGroup.add(displayMenuItem);
        displayDataMenu.add(displayMenuItem);
    }

    private static void showInfoDialog(Component c, String title, String msg) {
        JOptionPane.showMessageDialog(c, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showErrorDialog(Component c, String title, String msg) {
        JOptionPane.showMessageDialog(c, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}
