package edu.cmu.cs.cs214.hw5.plugin.display;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientCategory;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientInfo;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.DialChart;
import org.knowm.xchart.DialChartBuilder;
import org.knowm.xchart.XChartPanel;

/**
 * Displays a dial chart for the aggregate % daily value of the inputted foods for a specific nutrient. Takes the first
 * nutrient in the list if there are more than one nutrients. If the foods exceed 100% of the daily value for the
 * nutrient, the dial chart simply displays 100%. The daily values used are for 2000-calorie diets
 */
public class DailyValueDialChartPlugin implements DisplayPlugin{
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final String TITLE = "%% Daily Value of %s \n Foods: %s";
    private static final int MAX_FOOD_STRING_LEN = 30;
    private static final Map<NutrientCategory,Integer> DAILY_VALUES;

    /**
     * Initializes the mapping from nutrient category to its daily value for 2000-calorie diets.
     */
    static {
        DAILY_VALUES = new HashMap<>();
        DAILY_VALUES.put(NutrientCategory.CALCIUM,1000);
        DAILY_VALUES.put(NutrientCategory.CALORIES,2000);
        DAILY_VALUES.put(NutrientCategory.CARBOHYDRATE,300);
        DAILY_VALUES.put(NutrientCategory.CHOLESTEROL,300);
        DAILY_VALUES.put(NutrientCategory.FIBER,25);
        DAILY_VALUES.put(NutrientCategory.IRON,18);
        DAILY_VALUES.put(NutrientCategory.MAGNESIUM,400);
        DAILY_VALUES.put(NutrientCategory.MONOUNSATURATED_FAT,44);
        DAILY_VALUES.put(NutrientCategory.POLYUNSATURATED_FAT,44);
        DAILY_VALUES.put(NutrientCategory.PROTEIN,50);
        DAILY_VALUES.put(NutrientCategory.SODIUM,2400);
        DAILY_VALUES.put(NutrientCategory.SATURATED_FAT,16);
        DAILY_VALUES.put(NutrientCategory.SUGAR,25);
        DAILY_VALUES.put(NutrientCategory.TOTAL_FAT,65);
        DAILY_VALUES.put(NutrientCategory.TRANS_FAT,2);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_A,5000);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_B6,2);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_B12,6);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_C,60);
        DAILY_VALUES.put(NutrientCategory.VITAMIN_D,400);
    }

    @Override
    public JPanel getDisplay(Map<String, List<NutrientInfo>> nutrientData) {
        DialChartBuilder dailyValueMeter = new DialChartBuilder();
        dailyValueMeter.width(WIDTH);
        dailyValueMeter.height(HEIGHT);

        String[] foodsList = new String[nutrientData.size()];
        StringBuilder foodsBuilder = new StringBuilder();
        int i = 0;
        boolean overflowed = false;
        for(String food : nutrientData.keySet()) {
            foodsList[i] = food;
            if(foodsBuilder.length()+food.length() < MAX_FOOD_STRING_LEN) {
                overflowed = true;
                foodsBuilder.append(food);
                if (i < foodsList.length - 1) {
                    foodsBuilder.append(", ");
                }
            }
            i++;
        }

        if(overflowed) {
            foodsBuilder.append("...");
        }

        NutrientCategory nutrient = nutrientData.get(foodsList[0]).get(0).getName();
        String nutrientName = nutrientData.get(foodsList[0]).get(0).getName().toString();
        dailyValueMeter.title(String.format(TITLE,nutrientName,foodsBuilder.toString()));

        double total = 0;
        for(String food : nutrientData.keySet()) {
            for(NutrientInfo n : nutrientData.get(food)) {
                if(n.getName().toString().equals(nutrientName)) {
                    total += nutrientData.get(food).get(0).getValue();
                }
            }
        }

        DialChart chart = dailyValueMeter.build();
        if(total/(double)DAILY_VALUES.get(nutrient) >= 1) {
            chart.addSeries("Daily Value", 1);
        } else {
            chart.addSeries("Daily Value", total / (double) DAILY_VALUES.get(nutrient));
        }
        chart.getStyler().setLegendVisible(false);

        return new XChartPanel<>(chart);
    }

    @Override
    public String getName() {
        return "Daily Value Dial Chart";
    }

    @Override
    public int minNutrientsRequired() {
        return 1;
    }
}
