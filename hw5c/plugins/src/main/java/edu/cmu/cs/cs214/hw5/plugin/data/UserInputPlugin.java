package edu.cmu.cs.cs214.hw5.plugin.data;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.FoodInfo;

/**
 * Plugin for getting data from user input. The user inputs what foods they
 * want and the amount of each food, in grams. The framework asks for user
 * input and passes it back to the data plugin.
 */
public class UserInputPlugin implements DataPlugin {

  @Override
  public List<FoodInfo> getData(String filePath, List<List<String>> userParameters) {
    List<FoodInfo> foodInfo = new ArrayList<>();
    List<String> foods = userParameters.get(0);
    List<String> amounts = userParameters.get(1);
    for (int i = 0; i < foods.size(); i++) {
      FoodInfo info = new FoodInfo(foods.get(i).trim());
      if (!amounts.isEmpty()) {
        info.setAmount(Double.parseDouble(amounts.get(i).trim()));
      }
      foodInfo.add(info);
    }
    return foodInfo;
  }

  @Override
  public boolean requiresFilePath() {
    return false;
  }

  @Override
  public List<String> getParameters() {
    List<String> parameters = new ArrayList<>();
    parameters.add("Foods");
    parameters.add("Amount of each food, in grams");
    return parameters;
  }

  @Override
  public String getName() {
    return "User Input";
  }

  @Override
  public String getFilePathMessage() {
    return "";
  }

}
