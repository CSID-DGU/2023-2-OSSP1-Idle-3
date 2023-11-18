package com.almostThere.middleSpace.util;

import java.util.List;

public class NormUtil {

    public static List<Double> normalize_with_std(List<Double> data) {
        double mean = calculateMean(data);
        double stdDeviation = calculateStandardDeviation(data, mean);

        for (int i = 0; i < data.size(); i++) {
            data.set(i, (data.get(i) - mean) / stdDeviation);
        }

        return data;
    }

    public static List<Double> normalize_with_max(List<Double> data) {
        double M = calculateMax(data);
        for (int i = 0 ; i < data.size(); i++) {
            data.set(i, (data.get(i)) / M);
        }
        return data;
    }

    private static double calculateMax(List<Double> data) {
        return data.stream().max(Double::compareTo).get();
    }

    private static double calculateMean(List<Double> data) {
        double sum = 0.0;
        for (double d : data) {
            sum += d;
        }
        return sum / data.size();
    }

    private static double calculateStandardDeviation(List<Double> data, double mean) {
        double sum = 0.0;
        for (double d : data) {
            sum += Math.pow(d - mean, 2);
        }
        return Math.sqrt(sum / data.size());
    }
}
