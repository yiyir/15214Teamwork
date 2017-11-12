package edu.cmu.cs.cs214.hw5.plugin;

public interface DisplayPlugin {
    void showPlot();
    void setXAxisLabel(String label);
    void setYAxisLabel(String label);
    void setXAxisData(Integer value);
    void setYAxisData(Integer value);
    void setTitle(String title);


}
