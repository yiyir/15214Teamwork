package edu.cmu.cs.cs214.hw5.plugin.display;

import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knowm.xchart.BubbleChart;
import org.knowm.xchart.BubbleChartBuilder;
import org.knowm.xchart.XChartPanel;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.NutrientInfo;

/**
 * Plugin for displaying a bubble chart. Requires 3 nutrients for each food.
 * The nutrient with the smallest average value is used as the bubble size,
 * and the other two nutrients are used as X or Y axes.
 * Uses XChart to create the chart.
 */
public class BubbleChartPlugin implements DisplayPlugin {
  private static final int WIDTH = 500;
  private static final int HEIGHT = 300;
  private static final String TITLE = "%s vs %s vs %s";
  private static final String AXIS = "%s (%s)";
  
  @Override
  public JPanel getDisplay(Map<String, List<NutrientInfo>> nutrientData) {
    BubbleChartBuilder builder = new BubbleChartBuilder();
    builder.width(WIDTH);
    builder.height(HEIGHT);
    
    int size = nutrientData.size();
    String[] foods = new String[size];
    String[] categories = new String[3];
    double[][] data0 = new double[size][1];
    double[][] data1 = new double[size][1];
    double[][] data2 = new double[size][1];
    int avg0 = 0;
    int avg1 = 0;
    int avg2 = 0;
        
    int index = 0;
    for (String food : nutrientData.keySet()) {
      List<NutrientInfo> nutrients = nutrientData.get(food);
      NutrientInfo nutrient0 = nutrients.get(0);
      NutrientInfo nutrient1 = nutrients.get(1);
      NutrientInfo nutrient2 = nutrients.get(2);
      
      if (index == 0) {
        categories[0] = String.format(AXIS, nutrient0.getName(),
            nutrient0.getUnit());
        categories[1] = String.format(AXIS, nutrient1.getName(),
            nutrient1.getUnit());
        categories[2] = String.format(AXIS, nutrient2.getName(),
            nutrient2.getUnit());
      }
      
      foods[index] = food;
      data0[index] = new double[]{nutrient0.getValue()};
      avg0 += nutrient0.getValue();
      data1[index] = new double[]{nutrient1.getValue()};
      avg1 += nutrient1.getValue();
      data2[index] = new double[]{nutrient2.getValue()};
      avg2 += nutrient2.getValue();
      index++;
    }
   
    avg0 /= size;
    avg1 /= size;
    avg2 /= size;
    int bubble = Math.min(avg0, Math.min(avg1, avg2));
    if (bubble == avg0) {
      builder.title(String.format(TITLE, categories[0], categories[1],
          categories[2]));
      builder.xAxisTitle(categories[2]);
      builder.yAxisTitle(categories[1]);
      return makeBubbleChart(foods, data2, data1, data0, builder.build());
    }
    else if (bubble == avg1) {
      builder.title(String.format(TITLE, categories[1], categories[0],
          categories[2]));
      builder.xAxisTitle(categories[2]);
      builder.yAxisTitle(categories[0]);
      return makeBubbleChart(foods, data2, data0, data1, builder.build());
    }
    else {
      builder.title(String.format(TITLE, categories[2], categories[0],
          categories[1]));
      builder.xAxisTitle(categories[1]);
      builder.yAxisTitle(categories[0]);
      return makeBubbleChart(foods, data1, data0, data2, builder.build());
    }
  }

  @Override
  public String getName() {
    return "Bubble Chart";
  }

  @Override
  public int minNutrientsRequired() {
    return 3;
  }
  
  private JPanel makeBubbleChart(String[] foods, double[][] xData, 
      double[][] yData, double[][] bubbleData, BubbleChart chart) {
    for (int i = 0; i < foods.length; i++) {
      String name = foods[i];
      if (name.length() > 15) {
        name = name.substring(0, 15) + "...";
      }
      bubbleData[i][0] += 5;
      chart.addSeries(name, xData[i], yData[i], bubbleData[i]);
    }
    return new XChartPanel<BubbleChart>(chart);
  }

}
