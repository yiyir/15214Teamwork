# Nutrient Framework
Our domain is nutrition facts. Given a list of foods, our framework uses the USDA Nutrient Database to retrieve nutrition information for up to 20 nutrients about each food. The user can specify which nutrients they want the display plugin to compare. If the data plugin specifes the amount of each food in grams, the amount of each nutrient will be the value for the specified amount of food. Otherwise, the default is the nutrient value per 100 grams of food.

# APIs
Our framework uses the [USDA Nutrient Database](https://ndb.nal.usda.gov/ndb/)  to retrieve Nutrient Reports for a given food and set of nutrients. We support querying the following nutrients:
* Protein
* Total fat
* Carbohydrates
* Calories
* Sugar
* Fiber
* Calcium
* Iron
* Magnesium
* Sodium
* Vitamin A
* Vitamin D
* Vitamin C
* Vitamin B-6
* Vitamin B-12
* Cholesterol
* Trans fat
* Saturated fat
* Monosaturated fat
* Polysaturated fat

# Data Plugins
The main job of the data plugin is to provide the name and amount of foods that the framework will retrieve nutrition information about.

Data plugins implement the `DataPlugin` interface. Our framework expects a list of `FoodInfo` instances that users want nutrient information about. Each `FoodInfo` instance contains the name of a food, the amount of the food in grams, and the food group that the given food fits in.

It the data source does not specify the amount of a particular food, the default value is 100 grams.

Each food must have a food group to ensure accurate results from the USDA API. If the data source does not provide a food group for a particular food, the framework will ask the user to manually select the appropriate food group. If an invalid food group is chosen, e.g. the group `Fruits and Juices` for `Turkey`, nutrient data will not be retrieved for that food.

The `DataPlugin` interface includes methods for requesting a file path and/or parameters from the user. For example, our sample `UserInputPlugin` requests that the user input the foods and the amount of each food for which they want nutrition information.

Some example data plugins:
* Random food retriever [(link)](https://www.randomlists.com/food)
* Ingredients from a recipe
* Grocery list extraction
* ...and much more!

# Display Plugins
Display plugins implement the `DisplayPlugin` interface. Given a mapping from `String` food names to `NutrientInfo` lists, the plugin should return a `JPanel` of the display. Each `NutrientInfo` instance specifies the name of the nutrient, its value, and its unit.

The nutrients given to the display plugin will only be those requested by the user. If the user requests too few nutrients for the selected display, the framework will notify the user that the chosen display cannot be shown. For example, our sample `BubbleChartPlugin` requires a minimum of 3 nutrients because bubble charts display 3 dimensional data.

Some example data plugins:
* Pictograms with the food images
* Bar charts that compare one nutrient category across all foods
* Grouped bar charts comparing multiple nutrient categories
* Pie charts showing % daily value
* ...and much more!

# Running the Framework
Our framework uses `java.util.ServiceLoader` to load plugins. If you are implementing plugins in the `plugins` project, add the fully qualified class name of your plugin to the appropriate `META-INF/services` file in the `src/main/resources` folder of the `plugins` project. Start the framework with `gradle run` from the `plugins` directory.

The framework uses the [USDA Nutrient Database](https://ndb.nal.usda.gov/ndb/doc/apilist/API-NUTRIENT-REPORT.md), and thus requires an API Key. To request your own free API Key, go to [this link](https://ndb.nal.usda.gov/ndb/api/doc#), and under the "Gaining Access" section, click "Sign up now". The API key will be given to you immediately, and has a default rate of 1,000 requests per hour per IP address. Be sure to replace the `API_KEY` field in the file `FrameworkImpl` with your access key.

If you are implementing plugins in a different project, the project must have two `META-INF/services` files in the `src/main/resources` directory, one for the data and one for the display plugins. Add the following lines to your project's `build.gradle`:
```
apply plugin: 'application'
mainClassName = 'edu.cmu.cs.cs214.hw5.Main'

dependencies {
  compile project(':framework')
}
```
Start the framework with `gradle run` from your project directory (containing `framework` and `plugins`).
