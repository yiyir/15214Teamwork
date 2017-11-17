package edu.cmu.cs.cs214.hw5.plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.Restaurant;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class GoogleDataPlugin implements DataPlugin {

    private final String PREFIX = "https://maps.googleapis.com/maps/api/place/";
    private final String TEXTSEARCH = "textsearch/";
    private final String DETAILS = "details/";
    private final String OUTPUT = "json";
    private final String KEY = "AIzaSyDao1rF0kwWkq9TUfUOazH0VF6bNhfc_Nk";

    public List<Restaurant> getRestaurants(String city) {

        String query = PREFIX + TEXTSEARCH + OUTPUT + "?" + "key=" + KEY +
                "&query=" + city + "%20restaurant";
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        JsonObject queryResult = getQueryResultAsJsonObject(query);

        while (queryResult.has("next_page_token")) {
            String pagetoken = queryResult.get("next_page_token").toString();
            pagetoken = pagetoken.substring(1,pagetoken.length() - 1);

            JsonArray ja = queryResult.getAsJsonArray("results");
            addRestaurants(restaurants, ja, city);

            String newQuery = query + "&pagetoken=" + pagetoken;
            queryResult = getQueryResultAsJsonObject(newQuery);
        }
        System.out.println("Outside the loop");
        JsonArray ja = queryResult.getAsJsonArray("results");
        addRestaurants(restaurants, ja, city);

        return restaurants;
    }

    private JsonObject getQueryResultAsJsonObject(String query) {

        System.out.println(query);

        JsonObject rootObject = null;
        String temp = "temp.json";

        try {
            URL url = new URL(query);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String file = "";
            String line;
            try {
                while ((line = br.readLine()) != null)
                    file = file + line + "\n";
            } catch (IOException e) {
                e.printStackTrace();
            }

            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(file);
            rootObject = rootElement.getAsJsonObject();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootObject;
    }

    private void addRestaurants(List<Restaurant> restaurants, JsonArray ja,
                               String city) {
        for (JsonElement je : ja) {
            JsonObject jo = je.getAsJsonObject();
            String name = jo.get("name").getAsString();
            Restaurant restaurant = new Restaurant(name, city);
            List<List<Integer>> hours = getHours(jo.get("place_id").toString());
            restaurant.setHours(hours);
            if (jo.has("photos")) {
                restaurant.addData("photos", (double) jo.getAsJsonArray
                        ("photos").size());
            }
            restaurants.add(restaurant);
        }
    }

    private List<List<Integer>> getHours(String placeid) {
        List<List<Integer>> weeklyHours = new ArrayList<List<Integer>>();
        String query = PREFIX + DETAILS + OUTPUT + "?" + "key=" + KEY +
                "&placeid=" + placeid;
        JsonObject details = getQueryResultAsJsonObject(query);
        if (details.has("opening_hours")) {
            JsonArray periods = details.getAsJsonObject("opening_hours")
                    .getAsJsonArray("periods");
            for (JsonElement day : periods) {
                List<Integer> dailyHours = new ArrayList<Integer>();
                dailyHours.add((day.getAsJsonObject().get("open").getAsJsonObject
                        ().get("time").getAsInt()));
                dailyHours.add((day.getAsJsonObject().get("close").getAsJsonObject
                        ().get("time").getAsInt()));
                weeklyHours.add(dailyHours);
            }
        }
        return weeklyHours;
    }
}
