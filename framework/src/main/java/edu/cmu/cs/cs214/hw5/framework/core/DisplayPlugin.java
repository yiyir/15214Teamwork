package edu.cmu.cs.cs214.hw5.framework.core;

public interface DisplayPlugin {

    void display(String[] xData, Double[] yData, String xLabel, String yLabel, String title);

    @Override
    String toString();


}
