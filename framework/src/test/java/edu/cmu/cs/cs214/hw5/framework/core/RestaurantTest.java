package edu.cmu.cs.cs214.hw5.framework.core;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RestaurantTest {
    private Restaurant restaurant = new Restaurant("example", "Pittsburgh");

    @Before
    public void setUp() {
        List<List<Integer>> hours = new ArrayList<>();
        List<Integer> weekdays = new ArrayList<>();
        weekdays.add(1000);
        weekdays.add(2200);
        List<Integer> weekends = new ArrayList<>();
        weekends.add(1000);
        weekends.add(200);
        for (int i = 0; i < 4; i++) {
            hours.add(weekdays);
        }
        for (int i = 0; i < 3; i++) {
            hours.add(weekends);
        }
        restaurant.setHours(hours);
        restaurant.addData("ReviewCounts",100.0);
        restaurant.addData("ImageCounts", 40.0);
        restaurant.addData("CheckinCounts",250.0);
    }

    @Test
    public void isOpenTest() throws Exception {
        assertFalse(restaurant.isOpen(1, 900));
        assertTrue(restaurant.isOpen(3, 1600));
        assertTrue(restaurant.isOpen(4, 2344));
        assertTrue(restaurant.isOpen(5, 105));
        assertFalse(restaurant.isOpen(6, 344));
    }

    public void getHoursTest() throws Exception {
        List<List<Integer>> hours = restaurant.getHours();
        assertEquals(hours.size(),7);
        assertEquals(hours.get(0).size(),2);
        assertTrue(hours.get(0).get(0)==1000);
    }

    @Test
    public void getKeysTest() throws Exception {
        List<String> keys = restaurant.getKeys();
        assertEquals(keys.size(),3);
    }

    @Test
    public void getValuesTest() throws Exception {
        List<Double> values = restaurant.getValues();
        assertEquals(values.size(),3);
        assertTrue(values.get(0)==100.0);
        assertTrue(values.get(1)==40.0);
        assertTrue(values.get(2)==250.0);
    }

    @Test
    public void getNameTest() throws Exception {
        assertEquals(restaurant.getName(),"example");
    }

    @Test
    public void toStringTest() throws Exception {
        assertEquals(restaurant.toString(),"example");
    }

    @Test
    public void getCityTest() throws Exception {
        assertEquals(restaurant.getCity(),"Pittsburgh");
    }

}