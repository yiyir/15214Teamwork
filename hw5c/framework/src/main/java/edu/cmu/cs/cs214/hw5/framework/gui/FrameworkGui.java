package edu.cmu.cs.cs214.hw5.framework.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.FoodGroup;
import edu.cmu.cs.cs214.hw5.framework.core.FoodInfo;
import edu.cmu.cs.cs214.hw5.framework.core.FrameworkImpl;
import edu.cmu.cs.cs214.hw5.framework.core.FrameworkListener;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientCategory;

/**
 * The framework GUI implementation. This class is responsible for displaying
 * the framework GUI to the screen, and for forwarding events to
 * {@link FrameworkImpl} when GUI-related events are detected.
 */
public class FrameworkGui implements FrameworkListener {

  private static final String DEFAULT_TITLE = "Nutrient Framework";
  private static final String CHOOSE_PLUGINS = "Choose Plugins";
  private static final String FOOTER_TEXT =
      "Select a data and display plugin to begin.";
  private static final String INSTRUCTIONS = 
      "Select a data plugin, display plugin, and the nutrients you want the display to compare.";
  private static final String INVALID_GROUP =
      "'%s' in the selected food group '%s' does not exist.";
  private static final String NUTRITION_ERROR =
      "Nutrition information was not retrieved for the following foods:";
  private static final String INVALID_NUTRIENTS =
      "You chose %d nutrient(s) to compare, but the display plugin "
      + "requires a minimum of %d.";
  private static final String PLUGIN_ERROR = 
      "The data plugin '%s' didn't return anything. Try another plugin!";
  private static final String REQUEST_PARAMETERS = 
      "For each text field, separate input by commas,\n"
        + "e.g. for the parameter 'Foods' input 'banana,apple'";
  
  private static final NutrientCategory[] NUTRIENTS = NutrientCategory.values();
  private static final FoodGroup[] FOOD_GROUPS = FoodGroup.values();
  
  private static final int FRAME_SIZE = 800;
  private static final int SCROLL_SIZE = 200;
  private static final int SCROLL_SPEED = 25;
  private static final int PAD = 10;
  private static final int BORDER = 10;
  
  private final JFrame frame;
  private final JPanel outerPanel;
  private final JPanel innerPanel;
  private final JScrollPane innerScroll;
  
  private final JLabel footerLabel;
  private final JLabel instructionLabel;
  private final JLabel nutritionErrorLabel;
  private final JLabel requestParametersLabel;

  private final NutrientCheckBox[] nutrientBoxes;
  private final JScrollPane nutrientPane;
  private final JButton selectAll;
  private boolean allSelected;

  private final List<DataPlugin> dataPlugins;
  private final List<DisplayPlugin> displayPlugins;
  private final JPanel choosePluginsPanel;
  private final JButton choosePluginsButton;

  private FrameworkImpl core;

  /**
   * Construct a gui.
   * @param f the framework implementation instance
   */
  public FrameworkGui(FrameworkImpl f) {
    core = f;
    frame = new JFrame(DEFAULT_TITLE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));

    outerPanel = new JPanel(new BorderLayout());

    // Scroll pane of check boxes for selecting nutrients
    JPanel nutrientPanel = new JPanel();
    nutrientPanel.setLayout(new BoxLayout(nutrientPanel, BoxLayout.Y_AXIS));
    nutrientPanel.add(new JLabel("Select Nutrients"));
    nutrientBoxes = new NutrientCheckBox[NUTRIENTS.length];
    allSelected = false;
    selectAll = new JButton("Select All");
    selectAll.addActionListener(e -> {
      allSelected = !allSelected;
      for (NutrientCheckBox box : nutrientBoxes) {
        box.setSelected(allSelected);
      }
    });
    nutrientPanel.add(selectAll);
    for (int i = 0; i < NUTRIENTS.length; i++) {
      nutrientBoxes[i] = new NutrientCheckBox(NUTRIENTS[i]);
      nutrientPanel.add(nutrientBoxes[i]);
    }
    nutrientPane = new JScrollPane(nutrientPanel);
    nutrientPane.setPreferredSize(new Dimension(SCROLL_SIZE, SCROLL_SIZE));
    nutrientPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
    
