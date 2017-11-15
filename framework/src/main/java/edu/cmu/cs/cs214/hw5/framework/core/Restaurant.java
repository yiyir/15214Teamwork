package edu.cmu.cs.cs214.hw5.framework.core;


import java.util.ArrayList;
import java.util.List;

/**
 * This class is a data structure that we defined to help exchange data between data plugins and the framework.
 */
public class Restaurant {
    /**
     * the name of the restaurant
     */
    private final String name;
    /**
     * the city where the restaurant is located
     */
    private final String city;
    /**
     * the business hours of the restaurant
     */
    private List<List<Integer>> hours;
    /**
     * the column names(keys) which represent the meaning of the data
     */
    private final List<String> keys;
    /**
     * the values corresponding to the keys
     */
    private final List<Double> values;

    /**
     * Constructor method.
     *
     * @param name the name of the restaurant
     * @param city the city where the restaurant is located
     */
    public Restaurant(String name, String city) {
        this.name = name;
        this.city = city;
        hours = new ArrayList<>();
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    /**
     * Sets the business hours of the restaurant.
     *
     * @param hours the business hours
     */
    public void setHours(List<List<Integer>> hours) {
        this.hours = hours;
    }

    /**
     * Adds a new column of data to the Restaurant object.
     *
     * @param key   the key which represents the meaning of the data
     * @param value the value corresponding to the key
     */
    public void addData(String key, Double value) {
        keys.add(key);
        values.add(value);
    }

    /**
     * Gets the business hours of the restaurant.
     *
     * @return the business hours of the restaurant
     */
    public List<List<Integer>> getHours() {
        return hours;
    }

    /**
     * Gets the keys of the Restaurant object.
     *
     * @return the keys
     */
    public List<String> getKeys() {
        return keys;
    }

    /**
     * Gets the values corresponding to the keys of the Restaurant object.
     *
     * @return the values
     */
    public List<Double> getValues() {
        return values;
    }

    /**
     * Gets the name of the restaurant.
     *
     * @return the name of the restaurant
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the city where the restaurant is located
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Checks if the restaurant is open at the given time.
     *
     * @param day  the given day
     * @param time the given time of the day
     * @return whether the restaurant is open at the given time
     */
    public boolean isOpen(int day, int time) {
        // we assume the input day and time are both valid
        List<Integer> hoursRange = hours.get(day);
        if (hoursRange == null || hoursRange.size() == 0) return false;
        for (int i = 0; i < hoursRange.size(); i += 2) {
            if (hoursRange.get(i) < hoursRange.get(i + 1)) {
                if (time >= hoursRange.get(i) && time < hoursRange.get(i + 1)) return true;
            } else {
                if (time >= hoursRange.get(i)) return true;
            }
        }
        // check if the input time is within last day's overnight opening hours range
        int lastDay = (day + 6) % 7;
        List<Integer> lastDayHours = hours.get(lastDay);
        if (lastDayHours != null && lastDayHours.size() != 0) {
            int size = lastDayHours.size();
            if (lastDayHours.get(size - 1) < lastDayHours.get(size - 2)) {
                if (time < lastDayHours.get(size - 1)) return true;
            }
        }
        return false;
    }

    /**
     * Gets the string representation of the Restaurant object.
     *
     * @return the string representation of the Restaurant object
     */
    @Override
    public String toString() {
        return name;
    }
}
