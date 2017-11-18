package edu.cmu.cs.cs214.hw5.framework.core;

/**
 * Enum that specifies the food groups used in USDA database API, with English description along with API id.
 */
public enum FoodGroup {
    BAKED_GOODS("1800", "Baked Goods"),
    BEEF_PRODUCTS("1300", "Beef Products"),
    BEVERAGES("1400", "Beverages"),
    BREAKFAST_CEREALS("0800", "Breakfast Cereals"),
    CEREAL_GRAINS_PASTA("2000", "Cereals, Grains, and Pasta"),
    DAIRY_EGG("0100", "Dairy and Egg Products"),
    FAST_FOOD("2100", "Fast Food"),
    FATS_OILS("0400", "Fats and Oils"),
    FISH("1500", "Fish and Shellfish Products"),
    FRUITS_AND_JUICES("0900", "Fruits and Juices"),
    LAMB_VEAL_GAME("1700", "Lamb, Veal, and Game Products"),
    LEGUMES("1600", "Legumes and Legume Products"),
    MEALS_ENTREES_SIDEDISHES("2200", "Meals, Entrees, and Side Dishes"),
    NUTS_SEEDS("1200", "Nut and Seed Products"),
    PORK("1000", "Pork Products"),
    POULTRY("0500", "Poultry Products"),
    RESTAURANT_FOODS("3600", "Restaurant Foods"),
    SAUSAGES_AND_LUNCHEON_MEATS("0700", "Sausages and Luncheon Meats"),
    SNACKS("2500", "Snacks"),
    SOUPS_SAUCES_GRAVIES("0600", "Soups, Sauces, and Gravies"),
    SPICES_HERBS("0200", "Spices and Herbs"),
    SWEETS("1900", "Sweets"),
    VEGETABLES("1100", "Vegetables and Vegetable Products"),
    NO_FOOD_GROUP("-1", "No Group");

    private final String fgID;
    private final String name;

    FoodGroup(String fg, String name) {
        fgID = fg;
        this.name = name;
    }

    /**
     * Gets the ID of the food group in the USDA Food API.
     *
     * @return String representing the ID of the food group in the USDA Food Database.
     */
    public String getID() {
        return fgID;
    }

    @Override
    public String toString() {
        return name;
    }
}
