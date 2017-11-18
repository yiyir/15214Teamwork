package edu.cmu.cs.cs214.hw5.framework.core;

/**
 * Contains information about a particular food.
 */
public class FoodInfo {
    private String foodName;
    private FoodGroup fg;
    private double amount;

    public static final double DEFAULT_AMOUNT = 100.0;

    /**
     * Initializes the name of the food; sets the default amount of food to 100g; sets the food group to initially be
     * NO_FOOD_GROUP.
     *
     * @param foodName The name of the food that the instance will hold information about.
     */
    public FoodInfo(String foodName) {
        this.foodName = foodName;
        this.fg = FoodGroup.NO_FOOD_GROUP;
        amount = DEFAULT_AMOUNT;
    }

    /**
     * Initializes the name of the food and the food group; sets the default amount of food to 100g.
     *
     * @param foodName The name of the food that the instance will hold information about.
     * @param fg       The food group that the food belongs to.
     */
    public FoodInfo(String foodName, FoodGroup fg) {
        this.foodName = foodName;
        this.fg = fg;
        amount = DEFAULT_AMOUNT;
    }

    /**
     * Sets the amount of food in grams.
     *
     * @param amount The amount of food in grams.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Sets the food group of this food.
     *
     * @param fg An instance of the FoodGroup enum.
     */
    public void setFoodGroup(FoodGroup fg) {
        this.fg = fg;
    }

    /**
     * Retrieves the food group.
     *
     * @return The food group that this food belongs to.
     */
    public FoodGroup getFoodGroup() {
        return fg;
    }

    /**
     * Retrieves the name of the food.
     *
     * @return The name of the food.
     */
    public String getFoodName() {
        return foodName;
    }

    /**
     * Retrieves the amount of food, in grams.
     *
     * @return The amount of food, in grams.
     */
    public double getAmount() {
        return amount;
    }
}
