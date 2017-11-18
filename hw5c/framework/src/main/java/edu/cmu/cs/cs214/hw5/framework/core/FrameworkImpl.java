package edu.cmu.cs.cs214.hw5.framework.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Framework implementation. Given a data plugin, a display plugin, and a list of nutrients, the framework will get
 * a list of foods (along with information on the food group and quantity of each food if available) from the data
 * plugin, calculate each food's nutrition facts for the given list of nutrients, and pass the data along to the display
 * plugin. The framework will request user input when: the food groups were not provided for all foods and/or
 * the data plugin requests user input for a set of parameters.
 */
public class FrameworkImpl {
    // Refer to README.md to gain access to API key
    private static final String API_KEY = "lcNzAcJB1TvoBtvBGn1oMLItPCVpjsV2dUC7NBdq";
    // Endpoint URL for finding correct ndbno id
    private static final String ID_ENDPOINT = "https://api.nal.usda.gov/ndb/search/?format=json&q=%s&max=1&offset=0" +
            "&api_key=%s";
    // Endpoint URL for finding nutrient information, given an ndbno id
    private static final String NUTRIENT_ENDPOINT = "https://api.nal.usda.gov/ndb/nutrients/?format=json&" +
            "api_key=%s&nutrients=203&nutrients=204&nutrients=205&" +
            "nutrients=208&nutrients=269&nutrients=291&nutrients=301&nutrients=303&nutrients=304&nutrients=307" +
            "&nutrients=318&nutrients=324&nutrients=401&nutrients=415&nutrients=418&nutrients=601&nutrients=605" +
            "&nutrients=606&nutrients=645&nutrients=646&ndbno=%s";
    // Mapping from the String representing a nutrient category to the appropriate NutrientCategory enum value
    private static final Map<String, NutrientCategory> NUTRIENT_MAP;

    private OkHttpClient client = new OkHttpClient();
    private final List<FrameworkListener> listeners;

    private List<NutrientCategory> currentNutrients;
    private DisplayPlugin currentDisplayPlugin;
    private DataPlugin currentDataPlugin;
    private List<FoodInfo> foods;

    static {
        NUTRIENT_MAP = new HashMap<>();
        NUTRIENT_MAP.put("Protein", NutrientCategory.PROTEIN);
        NUTRIENT_MAP.put("Total lipid (fat)", NutrientCategory.TOTAL_FAT);
        NUTRIENT_MAP.put("Carbohydrate, by difference", NutrientCategory.CARBOHYDRATE);
        NUTRIENT_MAP.put("Energy", NutrientCategory.CALORIES);
        NUTRIENT_MAP.put("Sugars, total", NutrientCategory.SUGAR);
        NUTRIENT_MAP.put("Fiber, total dietary", NutrientCategory.FIBER);
        NUTRIENT_MAP.put("Calcium, Ca", NutrientCategory.CALCIUM);
        NUTRIENT_MAP.put("Iron, Fe", NutrientCategory.IRON);
        NUTRIENT_MAP.put("Magnesium, Mg", NutrientCategory.MAGNESIUM);
        NUTRIENT_MAP.put("Sodium, Na", NutrientCategory.SODIUM);
        NUTRIENT_MAP.put("Vitamin A, IU", NutrientCategory.VITAMIN_A);
        NUTRIENT_MAP.put("Vitamin D", NutrientCategory.VITAMIN_D);
        NUTRIENT_MAP.put("Vitamin C, total ascorbic acid", NutrientCategory.VITAMIN_C);
        NUTRIENT_MAP.put("Vitamin B-6", NutrientCategory.VITAMIN_B6);
        NUTRIENT_MAP.put("Vitamin B-12", NutrientCategory.VITAMIN_B12);
        NUTRIENT_MAP.put("Cholesterol", NutrientCategory.CHOLESTEROL);
        NUTRIENT_MAP.put("Fatty acids, total trans", NutrientCategory.TRANS_FAT);
        NUTRIENT_MAP.put("Fatty acids, total saturated", NutrientCategory.SATURATED_FAT);
        NUTRIENT_MAP.put("Fatty acids, total monounsaturated", NutrientCategory.MONOUNSATURATED_FAT);
        NUTRIENT_MAP.put("Fatty acids, total polyunsaturated", NutrientCategory.POLYUNSATURATED_FAT);
    }

