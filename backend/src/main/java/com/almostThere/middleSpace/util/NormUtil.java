package com.almostThere.middleSpace.util;

import java.util.List;

public class NormUtil {
    public static double calculateMean(List<Double> data) {
        double sum = 0.0;
        for (double d : data) {
            sum += d;
        }
        return sum / data.size();
    }

    public static double calculateStandardDeviation(List<Double> data, double mean) {
        double sum = 0.0;
        for (double d : data) {
            sum += Math.pow(d - mean, 2);
        }
        return Math.sqrt(sum / data.size());
    }
}
