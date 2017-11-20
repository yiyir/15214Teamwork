package edu.cmu.cs.cs214.hw5.plugin.display;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientCategory;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientInfo;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Displays a pie chart for the percentage of daily value of each inputted food for a specific nutrient. Takes the first
 * nutrient in the list if there are more than one nutrients. If the foods exceed 100% of the daily value for the
 * nutrient, the pie chart simply displays 100%. The daily values used are for 2000-calorie diets.
 */
public class DailyValuePieChartPlugin implements DisplayPlugin {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final String TITLE = "Percentage of Daily Value of %s";
    private static final Map<NutrientCategory, Integer> DAILY_VALUES;
    private static final Color[] SLICES = new Color[]{new Color(230, 105, 62), new Color(236, 143, 110), new Color(246, 199, 182)};


    /**
     * Initializes the mapping from nutrient category to its daily value for 2000-calorie diets.
     */
    static {
        DAILY_VALUES = new HashMap<>();
        DAILY_VALUES.put(NutrientCategory.CALCIUM, 1000);
        DAILY_VALUES.put(NutrientCategory.CALORIES, 2000);
        DAILY_VALUES.put(NutrientCategory.CARBOHYDRATE, 300);
        DAILY_VALUES.put(NutrientCategory.CHOLESTEROL, 300);
        DAILY_VALUES.put(NutrientCategory.FIBER, 25);
        DAILY_VALUES.put(NutrientCategory.IRON, 18);
        DAILY_VALUES.put(NutrientCategory.MAGNESIUM, 400);
        DAILY_VALUES.put(NutrientCategory.MONOUNSATURATED_FAT, 44);
        DAILY_VALUES.put(NutrientCategory.POLYUNSATURATED_FAT, 44);
        DAILY_VALUES.put(NutrientCategory.PROTEIN, 50);
        DAILY_VALUES.put(NutrientCategory.SODIUM, 2400);
        DAILY_VALUES.put(NutrientCategory.SATURATED_FAT, 16);
        DAILY_VALUES.put(NutrientCategory.SUGAR, 25);
        DAILY_VALUES.put(NutrientCategory.TOTAL_FAT, 65);
        DAILY_VALUES.put(NutrientCategory.TRANS_FAT, 2);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_A, 5000);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_B6, 2);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_B12, 6);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_C, 60);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_D, 400);

    }

    @Override
    public JPanel getDisplay(Map<String, List<NutrientInfo>> nutrientData) {
        PieChartBuilder pieChartBuilder = new PieChartBuilder();
        pieChartBuilder.width(WIDTH);
        pieChartBuilder.height(HEIGHT);
        // get the food names
        List<String> foods = new ArrayList<>();
        for (String food : nutrientData.keySet()) {
            foods.add(food);
        }
        // get the first nutrient category in the list
        NutrientCategory nutrientCategory = nutrientData.get(foods.get(0)).get(0).getName();
        String nutrient = nutrientCategory.toString();
        // set the title of the pie chart and build
        pieChartBuilder.title(String.format(TITLE, nutrient));
        PieChart pieChart = pieChartBuilder.build();
        // check the total aggregate value of the inputted foods for the specific nutrient
        int totalValue = 0;
        StringBuilder sb = new StringBuilder("Foods: ");
        for (String food : foods) {
            sb.append(food + " ");
            totalValue += nutrientData.get(food).get(0).getValue();
        }
        if (totalValue >= DAILY_VALUES.get(nutrientCategory)) {
            pieChart.addSeries(sb.toString(), DAILY_VALUES.get(nutrientCategory));
        } else {
            pieChart.getStyler().setSeriesColors(SLICES);
            for (String food : foods) {
                pieChart.addSeries(food, nutrientData.get(food).get(0).getValue());
            }
            pieChart.addSeries("The Rest", DAILY_VALUES.get(nutrientCategory) - totalValue);
        }
        pieChart.getStyler().setLegendVisible(true);
        return new XChartPanel<>(pieChart);
    }

    @Override
    public String getName() {
        return "Daily Value Pie Chart";
    }

    @Override
    public int minNutrientsRequired() {
        return 1;
    }
}
