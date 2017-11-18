package edu.cmu.cs.cs214.hw5.framework.core;

/**
 * Stores information about a specific category of nutrient.
 */
public class NutrientInfo {
    private final NutrientCategory name;
    private final String unit;
    private double value;

    /**
     * Takes in a nutrient name, unit of measure, and the value of that nutrient.
     *
     * @param n Name of nutrient category (calories, fat, etc.)
     * @param u Unit of measurement for this category (grams, milligrams, etc.)
     * @param v Value of this nutrient category in unit of measure.
     */
    public NutrientInfo(NutrientCategory n, String u, double v) {
        name = n;
        unit = u;
        value = v;
    }

    /**
     * Retrives the name of the nutrient.
     *
     * @return Name of the nutrient category (calories, fat, etc.)
     */
    public NutrientCategory getName() {
        return name;
    }

    /**
     * Retrieves the unit of how the value is measured.
     *
     * @return Unit of measurement for this category (grams, milligrams, etc.)
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Retrieves the value of the nutrient.
     *
     * @return Value of this nutrient category in unit of measure.
     */
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the nutrient.
     *
     * @param newV The new value of the nutrient.
     */
    public void setValue(double newV) {
        value = newV;
    }

    /**
     * toString method for testing.
     *
     * @return String version of nutrient.
     */
    @Override
    public String toString() {
        return name + ": " + value + " " + unit;
    }
}
