package edu.cmu.cs.cs214.hw5.plugin;

import edu.cmu.cs.cs214.hw5.framework.core.DataPlugin;
import edu.cmu.cs.cs214.hw5.framework.core.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class YelpDataPlugin implements DataPlugin{
    private static final String IMAGE_COUNTS = "ImageCounts";
    private static final String REVIEW_COUNTS = "ReviewCounts";
    @Override
    public List<Restaurant> getRestaurants(String city) {
        if(city.equals("pittsburgh")){
            Restaurant res1 = new Restaurant("KFC",city);
            Restaurant res2 = new Restaurant("McDonald", city);
            Restaurant res3 = new Restaurant("PizzaHut",city);
            List<List<Integer>> hours1  = new ArrayList<>();
            List<List<Integer>> hours2  = new ArrayList<>();
            List<Integer> standardDay = new ArrayList<>();
            List<Integer> specialDay = new ArrayList<>();
            specialDay.add(1000);
            specialDay.add(1400);
            standardDay.add(1000);
            standardDay.add(2200);
            for(int i = 0;i <7;i++){
                hours1.add(standardDay);
            }
            for(int i = 0;i <7;i++){
                hours2.add(specialDay);
            }
            res1.setHours(hours1);
            res2.setHours(hours1);
            res3.setHours(hours2);
            res1.addData(IMAGE_COUNTS,220.0);
            res1.addData(REVIEW_COUNTS,300.0);
            res2.addData(IMAGE_COUNTS,50.0);
            res2.addData(REVIEW_COUNTS,100.0);
            res3.addData(IMAGE_COUNTS,59.0);
            res3.addData(REVIEW_COUNTS,28.0);
            List<Restaurant> restaurants = new ArrayList<>();
            restaurants.add(res1);
            restaurants.add(res2);
            restaurants.add(res3);
            return restaurants;
        }
        return new ArrayList<Restaurant>();
    }

    @Override
    public String toString() {
        return "YelpDataPlugin";
    }
}
