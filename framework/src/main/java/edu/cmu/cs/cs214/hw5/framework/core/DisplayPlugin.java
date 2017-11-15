package edu.cmu.cs.cs214.hw5.framework.core;

public interface DisplayPlugin {
    void showPlot();

    void setXAxisLabel(String label);

    void setYAxisLabel(String label);

    void setXAxisData(String value);

    void setYAxisData(Double value);

    void setTitle(String title);

    @Override
    String toString();


}
