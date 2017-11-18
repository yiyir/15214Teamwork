package edu.cmu.cs.cs214.hw5.framework.core;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5.framework.gui.FrameworkGui;

/**
 * Observer interface that listens for changes in the framework. The
 * {@link FrameworkImpl} calls these methods to notify the {@link FrameworkGui}
 * when it should update its display.
 */
public interface FrameworkListener {
  
  /**
   * Called when a display is added.
   * 
   * @param panel the panel to add
   */
  void onDisplayAdded(JPanel panel);
  
  /**
   * Called when framework needs to request food groups for the specified foods.
   * 
   * @param foods the list of foods
   */
  void onRequestFoodGroup(List<String> foods);
  
  /**
   * Called when the framework needs to request a file path for a data source.
   * @param filePathMessage message for the user about file path specifics, e.g.
   * how it should be formatted
   */
  void onRequestFilePath(String filePathMessage);

  /**
   * Called when the framework needs to get user input for a data plugin.
   * @param parameters List of parameters data plugin needs
   */
  void onRequestParameters(List<String> parameters);
  
  /**
   * Called when a display plugin is registered.
   * 
   * @param plugin the registered plugin
   */
  void onDisplayPluginRegistered(DisplayPlugin plugin);
  
  /**
   * Called when a data plugin is registered.
   * 
   * @param plugin the registered plugin
   */
  void onDataPluginRegistered(DataPlugin plugin);

  /**
   * Called when a user inputs a foodGroup for a food, and there are no results.
   *
   * @param invalidFoods List of FoodInfo instances for which the above happens
   */
  void notifyInvalidFoodGroups(List<FoodInfo> invalidFoods);
  
  /**
   * Called when a user choose too few nutrients for a display plugin.
   * @param numInput the number of nutrients chosen by the user
   * @param numRequired the minimum number of nutrients required by the plugin
   */
  void notifyInvalidNutrients(int numInput, int numRequired);

  /**
   * Called when we receive no data from the data plugin; means something went wrong with the query.
   *
   * @param dataPluginName Name of plugin that didn't give back data.
   */
  void notifyPluginFailure(String dataPluginName);
}
