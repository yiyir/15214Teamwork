package edu.cmu.cs.cs214.hw5.plugin.data;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.FoodInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomListPlugin implements DataPlugin {

    private static final Pattern p = Pattern.compile("([a-zA-Z]+)(</span></div></li>)");

    /**
     * Retrieves data from the plugin--expects a list of foods that contains information about the food, including
     * our enum food group to make it easier to search for nutrient information and the amount of food to scale the
     * nutrition facts. Can also specify no food group within the enum. The default amount of food if the data plugin
     * does not explicitly provide it is 100g.
     *
     * @param filePath       The file path to extract data from; empty string if file path is not required.
     * @param userParameters The user parameters; the ith list in userParameters contains the user input
     *                       for the ith parameter in the list returned by getParameters().
     * @return A list of FoodInfo instances that each contains the food name, the food group, and the amount of food (in
     * grams) that the data plugin would like nutrition facts for. The default amount of food for each instance is 100g,
     * and the default food group is NO_FOOD_GROUP.
     */
    @Override
    public List<FoodInfo> getData(String filePath, List<List<String>> userParameters) {
        List<FoodInfo> foodInfo = new ArrayList<>();
        try {
            URL url = new URL("https://www.randomlists.com/food");
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            try {
                while ((line = br.readLine()) != null) {
                    Matcher m = p.matcher(line);
                    while (m.find()) {
                        foodInfo.add(new FoodInfo(m.group(1)));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foodInfo;
    }

    /**
     * Lets framework know if the data plugin requires a file path.
     *
     * @return True if data plug in requires file path, false otherwise.
     */
    @Override
    public boolean requiresFilePath() {
        return false;
    }

    /**
     * Returns a message about file path specifics to be displayed by the GUI,
     * e.g. how the file path should be formatted or where the file should be
     * located, if applicable.
     *
     * @return Message about the file path
     */
    @Override
    public String getFilePathMessage() {
        return "";
    }

    /**
     * Returns the names of the required parameters for this data plugin. For example, a user input data plugin might
     * request the user to input a list of foods and their quantities; in this case, getParameters() would return
     * ["Food", "Amount"].
     *
     * @return A list as described above; empty if no parameters required.
     */
    @Override
    public List<String> getParameters() {
        return new ArrayList<String>();
    }

    /**
     * Returns the name of the plugin.
     *
     * @return The plugin name
     */
    @Override
    public String getName() {
        return "Random Food List";
    }
}
