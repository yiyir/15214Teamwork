package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.List;

/**
 * DataPlugin interface: data plugins should extract a list of foods along with their respective food groups and
 * quantities (in grams). Data plugins have the option to take in a file path and/or a list of parameters from the user.
 */
public interface DataPlugin {

    /**
     * Retrieves data from the plugin--expects a list of foods that contains information about the food, including
     * our enum food group to make it easier to search for nutrient information and the amount of food to scale the
     * nutrition facts. Can also specify no food group within the enum. The default amount of food if the data plugin
     * does not explicitly provide it is 100g.
     *
     * @param filePath       The file path to extract data from; empty string if file path is not required.
     * @param userParameters The user parameters; the ith list in userParameters contains the user input
     *                       for the ith parameter in the list returned by getParameters().
     * @return A list of FoodInfo instances that each contains the food name, the food group, and the amount of food (in
     * grams) that the data plugin would like nutrition facts for. The default amount of food for each instance is 100g,
     * and the default food group is NO_FOOD_GROUP.
     */
    List<FoodInfo> getData(String filePath, List<List<String>> userParameters);

    /**
     * Lets framework know if the data plugin requires a file path.
     *
     * @return True if data plug in requires file path, false otherwise.
     */
    boolean requiresFilePath();

    /**
     * Returns a message about file path specifics to be displayed by the GUI,
     * e.g. how the file path should be formatted or where the file should be
     * located, if applicable.
     *
     * @return Message about the file path
     */
    String getFilePathMessage();

    /**
     * Returns the names of the required parameters for this data plugin. For example, a user input data plugin might
     * request the user to input a list of foods and their quantities; in this case, getParameters() would return
     * ["Food", "Amount"].
     *
     * @return A list as described above; empty if no parameters required.
     */
    List<String> getParameters();

    /**
     * Returns the name of the plugin.
     *
     * @return The plugin name
     */
    String getName();
}
