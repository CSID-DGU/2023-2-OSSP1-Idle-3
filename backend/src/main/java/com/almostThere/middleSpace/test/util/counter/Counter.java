package com.almostThere.middleSpace.test.util.counter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter {
    public static Map<Double, Integer> frequencyCount(List<Double> values, double interval) {
        Map<Double, Integer> map = new HashMap<>();
        for (Double value : values) {
            double key = Math.floor(value / interval) * interval;
            map.put(key, map.getOrDefault(key, 0) + 1);
        }
        return map;
    }
}
