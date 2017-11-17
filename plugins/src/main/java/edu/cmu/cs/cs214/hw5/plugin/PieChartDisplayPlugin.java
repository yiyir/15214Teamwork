package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;

import java.awt.*;
import java.util.Random;

import static java.lang.Thread.sleep;

public class PieChartDisplayPlugin implements DisplayPlugin {

    public void display(String[] xData, Double[] yData, String xLabel, String yLabel, String title) {

        assert (xData.length == yData.length);

        // Create Chart
        PieChart chart = new PieChartBuilder().width(800).height(600).title
                (title).build();

        // Customize Chart
        Color[] sliceColors = new Color[xData.length];
        for (int i = 0; i < xData.length; i++) {
            sliceColors[i] = new Color(
                    (int) (Math.random()*256),
                    (int) (Math.random()*256),
                    (int) (Math.random()*256));
            chart.addSeries(xData[i], yData[i]);
        }
        chart.getStyler().setSeriesColors(sliceColors);

        new SwingWrapper<PieChart>(chart).displayChart();
    }
}
