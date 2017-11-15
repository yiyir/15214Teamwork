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
    }

    @Test
    public void isOpen() throws Exception {
        assertFalse(restaurant.isOpen(1, 900));
        assertTrue(restaurant.isOpen(3, 1600));
        assertTrue(restaurant.isOpen(4, 2344));
        assertTrue(restaurant.isOpen(5, 105));
        assertFalse(restaurant.isOpen(6, 344));
    }

}