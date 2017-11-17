package edu.cmu.cs.cs214.hw5.framework.gui;

import edu.cmu.cs.cs214.hw5.framework.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * The TopfoodFramework GUI implementation.
 */
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
    private static final String ERROR_LOAD_DATA_MSG = "No data is available for the given search, please re-select.";
    private static final String SET_HOUR_TITLE = "Filter by opening hours";
    private static final String SET_HOUR_MSG = "Select time:";
    private static final String SET_DAY_TITLE = "Filter by opening hours";
    private static final String SET_DAY_MSG = "Select day:";
    private static final Object[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private static final Object[] HOURS = new Object[48];
    private static final String ERROR_NO_DATA_MSG = "No data is loaded to be displayed.";
    private static final String ERROR_SORT_NOT_SELECTED_MSG = "Sorting function is not selected.";
    private static final String ERROR_NO_RESULTS_MSG = "No results are found given the filtering conditions.";

    // Menu-related stuff.
    private final JMenu loadDataMenu;
    private final JMenu displayDataMenu;
    private final JMenu sortMenu;
    private final ButtonGroup dataPluginGroup = new ButtonGroup();
    private final ButtonGroup displayPluginGroup = new ButtonGroup();
    private final ButtonGroup sortItemGroup = new ButtonGroup();
    private final JCheckBoxMenuItem filterFunction;

    // Labels to show a summary of current status of the framework.
    private final JLabel currentDataLabel;
    private final JLabel currentSortLabel;
    private final JLabel currentFilterLabel;
    private final JLabel currentDisplayLabel;

    // Input-dialogue-related stuff.
    private Object selectedDay;
    private Object selectedHour;

    // The parent JFrame window.
    private final JFrame frame;
    private TopfoodFrameworkImpl core;

    /**
     * Constructor method.
     *
     * @param core the core of framework
     */
    public TopfoodFrameworkGui(TopfoodFrameworkImpl core) {
        this.core = core;
        frame = new JFrame(DEFAULT_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setLocation(350, 350);

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
        filterFunction = new JCheckBoxMenuItem(FUNCTION_FILTER);
        filterFunction.setSelected(false);
        filterFunction.addActionListener(e -> {
            if (filterFunction.isSelected()) {
                // Pop up input dialogue boxes to ask user for day and hour.
                selectedDay = JOptionPane.showInputDialog(frame,
                        SET_DAY_MSG, SET_DAY_TITLE,
                        JOptionPane.PLAIN_MESSAGE, null,
                        DAYS, DAYS[0]);
                if (selectedDay == null) {
                    filterFunction.setSelected(false);
                    onFilterChanged(null, null);
                } else {
                    for (int i = 0; i < 48; i += 2) {
                        HOURS[i] = i / 2 + ":" + "00";
                        HOURS[i + 1] = i / 2 + ":" + "30";
                    }
                    selectedHour = JOptionPane.showInputDialog(frame,
                            SET_HOUR_MSG, SET_HOUR_TITLE,
                            JOptionPane.PLAIN_MESSAGE, null,
                            HOURS, HOURS[0]);
                    if (selectedHour == null) {
                        filterFunction.setSelected(false);
                        onFilterChanged(null, null);
                    } else {
                        String day = new String();
                        String hour = new String();
                        for (int counter = 0, maxCounter = DAYS.length;
                             counter < maxCounter; counter++) {
                            if (DAYS[counter].equals(selectedDay))
                                day = DAYS[counter].toString();
                        }
                        for (int counter = 0, maxCounter = HOURS.length;
                             counter < maxCounter; counter++) {
                            if (HOURS[counter].equals(selectedHour))
                                hour = HOURS[counter].toString();
                        }
                        onFilterChanged(day, hour);

                    }
                }
            } else {
                filterFunction.setSelected(false);
                onFilterChanged(null, null);
            }
        });
        functionsMenu.add(filterFunction);

        // Add a 'Sort Data' menu item to the 'Functions' menu.
        sortMenu = new JMenu(FUNCTION_SORT);
        sortMenu.setMnemonic(KeyEvent.VK_N);
        functionsMenu.add(sortMenu);


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

        // Add an information panel to the frame.
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        currentDataLabel = new JLabel("Current data plugin: null   City name: null");
        currentSortLabel = new JLabel("Sort by: null");
        currentFilterLabel = new JLabel("Filter by: null");
        currentDisplayLabel = new JLabel("Current display plugin: null");
        infoPanel.add(currentDataLabel);
        infoPanel.add(currentSortLabel);
        infoPanel.add(currentFilterLabel);
        infoPanel.add(currentDisplayLabel);


        menuBar.add(dataMenu);
        frame.setJMenuBar(menuBar);
        frame.add(infoPanel);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onDataPluginRegistered(final DataPlugin plugin) {
        JRadioButtonMenuItem dataMenuItem = new JRadioButtonMenuItem(plugin.toString());
        dataMenuItem.setSelected(false);
        dataMenuItem.addActionListener(event -> {
            // Pop up an input dialogue box to ask user for city name.
            String city = (String) JOptionPane.showInputDialog(frame, SET_CITY_MSG,
                    SET_CITY_TITLE, JOptionPane.PLAIN_MESSAGE, null, null, "");
            // Try to load data if the input dialogue box is not cancelled.
            if (city != null) {
                city = city.trim().toLowerCase();
                boolean success = core.loadData(plugin, city);
                // An error message is popped up if data is not available for the given search.
                // And the user needs to re-select the data plugin.
                if (!success) {
                    showErrorDialog(frame, ERROR_LOAD_DATA_MSG);
                    dataPluginGroup.clearSelection();
                } else {
                    // Add buttons to the 'Sort Data' menu.
                    List<String> keys = core.getKeys();
                    if (keys != null) {
                        for (int i = 0; i < keys.size(); i++) {
                            final int index = i;
                            JRadioButtonMenuItem sortMenuItem = new JRadioButtonMenuItem(keys.get(i));
                            sortMenuItem.setSelected(false);
                            sortMenuItem.setActionCommand(String.valueOf(index));
                            sortMenuItem.addActionListener(e -> {
                                onSortChanged(index);
                            });
                            sortItemGroup.add(sortMenuItem);
                            sortMenu.add(sortMenuItem);
                        }
                    }
                }
            }
        });
        dataPluginGroup.add(dataMenuItem);
        loadDataMenu.add(dataMenuItem);
    }

    @Override
    public void onDisplayPluginRegistered(final DisplayPlugin plugin) {
        JRadioButtonMenuItem displayMenuItem = new JRadioButtonMenuItem(plugin.toString());
        displayMenuItem.setSelected(false);
        displayMenuItem.addActionListener(event -> {
            onDisplayPluginChanged(null);
            if (core.getCurrentDataPlugin() == null) { // Cannot display if no data is loaded.
                showErrorDialog(frame, ERROR_NO_DATA_MSG);
                displayPluginGroup.clearSelection();
            } else if (sortItemGroup.getSelection() == null) { // Cannot display when sorting function is not selected.
                showErrorDialog(frame, ERROR_SORT_NOT_SELECTED_MSG);
                displayPluginGroup.clearSelection();
            } else { // Finally we can display the data now.
                core.resetData();
                if (filterFunction.isSelected()) {
                    int dayIndex = 0;
                    int timeIndex = 0;
                    for (int counter = 0, maxCounter = DAYS.length;
                         counter < maxCounter; counter++) {
                        if (DAYS[counter].equals(selectedDay))
                            dayIndex = counter;
                    }
                    for (int counter = 0, maxCounter = HOURS.length;
                         counter < maxCounter; counter++) {
                        if (HOURS[counter].equals(selectedHour))
                            timeIndex = counter;
                    }
                    int time = 50 * timeIndex;
                    core.filterByHours(dayIndex, time);
                }
                String indexOfKey = sortItemGroup.getSelection().getActionCommand();
                int index = Integer.valueOf(indexOfKey);
                core.sortByKey(index);
                if (!core.display(plugin, index)) {
                    showErrorDialog(frame, ERROR_NO_RESULTS_MSG);
                    filterFunction.setSelected(false);
                    onFilterChanged(null, null);
                } else {
                    onDisplayPluginChanged(plugin);
                }
            }
        });
        displayPluginGroup.add(displayMenuItem);
        displayDataMenu.add(displayMenuItem);
    }

    @Override
    public void onDataPluginChanged(DataPlugin plugin, String city) {
        StringBuilder sb = new StringBuilder();
        sb.append("Current data plugin: ");
        if (plugin != null) {
            sb.append(plugin.toString());
        } else {
            sb.append("null");
        }
        sb.append("   City name: ");
        sb.append(city);
        currentDataLabel.setText(sb.toString());
    }

    @Override
    public void onSortChanged(int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("Sort by: ");
        sb.append(core.getKeys().get(index));
        currentSortLabel.setText(sb.toString());
    }

    @Override
    public void onFilterChanged(String day, String time) {
        StringBuilder sb = new StringBuilder();
        sb.append("Filter by: ");
        if (day == null && time == null) {
            sb.append("null");
        } else {
            sb.append(day + " " + time);
        }
        currentFilterLabel.setText(sb.toString());
    }

    @Override
    public void onDisplayPluginChanged(DisplayPlugin plugin) {
        StringBuilder sb = new StringBuilder();
        sb.append("Current display plugin: ");
        if (plugin != null) {
            sb.append(plugin.toString());
        } else {
            sb.append("null");
        }
        currentDisplayLabel.setText(sb.toString());
    }

    /**
     * Shows an error message dialogue box.
     *
     * @param c   the parent component of the message box
     * @param msg the error message to be shown
     */
    private static void showErrorDialog(Component c, String msg) {
        JOptionPane.showMessageDialog(c, msg, "Error!", JOptionPane.ERROR_MESSAGE);
    }
}
