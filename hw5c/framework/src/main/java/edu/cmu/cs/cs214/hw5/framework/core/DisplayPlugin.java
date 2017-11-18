package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Display plugin interface, takes in a mapping of food name to a list of NutrientInfo objects, name of the display
 * plugin, and the minimum number of nutrients required in the data passed into it.
 */
public interface DisplayPlugin {

    /**
     * Given nutrient information (separated by nutrition category) for each food, returns the JPanel
     * displaying the information.
     *
     * @param nutrientData Processed information mapping name of food to list of nutrient information.
     * @return JPanel with the display of nutrient information.
     */
    JPanel getDisplay(Map<String, List<NutrientInfo>> nutrientData);

    /**
     * Returns the name of the display plugin (to be shown as an available option of display plugin).
     *
     * @return Plugin name
     */
    String getName();

    /**
     * Returns the minimum number of nutrients required for this display, e.g.
     * a bubble chart requires 3-dimensional data so it requires 3 nutrients.
     *
     * @return Minimum number of nutrients required
     */
    int minNutrientsRequired();
}
