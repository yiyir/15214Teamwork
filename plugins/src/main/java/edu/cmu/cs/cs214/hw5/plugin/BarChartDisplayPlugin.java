package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.util.Arrays;

public class BarChartDisplayPlugin implements DisplayPlugin {

    public void display(String[] xData, Double[] yData, String xLabel, String
            yLabel, String title) {

        // Create Chart
        CategoryChart chart = new CategoryChartBuilder().width(800).height
                (600).title(title).xAxisTitle(xLabel).yAxisTitle(yLabel)
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);

        // Series
        chart.addSeries("Frequency", Arrays.asList(xData), Arrays.asList
                (yData));

        new SwingWrapper<CategoryChart>(chart).displayChart();
    }
}
