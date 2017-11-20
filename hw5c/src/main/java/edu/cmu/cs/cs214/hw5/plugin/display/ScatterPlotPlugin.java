package edu.cmu.cs.cs214.hw5.plugin.display;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientCategory;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientInfo;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.util.*;

/**
 * Plugin for displaying a scatter plot. Requires 2 nutrients for each food.
 * Take the first two nutrients in the list if there are more than 2 nutrients.
 * X and Y axes represent the values of each food for the specified two nutrients.
 */
public class ScatterPlotPlugin implements DisplayPlugin {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;

    @Override
    public JPanel getDisplay(Map<String, List<NutrientInfo>> nutrientData) {
        // Create chart builder
        XYChartBuilder builder = new XYChartBuilder().width(WIDTH).height(HEIGHT);
        // get the food names
        List<String> foods = new ArrayList<>();
        for (String food : nutrientData.keySet()) {
            foods.add(food);
        }
        // get the first 2 nutrients in the list
        NutrientCategory nutrientCategory1 = nutrientData.get(foods.get(0)).get(0).getName();
        NutrientCategory nutrientCategory2 = nutrientData.get(foods.get(0)).get(1).getName();
        String nutrient1 = nutrientCategory1.toString();
        String nutrient2 = nutrientCategory2.toString();
        // set the title of the scatter plot and build
        builder.title(nutrient1 + " and " + nutrient2 + " Comparison");
        builder.xAxisTitle(nutrient1 + "(" + nutrientData.get(foods.get(0)).get(0).getUnit() + ")");
        builder.yAxisTitle(nutrient2 + "(" + nutrientData.get(foods.get(0)).get(1).getUnit() + ")");
        XYChart scatterPlot = builder.build();
        // Customize Chart
        scatterPlot.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        scatterPlot.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        scatterPlot.getStyler().setMarkerSize(15);
        // Add Series
        for (String food : foods) {
            double[] xData = new double[1];
            double[] yData = new double[1];
            xData[0] = nutrientData.get(food).get(0).getValue();
            yData[0] = nutrientData.get(food).get(1).getValue();
            scatterPlot.addSeries(food, xData, yData);
        }
        return new XChartPanel<>(scatterPlot);
    }

    @Override
    public String getName() {
        return "Scatter Plot";
    }

    @Override
    public int minNutrientsRequired() {
        return 2;
    }
}
