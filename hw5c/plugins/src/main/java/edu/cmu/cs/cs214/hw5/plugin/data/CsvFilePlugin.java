package edu.cmu.cs.cs214.hw5.plugin.data;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.FoodGroup;
import edu.cmu.cs.cs214.hw5.framework.core.FoodInfo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Takes in the name of a csv file, which must be inside the resources folder under plugins. This particular data
 * plugin requires csv files to be of the format: foodName, amountInGrams, FoodCategoryName
 *
 * FoodCategoryName can be left blank (user will then be prompted to fill in by GUI afterwards).
 *
 */
public class CsvFilePlugin implements DataPlugin {

    /* @Override */
    public List<FoodInfo> getData(String fileName, List<List<String>> userParameters) {
        List<FoodInfo> fileInfo = new ArrayList<>();
        String line = "";
        String csvSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + fileName + ".csv"))) {
            while ((line = br.readLine()) != null) {
                String[] oneFood = line.split(csvSplitBy);
                if (oneFood.length < 2){
                    System.out.println("Invalid input, skipped");
                }
                else {
                    FoodInfo row = new FoodInfo(oneFood[0]);
                    row.setAmount(Double.parseDouble(oneFood[1]));
                    if (oneFood.length > 2){
                        for (FoodGroup f : FoodGroup.values()){
                            if (f.toString().equals(oneFood[2])){
                                row.setFoodGroup(f);
                            }
                        }
                    }
                    fileInfo.add(row);
                }
            }
            return fileInfo;
        } catch (IOException e) {
            System.out.println("Invalid file name");
            return new ArrayList<>();
        }
    }

    @Override
    public boolean requiresFilePath() {
        return true;
    }

    @Override
    public List<String> getParameters() {
        return new ArrayList<String>();
    }

    @Override
    public String getName() {
        return "Csv File Reader";
    }

    @Override
    public String getFilePathMessage() {
      return "Do not include .csv extension. File must be in src/main/resources folder.";
    }
}
