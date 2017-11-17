package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.DisplayPlugin;

public class BarChartDisplayPlugin implements DisplayPlugin {
    @Override
    public void display(String[] xData, Double[] yData, String xLabel, String yLabel, String title) {
        for(String str:xData){
            System.out.print(str);
        }
        for(Double dou:yData){
            System.out.print(dou);
        }
        System.out.println(xLabel);
        System.out.println(yLabel);
        System.out.println(title);
    }

    @Override
    public String toString() {
        return "BarChartDisplayPlugin";
    }
}