    // Labels for GUI notifications
    instructionLabel = makeLabel(INSTRUCTIONS);
    nutritionErrorLabel = makeLabel(NUTRITION_ERROR);
    requestParametersLabel = makeLabel(REQUEST_PARAMETERS);
    
    dataPlugins = new ArrayList<>();
    displayPlugins = new ArrayList<>();

    // Set up "Choose Plugins" button
    choosePluginsPanel = new JPanel();
    choosePluginsButton = createPluginsButton();
    choosePluginsPanel.add(choosePluginsButton);
    outerPanel.add(choosePluginsPanel, BorderLayout.NORTH);

    footerLabel = makeLabel(FOOTER_TEXT);
    outerPanel.add(footerLabel, BorderLayout.SOUTH);
    
    // Inner panel for displays
    innerPanel = new JPanel();
    innerPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER, 0, BORDER));
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
    innerScroll = new JScrollPane(innerPanel);
    innerScroll.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
    innerScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
    innerScroll.getHorizontalScrollBar().setUnitIncrement(SCROLL_SPEED);
    outerPanel.add(innerScroll, BorderLayout.CENTER);

    frame.add(outerPanel);

    frame.pack();
    frame.setVisible(true);
  }

  @Override
  public void onDisplayAdded(JPanel displayPanel) {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    JButton close = new JButton("Close");
    close.setBorder(null);
    close.addActionListener(e -> {
      innerPanel.remove(panel);
      innerPanel.revalidate();
      innerPanel.repaint();
    });
    panel.add(close);
    panel.add(displayPanel);
    innerPanel.add(panel);
    innerPanel.revalidate();
    innerPanel.repaint();
  }

  @Override
  public void onRequestFoodGroup(List<String> foods) {
    Map<String, FoodGroup> userFoodGroups = new HashMap<>();
    if (!foods.isEmpty()) {
      List<JComboBox<FoodGroup>> foodGroupBoxes = new ArrayList<>();
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.add(new JLabel("For each food, select the appropriate food group."));
      for (String f : foods) {
        panel.add(new JLabel(f));
        JComboBox<FoodGroup> box = new JComboBox<FoodGroup>(FOOD_GROUPS);
        foodGroupBoxes.add(box);
        panel.add(box);
      }
      JOptionPane.showConfirmDialog(frame, panel,
          "Choose Food Groups", JOptionPane.OK_CANCEL_OPTION);
      for (int i = 0; i < foods.size(); i++) {
        userFoodGroups.put(foods.get(i),
            (FoodGroup)(foodGroupBoxes.get(i).getSelectedItem()));
      }
    }
    core.setDisplay(userFoodGroups);
  }

  @Override
  public void onRequestFilePath(String filePathMessage) {
    String path = JOptionPane.showInputDialog(frame, filePathMessage,
        "Input File Path", JOptionPane.PLAIN_MESSAGE, null, null, null).toString();
    core.getFoods(path, new ArrayList<List<String>>());
  }

  @Override
  public void onRequestParameters(List<String> parameters) {
    List<List<String>> userParameters = new ArrayList<>();
    List<JTextField> input = new ArrayList<>();
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(requestParametersLabel);
    for (String p : parameters) {
      JTextField field = new JTextField(PAD);
      input.add(field);
      panel.add(new JLabel(p));
      panel.add(field);
    }
    JOptionPane.showConfirmDialog(frame, panel,
        "Enter Data Plugin Parameters", JOptionPane.OK_CANCEL_OPTION);
    for (JTextField i : input) {
      if (i.getText().isEmpty()) {
        userParameters.add(new ArrayList<String>());
      }
      else {
        userParameters.add(Arrays.asList(i.getText().split(",")));
      }
    }
    core.getFoods("", userParameters);
  }

  @Override
  public void onDisplayPluginRegistered(DisplayPlugin plugin) {
    displayPlugins.add(plugin);
  }

  @Override
  public void onDataPluginRegistered(DataPlugin plugin) {
      dataPlugins.add(plugin);
  }

  @Override
  public void notifyInvalidFoodGroups(List<FoodInfo> invalidFoods) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.add(nutritionErrorLabel);

    StringBuilder sb = new StringBuilder();
    for (FoodInfo food : invalidFoods) {
      sb.append(String.format(INVALID_GROUP, food.getFoodName(), food.getFoodGroup()));
      sb.append("\n");
    }
    JTextArea msg = new JTextArea(sb.toString());
    JScrollPane message = new JScrollPane(msg);
    panel.add(message);
    JOptionPane.showMessageDialog(frame, panel, "Error Retrieving Nutrition Info",
        JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void notifyInvalidNutrients(int numInput, int numRequired) {
    JOptionPane.showMessageDialog(frame, String.format(INVALID_NUTRIENTS, numInput,
        numRequired), "Invalid Number of Nutrients", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void notifyPluginFailure(String dataPluginName) {
    JOptionPane.showMessageDialog(frame, String.format(PLUGIN_ERROR, dataPluginName), "Plugin Error",
            JOptionPane.INFORMATION_MESSAGE);
  }
  
  private JLabel makeLabel(String text) {
    JLabel label = new JLabel(text);
    label.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER, BORDER));
    return label;
  }

  private JButton createPluginsButton() {
    JButton button = new JButton(CHOOSE_PLUGINS);
    button.addActionListener(e -> {
      JPanel outer = new JPanel(new BorderLayout());
      outer.add(instructionLabel, BorderLayout.NORTH);
      JPanel inner = new JPanel();
      
      JComboBox<DataPlugin> dataBox = new JComboBox(dataPlugins.toArray());
      dataBox.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
          if (value == null) {
            setText("Data");
          }
          else {
            DataPlugin plugin = (DataPlugin)value;
            setText(plugin.getName());
          }
          return this;
        }
      });
      dataBox.setSelectedIndex(-1);
      dataBox.setAlignmentY(Component.TOP_ALIGNMENT);
      inner.add(dataBox);
      inner.add(Box.createHorizontalStrut(PAD));

      JComboBox<DisplayPlugin> displayBox = new JComboBox(displayPlugins.toArray());
      displayBox.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
          if (value == null) {
            setText("Display");
          }
          else {
            DisplayPlugin plugin = (DisplayPlugin)value;
            setText(plugin.getName());
          }
          return this;
        }
      });
      displayBox.setSelectedIndex(-1);
      displayBox.setAlignmentY(Component.TOP_ALIGNMENT);
      inner.add(displayBox);
      inner.add(Box.createHorizontalStrut(PAD));

      for (NutrientCheckBox box : nutrientBoxes) {
        box.setSelected(false);
      }
      nutrientPane.setAlignmentY(Component.TOP_ALIGNMENT);
      inner.add(nutrientPane);
      inner.add(Box.createHorizontalStrut(PAD));
      outer.add(inner, BorderLayout.CENTER);

      int result = JOptionPane.showConfirmDialog(frame, outer,
          "Choose Plugins", JOptionPane.OK_CANCEL_OPTION);
      if (result == JOptionPane.OK_OPTION) {
        DataPlugin dataPlugin = (DataPlugin)dataBox.getSelectedItem();
        DisplayPlugin displayPlugin = (DisplayPlugin)displayBox.getSelectedItem();
        List<NutrientCategory> nutrients = new ArrayList<>();
        for (NutrientCheckBox box : nutrientBoxes) {
          if (box.isSelected()) {
            nutrients.add(box.getNutrient());
          }
        }
        core.startNewDisplay(dataPlugin, displayPlugin, nutrients);
      }
    });

    return button;
  }

  /**
   * Check box associated with a NutrientCategory.
   */
  private class NutrientCheckBox extends JCheckBox {
    private final NutrientCategory nutrient;

    NutrientCheckBox(NutrientCategory nutrient) {
      this.nutrient = nutrient;
      setText(nutrient.toString());
    }

    NutrientCategory getNutrient() {
      return nutrient;
    }
  }

}
