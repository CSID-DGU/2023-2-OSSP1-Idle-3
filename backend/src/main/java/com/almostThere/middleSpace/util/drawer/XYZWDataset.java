package com.almostThere.middleSpace.util.drawer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jfree.data.xy.AbstractXYZDataset;

public class XYZWDataset extends AbstractXYZDataset {
    private List<Double> longitudes;
    private List<Double> latitudes;
    private List<Double> averageDeviations;
    private List<Double> sums;

    public XYZWDataset() {
        longitudes = new ArrayList<>();
        latitudes = new ArrayList<>();
        averageDeviations = new ArrayList<>();
        sums = new ArrayList<>();
    }

    public void add(Double longitude, Double latitude, Double averageDeviation, Double sum) {
        this.longitudes.add(longitude);
        this.latitudes.add(latitude);
        this.averageDeviations.add(averageDeviation);
        this.sums.add(sum);
    }

    public void normalize() {
        Double maxAverageDeviations = this.averageDeviations.stream().max(Double::compareTo).get();
        this.averageDeviations = this.averageDeviations.stream()
                .map(derivation -> derivation / maxAverageDeviations)
                .collect(Collectors.toList());

        Double maxSum = this.sums.stream().max(Double::compareTo).get();
        this.sums = this.sums.stream()
                .map(sum -> sum / maxSum)
                .collect(Collectors.toList());
    }

    @Override
    public int getSeriesCount() {
        return 1;
    }

    @Override
    public Comparable getSeriesKey(int series) {
        return "Series1";
    }

    @Override
    public Number getZ(int series, int item) {
        return this.averageDeviations.get(item);
    }

    @Override
    public int getItemCount(int series) {
        return this.longitudes.size();
    }

    @Override
    public Number getX(int series, int item) {
        return this.longitudes.get(item);
    }

    @Override
    public Number getY(int series, int item) {
        return this.latitudes.get(item);
    }

    public Number getW(int series, int item) {
        return this.sums.get(item);
    }
}
