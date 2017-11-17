package edu.cmu.cs.cs214.hw5.plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.Restaurant;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class YelpDataPlugin implements DataPlugin
{
    public List<Restaurant> getRestaurants(String city)
    {
        InputStream inputStream = YelpDataPlugin.class.getClassLoader()
                .getResourceAsStream("business" + ".json");
        Reader reader = new InputStreamReader(inputStream);
        JsonParser parser = new JsonParser();
        JsonElement rootElement = parser.parse(reader);
        JsonArray rootArray = rootElement.getAsJsonArray();

        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (JsonElement je : rootArray)
        {
            JsonObject jo = je.getAsJsonObject();
            if (jo.get("city").getAsString().equals(city)) {
                Restaurant restaurant = new Restaurant(jo.get("name")
                        .toString(), jo.get("city").toString());
                List<List<Integer>> hours = getHours(jo.get("hours")
                        .getAsJsonObject());
                restaurant.setHours(hours);
                restaurant.addData("reviews", jo.get("review_count").getAsDouble());
                restaurants.add(restaurant);
            }
        }
        return restaurants;
    }

    private List<List<Integer>> getHours(JsonObject jo) {
        List<List<Integer>> weeklyHours = new ArrayList<List<Integer>>(7);

        for (String key : jo.keySet()) {
            String times = jo.get(key).getAsString();
            List<Integer> dailyHours = new ArrayList<Integer>();
            dailyHours.add(getTimeFromString(times.split("-")[0]));
            dailyHours.add(getTimeFromString(times.split("-")[1]));
            weeklyHours.add(dailyHours);
        }
        return weeklyHours;
    }

    private Integer getTimeFromString(String times) {
        int pre = Integer.parseInt(times.split(":")[0]);
        int post = Integer.parseInt(times.split(":")[1]);
        return pre*100 + post;
    }
}
