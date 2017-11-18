package edu.cmu.cs.cs214.hw5.framework.core;

/**
 * Enum for the different nutrient categories the framework supports.
 */
public enum NutrientCategory {
    PROTEIN("Protein"),
    TOTAL_FAT("Total fat"),
    CARBOHYDRATE("Carbohydrates"),
    CALORIES("Calories"),
    SUGAR("Sugar"),
    FIBER("Fiber"),
    CALCIUM("Calcium"),
    IRON("Iron"),
    MAGNESIUM("Magnesium"),
    SODIUM("Sodium"),
    VITAMIN_A("Vitamin A"),
    VITAMIN_D("Vitamin D"),
    VITAMIN_C("Vitamin C"),
    VITAMIN_B6("Vitamin B-6"),
    VITAMIN_B12("Vitamin B-12"),
    CHOLESTEROL("Cholesterol"),
    TRANS_FAT("Trans fat"),
    SATURATED_FAT("Saturated fat"),
    MONOUNSATURATED_FAT("Monosaturated fat"),
    POLYUNSATURATED_FAT("Polysaturated fat");

    private final String name;

    NutrientCategory(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }
}