    /**
     * Function for API calls.
     *
     * @param url Endpoint required for API call
     * @return String of the JSON object that is returned
     */
    private String run(String url) {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            return "IOException error";
        }
    }

    /**
     * Construct an instance of the framework.
     */
    public FrameworkImpl() {
        listeners = new ArrayList<FrameworkListener>();
    }

    /**
     * Add the given framework listener to the framework.
     *
     * @param f the framework listener
     */
    public void addFrameworkListener(FrameworkListener f) {
        listeners.add(f);
    }

    /**
     * Used to retrieve foods that are not assigned food groups by the data plugin and requires user input.
     *
     * @param foods List of FoodInfo instances that Dataplugin provides
     * @return List of foods that require user input
     */
    private List<String> getNotAssignedFoods(List<FoodInfo> foods) {
        List<String> notAssignedFoods = new ArrayList<String>();
        for (FoodInfo food : foods) {
            if (food.getFoodGroup() == FoodGroup.NO_FOOD_GROUP) {
                notAssignedFoods.add(food.getFoodName());
            }
        }
        return notAssignedFoods;
    }

    /**
     * Given list of FoodInfos, with all food groups assigned, returns list of ndbno's (Food ID numbers) that wil be
     * used to query nutrition information later.
     *
     * @param foods List of FoodInfos to get nutrition information about; food groups all assigned
     * @return Map from food name to ndbnos to query
     */
    private Map<String, String> getIDs(List<FoodInfo> foods) {
        FrameworkImpl example = new FrameworkImpl();
        List<FoodInfo> invalidFoodGroups = new ArrayList<>();
        HashMap<String, String> ids = new HashMap<>();
        for (FoodInfo food : foods) {
            FoodGroup f = food.getFoodGroup();
            String response = example.run(String.format(ID_ENDPOINT, food.getFoodName() + "&fg=" + f.getID(), API_KEY));
            JSONObject json = new JSONObject(response);
            if (json.has("errors")) {
                invalidFoodGroups.add(food);
            } else {
                ids.put(food.getFoodName(), json.getJSONObject("list").getJSONArray("item").getJSONObject(0)
                        .getString("ndbno"));
            }
        }
        if (invalidFoodGroups.size() > 0) {
            for (FrameworkListener f : listeners) {
                f.notifyInvalidFoodGroups(invalidFoodGroups);
            }
        }
        return ids;
    }

    /**
     * Given a mapping of food name to ndbno's (Food ID numbers), passes back a mapping from food name to the list of
     * nutrientInfos, each nutrient info reflecting a nutrient category (fat, calories, etc.).
     *
     * @param idList Mapping from food name to of ndbno
     * @return Mapping from food name to the list of nutrient objects for that food
     */
    private Map<String, List<NutrientInfo>> getNutrients(Map<String, String> idList) {
        HashMap<String, List<NutrientInfo>> nutrients = new HashMap<>();
        for (Map.Entry<String, String> food : idList.entrySet()) {
            String id = food.getValue();
            String response = run(String.format(NUTRIENT_ENDPOINT, API_KEY, id));
            JSONObject json = new JSONObject(response);
            JSONArray arr = json.getJSONObject("report").getJSONArray("foods");
            if (arr.length() != 0) {
                JSONArray nutrient = arr.getJSONObject(0).getJSONArray("nutrients");
                List<NutrientInfo> nutrientList = new ArrayList<>();
                for (int j = 0; j < nutrient.length(); j++) {
                    JSONObject o = nutrient.getJSONObject(j);
                    double value;
                    try {
                        value = o.getDouble("gm"); // API gives back value -- if value is 0
                    } catch (Exception e) {
                        value = 0.0;
                    }
                    if (o.getString("nutrient") != null) {
                        NutrientInfo n = new NutrientInfo(NUTRIENT_MAP.get(o.getString("nutrient")),
                                o.getString("unit"), value);
                        nutrientList.add(n);
                    }
                }
                nutrients.put(food.getKey(), nutrientList);
            }
        }
        return nutrients;
    }

    /**
     * Notifies listeners that data plugin was registered.
     *
     * @param d Data plugin that was registered
     */
    public void registerDataPlugin(DataPlugin d) {
        for (FrameworkListener f : listeners) {
            f.onDataPluginRegistered(d);
        }
    }

    /**
     * Notifies listeners that display plugin was registered.
     *
     * @param d Display plugin that was registered
     */
    public void registerDisplayPlugin(DisplayPlugin d) {
        for (FrameworkListener f : listeners) {
            f.onDisplayPluginRegistered(d);
        }
    }

    /**
     * Gets new display for the current list of foods and list of nutrients chosen.
     *
     * @param userFoodGroups Mapping of food names to food groups that the user picks
     */
    public void setDisplay(Map<String, FoodGroup> userFoodGroups) {
        // add user-specified food groups to map
        for (FoodInfo f : foods) {
            if (f.getFoodGroup() == FoodGroup.NO_FOOD_GROUP) {
                f.setFoodGroup(userFoodGroups.get(f.getFoodName()));
            }
        }

        Map<String, List<NutrientInfo>> currentData = new HashMap<>();

        Map<String, List<NutrientInfo>> newFoods = getNutrients(getIDs(foods));

        for (FoodInfo food : foods) {
            if (newFoods.get(food.getFoodName()) != null) {
                List<NutrientInfo> l = newFoods.get(food.getFoodName());
                List<NutrientInfo> newL = new ArrayList<>();
                for (NutrientInfo n : l) {
                    NutrientInfo newN = new NutrientInfo(n.getName(), n.getUnit(), n.getValue());
                    newN.setValue(n.getValue() * food.getAmount() / FoodInfo.DEFAULT_AMOUNT);
                    newL.add(newN);
                }
                if (currentData.get(food.getFoodName()) != null) {
                    List<NutrientInfo> oldNutrients = currentData.get(food.getFoodName());
                    for (int i = 0; i < oldNutrients.size(); i++) {
                        newL.get(i).setValue(newL.get(i).getValue() + oldNutrients.get(i).getValue());
                    }
                }
                currentData.put(food.getFoodName(), newL);
            }
        }

        for (NutrientCategory n : NutrientCategory.values()) {
            if (!currentNutrients.contains(n)) {
                for (Map.Entry<String, List<NutrientInfo>> food : currentData.entrySet()) {
                    List<NutrientInfo> l = food.getValue();
                    int i = 0;
                    while (i < l.size() && l.get(i).getName() != (n)) {
                        i++;
                    }
                    if (l.get(i).getName() == n) {
                        l.remove(i);
                    }
                }
            }
        }

        if (currentData.isEmpty()) {
            notifyPluginFailure(currentDataPlugin.getName());
        } else {
            JPanel j = currentDisplayPlugin.getDisplay(currentData);
            for (FrameworkListener f : listeners) {
                f.onDisplayAdded(j);
            }
        }
    }

    /**
     * Get foods to retrieve nutrient information for from the data plugin.
     *
     * @param filePath       File path that the data plugin should extract data from; "" if no file path is required
     * @param userParameters Parameters for data plugin
     */
    public void getFoods(String filePath, List<List<String>> userParameters) {
        foods = currentDataPlugin.getData(filePath, userParameters);
        if (foods.size() == 0) {
            notifyPluginFailure(currentDataPlugin.getName());
            return;
        }
        List<String> notGrouped = getNotAssignedFoods(foods);
        if (notGrouped.size() > 0) {
            for (FrameworkListener f : listeners) {
                f.onRequestFoodGroup(notGrouped);
            }
        } else {
            setDisplay(new HashMap<>());
        }
    }

    /**
     * Start display process; notifies user if invalid number of nutrients selected for chosen display plugin,
     * prompts for file path and parameters if necessary, or proceeds to extract
     *
     * @param dataPlugin    Data plugin to get data from
     * @param displayPlugin Display plugin used to display the data
     * @param nutrients     List of nutrients to send the display plugin
     */
    public void startNewDisplay(DataPlugin dataPlugin, DisplayPlugin displayPlugin, List<NutrientCategory> nutrients) {
        if (displayPlugin.minNutrientsRequired() > nutrients.size()) {
            for (FrameworkListener f : listeners) {
                f.notifyInvalidNutrients(nutrients.size(), displayPlugin.minNutrientsRequired());
            }
        } else {
            this.currentNutrients = nutrients;
            this.currentDisplayPlugin = displayPlugin;
            this.currentDataPlugin = dataPlugin;
            if (dataPlugin.requiresFilePath()) {
                for (FrameworkListener f : listeners) {
                    f.onRequestFilePath(dataPlugin.getFilePathMessage());
                }
            } else if (dataPlugin.getParameters().size() != 0) {
                for (FrameworkListener f : listeners) {
                    f.onRequestParameters(dataPlugin.getParameters());
                }
            } else {
                getFoods("", new ArrayList<>());
            }
        }

    }

    private void notifyPluginFailure(String dataPluginName) {
        for (FrameworkListener f : listeners) {
            f.notifyPluginFailure(dataPluginName);
        }
    }
}
